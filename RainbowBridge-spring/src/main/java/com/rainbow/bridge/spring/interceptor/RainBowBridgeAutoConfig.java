package com.rainbow.bridge.spring.interceptor;

import com.rainbow.bridge.core.base.filter.BaseFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RainBowBridgeAutoConfig {
    @Bean
    @ConditionalOnMissingBean
    public BaseFilter initFilter() {
        return new BaseFilter();
    }

    @Bean
    public WebMvcConfigurer initConfig(@Autowired InterceptorAdapter adapter) {
        return new InterceptorConfig(adapter);
    }

    @Bean
    @ConditionalOnMissingBean
    public InterceptorAdapter initBridge(@Autowired BaseFilter baseFilter) {
        return new InterceptorAdapter(baseFilter);
    }

}
