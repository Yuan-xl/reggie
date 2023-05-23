package com.yuakk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuakk.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yuakk
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
