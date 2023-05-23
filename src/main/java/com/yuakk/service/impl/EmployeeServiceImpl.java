package com.yuakk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuakk.common.BizException;
import com.yuakk.common.ErrorCodeEnum;
import com.yuakk.mapper.EmployeeMapper;
import com.yuakk.pojo.Employee;
import com.yuakk.service.EmployeeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Map;

/**
 * @author yuakk
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Resource
    private EmployeeMapper employeeMapper;
    @Override
    public Employee getEmployee(Employee employee) {
        String password=employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee employeeSelect = employeeMapper.selectOne(lambdaQueryWrapper);
        if (employeeSelect == null) {
            throw new BizException(400, ErrorCodeEnum.USER_ERROR_A0201);
        }
        if (!employeeSelect.getPassword().equals(password)){
            throw new BizException(400,ErrorCodeEnum.USER_ERROR_A0210);
        }
        if (employeeSelect.getStatus() == 0){
            throw new BizException(400,ErrorCodeEnum.USER_ERROR_A0202);
        }
        return employeeSelect;
    }


    @Override
    public boolean save(HttpServletRequest request, Employee employee) {
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        int insert = employeeMapper.insert(employee);
        if (insert <= 0){
            throw new BizException(400, ErrorCodeEnum.USER_ERROR_0001);
        }
        return true;
    }

    @Override
    public Page<Map<String,Object>> getEmployeeByPage(int page, int pageSize, String name) {
        Page<Map<String,Object>> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName,name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        Page<Map<String, Object>> mapPage = employeeMapper.selectMapsPage(pageInfo, queryWrapper);
        long total = mapPage.getTotal();
        System.out.println(total);
        return mapPage;
    }

    @Override
    public boolean update(HttpServletRequest request,Employee employee) {
        return employeeMapper.updateById(employee)>0;
    }
}
