package com.yuakk.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 自定义元数据对象处理器
 * @author yuakk
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {

        Object createTime = getFieldValByName("createTime", metaObject);
        boolean createTime1 = metaObject.hasSetter("createTime");
        log.info("creat1Time{}",createTime1);
        if (createTime1 && Objects.isNull(createTime)){
            metaObject.setValue("createTime", LocalDateTime.now());
        }

        Object updateTime = getFieldValByName("updateTime", metaObject);
        boolean updateTime1 = metaObject.hasSetter("updateTime");
        if (updateTime1 && Objects.isNull(updateTime)){
            metaObject.setValue("updateTime", LocalDateTime.now());
        }

        Object createUser = getFieldValByName("createUser", metaObject);
        boolean createUser1 = metaObject.hasSetter("createUser");
        if (createUser1 && Objects.isNull(createUser)){
            metaObject.setValue("createUser", BaseContext.getCurrentId());
        }

        Object updateUser = getFieldValByName("updateUser", metaObject);
        boolean updateUser1 = metaObject.hasSetter("updateUser");
        if (updateUser1 && Objects.isNull(updateUser)){
            metaObject.setValue("updateUser", BaseContext.getCurrentId());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }
}
