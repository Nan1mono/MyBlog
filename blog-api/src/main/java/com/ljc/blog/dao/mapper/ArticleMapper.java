package com.ljc.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljc.blog.dao.dos.Archives;
import com.ljc.blog.dao.pojo.Article;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 将文章按照 年 月 数量 的格式归档
     * @return
     */
    List<Archives> listArchives();
}
