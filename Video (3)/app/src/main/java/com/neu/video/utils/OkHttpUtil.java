package com.neu.video.utils;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OkHttpUtil {
    private static OkHttpUtil okHttpUtil;
    private static OkHttpClient okHttpClient;
    private static Request request;
    private static Response response;
    private static String responseData;

    public static void instance() {
        okHttpUtil = new OkHttpUtil();

    }

    //RequestBody传JAVA对象
    public static String okHttpPostRequestBody(String url, String json) throws IOException {
        instance();
        okHttpClient = new OkHttpClient();//创建http客户端
        request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json"), json))
                .build();//创造http请求
        response = okHttpClient.newCall(request).execute();//执行发送的指令
        responseData=response.body().string();
        return responseData;
    }

    public static String okHttpPostRequestParam(String url, FormBody.Builder params) throws IOException {
        instance();
        okHttpClient = new OkHttpClient();//创建http客户端
        request = new Request.Builder()
                .url(url)//后端请求接口的地址
                .post(params.build())
                .build();//创造http请求
        response = okHttpClient.newCall(request).execute();//执行发送的指令
        responseData=response.body().string();
        return responseData;
    }

    //get
    public static String okHttpGet(String url) throws IOException {
        instance();
        okHttpClient = new OkHttpClient();//创建http客户端
        request = new Request.Builder()
                .url(url)
                .build();//创造http请求
        response = okHttpClient.newCall(request).execute();//执行发送的指令
        responseData=response.body().string();
        return responseData;
    }

}
