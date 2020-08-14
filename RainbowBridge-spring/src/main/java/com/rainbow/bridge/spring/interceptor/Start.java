package com.rainbow.bridge.spring.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Start implements ServletContextAware {
    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @Override
    public void setServletContext(ServletContext servletContext) {
        List<HandlerMethodReturnValueHandler> list = requestMappingHandlerAdapter
                .getReturnValueHandlers()
                .stream()
                .map(HandlerMethodReturnValueHandlerAdapter::new)
                .collect(Collectors.toList());
        requestMappingHandlerAdapter.setReturnValueHandlers(list);
    }
}
