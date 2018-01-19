package com.xmwang.cyh.activity.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xmwang.cyh.BaseActivity;
import com.xmwang.cyh.R;
import com.xmwang.cyh.activity.home.IndexActivity;
import com.xmwang.cyh.common.Common;
import com.xmwang.cyh.common.CustomToast;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.common.SADialog;
import com.xmwang.cyh.model.UserInfo;
import com.xmwang.cyh.utils.ActivityManager;
import com.xmwang.cyh.utils.GetTime;
import com.xmwang.cyh.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.txt_phone)
    EditText txtPhone;
    @BindView(R.id.txt_pwd)
    EditText txtPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_forget_pwd, R.id.tv_register, R.id.btn_login, R.id.btn_qq, R.id.btn_weixin, R.id.btn_sina})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_forget_pwd:
                startActivity(new Intent(this,ForgetPwdActivity.class));
                break;
            case R.id.tv_register:
                startActivity(new Intent(this,RegisterActivity.class));
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_qq:
                third_login(1);
                break;
            case R.id.btn_weixin:
                third_login(2);
                break;
            case R.id.btn_sina:
                third_login(3);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SADialog.instance.hideProgress();
    }

    @Override
    public void onBackPressed() {
//        自己处理返回按键的内容
//        //super.onBackPressed();
        // 当popupWindow 正在展示的时候 按下返回键 关闭popupWindow 否则关闭activity

            if (GetTime.getInstance().isFastDoubleClick(2000)) {
                ActivityManager.getInstance().removeAllActivity();
            } else {
                ToastUtils.getInstance().toastShow("再按一次退出程序");
            }
    }

    private void third_login(int type){
        ToastUtils.getInstance().toastShow("暂未开放");
    }




    private void login(){
        if (!Common.isMobile(txtPhone.getText().toString())){
            ToastUtils.getInstance().toastShow("手机号格式不正确");
            return;
        }
        if (TextUtils.isEmpty(txtPwd.getText())){
            ToastUtils.getInstance().toastShow("密码不能为空");
            return;
        }

        SADialog.instance.showProgress(LoginActivity.this);
        Call<UserInfo> call = RetrofitHelper.instance.getApiUserService().login(
                Data.instance.AdminId,
                txtPhone.getText().toString().trim(),
                txtPwd.getText().toString().trim()
        );
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                UserInfo userInfo = response.body();
                SADialog.instance.hideProgress();
                if (userInfo.getCode() != RetrofitHelper.instance.SUCCESS_CODE) {
                    ToastUtils.getInstance().toastShow(userInfo.getMessage());
                    return;
                }
                Data.instance.setUserInfo(userInfo.getData().get(0));
                startActivity(new Intent(LoginActivity.this, IndexActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                SADialog.instance.hideProgress();
                ToastUtils.getInstance().toastShow("登录失败");
            }
        });
    }
}
