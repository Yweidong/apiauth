package com.example.apiauth.controller;

import com.example.apiauth.common.RedisKeyCommon;
import com.example.apiauth.utils.DeshfuUtil;
import com.example.apiauth.utils.TokenGenerateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @program: apiauth
 * @description:
 * @author: ywd
 * @contact:1371690483@qq.com
 * @create: 2020-11-19 09:54
 **/
@RestController
@RequestMapping("/oauth2")
public class AuthController{
    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping("/authorize")
    public void authorize(HttpServletRequest request,HttpServletResponse response) throws IOException {

        response.sendRedirect("http://optiontest.easyong.cn/admin/auth/auth.html?client_id="+request.getParameter("client_id")+"&redirect_uri="+
                request.getParameter("redirect_uri"));

    }

    //重定向同意后获取code
    @GetMapping("/getcode")
    public void getRedirectCode(@RequestParam("redirect_uri") String redirect_uri,
                                @RequestParam("client_id") String client_id,
                                HttpServletResponse response) throws IOException {
            String code = DeshfuUtil.encrypt(client_id);
           redisTemplate.opsForValue().set(RedisKeyCommon.OAUTH_CODE+client_id,code,300, TimeUnit.SECONDS);
           response.setHeader("content-type","text/html;charset=utf-8");
        response.sendRedirect(redirect_uri+"?code="+code);
    }

    @PostMapping("access_token")
    public HashMap<Object, Object> getCodeAccessToken(@RequestParam("client_id") String client_id) {
        HashMap<Object, Object> map = new HashMap<>();
        String authToken = TokenGenerateUtil.createAccessToken();
        redisTemplate.delete(RedisKeyCommon.OAUTH_CODE+client_id);
        map.put("access_token",authToken);
        map.put("expire_in",3600);
        map.put("reflash_token",redisTemplate.opsForHash().get(RedisKeyCommon.CODE_TOKEN+client_id,"reflash_token"));
        return map;
    }

    //通过刷新token来重新获取access_token凭证
    @PostMapping("reflash_token")
    public HashMap<Object, Object> reflash_token(@RequestParam("reflash_token") String reflash_token) {
        HashMap<Object, Object> map = new HashMap<>();
        String authToken = TokenGenerateUtil.createAccessToken();
        map.put("access_token",authToken);
        map.put("expire_in",3600);
        map.put("reflash_token",reflash_token);
        return map;

    }


}
