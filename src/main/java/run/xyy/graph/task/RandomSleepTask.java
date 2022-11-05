package run.xyy.graph.core.task;

import run.xyy.graph.core.RunContext;
import run.xyy.graph.core.Task;

import java.util.Random;

public class RandomSleepTask implements Task {
    private final String name;

    public RandomSleepTask(String name) {
        this.name = name;
    }

    @Override
    public Object run(RunContext context) {
        Random random = new Random();
        long value = random.nextLong(1000, 3000);
        System.out.println("任务:" + name + "准备休眠" + value + "毫秒");
        try {
            Thread.sleep(random.nextLong(1000, 5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("任务:" + name + "结束休眠");
        return name;
    }
}
