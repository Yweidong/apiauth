package com.example.apiauth.interceptor;

import com.alibaba.druid.util.StringUtils;
import com.example.apiauth.common.RedisKeyCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
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

public class Oauth2Interceptor implements HandlerInterceptor {
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String client_id = request.getParameter("client_id");
        if(StringUtils.isEmpty(client_id)) {
            response.getWriter().write("be shor of client_id");
            return false;
        }
        /**
         * 判断client_id 是否是有效值 (这里暂时适用redis)
         *
         */
        Boolean member = redisTemplate.opsForSet().isMember(RedisKeyCommon.OAUTH_CLIENT_ID, client_id);

        if(member) {
            return true;
        }else {
            response.getWriter().write("client_id is not exists2");
            return false;
        }
//        return true;
    }
}
