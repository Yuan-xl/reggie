package com.yuakk.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuakk.common.BizException;
import com.yuakk.common.ErrorCodeEnum;
import com.yuakk.pojo.Employee;
import com.yuakk.service.EmployeeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author yuakk
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request 请求
     * @param employee 员工
     * @return 员工
     */
    @PostMapping("/login")
    public Employee login(HttpServletRequest request, @RequestBody Employee employee){
        Employee user = employeeService.getEmployee(employee);
        if (user == null) {
            throw new BizException(400, ErrorCodeEnum.USER_ERROR_A0201);
        }
        request.getSession().setAttribute("employee", user.getId());
        return user;
    }

    @PostMapping("/logout")
    public boolean logout(HttpServletRequest request){

        request.getSession().removeAttribute("employee");
        return true;
    }

    @PostMapping
    public boolean save(HttpServletRequest request,@RequestBody Employee employee){
       return employeeService.save(request,employee);
    }

    @GetMapping("/page")
    public Page<Map<String,Object>> getEmployeeByPage(int page, int pageSize, String name){
        return employeeService.getEmployeeByPage(page, pageSize, name);
    }

    /**
     * 0禁用，1禁用
     */
    @PutMapping
    public boolean update(HttpServletRequest request,@RequestBody Employee employee){

        return employeeService.update(request, employee);
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id){
        return employeeService.getById(id);
    }

    


}
