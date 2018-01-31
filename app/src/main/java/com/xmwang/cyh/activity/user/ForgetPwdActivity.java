package com.xmwang.cyh.activity.user;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xmwang.cyh.BaseActivity;
import com.xmwang.cyh.R;
import com.xmwang.cyh.api.ApiHomeService;
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

public class ForgetPwdActivity extends BaseActivity {

    @BindView(R.id.txt_phone)
    EditText txtPhone;
    @BindView(R.id.txt_new_pwd)
    EditText txtNewPwd;
    @BindView(R.id.txt_code)
    EditText txtCode;
    @BindView(R.id.btn_send_code)
    TextView btnSendCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SADialog.instance.hideProgress();
    }

    @OnClick({R.id.title_back, R.id.btn_send_code, R.id.btn_done})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.btn_send_code:
                sendCode();
                break;
            case R.id.btn_done:
                forgetPwd();
                break;
        }
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

    private void forgetPwd() {
        if (!Common.isMobile(txtPhone.getText().toString())) {
            ToastUtils.getInstance().toastShow("手机号不正确");
            return;
        }
        if (TextUtils.isEmpty(txtNewPwd.getText())) {
            ToastUtils.getInstance().toastShow("请输入密码");
            return;
        }
        if (TextUtils.isEmpty(txtCode.getText())) {
            ToastUtils.getInstance().toastShow("请输入验证码");
            return;
        }
//        SADialog.instance.showProgress(this);
        Observable observable = RetrofitUtil.getInstance().getmRetrofit().create(ApiUserService.class).forgetPwd(
                Data.instance.AdminId,
                txtPhone.getText().toString().trim(),
                txtNewPwd.getText().toString().trim(),
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
}
