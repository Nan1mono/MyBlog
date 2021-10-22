package com.ljc.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ljc.blog.dao.mapper.ArticleMapper;
import com.ljc.blog.dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TheadService {

    // 通知这个方法创建子线程运行
    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article){
        // 拿到当前文章的阅读量
        int viewCounts = article.getViewCounts();
        // 在mapper的update中，对于包装类只会更新非null的数据，也就是说有什么值就更新什么值，没有值不更新
        // 所以我们创建一个新的Article实体类，只传入更新一次阅读量
        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(viewCounts + 1);
        // 设定条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getId, article.getId());
        // 乐观锁：当有多个用户同时浏览这篇文章时，可能会产生数据脏读，也就是说有部分用户可能会拿到仍未更新阅读量的数据。所以我们添加一个条件，只有当阅读量相等（即还未更新时）增加阅读量
        // 其实这样还是会丢失阅读量，但是为了线程安全考虑这是目前比较好的解决办法
        queryWrapper.eq(Article::getViewCounts, viewCounts);
        articleMapper.update(articleUpdate, queryWrapper);
    }
}
