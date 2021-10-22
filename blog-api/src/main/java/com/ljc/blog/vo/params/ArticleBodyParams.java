package com.ljc.blog.vo.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章内容和格式
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleBodyParams {
    private String content;
    private String contentHtml;
}
