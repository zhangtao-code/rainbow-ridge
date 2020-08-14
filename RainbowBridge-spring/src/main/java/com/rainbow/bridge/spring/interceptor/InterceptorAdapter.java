package com.rainbow.bridge.spring.interceptor;

import com.rainbow.bridge.core.base.filter.BaseFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class InterceptorAdapter implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(InterceptorAdapter.class);
    private BaseFilter baseFilter;

    public InterceptorAdapter(BaseFilter baseFilter) {
        this.baseFilter = baseFilter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        boolean success = baseFilter.before(handlerMethod.getBeanType(), handlerMethod.getMethod());
        if (success) {
            return true;
        }
        return false;
    }

}
