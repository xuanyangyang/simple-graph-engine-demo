package run.xyy.graph.task;

import run.xyy.graph.core.Task;
import run.xyy.graph.utils.PrintUtils;

import java.util.Random;

/**
 * 模拟耗时处理
 */
public class RandomSleepTask implements Task {
    private final String name;

    public RandomSleepTask(String name) {
        this.name = name;
    }

    @Override
    public Object run() {
        Random random = new Random();
        long value = random.nextLong(1000, 3000);
        PrintUtils.print("开始运行，预计耗时" + value + "毫秒");
        try {
            Thread.sleep(value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        PrintUtils.print("任务:" + name + "已经完成");
        return name;
    }
}
