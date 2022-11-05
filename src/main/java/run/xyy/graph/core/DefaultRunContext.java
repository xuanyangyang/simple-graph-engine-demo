package run.xyy.graph.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DefaultRunContext implements RunContext {
    private final ConcurrentMap<String, Object> resultMap = new ConcurrentHashMap<>();

    @Override
    public Object getResult(String taskName) {
        return resultMap.get(taskName);
    }

    @Override
    public void putResult(String taskName, Object result) {
        resultMap.put(taskName, result);
    }
}
