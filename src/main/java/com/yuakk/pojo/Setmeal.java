package com.yuakk.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 套餐
 * @author yuakk
 */
@Data
public class Setmeal implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;


    /**
     * 分类id
     */
    private Long categoryId;



    /**
     * 套餐名称
     */
    private String name;


    /**
     *套餐价格
     */
    private BigDecimal price;


    /**
     *状态 0:停用 1:启用
     */
    private Integer status;


    /**
     *编码
     */
    private String code;

    /**
     *描述信息
     */
    private String description;



    /**
     * 图片
     */
    private String image;


    @TableField(fill = FieldFill.INSERT)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)		// 反序列化
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)		// 反序列化
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateTime;


    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     *是否删除
     */
    @TableLogic
    private Integer isDeleted;
}
