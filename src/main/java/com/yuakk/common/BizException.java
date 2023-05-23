package com.yuakk.common;

import lombok.Data;

/**
 * @author yuakk
 */
@Data
public class BizException extends RuntimeException{
    /**
    *状态码
    */
    public int status;
    /**
     * 错误码
     */
    protected String errorCode;
    /**
     * 错误信息
     */
    protected String errorMsg;

    public BizException(String errorCode,String errorMsg) {
        this.status=400;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    public BizException(int status,ErrorCodeEnum code) {
        this.status=status;
        this.errorCode = code.getCode();
        this.errorMsg = code.getMessage();
    }
}