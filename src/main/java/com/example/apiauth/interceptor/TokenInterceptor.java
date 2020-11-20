package com.example.apiauth.interceptor;

import com.alibaba.druid.util.StringUtils;
import com.example.apiauth.common.RedisKeyCommon;
import com.example.apiauth.utils.DeshfuUtil;

import com.example.apiauth.utils.TokenGenerateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
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
            case "app_id_secret":
                return getAppSecretToken(request,response);
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
        String reflash_token = TokenGenerateUtil.createAuthToken();
        redisTemplate.opsForHash().put(RedisKeyCommon.CODE_REFLASH,reflash_token,client_id);
        redisTemplate.opsForHash().put(RedisKeyCommon.CODE_TOKEN+client_id,"reflash_token",reflash_token);
        redisTemplate.expire(RedisKeyCommon.CODE_TOKEN+client_id,30,TimeUnit.DAYS);
        redisTemplate.expire(RedisKeyCommon.CODE_REFLASH,30,TimeUnit.DAYS);

        return true;
    }

    public boolean getAppSecretToken(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String appid = request.getParameter("appid");
        if(StringUtils.isEmpty(appid)) {
            response.getWriter().write("appid is must required");
            return false;
        }
        String appsecret = request.getParameter("appsecret");
        if(StringUtils.isEmpty(appsecret)) {
            response.getWriter().write("appsecret is must required");
            return false;
        }
        Calendar cal = Calendar.getInstance();
        int i = cal.get(Calendar.MONTH)+1;
        if(redisTemplate.opsForHash().hasKey(RedisKeyCommon.APP_ID_SECRET+i,appid)) {
            String o = (String) redisTemplate.opsForHash().get(RedisKeyCommon.APP_ID_SECRET + i, appid);
            if(o.equals("30")) {
                response.getWriter().write("This month the call has reached its limit");
                return false;
            }
            redisTemplate.opsForHash().put(RedisKeyCommon.APP_ID_SECRET+i,appid,String.valueOf(Integer.parseInt(o)+1));
        }else {
            redisTemplate.opsForHash().put(RedisKeyCommon.APP_ID_SECRET+i,appid,"1");
        }
        return true;
    }
}
