package com.ljc.blog.service;

import com.ljc.blog.dao.pojo.Tag;
import com.ljc.blog.vo.Result;
import com.ljc.blog.vo.TagVo;

import java.util.List;

public interface TagService {
    /**
     * 根据文章Id查询文章的类别
     * 注意：文章类别归档时，可以有多个类别，所以我们用List
     * 同样的，我们依然采用vo层的数据转移将数据转换为方便查看的类型
     */
    List<TagVo> listTagsByArticleId(Long articleId);

    /**
     * 获取所有标签，并根据热门程度进行排序，取前limit个
     */
    Result hots(int limit);

    /**
     * 获取所有标签
     */
    Result listAllTags();
}
