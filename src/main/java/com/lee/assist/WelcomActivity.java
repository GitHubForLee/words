package com.lee.assist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.lee.words.MainActivity;
import com.lee.words.R;

/**
 * Created by Administrator on 2016/9/21.
 */
public class WelcomActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(WelcomActivity.this,MainActivity.class));
                finish();
            }
        }, 2000);
    }
}
