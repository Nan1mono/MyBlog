package com.ljc.blog.service;

import com.ljc.blog.dao.pojo.ArticleBody;
import com.ljc.blog.vo.ArticleBodyVo;
import com.ljc.blog.vo.Result;
import com.ljc.blog.vo.params.ArticleParams;
import com.ljc.blog.vo.params.PageParams;

public interface ArticleService {
    /**
     * 分页查询文章 按照发布时间和置顶标记排序
     * @param pageParams
     * @return
     */
    Result listArticles(PageParams pageParams);

    /**
     * 查找最热门文章
     * @param limit
     * @return
     */
    Result listHotArticles(int limit);

    /**
     * 查找最近文章
     * @param limit
     * @return
     */
    Result listNewArticles(int limit);

    /**
     * 文章归档，将文章按照年 月的格式进行整理
     * @return
     */
    Result listArchives();

    /**
     * 根据文章id查询文章的主信息，和文章内容详情、类别，需要联合查询和数据转移
     * @param articleId
     * @return
     */
    Result getArticleById(Long articleId);

    /**
     * 根据bodyId获取articleBody
     */
    ArticleBodyVo getArticleBodyById(Long bodyId);

    /**
     * 发布文章
     * 插入文章到数据库
     * @param articleParams
     * @return
     */
    Result publish(ArticleParams articleParams);
}
