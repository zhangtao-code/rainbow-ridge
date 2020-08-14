package com.rainbow.bridge.core.base.filter;


import java.lang.annotation.Annotation;

public abstract class AbstractFilter<U extends FilterEntity, V extends Annotation> {

    /**
     * 拦截实体
     */
    protected U filter;

    /**
     * 拦截实体的解析
     *
     * @param v
     * @return
     */
    public abstract U resolve(V v);

    /**
     * 真正的拦截方法
     *
     * @return
     */
    public abstract String before(Object... object);

    /**
     * 方法执行之后的收尾清扫
     */
    public abstract void after(Object object);

    public U getFilter() {
        return filter;
    }

    public void setFilter(U filter) {
        this.filter = filter;
    }

    public int order() {
        return filter.getOrder();
    }

    /**
     * 合并两个拦截实体
     *
     * @param u
     * @return
     */
    public AbstractFilter merge(U u) {
        if (u != null) {
            filter = u;
        }
        return this;
    }

}
