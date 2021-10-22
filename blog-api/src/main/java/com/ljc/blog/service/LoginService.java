package com.ljc.blog.service;

import com.ljc.blog.dao.pojo.SysUser;
import com.ljc.blog.vo.Result;
import com.ljc.blog.vo.params.LoginParams;

public interface LoginService {
    /**
     * 登录功能
     * @param loginParams
     * @return
     */
    Result login(LoginParams loginParams);

    SysUser checkToken(String token);

    /**
     * 清楚redis的token从而实现退出登录功能
     * @param token
     * @return
     */
    Result logout(String token);

    /**
     * 注册功能
     * @param loginParams
     * @return
     */
    Result register(LoginParams loginParams);
}
