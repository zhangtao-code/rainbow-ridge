package com.rainbow.bridge.core.base.filter;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FilterAnno {
    Class<? extends AbstractFilter> value();
}
