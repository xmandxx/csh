package com.xmwang.cyh.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.xmwang.cyh.BaseActivity;
import com.xmwang.cyh.R;
import com.xmwang.cyh.api.ApiUserService;
import com.xmwang.cyh.common.Common;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.common.SADialog;
import com.xmwang.cyh.common.retrofit.BaseResponse;
import com.xmwang.cyh.common.retrofit.RetrofitUtil;
import com.xmwang.cyh.common.retrofit.SubscriberOnNextListener;
import com.xmwang.cyh.model.BaseModel;
import com.xmwang.cyh.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.txt_phone)
    EditText txtPhone;
    @BindView(R.id.txt_pwd)
    EditText txtPwd;
    @BindView(R.id.txt_code)
    EditText txtCode;
    @BindView(R.id.btn_send_code)
    TextView btnSendCode;
    @BindView(R.id.cb_sm)
    CheckBox cbSm;
    boolean isSend = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.title_back, R.id.btn_send_code, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.btn_send_code:
                sendCode();
                break;
            case R.id.btn_register:
                register();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SADialog.instance.hideProgress();
    }

    private void register() {
        if (!cbSm.isChecked()) {
            ToastUtils.getInstance().toastShow("请先接受协议");
            return;
        }
        if (!Common.isMobile(txtPhone.getText().toString())) {
            ToastUtils.getInstance().toastShow("请输入正确手机号");
            return;
        }
        if (Common.isEmptyTrim(txtPwd.getText().toString())) {
            ToastUtils.getInstance().toastShow("请输入密码");
            return;
        }
        if (Common.isEmptyTrim(txtCode.getText().toString())) {
            ToastUtils.getInstance().toastShow("请输入验证码");
            return;
        }
//        SADialog.instance.showProgress(this);
        Observable observable = RetrofitUtil.getInstance().getmRetrofit().create(ApiUserService.class).register(
                Data.instance.AdminId,
                txtPhone.getText().toString().trim(),
                txtPwd.getText().toString().trim(),
                txtCode.getText().toString().trim()
        );

        RetrofitUtil.getInstance()
                .setObservable(observable)
                .base(new SubscriberOnNextListener<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        if (baseResponse.isSuccess()) {
                            ToastUtils.getInstance().toastShow(baseResponse.message);
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                            }
                            finish();
                        }
                    }
                }, this);
    }

    private void sendCode() {
        if (!Common.isMobile(txtPhone.getText().toString())) {
            ToastUtils.getInstance().toastShow("手机号不正确");
            return;
        }
        Observable observable = RetrofitUtil.getInstance().getmRetrofit().create(ApiUserService.class).sendCode(
                Data.instance.AdminId,
                txtPhone.getText().toString().trim()
        );
        RetrofitUtil.getInstance()
                .setObservable(observable)
                .base(new SubscriberOnNextListener<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        if (baseResponse.isSuccess()) {
                            btnSendCode.setEnabled(false);
                            timer.start();
                        } else {
//                            ToastUtils.getInstance().toastShow("发送失败");
                        }
                    }
                },this);
    }

    public CountDownTimer timer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            btnSendCode.setText("重试(" + millisUntilFinished / 1000 + "s)");
        }

        @Override
        public void onFinish() {
            btnSendCode.setEnabled(true);
            btnSendCode.setText("发送验证码");
        }
    };
}
