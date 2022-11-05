package run.xyy.graph.core;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * 图引擎
 * 临接表实现图存储
 *
 * @author xuanyangyang
 */
public class GraphEngine {
    /**
     * 名称->任务
     */
    private final Map<String, Task> name2TaskMap = new HashMap<>();
    /**
     * 任务 -> 依赖的任务
     */
    private final Map<String, Set<Task>> taskMap = new HashMap<>();

    /**
     * 添加任务
     *
     * @param task 任务
     */
    public void addTask(Task task) {
        String taskName = task.getName();
        if (name2TaskMap.containsKey(taskName)) {
            return;
        }
        ProxyTask proxyTask = new ProxyTask(task);
        name2TaskMap.put(taskName, proxyTask);
        taskMap.put(taskName, new HashSet<>());
    }

    public Task getTask(String name) {
        return name2TaskMap.get(name);
    }

    /**
     * 添加任务的依赖任务
     *
     * @param taskName           任务名称
     * @param dependentTaskNames 依赖的任务名称
     */
    public void addTaskDependentTask(String taskName, String... dependentTaskNames) {
        if (dependentTaskNames == null || dependentTaskNames.length < 1) {
            return;
        }
        Task task = getTaskIfAbsentThrowException(taskName);
        List<Task> dependentTasks = new ArrayList<>(dependentTaskNames.length);
        for (String dependentTaskName : dependentTaskNames) {
            dependentTasks.add(getTaskIfAbsentThrowException(dependentTaskName));
        }
        addTaskDependentTask(task, dependentTasks);
    }

    private Task getTaskIfAbsentThrowException(String taskName) {
        Task task = getTask(taskName);
        if (task == null) {
            throw new RuntimeException("找不到" + taskName + "任务");
        }
        return task;
    }

    /**
     * 添加任务的依赖任务
     *
     * @param task           任务
     * @param dependentTasks 依赖任务
     */
    private void addTaskDependentTask(Task task, Collection<Task> dependentTasks) {
        Set<Task> tasks = taskMap.computeIfAbsent(task.getName(), key -> new HashSet<>());
        tasks.addAll(dependentTasks);
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
        Map<String, CompletableFuture<Object>> runMap = new HashMap<>();
        RunContext runContext = new DefaultRunContext();
        for (String taskName : taskMap.keySet()) {
            Task task = getTask(taskName);
            CompletableFuture<Object> future = runTask(runContext, task, runMap);
            futures[index] = future;
            index++;
        }
        return CompletableFuture.allOf(futures);
    }

    private CompletableFuture<Object> runTask(RunContext runContext, Task task, Map<String, CompletableFuture<Object>> runMap) {
        // 检查是否已经执行
        CompletableFuture<Object> runFuture = runMap.get(task.getName());
        if (runFuture != null) {
            return runFuture;
        }
        // 1.执行依赖任务
        // 2.执行本任务
        Set<Task> dependentTasks = taskMap.get(task.getName());
        CompletableFuture<Object> future;
        if (dependentTasks.isEmpty()) {
            future = CompletableFuture.supplyAsync(() -> runAndSaveResult(runContext, task));
        } else {
            CompletableFuture<?>[] futures = new CompletableFuture[dependentTasks.size()];
            int index = 0;
            for (Task dependentTask : dependentTasks) {
                CompletableFuture<Object> dependentFuture = runTask(runContext, dependentTask, runMap);
                futures[index] = dependentFuture;
                index++;
            }
            future = CompletableFuture.allOf(futures).thenApply(unused -> runAndSaveResult(runContext, task));
        }
        runMap.put(task.getName(), future);
        return future;
    }

    private Object runAndSaveResult(RunContext runContext, Task task) {
        Object result = task.run(runContext);
        runContext.putResult(task.getName(), result);
        return result;
    }
}
