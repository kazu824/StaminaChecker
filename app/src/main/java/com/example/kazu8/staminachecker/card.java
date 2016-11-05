package com.example.kazu8.staminachecker;


public class card {

    public String title;
    public int maxNum;
    public int rTime;
    public int sTime;
    public boolean alertCheck;
    public int alertTime;

    public card(String title,int maxNum,int rTime,int sTime,boolean alertCheck,int alertTime){
        this.title = title;
        this.maxNum = maxNum;
        this.rTime = rTime;
        this.sTime = sTime;
        this.alertCheck = alertCheck;
        this.alertTime = alertTime;
    }

}
