package com.ljc.blog.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljc.blog.dao.dos.Archives;
import com.ljc.blog.dao.mapper.ArticleBodyMapper;
import com.ljc.blog.dao.mapper.ArticleMapper;
import com.ljc.blog.dao.mapper.ArticleTagsMapper;
import com.ljc.blog.dao.pojo.Article;
import com.ljc.blog.dao.pojo.ArticleBody;
import com.ljc.blog.dao.pojo.ArticleTag;
import com.ljc.blog.dao.pojo.SysUser;
import com.ljc.blog.service.*;
import com.ljc.blog.util.UserThreadLocal;
import com.ljc.blog.vo.*;
import com.ljc.blog.vo.params.ArticleParams;
import com.ljc.blog.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TheadService theadService;
    @Autowired
    private ArticleTagsMapper articleTagsMapper;

    /**
     * 分页查询文章 按照发布时间和置顶标记排序
     * @param pageParams
     * @return
     */
    @Override
    public Result listArticles(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        IPage<Article> iPage = articleMapper.listArticles(page, pageParams.getCategoryId(), pageParams.getTagId(), pageParams.getYear(), pageParams.getMonth());
        List<Article> articleList = iPage.getRecords();
        List<ArticleVo> articleVoList = copyList(articleList, true, true);
        return Result.success(articleVoList);
    }

//    @Override
//    public Result listArticles(PageParams pageParams) {
//        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
//        // 查定查询条件 数据按照时间倒叙排序
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        // 时间排序 和 置顶排序
//        queryWrapper.orderByDesc(Article::getCreateDate, Article::getWeight);       // 等价于 order by created_data  等价于 order by weight
//        if (pageParams.getCategoryId() != null && pageParams.getCategoryId() != 0L){
//            queryWrapper.eq(Article::getCategoryId, pageParams.getCategoryId());
//        }
//        // 用于存储articleId
//        List<Long> articleIdList = new ArrayList<>();
//        if (pageParams.getTagId() != null && pageParams.getTagId() != 0L){
//            // 查询tag_article表 查询出指定tagId的articleId
//            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId, pageParams.getTagId());
//            List<ArticleTag> articleTagList = articleTagsMapper.selectList(articleTagLambdaQueryWrapper);
//            // 如果大于0 就代表改分类下有文章，将所有的articleId存进去
//            if (articleTagList.size() > 0){
//                for (ArticleTag articleTag : articleTagList){
//                    articleIdList.add(articleTag.getArticleId());
//                }
//                // 添加查询条件 in 表示包含
//                queryWrapper.in(Article::getId, articleIdList);
//            }
//        }
//        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
//        // 取出查询文章集合
//        List<Article> articleList = articlePage.getRecords();
//        // vo层数据转移  需要查询文章归档类别和文章作者
//        List<ArticleVo> articleVoList = copyList(articleList, true, true);
//        return Result.success(articleVoList);
//    }

    /**
     * 查找最热门文章
     * @param limit
     * @return
     */
    @Override
    public Result listHotArticles(int limit) {
        // 设定查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);           // 根据浏览量倒序排序
        queryWrapper.select(Article::getId, Article::getTitle);     // 只取文章的id和标题
        queryWrapper.last("limit "+limit);                   // 只取最热门的前limit条
        // 等价于select id,title from article order by viewCounts limit 6
        List<Article> articleList = articleMapper.selectList(queryWrapper);
        // 依然是数据转移  不需要标签和作者
        List<ArticleVo> articleVoList = copyList(articleList, false, false);
        return Result.success(articleVoList);
    }

    /**
     * 查找最新文章
     * @param limit
     * @return
     */
    @Override
    public Result listNewArticles(int limit) {
        // 设定查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);           // 根据浏览量倒序排序
        queryWrapper.select(Article::getId, Article::getTitle);     // 只取文章的id和标题
        queryWrapper.last("limit "+limit);                   // 只取最热门的前limit条
        // 等价于select id,title from article order by viewCounts limit 6
        List<Article> articleList = articleMapper.selectList(queryWrapper);
        // 依然是数据转移  不需要标签和作者
        List<ArticleVo> articleVoList = copyList(articleList, false, false);
        return Result.success(articleVoList);
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    /**
     * 根据文章id查询文章相关的所有详细信息，包括内容和类别
     * 1.查询文章信息
     * 2.根据文章id查询article_body中相应文章的详细内容（关联查询）
     * 需要数据转移，直接从article表中出的表并不能直接展示，需要于article_body进行合并，也就是传说中的数据转移。这里不仅需要查询文章内容，还需要查询文章的类别，两者查询方法基本一致
     * @param articleId
     * @return
     */
    @Override
    public Result getArticleById(Long articleId) {
        // 查询文章主题信息
        Article article = articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article, true, true, true, true);
        // 到这里已经将文章详情加载完毕了，所以我们增加一次阅读量，开启子线程，详情见开发日志
        theadService.updateArticleViewCount(articleMapper, article);
        return Result.success(articleVo);
    }

    /**
     * 根据article的bodyId获取文章的body
     * @param bodyId
     * @return
     */
    @Override
    public ArticleBodyVo getArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        // 做数据转移 只取文章内容
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

    /**
     * 发布文章
     * 插入文章到数据库
     * 1.需要获取当前用户
     * 2.需要将标签加入到关联表中 tag_article 一个文章有多个标签
     * 3.将文章内容插入到article_body关联表中
     * @param articleParams
     * @return
     */
    @Override
    public Result publish(ArticleParams articleParams) {
        // 获取当前用户
        SysUser sysUser = UserThreadLocal.get();
        Article article = new Article();
        // 设定文章的作者id
        article.setAuthorId(sysUser.getId());
        article.setWeight(Article.Article_Common);                  // 置顶权重
        article.setViewCounts(0);                                   // 浏览量
        article.setTitle(articleParams.getTitle());                 // 标题
        article.setSummary(articleParams.getSummary());             // 简介
        article.setCreateDate(System.currentTimeMillis());          // 当前发布时间
        article.setCommentCounts(0);                                // 评论数量
        article.setCategoryId(Long.parseLong(articleParams.getCategory().getId())); // 类别
        // 将文章id和tagId插入tag_article
        articleMapper.insert(article);
        // 取出文章的标签对象
        List<TagVo> tags = articleParams.getTags();
        // 如果标签存在获取文章id 准备配合文章id 插入到tag_article中
        if (tags != null){
            Long articleId = article.getId();
            for (TagVo tagVo : tags){
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(articleId);
                articleTag.setTagId(Long.parseLong(tagVo.getId()));
                articleTagsMapper.insert(articleTag);
            }
        }
        // body同理
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParams.getBody().getContent());
        articleBody.setContentHtml(articleParams.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);
        // 插入完成之后，给article设定bodyId 前面已经插入过了，需要更新bodyId
        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        Map<String, String> map = new HashMap<>();
        map.put("id", article.getId().toString());
        return Result.success(map);
    }

    /**
     * Article -> ArticleVo数据转移
     */
    public ArticleVo copy(Article article, boolean isTag, boolean isAuthor){
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setId(String.valueOf(article.getId()));
        // 手动转换时间格式
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        // 当然查询博客并不是一定需要tag归类和作者昵称，为了模块的灵活度，在这里我们做一个判断，判断tag和author在查询时是否需要文章类别和作者昵称
        // true:需要 false:不需要
        if (isTag){
            // 设定文章归档类型
            articleVo.setTags(tagService.listTagsByArticleId(article.getId()));
        }
        if (isAuthor){
            // 设定文章作者昵称
            articleVo.setAuthor(sysUserService.getUserById(article.getAuthorId()).getNickname());
        }
        return articleVo;
    }

    /**
     * 更新：copy方法的重写
     * 查询中，我们还需要判断是否需要body和category信息，为了不影响其他的调用，直接重写copy方法
     */
    public ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setId(String.valueOf(article.getId()));
        // 手动转换时间格式
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        // 当然查询博客并不是一定需要tag归类和作者昵称，为了模块的灵活度，在这里我们做一个判断，判断tag和author在查询时是否需要文章类别和作者昵称
        // true:需要 false:不需要
        if (isTag){
            // 设定文章归档类型
            articleVo.setTags(tagService.listTagsByArticleId(article.getId()));
        }
        if (isAuthor){
            // 设定文章作者昵称
            articleVo.setAuthor(sysUserService.getUserById(article.getAuthorId()).getNickname());
        }
        if (isBody){
            // 设定文章详情
            Long bodyId = article.getBodyId();
            ArticleBodyVo articleBodyVo = getArticleBodyById(bodyId);
            articleVo.setBody(articleBodyVo);
        }
        if (isCategory){
            // 设定文章类别
            Long categoryId = article.getCategoryId();
            CategoryVo categoryVo = categoryService.getCategoryById(categoryId);
            articleVo.setCategory(categoryVo);
        }
        return articleVo;
    }

    /**
     * List<Article> -> List<ArticleVo>
     */
    private List<ArticleVo> copyList(List<Article> articleList, boolean isTag, boolean isAuthor){
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article article : articleList){
            articleVoList.add(copy(article, isTag, isAuthor));
        }
        return articleVoList;
    }

//    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor,boolean isBody,boolean isCategory) {
//        List<ArticleVo> articleVoList = new ArrayList<>();
//        for (Article record : records) {
//            articleVoList.add(copy(record,isTag,isAuthor,isBody,isCategory));
//        }
//        return articleVoList;
//    }
}
