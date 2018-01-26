package com.xmwang.cyh.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xmwang.cyh.R;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.event.UserDriverInfoEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    @BindView(R.id.txt_wz)
    TextView txtWz;

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);
        ButterKnife.bind(this);
        api = WXAPIFactory.createWXAPI(this, Data.instance.WXKEY);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {

    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {

        Log.d("wxpay", "onPayFinish, errCode = " + resp.errCode);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            String msg = "";
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    EventBus.getDefault().post(new UserDriverInfoEvent());
                    msg = "支付成功，等待充值完成";
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    msg = "取消支付";
                    break;
                default:
                    msg = "支付失败";
                    break;
            }
            txtWz.setText(msg);
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle(R.string.app_tip);
//            builder.setMessage(msg);
//            builder.show();
        }
//        finish();
    }


    @OnClick(R.id.title_back)
    public void onViewClicked() {
        finish();
    }
}