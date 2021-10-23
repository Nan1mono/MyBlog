package com.ljc.blog.controller;

import com.ljc.blog.common.aop.LogAnnotation;
import com.ljc.blog.common.chache.Cache;
import com.ljc.blog.service.ArticleService;
import com.ljc.blog.vo.Result;
import com.ljc.blog.vo.params.ArticleParams;
import com.ljc.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    /**
     * 首页文章列表
     * 接受类封装为PageParams
     * 包含了page和pageSize
     */
    @PostMapping
    @LogAnnotation(module = "文章", operation = "获取首页文章列表")
    @Cache(expire = 5 * 60 * 1000,name = "news_article")
    public Result listArticles(@RequestBody PageParams pageParams){
        Result result = articleService.listArticles(pageParams);
        return result;
    }

    /**
     * 查找浏览量最多的文章，并取前limit条数据
     */
    @PostMapping("/hot")
    @Cache(expire = 5 * 60 * 1000,name = "news_article")
    public Result listHotArticles(){
        int limit = 5;
        Result result = articleService.listHotArticles(limit);
        return result;
    }

    /**
     * 查找最新文章，并取前limit条数据
     */
    @PostMapping("/new")
    @Cache(expire = 5 * 60 * 1000,name = "news_article")
    public Result newArticles(){
        int limit = 5;
        Result result = articleService.listNewArticles(limit);
        return result;
    }

    /**
     * 文章归档 即按照时间整理文章
     */
    @PostMapping("/listArchives")
    @Cache(expire = 5 * 60 * 1000,name = "news_article")
    public Result listArchives(){
        return articleService.listArchives();
    }

    /**
     * 查询文章id查询文章的详细信息
     * 包括文章详细内容和类别
     */
    @PostMapping("/view/{id}")
    public Result getArticleById(@PathVariable("id") Long articleId){
        return articleService.getArticleById(articleId);
    }

    /**
     * 发布文章
     */
    @PostMapping("/publish")
    public Result publish(@RequestBody ArticleParams articleParams){
        return articleService.publish(articleParams);
    }
}
