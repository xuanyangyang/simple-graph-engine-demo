package run.xyy.graph.core;

/**
 * 运行上下文
 */
public interface RunContext {
    /**
     * 获取任务结果
     *
     * @param taskName 任务名称
     * @return 任务结果
     */
    Object getResult(String taskName);

    /**
     * 放任务结果
     *
     * @param taskName 任务名称
     * @param result   任务结果
     */
    void putResult(String taskName, Object result);
}
