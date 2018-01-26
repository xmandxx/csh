package com.xmwang.cyh;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.xmwang.cyh.utils.ActivityManager;

/**
 * Created by xmWang on 2018/1/18.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().removeActivity(this);
    }
}
