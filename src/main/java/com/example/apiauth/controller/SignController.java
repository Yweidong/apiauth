package com.example.apiauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: apiauth
 * @description:
 * @author: ywd
 * @contact:1371690483@qq.com
 * @create: 2020-11-20 11:55
 **/
@RestController
@RequestMapping("/api/sign/v1")
public class SignController {

    @GetMapping("/getuser")
    public void getUser() {

    }
}
