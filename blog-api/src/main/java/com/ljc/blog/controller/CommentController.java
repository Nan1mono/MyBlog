package com.ljc.blog.controller;

import com.ljc.blog.common.chache.Cache;
import com.ljc.blog.dao.pojo.Comment;
import com.ljc.blog.service.CommentService;
import com.ljc.blog.vo.Result;
import com.ljc.blog.vo.params.CommentParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 根据文章id获取评论列表
     * @param id
     * @return
     */
    @GetMapping("/article/{id}")
    public Result listCommentsById(@PathVariable("id") Long id){
        return commentService.listCommentsById(id);
    }

    /**
     * 评论功能，注意需要登录才能评论，已经添加到拦截器中
     * @param commentParams
     * @return
     */
    @PostMapping("/create/change")
    public Result comment(@RequestBody CommentParams commentParams){
        return commentService.comment(commentParams);
    }
}
