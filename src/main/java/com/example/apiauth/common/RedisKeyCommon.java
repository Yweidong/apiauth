package com.example.apiauth.common;

/**
 * @program: apiauth
 * @description:
 * @author: ywd
 * @contact:1371690483@qq.com
 * @create: 2020-11-19 10:47
 **/
public class RedisKeyCommon {
    public static final String OAUTH_CLIENT_ID = "client_id"; //存储client_id
    public static final String OAUTH_CODE = "temporary_code:";//临时凭证code
    public static final String CODE_REFLASH = "Access_token:reflash";
    public static final String CODE_TOKEN = "Access_token:token:";

    public static final String APP_ID_SECRET="appidsecret:";
}
