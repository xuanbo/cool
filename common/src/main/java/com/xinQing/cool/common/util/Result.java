package com.xinQing.cool.common.util;

import io.vertx.core.json.Json;

/**
 * 统一数据返回
 *
 * Created by null on 2017/9/2.
 */
public class Result<T> {

    private int code;

    private String message;

    private T data;

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result<Void> ok() {
        return ok("ok", null);
    }

    public static <T> Result<T> ok(T data) {
        return ok("ok", data);
    }

    public static <T> Result<T> ok(String message, T data) {
        return new Result<>(200, message, data);
    }

    public static <T> Result<T> fail() {
        return fail(500, "fail", null);
    }

    public static <T> Result<T> fail(int code) {
        return fail(code, "fail", null);
    }

    public static Result<Void> fail(int code, String message) {
        return fail(code, message, null);
    }

    public static <T> Result<T> fail(String message) {
        return fail(500, message, null);
    }

    public static <T> Result<T> fail(String message, T data) {
        return fail(500, message, data);
    }

    public static <T> Result<T> fail(int code, String message, T data) {
        return new Result<>(code, message, data);
    }

    public static Result<Void> notFound() {
        return fail(404, "resource not found", null);
    }

    public String toJson() {
        return Json.encode(this);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}