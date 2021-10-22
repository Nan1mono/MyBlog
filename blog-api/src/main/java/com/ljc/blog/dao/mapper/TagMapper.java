package com.ljc.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljc.blog.dao.pojo.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 根据文章id查询文章类别
     * ms_article_tag, ms_tag联合查询
     * @param articleId
     * @return
     */
    List<Tag> listTagsByArticleId(Long articleId);

    /**
     * 查询所有标签并按照热门程序排序，取前limit条
     */
    List<Long> listHotTagIds(int limit);

    /**
     * 根据标签id集合查询标签信息
     */
    List<Tag> listTagsByTagIds(List<Long> tagIds);
}
