package com.ljc.blog.config;

import com.ljc.blog.handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

    /**
     * 添加跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
         registry.addMapping("/**").allowedOrigins("http://localhost:8080");
    }

    /**
     * 配置拦截器
     * 这里作为保留功能，暂时没有配置真正的拦截路径，后续有需要的拦截功能再添加拦截路径
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/test").addPathPatterns("/comments/create/change").addPathPatterns("/articles/publish");
    }
}
