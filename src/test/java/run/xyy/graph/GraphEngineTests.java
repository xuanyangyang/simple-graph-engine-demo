package run.xyy.graph;

import org.junit.jupiter.api.Test;
import run.xyy.graph.core.GraphEngine;
import run.xyy.graph.core.Task;
import run.xyy.graph.task.RandomSleepTask;
import run.xyy.graph.utils.PrintUtils;

import java.util.concurrent.ExecutionException;

public class GraphEngineTests {

    /**
     *
     */
    @Test
    public void test() {
        GraphEngine graphEngine = new GraphEngine();
        Task aTask = new RandomSleepTask("a");
        Task bTask = new RandomSleepTask("b");
        Task cTask = new RandomSleepTask("c");
        Task dTask = new RandomSleepTask("d");
        Task eTask = new RandomSleepTask("e");
        graphEngine.addTask(aTask);
        graphEngine.addTask(bTask);
        graphEngine.addTask(cTask);
        graphEngine.addTask(dTask);
        graphEngine.addTask(eTask);

        graphEngine.addTaskDependentTask(aTask, bTask);
        graphEngine.addTaskDependentTask(aTask, cTask);
        graphEngine.addTaskDependentTask(cTask, dTask);
        graphEngine.addTaskDependentTask(cTask, eTask);

        try {
            graphEngine.run().thenRun(() -> PrintUtils.print("执行完毕")).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
