package com.ljc.blog.controller;

import com.ljc.blog.service.LoginService;
import com.ljc.blog.vo.Result;
import com.ljc.blog.vo.params.LoginParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result login(@RequestBody LoginParams loginParams){
        Result result = loginService.login(loginParams);
        return result;
    }
}
