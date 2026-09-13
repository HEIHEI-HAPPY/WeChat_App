package com.mall.exception;

/**
 * 业务异常：service 层抛出，由 GlobalExceptionHandler 转为 ApiResponse
 */
public class ApiException extends RuntimeException {

    private final int code;

    public ApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ApiException(String message) {
        this(400, message);
    }

    public int getCode() { return code; }
}