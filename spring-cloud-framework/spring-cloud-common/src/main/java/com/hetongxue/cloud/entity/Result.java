package com.hetongxue.cloud.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回类
 *
 * @author hy
 * @version 1.0
 */
@Data
@AllArgsConstructor
public class Result<T> implements Serializable {

    private Integer status;

    private String message;

    private T data;

    private Result() {
    }

    public static <T> Result<T> ok() {
        return new Result<T>().setStatus(200).setMessage("成功").setData(null);
    }

    public static <T> Result<T> fail() {
        return new Result<T>().setStatus(400).setMessage("失败").setData(null);
    }

}