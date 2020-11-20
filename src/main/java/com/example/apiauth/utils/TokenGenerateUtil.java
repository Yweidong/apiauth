package com.example.apiauth.utils;

/**
 * @program: apiauth
 * @description:
 * @author: ywd
 * @contact:1371690483@qq.com
 * @create: 2020-11-20 09:25
 **/
public class TokenGenerateUtil {


    private static final SnowflakeIdWorkerUtil idWorker;
    private static final String TOKEN_KEY = "reflash/*$";
    static {
        idWorker = new SnowflakeIdWorkerUtil(0, 0);
    }

    public static String createAuthToken() {

        String encrypt = DeshfuUtil.encrypt(TOKEN_KEY+"_"+idWorker.nextId());
        return encrypt;
    }

    public static String createAccessToken() {
        String l = System.currentTimeMillis()/1000+"_"+idWorker.nextId();
        String access_token = DeshfuUtil.encrypt(l);
        return access_token;
    }


}
