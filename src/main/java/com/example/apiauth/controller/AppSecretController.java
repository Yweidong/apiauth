package com.example.apiauth.controller;

import com.example.apiauth.utils.TokenGenerateUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @program: apiauth
 * @description:
 * @author: ywd
 * @contact:1371690483@qq.com
 * @create: 2020-11-20 10:54
 **/
@RestController
@RequestMapping("/appsecret")
public class AppSecretController {

    @PostMapping("/access_token")
    public HashMap<String,Object> getAppSecretAccessToken(@RequestParam("appid") String appid,
                                                          @RequestParam("appsecret") String appsecret,
                                                          @RequestParam("grant_type") String grant_type) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("access_token", TokenGenerateUtil.createAccessToken());
        map.put("expire_in",3600);
        return map;
    }
}
