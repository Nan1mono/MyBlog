package com.ljc.blog.service;

import com.ljc.blog.vo.CommentVo;
import com.ljc.blog.vo.Result;
import com.ljc.blog.vo.params.CommentParams;

import java.util.List;

public interface CommentService {
    /**
     * 根据文章Id查询评论列表
     * @param articleId
     * @return
     */
    Result listCommentsById(Long articleId);

    /**
     * 根据评论Id查找该评论的子评论
     */
    List<CommentVo> listCommentByParentId(Long id);

    /**
     * 对文章实现评论功能
     */
    Result comment(CommentParams commentParams);
}
