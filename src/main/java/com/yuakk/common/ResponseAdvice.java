package com.yuakk.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Arrays;

/**
 * @author yuakk
 */
@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {
    private static final String[] EXCLUDE = {
            "SwaggerConfigResource",
            "MultipleOpenApiWebMvcResource",
            "ApiResourceController",
            "OpenApiWebMvcResource",
            "SwaggerWelcomeWebMvc"
    };
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {

        if (Arrays.asList(EXCLUDE).contains(methodParameter.getDeclaringClass().getSimpleName())) {
            return o;
        }
        if(o instanceof GlobalResult){
            System.out.println(o);
            return o;
        }
        if(o instanceof String){
            return objectMapper.writeValueAsString(GlobalResult.success(o));
        }
        return GlobalResult.success(o);
    }
}
