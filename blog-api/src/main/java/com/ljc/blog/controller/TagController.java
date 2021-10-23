package com.ljc.blog.controller;

import com.ljc.blog.common.chache.Cache;
import com.ljc.blog.service.TagService;
import com.ljc.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/hot")
    public Result hot(){
        // 获取最热门的前6条
        int limit = 6;
        Result result = tagService.hots(6);
        return result;
    }

    /**
     * 获取所有文章标签
     * @return
     */
    @GetMapping
    public Result listAllTags(){
        return tagService.listAllTags();
    }

    /**
     * 获取所有标签的详细信息
     * 对应客户端导航栏标签模块
     */
    @Cache
    @GetMapping("/detail")
    public Result listAllTagsDetail(){
        return tagService.listAllTagsDetail();
    }

    /**
     * 标签列表
     */
    @Cache
    @GetMapping("/detail/{id}")
    public Result getTagsDetailById(@PathVariable("id") Long id){
        return tagService.getTagsDetailById(id);
    }
}
