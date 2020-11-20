package com.example.apiauth.interceptor;

import com.alibaba.druid.util.StringUtils;
import com.example.apiauth.utils.DeshfuUtil;
import com.example.apiauth.utils.SignUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * @program: apiauth
 * @description: 签名拦截
 * @author: ywd
 * @contact:1371690483@qq.com
 * @create: 2020-11-20 11:57
 **/
public class SignInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String sign = request.getParameter("sign");
        if(StringUtils.isEmpty(sign)) {
            response.getWriter().write("sign is must required");
            return false;
        }

        String timestamp = request.getParameter("timestamp");
        if(StringUtils.isEmpty(timestamp)) {
            response.getWriter().write("timestamp is must required");
            return false;
        }

        String access_token = request.getParameter("access_token");
        if(StringUtils.isEmpty(timestamp)) {
            response.getWriter().write("access_token is must required");
            return false;
        }
        String decrypt = DeshfuUtil.decrypt(access_token);
        if(decrypt == null) {
            response.getWriter().write("access_token is error");
            return false;
        }
        if(decrypt.contains("_")) {
            String s = decrypt.replaceAll("_(.*)", "");
            if(System.currentTimeMillis()/1000 - Long.parseLong(s) >=3600) {
                response.getWriter().write("access_token is disabled");
                return false;
            }
        }else {
            response.getWriter().write("access_token is error");
            return false;
        }

        Enumeration<String> pNames = request.getParameterNames();
        HashMap<String, Object> map = new HashMap<>();
        while(pNames.hasMoreElements()) {
            String pName =  pNames.nextElement();
            if("sign".equals(pName)) continue;
            String pValue = request.getParameter(pName);
            if(pValue == null) continue;
            map.put(pName,pValue);
        }
        if(SignUtil.createSign(map)!=sign) {
            response.getWriter().write("Signature encryption error");
            return false;
        }

        return true;
    }


}
