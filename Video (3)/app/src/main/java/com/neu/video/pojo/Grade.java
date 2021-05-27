package com.neu.video.pojo;

public class Grade {
    private int pid;
    private double score;
    private int vid;

    public Grade() {
    }

    public Grade(int pid, double score, int vid) {
        this.pid = pid;
        this.score = score;
        this.vid = vid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "pid=" + pid +
                ", score=" + score +
                ", vid=" + vid +
                '}';
    }
}
