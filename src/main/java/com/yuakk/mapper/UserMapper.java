package com.yuakk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuakk.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yuakk
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
