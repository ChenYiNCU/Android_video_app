package com.neu.video.pojo;


public class Video {
    private int vid;
    private String vname;
    private String vtime;
    private String vdesc;
    private String vdirector;
    private String vactor;
    private String vurl;
    private String vimg;
    private int playNum;
    private int storeNum;
    private VType vtype= new VType();

    public Video() {
    }

    public Video(int vid, String vname, String vtime, String vdesc, String vdirector, String vactor, String vurl, String vimg, int playNum, int storeNum, VType vtype) {
        this.vid = vid;
        this.vname = vname;
        this.vtime = vtime;
        this.vdesc = vdesc;
        this.vdirector = vdirector;
        this.vactor = vactor;
        this.vurl = vurl;
        this.vimg = vimg;
        this.playNum = playNum;
        this.storeNum = storeNum;
        this.vtype = vtype;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getVtime() {
        return vtime;
    }

    public void setVtime(String vtime) {
        this.vtime = vtime;
    }

    public String getVdesc() {
        return vdesc;
    }

    public void setVdesc(String vdesc) {
        this.vdesc = vdesc;
    }

    public String getVdirector() {
        return vdirector;
    }

    public void setVdirector(String vdirector) {
        this.vdirector = vdirector;
    }

    public String getVactor() {
        return vactor;
    }

    public void setVactor(String vactor) {
        this.vactor = vactor;
    }

    public String getVurl() {
        return vurl;
    }

    public void setVurl(String vurl) {
        this.vurl = vurl;
    }

    public String getVimg() {
        return vimg;
    }

    public void setVimg(String vimg) {
        this.vimg = vimg;
    }

    public int getPlayNum() {
        return playNum;
    }

    public void setPlayNum(int playNum) {
        this.playNum = playNum;
    }

    public int getStoreNum() {
        return storeNum;
    }

    public void setStoreNum(int storeNum) {
        this.storeNum = storeNum;
    }

    public VType getVtype() {
        return vtype;
    }

    public void setVtype(VType vtype) {
        this.vtype = vtype;
    }

    @Override
    public String toString() {
        return "Video{" +
                "vid=" + vid +
                ", vname='" + vname + '\'' +
                ", vtime='" + vtime + '\'' +
                ", vdesc='" + vdesc + '\'' +
                ", vdirector='" + vdirector + '\'' +
                ", vactor='" + vactor + '\'' +
                ", vurl='" + vurl + '\'' +
                ", vimg='" + vimg + '\'' +
                ", playNum=" + playNum +
                ", storeNum=" + storeNum +
                ", vtype=" + vtype +
                '}';
    }
}
