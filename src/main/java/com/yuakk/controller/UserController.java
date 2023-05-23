package com.yuakk.controller;

import com.yuakk.pojo.User;
import com.yuakk.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author yuakk
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/sendMsg")
    public boolean sendMsg(@RequestBody User user, HttpSession session){
        return userService.sendMsg(user,session);
    }

    @PostMapping("/login")
    public User login(@RequestBody Map<String,Map<String,String>> map, HttpSession session){
        return userService.login(map, session);
    }

    @PostMapping("/loginout")
    public boolean loginOut(HttpSession httpSession){
        httpSession.removeAttribute("user");
        return true;
    }
}
