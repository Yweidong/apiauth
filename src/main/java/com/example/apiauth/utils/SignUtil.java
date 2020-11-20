package com.example.apiauth.utils;

import org.springframework.util.DigestUtils;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @program: apiauth
 * @description: 生成签名
 * @author: ywd
 * @contact:1371690483@qq.com
 * @create: 2020-11-20 15:23
 **/
public class SignUtil {
    public static String createSign(Map<String,Object> params) {
        Set<String> keySet = params.keySet();
        Object[] keys = keySet.toArray();
        Arrays.sort(keys);
        StringBuffer buffer = new StringBuffer();
        boolean first = true;

        for (Object key : keys) {
            if(first) {
                first = false;
            }else {
                buffer.append("&");
            }
            buffer.append(key).append("=");
            Object value = params.get(key);
            String valueString = String.valueOf(value);
            buffer.append(valueString);
        }
        System.out.println(buffer);
        return DigestUtils.md5DigestAsHex(buffer.toString().getBytes()).toUpperCase();
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("token",123);
        map.put("sign",123);
        System.out.println(createSign(map));
    }
}
