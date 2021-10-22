package com.ljc.blog.handler;

import com.ljc.blog.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 对异常进行统一处理，而不是将异常信息直接暴露给用户
 */
@ControllerAdvice       // 对所有Controller进行拦截 AOP实现
public class AllExceptionHandler {

    // 捕获所有Exception并进行处理
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result doException(Exception exception){
        exception.printStackTrace();
        return Result.fail(-999, "系统异常");
    }
}
