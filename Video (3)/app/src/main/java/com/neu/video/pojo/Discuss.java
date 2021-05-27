package com.neu.video.pojo;

public class Discuss {
    private int pid;
    private String uimg;     //头像
    private String uname;    //名字
    private String words;    //评论
    private int vid;

    public Discuss() {
    }

    public Discuss(int pid, String uimg, String uname, String words, int vid) {
        this.pid = pid;
        this.uimg = uimg;
        this.uname = uname;
        this.words = words;
        this.vid = vid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getUimg() {
        return uimg;
    }

    public void setUimg(String uimg) {
        this.uimg = uimg;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }
}
