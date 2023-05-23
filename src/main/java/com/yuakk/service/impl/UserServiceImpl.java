package com.yuakk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuakk.common.BizException;
import com.yuakk.common.ErrorCodeEnum;
import com.yuakk.common.SmsUtils;
import com.yuakk.common.ValidateCodeUtils;
import com.yuakk.mapper.UserMapper;
import com.yuakk.pojo.User;
import com.yuakk.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author yuakk
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public boolean sendMsg(User user, HttpSession session) {
        String phone=user.getPhone();

        if (StringUtils.isNotEmpty(phone)){
            //生成随机验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);
            //调用阿里云短信服务
            SmsUtils.sendMessage("阿里云短信测试", "测试专用模板", phone, code);
            //将验证码存储在session
            session.setAttribute(phone, code);
            return true;
        }
        return false;
    }

    @Override
    public User login(Map<String, Map<String, String>> map, HttpSession session) {
        Map<String, String> check = map.get("phone");
        //拿到手机号
        String phone = check.get("phone");
        //拿到验证码
        String code = check.get("code");
        //进行验证码比对
        String sendCode = (String) session.getAttribute(phone);
        if (sendCode!=null && sendCode.equals(code)){
            //登陆成功,是否是新用户
            LambdaQueryWrapper<User> userLambdaQueryWrapper =new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getPhone, phone);
            User user = this.getOne(userLambdaQueryWrapper);
            if (user==null){
                //注册
                user=new User();
                user.setPhone(phone);
                this.save(user);
            }
            session.setAttribute("user", user.getId());
            return user;
        }
        throw new BizException(400, ErrorCodeEnum.USER_ERROR_A0200);
    }
}
