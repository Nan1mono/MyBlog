package com.ljc.blog.service.imp;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ljc.blog.dao.pojo.SysUser;
import com.ljc.blog.service.LoginService;
import com.ljc.blog.service.SysUserService;
import com.ljc.blog.util.JWTUtils;
import com.ljc.blog.vo.ErrorCode;
import com.ljc.blog.vo.Result;
import com.ljc.blog.vo.params.LoginParams;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {
    @Autowired
    private SysUserService sysUserService;
    // SpringBoot整合redis
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private static final String slat = "mszlu!@#";

    /**
     * 登录功能
     * 检查参数是否合法
     * 检查用户名和密码是否存在于数据库
     * 不存在：登陆失败 存在：jwt生成token发送至前端
     * token放入redis中，以提升效率，也就是说用户登陆时其实是首先查询redis认证是否存在，如果redis不存在再去查询数据库，而一天内查询到的token都会存在于redis中
     * @param loginParams
     * @return
     */
    @Override
    public Result login(LoginParams loginParams) {
        String account = loginParams.getAccount();
        String password = loginParams.getPassword();
        // 如果用户名或者密码不存在，返回特定的错误码和信息 详情见：ErrorCode.java
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        // 对数据进行Md5加密 后面额外加上一个字符串
        password = DigestUtils.md5Hex(password + slat);
        // 如果用户存在 根据用户名和密码查找用户 将用户对象返回 如果查找不到，就为空
        SysUser user = sysUserService.getUser(account, password);
        if (user == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        // 如果用户存在生成token
        String token = JWTUtils.createToken(user.getId());
        // 将TOKEN存到redis中，user对象以jsonString的形势存储到redis中，设置存活时间为1天
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(user), 1, TimeUnit.DAYS);
        return Result.success(token);
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
    public SysUser checkToken(String token) {
        // 判断token不为空，如果为空直接返回Null
        if (StringUtils.isBlank(token)){
            return null;
        }
        // 如果token不为空，开始解析token，如果token解析失败，说明token依然不合法，返回null
        Map<String, Object> checkTokenMap = JWTUtils.checkToken(token);
        if (checkTokenMap == null){
            return null;
        }
        // 如果token解析成功，开始检查redis中是否存在token，进行用户数据解析，数据会被解析成Json格式
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        // 判断redis中解析的json是否存在，如果不存在说明用户已经过期，需要重新登录以获取用户信息
        if (StringUtils.isBlank(userJson)){
            return null;
        }
        // 以上判断都通过，说明用户登录信息存在，返回user对象
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return sysUser;
    }

    /**
     * 清楚redis中的token 退出功能的实现
     * @param token
     * @return
     */
    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_"+token);
        return Result.success(null);
    }

    /**
     * 1.判断参数是否合法
     * 2.判断账户是否存在，存在提示账户已经存在。不存在就创建账户
     * 3.生成token，存入redis和数据库
     * 4.注册用户需要开启事务。注册过程中发生任何错误，异常等，直接将用户数据回滚，取消注册。保持数据库清洁
     * @param loginParams
     * @return
     */
    @Override
    public Result register(LoginParams loginParams) {
        String account = loginParams.getAccount();
        String password = loginParams.getPassword();
        String nickname = loginParams.getNickname();
        // 判断参数均不可空
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password) || StringUtils.isBlank(nickname)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        // 如果参数不为空，就要判断用户名已经注册了
        SysUser sysUser = sysUserService.getUserByAccount(account);
        if (sysUser != null){
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        // 如果sysUser为空，说明用户不存在，开始注册
        sysUser = new SysUser();
        sysUser.setAccount(account);
        sysUser.setNickname(nickname);
        // 对密码已经加密，数据库不存明文密码
        sysUser.setPassword(DigestUtils.md5Hex(password + slat));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        // 这一部先使用默认的，后续开发还有修改
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setAdmin(1); //1 为true
        sysUser.setDeleted(0); // 0 为false
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        // 进行数据库插入操作
        sysUserService.insertUser(sysUser);
        // 生成token，存入redis
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);
        return Result.success(token);
    }
}
