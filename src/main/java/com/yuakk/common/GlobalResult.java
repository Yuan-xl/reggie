package com.yuakk.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author yuakk
 */
@Data
@AllArgsConstructor
public class GlobalResult<T> {

    /**
     * 响应业务状态
     */
    private Integer status;

    private String code;
    private String message;

    /**
     * 响应中的数据
     */
    private Object data;
    private String time;

    public GlobalResult (){
        Date currentTime = new Date();
        SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.time= format0.format(currentTime.getTime());
    }


    public static <T> GlobalResult<T> success(T data) {
        GlobalResult<T> resultData = new GlobalResult<>();
        resultData.setStatus(200);
        resultData.setCode(ErrorCodeEnum.SUCCESS.getCode());
        resultData.setMessage(ErrorCodeEnum.SUCCESS.getMessage());
        resultData.setData(data);
        return resultData;
    }

    public static <T> GlobalResult<T> fail(String code, String message) {
        GlobalResult<T> resultData = new GlobalResult<T>();
        resultData.setStatus(400);
        resultData.setCode(code);
        resultData.setMessage(message);
        return resultData;
    }

    public static <T> GlobalResult<T> failViolation(String code, List<Object> message) {
        GlobalResult<T> resultData = new GlobalResult<T>();
        resultData.setStatus(400);
        resultData.setCode(code);
        resultData.setMessage(Arrays.toString(message.toArray()));
        return resultData;
    }
}
