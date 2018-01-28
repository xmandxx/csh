package com.xmwang.cyh.daijia;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xmwang.cyh.R;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.PayResult;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.common.event.UserDriverInfoEvent;
import com.xmwang.cyh.model.AlipayModel;
import com.xmwang.cyh.model.DriveInfo;
import com.xmwang.cyh.model.WXPayModel;
import com.xmwang.cyh.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import javax.xml.transform.Result;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    @BindView(R.id.txt_money)
    TextView txtMoney;
    @BindView(R.id.rdo_gorup)
    RadioGroup rdoGorup;
    @BindView(R.id.rdo_gorup_two)
    RadioGroup rdoGorupTwo;
    @BindView(R.id.img_wxpay)
    ImageView imgWxpay;
    @BindView(R.id.img_alipay)
    ImageView imgAlipay;
    String money = "500";
    int moneyType = 1; //1微信2支付宝
    IWXAPI msgApi;
    private String order_no;//存储支付订单号
    private boolean isChangeGroup = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daijia_acc);
        ButterKnife.bind(this);
        init();
        loadNetword();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//解除订阅
    }
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(final UserDriverInfoEvent event) {
        loadNetword();
    }
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if((radioGroup != null) && (checkedId > -1) && (!isChangeGroup)){
            RadioButton radioButton = (RadioButton) radioGroup.findViewById(checkedId);
            money = radioButton.getText().toString().replace("元","");
            Log.e("xmwang", "选择了"+money);
            if(radioGroup.equals(rdoGorup)){
                isChangeGroup = true;
                rdoGorupTwo.clearCheck();
                isChangeGroup = false;
            }else if(radioGroup.equals(rdoGorupTwo)){
                isChangeGroup = true;
                rdoGorup.clearCheck();
                isChangeGroup = false;
            }
        }
    }
    private void init() {
        //注册事件
        EventBus.getDefault().register(this);
        rdoGorup.setOnCheckedChangeListener(this);
        rdoGorupTwo.setOnCheckedChangeListener(this);
        setMoneyType(moneyType);
    }

    private void loadNetword(){
        Call<DriveInfo> call = RetrofitHelper.instance.getApiService().driveInfo(Data.instance.AdminId, Data.instance.getUserId());
        call.enqueue(new Callback<DriveInfo>() {
            @Override
            public void onResponse(Call<DriveInfo> call, Response<DriveInfo> response) {
                DriveInfo driveInfo = response.body();
                if (driveInfo != null){
                    if (driveInfo.getData().size() > 0){
                        Data.instance.setDriveInfo(driveInfo.getData().get(0));
                        txtMoney.setText(Data.instance.getDriveInfo().getDriver_money());
                    }
                }
            }

            @Override
            public void onFailure(Call<DriveInfo> call, Throwable t) {

            }
        });
    }


    @OnClick({R.id.title_back, R.id.ll_wxpay, R.id.ll_alipay, R.id.btn_cz})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.ll_wxpay:
                setMoneyType(1);
                break;
            case R.id.ll_alipay:
                setMoneyType(2);
                break;
            case R.id.btn_cz:
                submit();
                break;
        }
    }

    private void submit() {
        if (money.isEmpty()){
            ToastUtils.getInstance().toastShow("请先选择金额");
            return;
        }
        if (moneyType == 1){
            wxPay();
        }else{
            aliPay();
        }
    }
    private void wxPay() {
        if (msgApi == null) {
            msgApi = WXAPIFactory.createWXAPI(this, null);
            msgApi.registerApp(Data.instance.WXKEY);
        }
        retrofit2.Call<WXPayModel> call = RetrofitHelper.instance.getApiPayService().weixin_pay(
                Data.instance.AdminId,
                Data.instance.getUserId(),
                2,
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

            }
        });
    }

    private void aliPay() {
        RetrofitHelper.instance.getApiPayService().alipay_pay(
                Data.instance.AdminId,
                Data.instance.getUserId(),
                2,
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
                        PayTask alipay = new PayTask(AccActivity.this);
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
                loadNetword();
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

    private void setMoneyType(int mt){
        moneyType = mt;
        if (mt == 1){
            imgWxpay.setImageResource(R.mipmap.succeed);
            imgAlipay.setImageResource(R.mipmap.succeed_default);
        }else{
            imgWxpay.setImageResource(R.mipmap.succeed_default);
            imgAlipay.setImageResource(R.mipmap.succeed);
        }
    }
}
