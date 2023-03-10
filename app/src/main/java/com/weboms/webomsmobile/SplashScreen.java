package com.weboms.webomsmobile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;



public class SplashScreen extends Activity {
    Intent callThis;
    LinearLayout bgLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        bgLinearLayout = (LinearLayout) findViewById(R.id.bgLinearLayout);
        bgLinearLayout.setBackgroundColor(Color.parseColor("#Ffffff"));
        callThis = new Intent(".Login");
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                finally {
                    startActivity(callThis);
                    finish();
                }
            }
        };
        timer.start();
    }
}
