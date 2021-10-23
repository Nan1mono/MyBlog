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

    /**
     * 查询所有类别的全部信息
     * 用于导航栏的类别模块，进行详细分类
     * @return
     */
    Result listAllCategorysDetail();

    /**
     * 通过categoryId获取详细信息
     * @param id
     * @return
     */
    Result getCategoryDetailById(Long id);
}
