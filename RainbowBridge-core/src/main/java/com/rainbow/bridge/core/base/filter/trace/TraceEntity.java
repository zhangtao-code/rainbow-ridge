package com.rainbow.bridge.core.base.filter.trace;


import com.rainbow.bridge.core.base.filter.FilterEntity;

public class TraceEntity extends FilterEntity {
    /**
     * 是否 取消trace
     */
    private boolean unTrace;
    /**
     * trace 前缀key
     */
    private String key;

    public boolean isUnTrace() {
        return unTrace;
    }

    public void setUnTrace(boolean unTrace) {
        this.unTrace = unTrace;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
