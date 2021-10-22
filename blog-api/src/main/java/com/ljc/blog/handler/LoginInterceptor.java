package com.ljc.blog.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ljc.blog.dao.pojo.SysUser;
import com.ljc.blog.service.LoginService;
import com.ljc.blog.util.UserThreadLocal;
import com.ljc.blog.vo.ErrorCode;
import com.ljc.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 对接口实现登录功能拦截
 * 1.判断请求的接口路劲是否是HandlerMethod（controller）
 * 2.判断token是否为空，如果是空，就代表为登录，不为空代表已经登录
 * 3.如果token不为空，进行登录检查，检查token信息是否合法，是否存在于redis等，相当于登录接口中的登录检查（checkToken）
 * 4.如果认证成功，放行。
 */
@Component
@Slf4j      // 日志记录
public class LoginInterceptor implements HandlerInterceptor {       // 实现拦截器接口
    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断是否是指定类型（controller） 如果不是指定拦截类型，正常放行
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        // 如果是指定拦截类型，就要进行token的判断
        String token  = request.getHeader("Authorization");     // 抓取头信息

        // 日志记录
        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");

        // 判断token是否为空 如果为空拦截，并返回相应的json数据给客户端
        if (StringUtils.isBlank(token)){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(JSON.toJSONString(result));
            return false;
        }
        // 如果token不为空，开始解析token，判断sysUser是否存在，相当于登录接口的登录检测
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(JSON.toJSONString(result));
            return false;
        }
        // 将用户信息存放在ThreadLocal中
        UserThreadLocal.put(sysUser);
        // 以上验证全部通过，正常放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 删除ThreadLocal中保存的用户信息
        UserThreadLocal.remove();
    }
}
