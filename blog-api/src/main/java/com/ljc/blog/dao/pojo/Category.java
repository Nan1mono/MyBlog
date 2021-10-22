package com.ljc.blog.dao.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章类别表
 * 注意：不是归档分类，归档分类是tag表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private Long id;                    // 类别id
    private String avatar;              // 图标
    private String categoryName;        // 类别名
    private String description;         // 类别描述
}
