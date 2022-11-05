package run.xyy.graph.core;

import run.xyy.graph.utils.PrintUtils;

/**
 * 代理任务
 * 这里可以做的事情很多...充分想象
 *
 * @author xuanyangyang
 */
public class ProxyTask implements Task {
    private final Task task;

    public ProxyTask(Task task) {
        this.task = task;
    }

    @Override
    public Object run(RunContext context) {
        long start = System.currentTimeMillis();
        PrintUtils.print("任务:" + task.getName() + "开始运行");
        Object result = null;
        try {
            result = task.run(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long cost = System.currentTimeMillis() - start;
        PrintUtils.print("任务:" + task.getName() + "结束运行,耗时" + cost + "毫秒");
        return result;
    }

    @Override
    public String getName() {
        return task.getName();
    }
}
