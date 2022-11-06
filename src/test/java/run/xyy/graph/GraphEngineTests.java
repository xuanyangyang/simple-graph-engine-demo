package run.xyy.graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import run.xyy.graph.core.GraphEngine;
import run.xyy.graph.core.Task;
import run.xyy.graph.task.FindDependentResultTask;
import run.xyy.graph.task.RandomSleepTask;
import run.xyy.graph.utils.PrintUtils;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 测试
 *
 * @author xuanyangyang
 */
public class GraphEngineTests {

    /**
     *
     */
    @Test
    public void test() {
        GraphEngine graphEngine = new GraphEngine();
        Task aTask = new FindDependentResultTask("a", List.of("b", "c"));
        Task bTask = new RandomSleepTask("b");
        Task cTask = new RandomSleepTask("c");
        Task dTask = new RandomSleepTask("d");
        Task eTask = new RandomSleepTask("e");
        graphEngine.addTask(aTask);
        graphEngine.addTask(bTask);
        graphEngine.addTask(cTask);
        graphEngine.addTask(dTask);
        graphEngine.addTask(eTask);

        graphEngine.addTaskDependentTask("a", "b");
        graphEngine.addTaskDependentTask("a", "c");
        graphEngine.addTaskDependentTask("c", "d", "e");
        try {
            graphEngine.run().thenRun(() -> PrintUtils.print("执行完毕")).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 有环判断
     */
    @Test
    public void testCycle() {
        GraphEngine graphEngine = new GraphEngine();
        Task aTask = new FindDependentResultTask("a", List.of("b", "c"));
        Task bTask = new RandomSleepTask("b");
        Task cTask = new RandomSleepTask("c");
        Task dTask = new RandomSleepTask("d");
        Task eTask = new RandomSleepTask("e");
        graphEngine.addTask(aTask);
        graphEngine.addTask(bTask);
        graphEngine.addTask(cTask);
        graphEngine.addTask(dTask);
        graphEngine.addTask(eTask);

        graphEngine.addTaskDependentTask("a", "b");
        graphEngine.addTaskDependentTask("a", "c");
        graphEngine.addTaskDependentTask("c", "d", "e", "a");

        boolean hasCycle = graphEngine.hasCycle();
        PrintUtils.print("是否有环:" + hasCycle);
        Assertions.assertTrue(hasCycle);
        try {
            graphEngine.run().exceptionally(throwable -> {
                throwable.printStackTrace();
                return null;
            }).thenRun(() -> PrintUtils.print("执行完毕")).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
