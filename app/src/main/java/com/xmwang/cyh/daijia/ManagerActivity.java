package com.xmwang.cyh.daijia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xmwang.cyh.BaseActivity;
import com.xmwang.cyh.R;
import com.xmwang.cyh.activity.user.LoginActivity;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.model.Charging;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xmWang on 2017/12/25.
 */

public class ManagerActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daijia_manager);
        ButterKnife.bind(this);
        init();

    }
    @OnClick({R.id.ll_order, R.id.ll_acc, R.id.btn_logout,R.id.title_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.ll_order:
                startActivity(new Intent(this, OrderActivity.class));
                break;
            case R.id.ll_acc:
                startActivity(new Intent(this, AccActivity.class));
                break;
            case R.id.btn_logout:
                logout();
                break;
        }
    }

    private void init(){

    }


    private void logout(){
        Data.instance.setUserInfo(null);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}
