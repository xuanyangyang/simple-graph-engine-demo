package run.xyy.graph.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * 图引擎
 * 临接表实现图存储
 */
public class GraphEngine {
    /**
     * 任务 -> 依赖的任务
     */
    private Map<Task, Set<Task>> taskMap = new HashMap<>();

    /**
     * 添加任务
     *
     * @param task 任务
     */
    public void addTask(Task task) {
        taskMap.putIfAbsent(task, new HashSet<>());
    }

    /**
     * 添加任务的依赖任务
     *
     * @param task           任务
     * @param dependentTasks 依赖任务
     */
    public void addTaskDependentTask(Task task, Task... dependentTasks) {
        Set<Task> tasks = taskMap.computeIfAbsent(task, key -> new HashSet<>());
        for (Task dependentTask : dependentTasks) {
            addTask(dependentTask);
            tasks.add(dependentTask);
        }
    }

    /**
     * 开始执行任务
     *
     * @return 执行任务结果future
     */
    public CompletableFuture<Void> run() {
        if (taskMap.isEmpty()) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.complete(null);
            return future;
        }
        CompletableFuture<?>[] futures = new CompletableFuture[taskMap.size()];
        int index = 0;
        Map<Task, CompletableFuture<Object>> runMap = new HashMap<>();
        for (Task task : taskMap.keySet()) {
            CompletableFuture<Object> future = runTask(task, runMap);
            futures[index] = future;
            index++;
        }
        return CompletableFuture.allOf(futures);
    }

    private CompletableFuture<Object> runTask(Task task, Map<Task, CompletableFuture<Object>> runMap) {
        // 检查是否已经执行
        CompletableFuture<Object> runFuture = runMap.get(task);
        if (runFuture != null) {
            return runFuture;
        }
        // 1.执行依赖任务
        // 2.执行本任务
        Set<Task> dependentTasks = taskMap.get(task);
        CompletableFuture<Object> future;
        if (dependentTasks.isEmpty()) {
            future = CompletableFuture.supplyAsync(() -> task.run());
        } else {
            CompletableFuture<?>[] futures = new CompletableFuture[dependentTasks.size()];
            int index = 0;
            for (Task dependentTask : dependentTasks) {
                CompletableFuture<Object> dependentFuture = runTask(dependentTask, runMap);
                futures[index] = dependentFuture;
                index++;
            }
            future = CompletableFuture.allOf(futures).thenApply(unused -> task.run());
        }
        runMap.put(task, future);
        return future;
    }
}
