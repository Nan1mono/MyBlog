package com.ljc.blog.vo.params;

import com.ljc.blog.vo.CategoryVo;
import com.ljc.blog.vo.TagVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 文章发布
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleParams {
    private Long id;
    private ArticleBodyParams body;
    private CategoryVo category;
    private String summary;
    private List<TagVo> tags;
    private String title;
}
