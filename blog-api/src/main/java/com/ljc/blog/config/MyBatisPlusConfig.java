package com.ljc.blog.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.ljc.blog.dao.mapper")
public class MyBatisPlusConfig {

    /**
     * 引入myBatisPlus的网页分页插件 即滚轮加载
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        // 定义拦截器
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 集成网页翻页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }
}
