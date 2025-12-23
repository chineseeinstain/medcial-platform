package com.medical.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 * 捕获所有Controller抛出的异常，统一处理并返回统一格式
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 处理404错误
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<?> handleNotFound(HttpServletRequest request, NoHandlerFoundException e) {
        log.warn("请求路径不存在: {}", request.getRequestURI());
        return Result.error(HttpStatus.NOT_FOUND.value(), "请求路径不存在: " + request.getRequestURI() + "，请查看API文档: http://localhost:8080/swagger-ui/index.html");
    }
    
    /**
     * 处理所有异常
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error("系统异常：", e);
        return Result.error("系统异常：" + e.getMessage());
    }
    
    /**
     * 处理业务异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntimeException(RuntimeException e) {
        log.error("业务异常：", e);
        return Result.error(e.getMessage());
    }
}

