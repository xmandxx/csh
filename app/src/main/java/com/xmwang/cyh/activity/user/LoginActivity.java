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
import com.xmwang.cyh.api.ApiUserService;
import com.xmwang.cyh.common.Common;
import com.xmwang.cyh.common.CustomToast;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.common.SADialog;
import com.xmwang.cyh.common.retrofit.BaseResponse;
import com.xmwang.cyh.common.retrofit.RetrofitUtil;
import com.xmwang.cyh.common.retrofit.SubscriberOnNextListener;
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
import rx.Observable;

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

    //    R.id.tv_register, R.id.btn_qq, R.id.btn_weixin, R.id.btn_sina
    @OnClick({R.id.tv_forget_pwd, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_forget_pwd:
                startActivity(new Intent(this, ForgetPwdActivity.class));
                break;
//            case R.id.tv_register:
//                startActivity(new Intent(this,RegisterActivity.class));
//                break;
            case R.id.btn_login:
                login();
                break;
//            case R.id.btn_qq:
//                third_login(1);
//                break;
//            case R.id.btn_weixin:
//                third_login(2);
//                break;
//            case R.id.btn_sina:
//                third_login(3);
//                break;
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

    private void third_login(int type) {
        ToastUtils.getInstance().toastShow("暂未开放");
    }


    private void login() {
        if (!Common.isMobile(txtPhone.getText().toString())) {
            ToastUtils.getInstance().toastShow("手机号格式不正确");
            return;
        }
        if (TextUtils.isEmpty(txtPwd.getText())) {
            ToastUtils.getInstance().toastShow("密码不能为空");
            return;
        }

        Observable observable = RetrofitUtil.getInstance().getmRetrofit().create(ApiUserService.class).login(
                Data.instance.AdminId,
                txtPhone.getText().toString().trim(),
                txtPwd.getText().toString().trim()
        );
        RetrofitUtil.getInstance()
                .setObservable(observable)
                .base(new SubscriberOnNextListener<BaseResponse<UserInfo>>() {
                    @Override
                    public void onNext(BaseResponse<UserInfo> baseResponse) {
                        if (baseResponse.isSuccess() && baseResponse.getDataInfo() != null) {
                            Data.instance.setUserInfo(baseResponse.getDataInfo());
                            startActivity(new Intent(LoginActivity.this, com.xmwang.cyh.daijia.IndexActivity.class));
                            finish();
                        } else {
//                            ToastUtils.getInstance().toastShow("发送失败");
                        }
                    }
                }, this);
    }
}
