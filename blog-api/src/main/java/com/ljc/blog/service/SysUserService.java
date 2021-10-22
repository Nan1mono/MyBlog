package com.ljc.blog.service;

import com.ljc.blog.dao.pojo.SysUser;
import com.ljc.blog.vo.Result;
import com.ljc.blog.vo.UserVo;

public interface SysUserService {
    /**
     * 根据用户id获得这个用户的信息
     * @param id
     * @return
     */
    SysUser getUserById(Long id);

    /**
     * 根据账号和密码查找用户
     * @param account
     * @param password
     * @return
     */
    SysUser getUser(String account, String password);

    /**
     * 根据前端token解析用户信息
     * @param token
     * @return
     */
    Result getUserByToken(String token);

    /**
     * 根据account查询用户
     * @param account
     * @return
     */
    SysUser getUserByAccount(String account);

    /**
     * 插入一个用户
     * @param sysUser
     */
    void insertUser(SysUser sysUser);

    /**
     * 根据id获取作者信息
     * 做数据专业返回UserVo格式，方便数据展示
     */
    UserVo getUserVoById(Long id);
}
