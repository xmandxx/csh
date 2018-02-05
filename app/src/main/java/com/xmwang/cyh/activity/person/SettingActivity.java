package com.xmwang.cyh.activity.person;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xmwang.cyh.BaseActivity;
import com.xmwang.cyh.R;
import com.xmwang.cyh.activity.user.LoginActivity;
import com.xmwang.cyh.common.Data;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.title_back, R.id.ll_profile, R.id.ll_address, R.id.ll_clean_cache, R.id.ll_about, R.id.btn_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.ll_profile:
                startActivity(new Intent(this,ProfileActivity.class));
                break;
            case R.id.ll_address:
                break;
            case R.id.ll_clean_cache:
                break;
            case R.id.ll_about:
                break;
            case R.id.btn_logout:
                logout();
                break;
        }
    }

    private void logout(){
        Data.instance.setUserInfo(null);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
