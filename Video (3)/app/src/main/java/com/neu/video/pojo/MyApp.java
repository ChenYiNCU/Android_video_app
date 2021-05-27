package com.neu.video.pojo;

import android.app.Application;

/**
 * Created by 86189 on 2021/4/13.
 */

public class MyApp extends Application {
    public static MyApp mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }
    public static MyApp getInstance() {
        return mInstance;
    }

    private VUser vUser;
    private VAdmin vAdmin;

    public MyApp() {
    }

    public MyApp(VUser vUser, VAdmin vAdmin) {
        this.vUser = vUser;
        this.vAdmin = vAdmin;
    }

    public VUser getvUser() {
        return vUser;
    }

    public void setvUser(VUser vUser) {
        this.vUser = vUser;
    }

    public VAdmin getvAdmin() {
        return vAdmin;
    }

    public void setvAdmin(VAdmin vAdmin) {
        this.vAdmin = vAdmin;
    }

}

