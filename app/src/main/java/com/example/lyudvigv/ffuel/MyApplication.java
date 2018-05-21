package com.example.lyudvigv.ffuel;

import android.app.Application;

import com.example.lyudvigv.ffuel.loginAndRegistration.UserInfo;

/**
 * Created by LyudvigV on 12/11/2017.
 */

public class MyApplication extends Application {

    private UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }



}
