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
 菜品
 * @author yuakk
 */
@Data
public class Dish implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     *菜品名称
     */
    private String name;

    /**
     *菜品分类id
     */
    private Long categoryId;

    /**
     *菜品价格
     */
    private BigDecimal price;

    /**
     *商品码
     */
    private String code;

    /**
     *图片
     */
    private String image;

    /**
     *描述信息
     */
    private String description;

    /**
     *0 停售 1 起售
     */
    private Integer status;

    /**
     *顺序
     */
    private Integer sort;


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
