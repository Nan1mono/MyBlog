package com.ljc.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于结果返回
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private boolean success;    // 成功标记 true or false
    private int code;           // 结果编码 成功为200
    private String msg;         // 查询结果的自定义信息 成功为success
    private Object data;        // 结果数据

    /**
     * 查询结果成功时调用
     * @return
     */
    public static Result success(Object data){
        return new Result(true, 200, "success", data);
    }

    /**
     * 查询结果失败时调用
     * 调用时需要手动传入失败的code和Msg
     */
    public static Result fail(int code, String msg){
        return new Result(false, code, msg, null);
    }
}
