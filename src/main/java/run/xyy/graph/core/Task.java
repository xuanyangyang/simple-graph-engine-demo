package run.xyy.graph.core;

/**
 * 任务
 *
 * @author xuanyangyang
 */
public interface Task {
    /**
     * 运行任务
     *
     * @param context 运行上下文
     * @return 任务结果
     */
    Object run(RunContext context);

    /**
     * @return 任务名称
     */
    String getName();
}
