package com.rainbow.bridge.spring.interceptor;

import com.rainbow.bridge.core.base.filter.BaseFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Method;

public class HandlerMethodReturnValueHandlerAdapter implements HandlerMethodReturnValueHandler {
    private static final Logger logger = LoggerFactory.getLogger(HandlerMethodReturnValueHandlerAdapter.class);
    private HandlerMethodReturnValueHandler handler;
    @Autowired
    private BaseFilter baseFilter;

    public HandlerMethodReturnValueHandlerAdapter(HandlerMethodReturnValueHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return handler.supportsReturnType(returnType);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        try {
            handler.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
        } catch (Exception e) {
            logger.error("handle value error", e);
        }
        try {
            Method method = returnType.getMethod();
            Class clazz = returnType.getContainingClass();
            baseFilter.after(clazz, method, returnValue);
        } catch (Exception e) {
            logger.error("after handle value error", e);
        }


    }
}
