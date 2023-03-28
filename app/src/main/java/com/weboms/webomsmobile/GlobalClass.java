package com.weboms.webomsmobile;

import android.app.Application;

public class GlobalClass extends Application {
    private String user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
