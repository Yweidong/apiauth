package com.example.apiauth.interceptor;

import com.alibaba.druid.util.StringUtils;
import com.example.apiauth.common.RedisKeyCommon;
import com.example.apiauth.utils.DeshfuUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @program: apiauth
 * @description: access_token拦截器
 * @author: ywd
 * @contact:1371690483@qq.com
 * @create: 2020-11-19 14:30
 **/
public class TokenInterceptor implements HandlerInterceptor {
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String grant_type = request.getParameter("grant_type");//判断获取access_token的方式
        if(StringUtils.isEmpty(grant_type)) {

            return false;
        }
        switch (grant_type) {
            //通过code来获取凭证
            case "authorization_code":
               return getCodeToken(request,response);
        }

        return false;
    }

    public  boolean getCodeToken(HttpServletRequest request,HttpServletResponse response) throws Exception {
        String code = request.getParameter("code");
        if(StringUtils.isEmpty(code)) {
            response.getWriter().write("code is must required");
            return false;
        }

        if(DeshfuUtil.decrypt(code) == null) {
            response.getWriter().write("code is error");
            return false;
        }

        String client_id = request.getParameter("client_id");
        if(StringUtils.isEmpty(client_id)) {
            response.getWriter().write("client_id is must required");
            return false;
        }


        if(!redisTemplate.persist(RedisKeyCommon.OAUTH_CODE+client_id)) {
            response.getWriter().write("code is unallowed");
            return false;
        }


        String client_secret = request.getParameter("client_secret");//密钥后期可以加认证
        if(StringUtils.isEmpty(client_secret)) {
            return false;
        }

        //获取刷新的token


        return true;
    }
}
