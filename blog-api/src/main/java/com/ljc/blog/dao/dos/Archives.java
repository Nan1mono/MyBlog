package com.ljc.blog.dao.dos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 依然是存放数据库查询出来的语句
 * 但是并不是持久层
 * 这里是用于归档
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Archives {
    private Integer year;
    private Integer month;
    private Long count;
}
