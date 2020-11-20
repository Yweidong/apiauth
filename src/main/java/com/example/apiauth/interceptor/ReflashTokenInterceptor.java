package com.example.apiauth.interceptor;

import com.alibaba.druid.util.StringUtils;
import com.example.apiauth.common.RedisKeyCommon;
import com.example.apiauth.utils.DeshfuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: apiauth
 * @description:
 * @author: ywd
 * @contact:1371690483@qq.com
 * @create: 2020-11-20 09:49
 **/
public class ReflashTokenInterceptor implements HandlerInterceptor {
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String reflash_token = request.getParameter("reflash_token");
        if(StringUtils.isEmpty(reflash_token)) {
            response.getWriter().write("reflash_token is must required");
            return false;
        }
        if(!redisTemplate.opsForHash().hasKey(RedisKeyCommon.CODE_REFLASH,reflash_token)) {
            response.getWriter().write("please,afresh authorization");
            return false;
        }
        String decrypt = DeshfuUtil.decrypt(reflash_token);
        if(decrypt == null) {
            response.getWriter().write("reflash_token is error");
            return false;
        }
        if(!decrypt.contains("_")) {
            response.getWriter().write("reflash_token is format error");
            return false;
        }
        if(decrypt.indexOf("reflash/*$")== -1) {
            response.getWriter().write("reflash_token is  error");
            return false;
        }



        return true;
    }
}
