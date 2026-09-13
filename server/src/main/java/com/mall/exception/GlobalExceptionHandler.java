package com.mall.exception;

import com.mall.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理：所有 controller 抛出的异常都到这里
 * Spring Security 用 @RestControllerAdvice（不是 @ControllerAdvice + @ResponseBody）
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<?>> handleApi(ApiException e) {
        log.warn("[ApiException] code={} msg={}", e.getCode(), e.getMessage());
        return ResponseEntity
                .status(e.getCode() >= 400 && e.getCode() < 600 ? e.getCode() : 400)
                .body(ApiResponse.error(e.getCode(), e.getMessage()));
    }

    /** @Valid 校验失败：把第一个错误信息返回 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException e) {
        FieldError first = e.getBindingResult().getFieldError();
        String msg = first != null ? first.getDefaultMessage() : "参数错误";
        return ResponseEntity.badRequest().body(ApiResponse.error(400, msg));
    }

    /** 兜底：未捕获的异常打日志 + 返 500，不暴露内部细节 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleUnknown(Exception e) {
        log.error("[UnknownException] ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "服务器内部错误"));
    }
}