package com.yuakk.config;

import com.yuakk.common.BaseContext;
import com.yuakk.common.BizException;
import com.yuakk.common.ErrorCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
/**
 * @author yuakk
 */
@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String employee="employee";
        String user="user";
        if (request.getSession().getAttribute(employee) != null){
            Long employeeId = (Long) request.getSession().getAttribute(employee);
            BaseContext.setCurrentId(employeeId);
            return true;
        } else if (request.getSession().getAttribute(user) != null){
            Long userId = (Long) request.getSession().getAttribute(user);
            BaseContext.setCurrentId(userId);
            return true;
        } else {
            throw new BizException(400, ErrorCodeEnum.USER_ERROR_A0300);
        }
    }
}
