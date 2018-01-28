package com.xmwang.cyh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xmwang.cyh.activity.user.LoginActivity;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.activity.home.IndexActivity;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity {
    public static boolean isForeground = false;
    public static final String MESSAGE_RECEIVED_ACTION = "com.xmwang.cyh.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toActivity();
    }
    private void toActivity(){
        if (Data.instance.getIsLogin()){
//            Intent intent = new Intent(MainActivity.this, IndexActivity.class);
            Intent intent = new Intent(MainActivity.this, com.xmwang.cyh.daijia.IndexActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }
    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }
}
