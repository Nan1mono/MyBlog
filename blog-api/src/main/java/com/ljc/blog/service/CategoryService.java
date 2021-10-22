package com.ljc.blog.service;

import com.ljc.blog.vo.CategoryVo;
import com.ljc.blog.vo.Result;

public interface CategoryService {
    /**
     * 根据article的categoryId获取category
     * @param categoryId
     * @return
     */
    CategoryVo getCategoryById(Long categoryId);

    /**
     * 获取所有类别标签
     * @return
     */
    Result listAllCategorys();
}
