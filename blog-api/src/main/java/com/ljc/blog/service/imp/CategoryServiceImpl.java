package com.ljc.blog.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ljc.blog.dao.mapper.CategoryMapper;
import com.ljc.blog.dao.pojo.Category;
import com.ljc.blog.service.CategoryService;
import com.ljc.blog.vo.CategoryVo;
import com.ljc.blog.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据article的categoryId获取category
     * @param categoryId
     * @return
     */
    @Override
    public CategoryVo getCategoryById(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        // 做数据转移
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        categoryVo.setId(String.valueOf(category.getId()));
        return categoryVo;
    }

    /**
     * 获取所有类别标签
     * 需要数据转移
     * @return
     */
    @Override
    public Result listAllCategorys() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Category::getId, Category::getCategoryName);
        List<Category> categoryList = categoryMapper.selectList(queryWrapper);
        // 数据转移，转换成前端可识别对象
        List<CategoryVo> categoryVoList = copyList(categoryList);
        return Result.success(categoryVoList);
    }

    /**
     * 查询标签的所有详细信息
     * 不需要进行数据转移
     * 直接将原数据传输，详情见开发日志
     * @return
     */
    @Override
    public Result listAllCategorysDetail() {
        List<Category> categoryList = categoryMapper.selectList(null);
        List<CategoryVo> categoryVoList = copyList(categoryList);
        return Result.success(categoryVoList);
    }

    /**
     * 根据categoryId查询category的所有详细信息
     * 并以Result格式返回
     * @param id
     * @return
     */
    @Override
    public Result getCategoryDetailById(Long id) {
        Category category = categoryMapper.selectById(id);
        CategoryVo categoryVo = copy(category);
        return Result.success(categoryVo);
    }

    public CategoryVo copy(Category category){
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        categoryVo.setId(String.valueOf(category.getId()));
        return categoryVo;
    }

    public List<CategoryVo> copyList(List<Category> categoryList){
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category category : categoryList){
            categoryVoList.add(copy(category));
        }
        return categoryVoList;
    }

}
