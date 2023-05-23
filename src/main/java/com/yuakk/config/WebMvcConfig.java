package com.yuakk.config;

import com.yuakk.common.JacksonObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author yuakk
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private LoginCheckInterceptor loginCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//         注册拦截规则
        InterceptorRegistration ir = registry.addInterceptor(loginCheckInterceptor);
//         拦截路径，开放api请求的路径都拦截
        ir.addPathPatterns("/employee/**","/category/**","/dish/**","/setmeal/**","/user/**","/addressBook/**","/shoppingCart/**",
                "/order/**","/orderDetail/**");
//         不拦截路径，如：注册、登录、忘记密码等
        ir.excludePathPatterns("/employee/login","/employee/logout","/static/backend/**","/static/front/**","/user/sendMsg",
                "/user/login");
    }

    /**
     *扩展mvc消息转换器
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，将java对象转换为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将消息转换器追加到mvc框架的转换器集合中
        converters.add(0, messageConverter);
    }
}
