package com.xmwang.cyh.activity.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xmwang.cyh.BaseActivity;
import com.xmwang.cyh.R;
import com.xmwang.cyh.common.Common;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.PayResult;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.daijia.AccActivity;
import com.xmwang.cyh.model.AlipayModel;
import com.xmwang.cyh.model.BaseModel;
import com.xmwang.cyh.model.WXPayModel;
import com.xmwang.cyh.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Callback;
import retrofit2.Response;

public class FuelcardActivity extends BaseActivity {

//    @BindView(R.id.txt_phone)
//    EditText txtPhone;
    @BindView(R.id.txt_number)
    EditText txtNumber;
    @BindView(R.id.txt_tt_100)
    TextView txtTt100;
    @BindView(R.id.txt_dd_100)
    TextView txtDd100;
    @BindView(R.id.txt_tt_500)
    TextView txtTt500;
    @BindView(R.id.txt_dd_500)
    TextView txtDd500;
    @BindView(R.id.txt_tt_1000)
    TextView txtTt1000;
    @BindView(R.id.txt_dd_1000)
    TextView txtDd1000;
    @BindView(R.id.cbk)
    CheckBox cbk;
    @BindView(R.id.ll_100)
    LinearLayout ll100;
    @BindView(R.id.ll_500)
    LinearLayout ll500;
    @BindView(R.id.ll_1000)
    LinearLayout ll1000;
    String money = "";
    String original_price = "";
    IWXAPI msgApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_fuelcard);
        ButterKnife.bind(this);
        init();
    }

    @OnClick({R.id.title_back, R.id.ll_100, R.id.ll_500, R.id.ll_1000, R.id.txt_server, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.ll_100:
                chooseMoney(1);
                break;
            case R.id.ll_500:
                chooseMoney(2);
                break;
            case R.id.ll_1000:
                chooseMoney(3);
                break;
            case R.id.txt_server:
                break;
            case R.id.btn_submit:
                submit();
                break;
        }
    }

    private void init() {
        if (msgApi == null) {
            msgApi = WXAPIFactory.createWXAPI(this, null);
            msgApi.registerApp(Data.instance.WXKEY);
        }
    }

    private void submit() {
//        if (!Common.isMobile(txtPhone.getText().toString())) {
//            ToastUtils.getInstance().toastShow("请输入正确手机号");
//            return;
//        }
        if (TextUtils.isEmpty(txtNumber.getText())) {
            ToastUtils.getInstance().toastShow("请输入卡号");
            return;
        }
        if (TextUtils.isEmpty(money)) {
            ToastUtils.getInstance().toastShow("请选择充值金额");
            return;
        }
        if (!cbk.isChecked()) {
            ToastUtils.getInstance().toastShow("请先接受协议");
            return;
        }
        pay();

    }
    int payType = 0;
    private void pay() {
        RetrofitHelper.instance.getApiHomeService().fuelcard(
                Data.instance.AdminId,
                Data.instance.getUserId(),
//                txtPhone.getText().toString().trim(),
                txtNumber.getText().toString().trim(),
                original_price,
                money
        ).enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(retrofit2.Call<BaseModel> call, Response<BaseModel> response) {
                BaseModel model = response.body();
                if (model == null){
                    ToastUtils.getInstance().toastShow("网络错误");
                    return;
                }
                if (model.getCode() == 403) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FuelcardActivity.this);
                    builder.setTitle("余额不足，请选择支付方式");
                    final String[] payTypes = {"微信", "支付宝"};

                    //    设置一个单项选择下拉框
                    /**
                     * 第一个参数指定我们要显示的一组下拉单选框的数据集合
                     * 第二个参数代表索引，指定默认哪一个单选框被勾选上，1表示默认'女' 会被勾选上
                     * 第三个参数给每一个单选项绑定一个监听器
                     */
                    builder.setSingleChoiceItems(payTypes, payType, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            payType = which;
                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (payType == 0) {
                                wxPay();
                            } else {
                                aliPay();
                            }
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                    return;
                }
                ToastUtils.getInstance().toastShow(model.getMessage());
            }

            @Override
            public void onFailure(retrofit2.Call<BaseModel> call, Throwable t) {
                ToastUtils.getInstance().toastShow("网络错误");
            }
        });
    }

    private void wxPay() {
        retrofit2.Call<WXPayModel> call = RetrofitHelper.instance.getApiPayService().weixin_pay(
                Data.instance.AdminId,
                Data.instance.getUserId(),
                1,
//                txtPhone.getText().toString(),
                txtNumber.getText().toString(),
                original_price,
                money
        );

        call.enqueue(new Callback<WXPayModel>() {
            @Override
            public void onResponse(retrofit2.Call<WXPayModel> call, Response<WXPayModel> response) {
                WXPayModel model = response.body();
                if (model == null){
                    return;
                }
                if (model.getCode() != RetrofitHelper.instance.SUCCESS_CODE || model.getData().size() == 0) {
                    return;
                }
                WXPayModel.DataBean m = model.getData().get(0);
                PayReq request = new PayReq();
                request.appId = Data.instance.WXKEY;
                request.partnerId = m.getPartnerid();
                request.prepayId = m.getPrepayid();
                request.packageValue = "Sign=WXPay";
                request.nonceStr = m.getNoncestr();
                request.timeStamp = String.valueOf(m.getTimestamp());
                request.sign = m.getSign();
                msgApi.sendReq(request);
            }

            @Override
            public void onFailure(retrofit2.Call<WXPayModel> call, Throwable t) {
                ToastUtils.getInstance().toastShow("网络错误");
            }
        });
    }

    private void aliPay() {
        RetrofitHelper.instance.getApiPayService().alipay_pay(
                Data.instance.AdminId,
                Data.instance.getUserId(),
                1,
//                txtPhone.getText().toString(),
                txtNumber.getText().toString(),
                original_price,
                money
        ).enqueue(new Callback<AlipayModel>() {
            @Override
            public void onResponse(retrofit2.Call<AlipayModel> call, Response<AlipayModel> response) {
                AlipayModel model = response.body();
                if (model == null){
                    return;
                }
                if (model.getCode() != RetrofitHelper.instance.SUCCESS_CODE || model.getData().size() == 0) {
                    return;
                }
                final AlipayModel.DataBean m = model.getData().get(0);
                Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(FuelcardActivity.this);
                        Map<String, String> result = alipay.payV2(m.getOrderstr(),true);

                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };
                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();
            }

            @Override
            public void onFailure(retrofit2.Call<AlipayModel> call, Throwable t) {
                ToastUtils.getInstance().toastShow("网络错误");
            }
        });
    }
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            @SuppressWarnings("unchecked")
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            Log.e("xmwang",payResult.getResultStatus()+"|"+payResult.getResult());

// 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(payResult.getResultStatus(), "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                ToastUtils.getInstance().toastShow("支付成功");

            }else if (TextUtils.equals(payResult.getResultStatus(), "6001")){
                ToastUtils.getInstance().toastShow("用户取消支付");
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                ToastUtils.getInstance().toastShow("支付失败");
            }
//            Toast.makeText(DemoActivity.this, result.getResult(),
//                    Toast.LENGTH_LONG).show();
        };
    };

    private void chooseMoney(int i) {
        txtTt100.setTextColor(getResources().getColor(R.color.blackColor));
        txtDd100.setTextColor(getResources().getColor(R.color.darkgrey));
        txtTt500.setTextColor(getResources().getColor(R.color.blackColor));
        txtDd500.setTextColor(getResources().getColor(R.color.darkgrey));
        txtTt1000.setTextColor(getResources().getColor(R.color.blackColor));
        txtDd1000.setTextColor(getResources().getColor(R.color.darkgrey));
        ll100.setBackgroundResource(R.drawable.fillet_white);
        ll500.setBackgroundResource(R.drawable.fillet_white);
        ll1000.setBackgroundResource(R.drawable.fillet_white);
        switch (i) {
            case 1:
                money = "99.8";
                original_price = "100";
                txtTt100.setTextColor(getResources().getColor(R.color.dodgerblue));
                txtDd100.setTextColor(getResources().getColor(R.color.dodgerblue));
                ll100.setBackgroundResource(R.drawable.fillet_border_dodeferblue);
                break;
            case 2:
                money = "499.8";
                original_price = "500";
                txtTt500.setTextColor(getResources().getColor(R.color.dodgerblue));
                txtDd500.setTextColor(getResources().getColor(R.color.dodgerblue));
                ll500.setBackgroundResource(R.drawable.fillet_border_dodeferblue);
                break;
            case 3:
                money = "999";
                original_price = "1000";
                txtTt1000.setTextColor(getResources().getColor(R.color.dodgerblue));
                txtDd1000.setTextColor(getResources().getColor(R.color.dodgerblue));
                ll1000.setBackgroundResource(R.drawable.fillet_border_dodeferblue);
                break;
        }
    }
}
