package com.yuakk.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yuakk.pojo.Employee;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * @author yuakk
 */
public interface EmployeeService extends IService<Employee> {

    Employee getEmployee(Employee employee);

    boolean save(HttpServletRequest request, Employee employee);

    Page<Map<String,Object>> getEmployeeByPage(int page, int pageSize, String name);

    boolean update(HttpServletRequest request,Employee employee);
}
