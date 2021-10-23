package com.ljc.blog.common.chache;

import java.lang.annotation.*;

/**
 * 切点类注解
 * 为了提升访问速度我们开辟一个缓存
 * 将经常访问的数据放到内存中服务器内存中，以提升访问速度
 * 用redis当缓存
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {

    // 缓存存活时间
    long expire() default 1 * 60 * 1000;
    // 缓存标识
    String name() default "";

}