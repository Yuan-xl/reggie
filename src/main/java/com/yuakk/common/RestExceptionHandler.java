package com.yuakk.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yuakk
 */
@RestControllerAdvice
public class RestExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());


    /**
     *请求参数绑定到对象
     */
    @ExceptionHandler(BindException.class)
    public GlobalResult<String> formDaraParamsException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(o -> o.getField() + o.getDefaultMessage()).toList();
        List<Object> list = new ArrayList<>(collect);
        logger.warn("请求参数绑定到对象"+e.getMessage());
        logger.warn(String.valueOf(e.getClass()));
        return GlobalResult.failViolation(ErrorCodeEnum.USER_ERROR_A0421.getCode(),list);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public GlobalResult<String> noHandlerFoundException(NoHandlerFoundException e) {
        logger.warn("NoHandlerFoundException"+e.getMessage());
        logger.warn(String.valueOf(e.getClass()));
        return GlobalResult.fail(ErrorCodeEnum.USER_ERROR_A0440.getCode(),ErrorCodeEnum.USER_ERROR_A0440.getMessage());
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public GlobalResult<String> servletRequestBindingExceptionException(ServletRequestBindingException e) {
        logger.warn("ServletRequestBindingException"+e.getMessage());
        logger.warn(String.valueOf(e.getClass()));
        return GlobalResult.fail(ErrorCodeEnum.USER_ERROR_A0440.getCode(),ErrorCodeEnum.USER_ERROR_A0440.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public GlobalResult<String> missingServletRequestParameterException(MissingServletRequestParameterException e) {
        logger.warn("MissingServletRequestParameterException"+e.getMessage());
        logger.warn(String.valueOf(e.getClass()));
        return GlobalResult.fail(ErrorCodeEnum.USER_ERROR_A0440.getCode(),ErrorCodeEnum.USER_ERROR_A0440.getMessage());
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public GlobalResult<String> missingPathVariableException(MissingPathVariableException e) {
        logger.warn("MissingPathVariableException"+e.getMessage());
        logger.warn(String.valueOf(e.getClass()));
        return GlobalResult.fail(ErrorCodeEnum.USER_ERROR_A0440.getCode(),ErrorCodeEnum.USER_ERROR_A0440.getMessage());
    }

    /**
     *自定义
     */
    @ExceptionHandler(BizException.class)
    public GlobalResult<String> businessException(BizException e) {
        return GlobalResult.fail(e.getErrorCode(),e.getErrorMsg());
    }

    /**
     * 处理空指针的异常
     */
    @ExceptionHandler(value =NullPointerException.class)
    public GlobalResult<String> exceptionHandler(NullPointerException e)  {
        logger.error("处理空指针异常"+e.getMessage());
        logger.error(String.valueOf(e.getClass()));
        return GlobalResult.fail(ErrorCodeEnum.USER_ERROR_0001.getCode(),ErrorCodeEnum.USER_ERROR_0001.getMessage());
    }

    /**
     * 处理索引越界异常
     */
    @ExceptionHandler(value =IndexOutOfBoundsException.class)
    public GlobalResult<String> exceptionHandler(IndexOutOfBoundsException e){
        logger.error("处理索引越界异常"+e.getMessage());
        logger.error(String.valueOf(e.getClass()));
        return GlobalResult.fail(ErrorCodeEnum.USER_ERROR_0001.getCode(),ErrorCodeEnum.USER_ERROR_0001.getMessage());
    }

    /**
     * 处理类未找到异常
     */
    @ExceptionHandler(value =ClassNotFoundException.class)
    public GlobalResult<String> exceptionHandler(ClassNotFoundException e)  {
        logger.error("处理类未找到异常"+e.getMessage());
        logger.error(String.valueOf(e.getClass()));
        return GlobalResult.fail(ErrorCodeEnum.USER_ERROR_0001.getCode(),ErrorCodeEnum.USER_ERROR_0001.getMessage());
    }

    @ExceptionHandler(value = DuplicateKeyException.class)
    public GlobalResult<String> sqlIntegrityConstraintViolationException(DuplicateKeyException e)  {
        logger.error("DuplicateKeyException");
        logger.error(String.valueOf(e.getClass()));
        logger.error(e.getMessage());
        String s="Duplicate entry";
        if (e.getMessage().contains(s)) {
            String[] split = e.getMessage().split(" ");
            String name = split[9];
            int length = name.length();
            String msg = name.substring(1, length - 1) + "已存在";

            return GlobalResult.fail(ErrorCodeEnum.USER_ERROR_A0111.getCode(),msg);
        }
        return GlobalResult.fail(ErrorCodeEnum.USER_ERROR_0001.getCode(),ErrorCodeEnum.USER_ERROR_0001.getMessage());
    }

    /**
     * 处理IO异常
     */
    @ExceptionHandler(value = IOException.class)
    public GlobalResult<String> exceptionHandler(IOException e)  {
        logger.error("IO异常"+e.getMessage());
        logger.error(String.valueOf(e.getClass()));
        return GlobalResult.fail(ErrorCodeEnum.USER_ERROR_0001.getCode(),ErrorCodeEnum.USER_ERROR_0001.getMessage());
    }

    /**
     * 默认全局异常处理。
     * @param e the e
     * @return ResultData
     */
    @ExceptionHandler(Exception.class)
    public GlobalResult<String> exception(Exception e) {
        logger.error("其他异常:{}",e.getMessage());
        logger.error(String.valueOf(e.getClass()));
        return GlobalResult.fail(ErrorCodeEnum.USER_ERROR_0001.getCode(),ErrorCodeEnum.USER_ERROR_0001.getMessage());
    }


}
