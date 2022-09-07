package com.a243.magicbazaar.config.exception;

import com.a243.magicbazaar.util.code.BusinessCode;
import com.a243.magicbazaar.util.result.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局异常处理
 * 对控制层抛出的异常进行处理并返回视图层错误信息
 */
@RestControllerAdvice(value = {"com.a243.magicbazaar.controller"})
public class GlobalExceptionHandler {

    /**
     * 购物车异常
     * cookie转换发生错误
     * @param ex
     * @param <T>
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({JsonProcessingException.class})
    public <T> Result<T> JsonProcessingExceptionHandler(JsonProcessingException ex) {
        return Result.fail("购物车发生错误");
    }

    /**
     * 系统异常
     * 对Exception异常进行处理并返回视图层信息
     * @param ex
     * @param <T>
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({Exception.class})
    public <T> Result<T> ExceptionHandler(Exception ex) {
        return Result.fail(BusinessCode.SYSTEM_ERROR);
    }

}
