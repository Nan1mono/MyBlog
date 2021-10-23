package com.ljc.blog.controller;

import com.ljc.blog.common.chache.Cache;
import com.ljc.blog.service.CategoryService;
import com.ljc.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categorys")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public Result listAllCategorys(){
        return categoryService.listAllCategorys();
    }

    /**
     * 查询类别的详细信息
     * 用于导航栏的类别模块
     */
    @GetMapping("/detail")
    public Result listAllCategorysDetail(){
        return categoryService.listAllCategorysDetail();
    }

    /**
     * 类别列表
     */
    @GetMapping("/detail/{id}")
    @Cache
    public Result getCategoryDetailById(@PathVariable("id") Long id){
        return categoryService.getCategoryDetailById(id);
    }
}
