package com.neu.video.pojo;

import java.io.Serializable;
import java.util.List;

public class ResultDTO<T> implements Serializable {

    private int code = 0;   // 存储状态码
    private String msg = "";    // 存储状态信息
    private T data; //保存当个数据
    private List<T> datas;// 保存多个数据

    //有参、无参构造方法、set get方法

    public ResultDTO() {
    }

    public ResultDTO(int code, String msg, T data, List<T> datas) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.datas = datas;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }
}
