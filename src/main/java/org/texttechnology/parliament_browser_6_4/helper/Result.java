package org.texttechnology.parliament_browser_6_4.helper;

import cn.hutool.json.JSONUtil;

public class Result<T>{

    private Integer code;

    private String message;

    private T data;

    private boolean success;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Result(Integer code, String message, T data, boolean success) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
    }

    public static Result buildSuccess() {
        return buildSuccess(null);
    }

    public static <T> Result buildSuccess(T data) {
        return new Result(0, "success", data, true);
    }

    public static Result buildError() {
        return buildError("System error");
    }

    public static Result buildError(String errorMsg) {
        return new Result(null, errorMsg, null, false);
    }

    public static Result buildError(Integer code, String errorMsg) {
        return new Result(code, errorMsg, null, false);
    }

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}
