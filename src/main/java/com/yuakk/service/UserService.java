package com.yuakk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuakk.pojo.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author yuakk
 */
public interface UserService extends IService<User> {
    /**
     * 阿里云短信服务
     * @param user user
     * @param session session
     * @return boolean
     */
    boolean sendMsg(@RequestBody User user, HttpSession session);

    /**
     * 登录
     * @param map 手机号和code
     * @param session session
     * @return boolean
     */
    User login(Map<String, Map<String,String>> map, HttpSession session);
}
