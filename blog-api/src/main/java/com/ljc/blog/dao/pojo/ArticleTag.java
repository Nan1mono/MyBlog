package com.ljc.blog.dao.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * tag_article实体对象
 * 用户表标签关联
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleTag {
    private Long id;
    private Long articleId;
    private Long tagId;
}
