package run.xyy.graph.task;

import run.xyy.graph.core.RunContext;
import run.xyy.graph.core.Task;
import run.xyy.graph.utils.PrintUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 查找依赖结果任务
 */
public class FindDependentResultTask implements Task {
    private final String name;
    /**
     * 需要查找结果的任务名称
     */
    private final Set<String> needFindResultTaskNameSet = new HashSet<>();

    public FindDependentResultTask(String name, Collection<String> needFindResultTaskNames) {
        this.name = name;
        needFindResultTaskNameSet.addAll(needFindResultTaskNames);
    }

    public void addNeedFindResultTaskName(String taskName) {
        needFindResultTaskNameSet.add(taskName);
    }

    public void addNeedFindResultTaskName(Collection<String> taskNames) {
        needFindResultTaskNameSet.addAll(taskNames);
    }

    @Override
    public Object run(RunContext context) {
        for (String taskName : needFindResultTaskNameSet) {
            Object result = context.getResult(taskName);
            PrintUtils.print("任务" + taskName + "的运行结果:" + result);
        }
        Random random = new Random();
        return random.nextInt();
    }

    @Override
    public String getName() {
        return name;
    }
}
