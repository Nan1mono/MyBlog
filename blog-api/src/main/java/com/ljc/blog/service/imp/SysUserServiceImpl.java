package com.ljc.blog.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ljc.blog.dao.mapper.SysUserMapper;
import com.ljc.blog.dao.pojo.SysUser;
import com.ljc.blog.service.LoginService;
import com.ljc.blog.service.SysUserService;
import com.ljc.blog.vo.ErrorCode;
import com.ljc.blog.vo.LoginUserVo;
import com.ljc.blog.vo.Result;
import com.ljc.blog.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private LoginService loginService;

    /**
     * 根据用户id获得这个用户的信息
     * @param id
     * @return
     */
    @Override
    public SysUser getUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        // 做一个处理，方式用户不存在
        // 为了防止空指针异常，如果用户不存在会新建一个默认对象，使用默认的昵称
        if (sysUser == null){
            sysUser = new SysUser();
            sysUser.setNickname("注销用户");
        }
        return sysUser;
    }

    /**
     * 根据账号密码查找用户
     * @param account
     * @param password
     * @return
     */
    @Override
    public SysUser getUser(String account, String password) {
        // 设置查询条件
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, account);
        queryWrapper.eq(SysUser::getPassword, password);
        // 取出账号, 头像, 昵称即可
        queryWrapper.select(SysUser::getAccount, SysUser::getId, SysUser::getAvatar, SysUser::getNickname);
        queryWrapper.last("limit 1");
        SysUser user = sysUserMapper.selectOne(queryWrapper);
        return user;
    }

    /**
     * 根据前端token用户登录用户信息
     * 1.获取登录的token，进行校验，包括是否为空，是否可以被解析，redis中是否存在（正常的登录成功之后，redis中必然存有登录信息）
     * 2.校验失败时，返回相应的错误信息
     * 3.校验成功，获取结果，通过LoginUserVo进行数据转移以方便前端做更好的处理
     * @param token
     * @return
     */
    @Override
    public Result getUserByToken(String token) {
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }
        // 数据转移
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setId(sysUser.getId());
        loginUserVo.setAccount(sysUser.getAccount());
        loginUserVo.setNickname(sysUser.getNickname());
        loginUserVo.setAvatar(sysUser.getAvatar());
        return Result.success(loginUserVo);
    }

    @Override
    public SysUser getUserByAccount(String account) {
        // 条件设定
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, account);
        queryWrapper.last("limit 1");
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        return sysUser;
    }

    /**
     * 注册用户
     * @param sysUser
     */
    @Override
    public void insertUser(SysUser sysUser) {
        sysUserMapper.insert(sysUser);
    }

    /**
     * 根据id获取作者信息
     * 做数据专业返回UserVo格式，方便数据展示
     */
    @Override
    public UserVo getUserVoById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        UserVo userVo = new UserVo();
        // 如果用户不存在需要需要创建一个新用户，否则抛出指针异常
        if (sysUser == null){
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("注销用户");
        }
        BeanUtils.copyProperties(sysUser, userVo);
        return userVo;
    }

}
