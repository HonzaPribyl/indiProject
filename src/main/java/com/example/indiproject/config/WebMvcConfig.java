package com.example.indiproject.config;

import com.example.indiproject.services.util.BearerTokenInterceptor;
import com.example.indiproject.services.util.BearerTokenWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // register the interceptor
        registry.addInterceptor(bearerTokenInterceptor());
        // you can exclude certain URL patterns here, for example
        // .excludePathPatterns("/health")
    }

    // the 2 methods below produces the bean for token wrapper and interceptor in request scope

    @Bean
    public HandlerInterceptor bearerTokenInterceptor() {
        return new BearerTokenInterceptor(bearerTokenWrapper());
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public BearerTokenWrapper bearerTokenWrapper() {
        return new BearerTokenWrapper();
    }
}
