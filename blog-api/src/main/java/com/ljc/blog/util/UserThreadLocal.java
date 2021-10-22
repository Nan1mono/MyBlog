package com.ljc.blog.util;

import com.ljc.blog.dao.pojo.SysUser;

public class UserThreadLocal {
    private UserThreadLocal(){

    }

    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

    // 存放用户
    public static void put(SysUser sysUser){
        LOCAL.set(sysUser);
    }

    // 取出用户
    public static SysUser get(){
        return LOCAL.get();
    }

    // 移除用户
    public static void remove(){
        LOCAL.remove();
    }
}
