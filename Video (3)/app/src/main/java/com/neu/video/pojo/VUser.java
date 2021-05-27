package com.neu.video.pojo;

public class VUser {
    private int id;
    private String uname;
    private String upwd;
    private String uimg;
    private String usex;
    private int uage;
    private String utel;

    public VUser() {
    }

    public VUser(int id, String uname, String upwd, String uimg, String usex, int uage, String utel) {
        this.id = id;
        this.uname = uname;
        this.upwd = upwd;
        this.uimg = uimg;
        this.usex = usex;
        this.uage = uage;
        this.utel = utel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUpwd() {
        return upwd;
    }

    public void setUpwd(String upwd) {
        this.upwd = upwd;
    }

    public String getUimg() {
        return uimg;
    }

    public void setUimg(String uimg) {
        this.uimg = uimg;
    }

    public String getUsex() {
        return usex;
    }

    public void setUsex(String usex) {
        this.usex = usex;
    }

    public int getUage() {
        return uage;
    }

    public void setUage(int uage) {
        this.uage = uage;
    }

    public String getUtel() {
        return utel;
    }

    public void setUtel(String utel) {
        this.utel = utel;
    }

    @Override
    public String toString() {
        return "VUser{" +
                "id=" + id +
                ", uname='" + uname + '\'' +
                ", upwd='" + upwd + '\'' +
                ", uimg='" + uimg + '\'' +
                ", usex='" + usex + '\'' +
                ", uage=" + uage +
                ", utel='" + utel + '\'' +
                '}';
    }
}
