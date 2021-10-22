package com.ljc.blog.dao.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章主题内容实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleBody {
    private Long id;                 // 文章体id
    private String content;          //
    private String contentHtml;      //
    private Long articleId;          // 文章id
}
