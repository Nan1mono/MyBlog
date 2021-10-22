package com.ljc.blog.controller;

import com.ljc.blog.service.SysUserService;
import com.ljc.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token){
        // jwt的token是存在与Header中的，前端发送名为Authorization，在这里进行一个过滤
        return sysUserService.getUserByToken(token);
    }
}
