package com.rainbow.bridge.core.base.filter.trace;

import com.rainbow.bridge.core.base.filter.AbstractFilter;
import com.rainbow.bridge.core.base.result.ResultType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

public class TraceFilter extends AbstractFilter<TraceEntity, Trace> {
    private static Logger logger = LoggerFactory.getLogger(TraceFilter.class);

    @Override
    public TraceEntity resolve(Trace trace) {
        if (trace == null) {
            return null;
        }
        TraceEntity traceEntity = new TraceEntity();
        traceEntity.setKey(trace.key());
        traceEntity.setUnTrace(trace.unTrace());
        traceEntity.setOrder(trace.order());
        return traceEntity;
    }

    @Override
    public ResultType before(Object ...object) {
        if (filter.isUnTrace()) {
            return null;
        }
        String uuid = UUID.randomUUID().toString().replace("-", "");
        if (StringUtils.isNotBlank(filter.getKey())) {
            uuid = filter.getKey() + "-" + uuid;
        }
        MDC.put("traceId", uuid);
        return null;
    }

    @Override
    public void after(Object object) {
        MDC.clear();
    }
}
