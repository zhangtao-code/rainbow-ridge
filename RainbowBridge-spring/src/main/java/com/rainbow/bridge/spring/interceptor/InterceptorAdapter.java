package com.rainbow.bridge.spring.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainbow.bridge.core.base.filter.BaseFilter;
import com.rainbow.bridge.core.base.result.JsonResult;
import com.rainbow.bridge.core.base.result.ResultType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@Configuration
public class InterceptorAdapter implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(InterceptorAdapter.class);
    private static BaseFilter baseFilter = new BaseFilter();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        MethodParameter[] methodParameter = handlerMethod.getMethodParameters();
        for (MethodParameter parameter : methodParameter) {
            
        }
        ResultType resultType = baseFilter.before(handlerMethod.getBeanType(), handlerMethod.getMethod());
        if (resultType == null) {
            return true;
        }
        try {
            writeError(response, resultType);
        } catch (Exception e) {
            logger.error("write error data fail", e);
        }
        return false;
    }

    private void writeError(HttpServletResponse response, ResultType resultType) throws Exception {
        response.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonResult jsonResult = JsonResult.fail(resultType);
        OutputStream outputStream = response.getOutputStream();
        objectMapper.writeValue(outputStream, jsonResult);
    }


}
