package com.ljc.blog.vo.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评论体实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentParams {
    private Long articleId;         // 评论文章id
    private String content;         // 评论内容
    private Long parent;            // 父评论id
    private Long toUserId;          // 父评论用户id
}
