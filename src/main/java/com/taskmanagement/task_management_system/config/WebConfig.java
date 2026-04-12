package com.taskmanagement.task_management_system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    public WebConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
            .excludePathPatterns(
                "/error",
                "/app.css",
                "/favicon.ico",
                "/css/**", "/js/**", "/images/**", "/webjars/**",
                "/**/*.css",
                "/**/*.js",
                "/**/*.map",
                "/**/*.png",
                "/**/*.jpg",
                "/**/*.jpeg",
                "/**/*.gif",
                "/**/*.svg",
                "/**/*.ico",
                "/**/*.woff2",
                "/**/*.woff",
                "/**/*.ttf"
            );
    }
}
