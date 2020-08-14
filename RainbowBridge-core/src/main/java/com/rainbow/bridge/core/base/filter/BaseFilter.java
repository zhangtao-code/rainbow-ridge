package com.rainbow.bridge.core.base.filter;


import com.rainbow.bridge.core.base.FilterException;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 拦截实体
 */
public class BaseFilter {
    private static Logger logger = LoggerFactory.getLogger(BaseFilter.class);
    private static ConcurrentHashMap<String, List<AbstractFilter>> filterMap = new ConcurrentHashMap<>();

    public boolean before(Class clazz, Method method) throws Exception {
        String key = getKey(clazz, method);
        List<AbstractFilter> list = filterMap.get(key);
        if (list == null) {
            list = filterMap.computeIfAbsent(key, str -> getFilter(clazz, method));
        }
        int index = -1;
        for (AbstractFilter abstractFilter : list) {
            try {
                index++;
                String error = abstractFilter.before();
                if (error != null) {
                    after(list, index - 1, null);
                }
                return false;
            } catch (FilterException filterException) {
                throw filterException;
            } catch (Exception e) {
                logger.error("filter error", e);
            }
        }
        return true;
    }

    private List<AbstractFilter> getFilter(Class myClass, Method myMethod) {
        List<Annotation> filter = new ArrayList<>();
        List<Annotation> clazz = collectAnnotation(myClass);
        List<Annotation> method = collectAnnotation(myMethod);
        filter.addAll(clazz);
        filter.addAll(method);
        Map<Class<? extends AbstractFilter>, AbstractFilter> map = filter
                .stream()
                .map(this::convert)
                .filter(o -> o != null)
                .collect(Collectors.toMap(AbstractFilter::getClass, Function.identity(), (a1, a2) -> a1.merge(a2.getFilter())));
        List<AbstractFilter> list = map
                .values()
                .stream()
                .sorted(Comparator.comparing(af -> af.getFilter().getOrder()))
                .collect(Collectors.toList());
        return list;
    }

    /**
     * 拿到该元素身上所有和FilterAnno相关的注解
     *
     * @param annotatedElement
     * @return
     */
    private List<Annotation> collectAnnotation(AnnotatedElement annotatedElement) {
        Annotation[] array = annotatedElement.getAnnotations();
        if (array == null) {
            return Collections.EMPTY_LIST;
        }
        List<Annotation> list = new ArrayList<>();
        for (Annotation annotation : array) {
            FilterAnno filterAnno = annotation.annotationType().getDeclaredAnnotation(FilterAnno.class);
            if (filterAnno != null) {
                list.add(annotation);
            }
        }
        return list;
    }

    /**
     * 将注解转化为实体对象
     *
     * @param annotation
     * @return
     */
    private AbstractFilter convert(Annotation annotation) {
        FilterAnno anno = annotation.annotationType().getDeclaredAnnotation(FilterAnno.class);
        if (anno == null) {
            return null;
        }
        Class<? extends AbstractFilter> clazz = anno.value();
        if (clazz == null) {
            return null;
        }
        try {
            AbstractFilter abstractFilter = clazz.newInstance();
            FilterEntity filterEntity = abstractFilter.resolve(annotation);
            abstractFilter.setFilter(filterEntity);
            return abstractFilter;
        } catch (Exception e) {
            logger.error("filer convert error", e);
        }
        return null;
    }

    public void after(Class myClass, Method myMethod, Object object) throws Exception {
        String key = getKey(myClass, myMethod);
        List<AbstractFilter> filters = filterMap.get(key);
        if (CollectionUtils.isEmpty(filters)) {
            return;
        }
        after(filters, filters.size() - 1, object);
    }

    private void after(List<AbstractFilter> list, int endInclude, Object object) {
        for (int i = endInclude; i >= 0; i--) {
            AbstractFilter filter = list.get(i);
            try {
                filter.after(object);
            } catch (Exception e) {
                logger.error("filter after error", e);
            }
        }
    }

    protected String getKey(Class clazz, Method method) {
        return clazz.getName() + "." + method.getName();
    }
}
