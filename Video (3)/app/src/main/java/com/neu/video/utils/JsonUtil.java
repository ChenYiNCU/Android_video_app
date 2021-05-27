package com.neu.video.utils;


import android.util.Log;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by 86189 on 2021/5/19.
 */

public class JsonUtil {
    /**
     * JSON 转 POJO
     */
    public static <T> T getObject(String pojo, Class<T> tclass) {
        try {
            return JSONObject.parseObject(pojo, tclass);
        } catch (Exception e) {
            Log.e("error","转 JSON 失败");
        }
        return null;
    }

    /**
     * POJO 转 JSON
     */
    public static <T> String getJson(T tResponse){
        String pojo = JSONObject.toJSONString(tResponse);
        return pojo;
    }

}
