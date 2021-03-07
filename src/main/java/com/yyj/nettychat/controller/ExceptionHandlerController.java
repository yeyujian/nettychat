package com.yyj.nettychat.controller;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.yyj.nettychat.entity.ResponseResult;

/**
 * 全局异常处理类
 */
@ControllerAdvice
public class ExceptionHandlerController {
    private static Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

    private ResponseResult<String> getErrorRespone(String msg) {
        logger.debug(msg);
        ResponseResult<String> result = new ResponseResult<>();
        result.setStatus(10500);
        result.setMsg(msg);
        return result;
    }

    // 拦截未授权页面
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseResult<String> handleException(UnauthorizedException e) {
        return getErrorRespone(e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthorizationException.class)
    public ResponseResult<String> handleException2(AuthorizationException e) {
        return getErrorRespone(e.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseResult<String> uploadException(MaxUploadSizeExceededException e) {
        return getErrorRespone(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseResult<String> exceptionHandler(Exception e) {
        return getErrorRespone(e.getMessage());
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseResult<String> runtimeExceptionHandler(RuntimeException e) {
        return getErrorRespone(e.getMessage());
    }
}