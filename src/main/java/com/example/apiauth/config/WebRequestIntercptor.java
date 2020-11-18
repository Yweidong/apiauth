package com.example.apiauth.config;

import com.example.apiauth.interceptor.ParamInterceptor;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: apiauth
 * @description: 拦截器
 * @author: ywd
 * @contact:1371690483@qq.com
 * @create: 2020-11-18 16:31
 **/
@Configuration
public class WebRequestIntercptor implements WebMvcConfigurer {


    //参数验证
    @Bean
    ParamInterceptor paramInterceptor() {
        return new ParamInterceptor();
    }

    //自定义的一些拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(paramInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/api/v1/auth/*");
    }


    //跨域支持
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET","POST","DELETE")
                .maxAge(3600*24);
    }

}
