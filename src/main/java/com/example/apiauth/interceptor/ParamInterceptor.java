package com.example.apiauth.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: apiauth
 * @description: 参数拦截器
 * @author: ywd
 * @contact:1371690483@qq.com
 * @create: 2020-11-18 16:56
 **/
@Component

public class ParamInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        return true;
    }
}
