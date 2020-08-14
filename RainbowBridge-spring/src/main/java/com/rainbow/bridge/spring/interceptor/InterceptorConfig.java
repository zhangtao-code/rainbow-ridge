package com.rainbow.bridge.spring.interceptor;

import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

public class InterceptorConfig implements WebMvcConfigurer {
    private InterceptorAdapter adapter;

    public InterceptorConfig(InterceptorAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adapter);
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {

    }
}
