package com.ljc.blog.vo.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于接口接受实体类
 * 主要用于首页分页查询
 * 默认查询 第1页，每页默认10个
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageParams {
    private int page = 1;
    private int pageSize = 10;
    private Long categoryId;
    private Long tagId;
    private String year;
    private String month;

    public String getMonth(){
        if (this.month != null && this.month.length() == 1){
            return "0"+this.month;
        }
        return this.month;
    }
}
