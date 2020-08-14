package com.rainbow.bridge.core.base.filter.trace;


import com.rainbow.bridge.core.base.filter.FilterAnno;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@FilterAnno(TraceFilter.class)
public @interface Trace {
    /**
     * 拦截器排序
     * @return
     */
    int order() default -1;

    /**
     * 是否 取消 trace
     * @return
     */
    boolean unTrace() default false;

    /**
     * trace 前缀key
     * @return
     */
    String key() default "";
}
