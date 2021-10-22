package com.ljc.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 说明：
 * 在数据库查询中 大多数结果是需要转换的，比如直接从article表中查询到的author是authorId而不是作者
 * 为了给前端更好的响应数据，需要对数据进行加工
 * 数据的加工正式通过相应的vo类
 * 概括：与前端界面交互的数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleVo {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String title;

    private String summary;         // 简介

    private Integer commentCounts;

    private Integer viewCounts;

    private Integer weight;
    /**
     * 创建时间
     */
    private String createDate;      // 在Article中为long型，需要手动转换

    private String author;

    private ArticleBodyVo body;

    private List<TagVo> tags;       // 类别 文章归档时可以设定多个类别，所以用List存储

    private CategoryVo category;

}