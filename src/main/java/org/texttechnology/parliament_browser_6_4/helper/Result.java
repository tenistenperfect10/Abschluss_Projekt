package org.texttechnology.parliament_browser_6_4.helper;

import cn.hutool.json.JSONUtil;

/**
 * A generic class for wrapping the response data along with status information.
 * This includes status code, message, data, and a success flag to indicate the operation's outcome.
 *
 * @param <T> The type of the data field, allowing for flexibility in what can be returned.
 * @author He Liu
 * @author Yu Ming
 */
public class Result<T>{

    private Integer code;

    private String message;

    private T data;

    private boolean success;

    /**
     * Gets the status code of the operation.
     *
     * @return The status code.
     */
    public Integer getCode() {
        return code;
    }

    /**
     * Sets the status code of the operation.
     *
     * @param code The status code.
     */
    public void setCode(Integer code) {
        this.code = code;
    }


    /**
     * Gets the message associated with the operation's outcome.
     *
     * @return The outcome message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message associated with the operation's outcome.
     *
     * @param message The outcome message.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the data of the operation, of generic type T.
     *
     * @return The operation data.
     */
    public T getData() {
        return data;
    }

    /**
     * Sets the data of the operation, of generic type T.
     *
     * @param data The operation data.
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Indicates whether the operation was successful.
     *
     * @return {@code true} if the operation was successful, {@code false} otherwise.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the success status of the operation.
     *
     * @param success {@code true} if the operation was successful, {@code false} otherwise.
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }


    /**
     * Constructs a new {@code Result} instance with the specified code, message, data, and success status.
     *
     * @param code The status code of the operation.
     * @param message The message associated with the operation's outcome.
     * @param data The data of the operation, of generic type T.
     * @param success The success status of the operation.
     */
    public Result(Integer code, String message, T data, boolean success) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
    }


    /**
     * Builds a success {@code Result} with no data.
     *
     * @return A success {@code Result} instance.
     */
    public static Result buildSuccess() {
        return buildSuccess(null);
    }


    /**
     * Builds a success {@code Result} with the specified data.
     *
     * @param <T> The type of the data.
     * @param data The data to include in the result.
     * @return A success {@code Result} instance with data.
     */
    public static <T> Result buildSuccess(T data) {
        return new Result(0, "success", data, true);
    }


    /**
     * Builds an error {@code Result} with a default message.
     *
     * @return An error {@code Result} instance.
     */
    public static Result buildError() {
        return buildError("System error");
    }


    /**
     * Builds an error {@code Result} with the specified error message.
     *
     * @param errorMsg The error message.
     * @return An error {@code Result} instance with the specified message.
     */
    public static Result buildError(String errorMsg) {
        return new Result(null, errorMsg, null, false);
    }


    /**
     * Builds an error {@code Result} with the specified code and error message.
     *
     * @param code The error code.
     * @param errorMsg The error message.
     * @return An error {@code Result} instance with the specified code and message.
     */
    public static Result buildError(Integer code, String errorMsg) {
        return new Result(code, errorMsg, null, false);
    }

    /**
     * Returns a string representation of the {@code Result} in JSON format.
     *
     * @return The string representation of the {@code Result}.
     */
    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}
