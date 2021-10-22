package com.ljc.blog.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ljc.blog.dao.mapper.CommentMapper;
import com.ljc.blog.dao.pojo.Comment;
import com.ljc.blog.dao.pojo.SysUser;
import com.ljc.blog.service.CommentService;
import com.ljc.blog.service.SysUserService;
import com.ljc.blog.util.UserThreadLocal;
import com.ljc.blog.vo.CommentVo;
import com.ljc.blog.vo.Result;
import com.ljc.blog.vo.UserVo;
import com.ljc.blog.vo.params.CommentParams;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 根据文章id查询评论列表
     * 通过articleId在comment表中查找评论列表。
     * 还需要作者信息，comment中只有作者id，根据作者id查找作者信息
     * comment还有level字段，根据level等级来判断是否有子评论
     * 如果是子评论，还需要根parent_id进行查询，是哪条评论下的子评论
     * @param articleId
     * @return
     */
    @Override
    public Result listCommentsById(Long articleId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId, articleId);
        // 先查询主评论
        queryWrapper.eq(Comment::getLevel, 1);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        // 结果不能直接展示，我们做一个数据转移
        List<CommentVo> commentVoList = copyList(comments);
        return Result.success(commentVoList);
    }

    /**
     * 根据评论Id查找该评论的所有子评论
     * @param id
     * @return
     */
    @Override
    public List<CommentVo> listCommentByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId, id);
        queryWrapper.eq(Comment::getLevel, 2);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        List<CommentVo> commentVoList = copyList(comments);
        return commentVoList;
    }

    /**
     * 实现文章的评论功能
     * @param commentParams
     * @return
     */
    @Override
    public Result comment(CommentParams commentParams) {
        // 从子线程中取出当前登录用户的信息
        SysUser sysUser = UserThreadLocal.get();
        // 创建一个新的评论实体类 后续插入到数据表
        Comment comment = new Comment();
        comment.setArticleId(commentParams.getArticleId());
        comment.setContent(commentParams.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        comment.setAuthorId(sysUser.getId());
        Long parent = commentParams.getParent();
        // 如果当前评论的父评论id不存在或者为0 就代表他本身就是一个父评论
        // 如果当前评论为父评论需要将parentId设置为0
        if (parent == null || parent == 0){
            comment.setLevel(1);
            comment.setParentId(0L);
        }else {
            comment.setLevel(2);
            comment.setParentId(parent);
        }
        Long toUserId = commentParams.getToUserId();
        if (toUserId == null || toUserId == 0){
            comment.setToUid(0L);
        }else {
            comment.setToUid(toUserId);
        }
        commentMapper.insert(comment);
        return Result.success(null);
    }

    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : comments){
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment, commentVo);
        // 根据作者Id获取作者信息
        Long userId = comment.getAuthorId();
        UserVo userVo = sysUserService.getUserVoById(userId);
        commentVo.setAuthor(userVo);
        // 查询当前评论的子评论 首先需要当前评论level=1，否则跳过
        Integer level = comment.getLevel();
        // 主评论判断 需要查询当前评论的子评论
        if (level == 1){
            // 将当前评论的id当作父评论id取查询当前评论的子评论
            Long commentId = comment.getId();
            List<CommentVo> commentVoList = listCommentByParentId(commentId);
            commentVo.setChildrens(commentVoList);
        }
        // 子评论判断 需要查询当前评论的父评论
        if (level > 1){
            // 获取父评论id
            Long toUid = comment.getToUid();
            // 根据父评论id获取父评论对象
            UserVo toUserVo = sysUserService.getUserVoById(toUid);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }
}
