package run.xyy.graph.task;

import run.xyy.graph.core.RunContext;
import run.xyy.graph.core.Task;

import java.util.Random;

/**
 * 模拟耗时处理
 *
 * @author xuanyangyang
 */
public class RandomSleepTask implements Task {
    private final String name;

    public RandomSleepTask(String name) {
        this.name = name;
    }

    @Override
    public Object run(RunContext context) {
        Random random = new Random();
        long value = random.nextLong(1000, 3000);
        try {
            Thread.sleep(value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return name;
    }

    @Override
    public String getName() {
        return name;
    }
}
