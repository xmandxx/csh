package com.xmwang.cyh.daijia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.xmwang.cyh.BaseActivity;
import com.xmwang.cyh.R;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.common.event.UserDriverInfoEvent;
import com.xmwang.cyh.model.DriveOrderInfo;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xmWang on 2017/12/25.
 */

public class OverOrderActivity extends BaseActivity {
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.txt_order_number)
    TextView txtOrderNumber;
    @BindView(R.id.start_location)
    TextView startLocation;
    @BindView(R.id.end_location)
    TextView endLocation;
    @BindView(R.id.txt_km)
    TextView txtKm;
    @BindView(R.id.txt_wait_time)
    TextView txtWaitTime;
    @BindView(R.id.txt_money)
    TextView txtMoney;
    @BindView(R.id.txt_wait_money)
    TextView txtWaitMoney;
    @BindView(R.id.txt_sum_money)
    TextView txtSumMoney;
    @BindView(R.id.btn_go_home)
    FancyButton btnGoHome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_order);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        Call<DriveOrderInfo> call = RetrofitHelper.instance.getApiService().getDriveOrderInfo(
                Data.instance.AdminId,
                Data.instance.getUserId(),
                Data.instance.getDriverOrderId()
        );
        call.enqueue(new Callback<DriveOrderInfo>() {
            @Override
            public void onResponse(Call<DriveOrderInfo> call, Response<DriveOrderInfo> response) {
                DriveOrderInfo driveOrderInfo = response.body();
                if (driveOrderInfo == null || driveOrderInfo.getData().size() == 0) {
                    Toast.makeText(OverOrderActivity.this, driveOrderInfo.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                DriveOrderInfo.DataBean dd = driveOrderInfo.getData().get(0);
                txtKm.setText("行驶距离："+dd.getRunning_kilometre()+"km");
                txtMoney.setText("行驶费用："+dd.getRunning_money()+"元");
                txtOrderNumber.setText("订单编号："+dd.getOrder_sn());
                txtWaitTime.setText("等待时间："+String.valueOf(dd.getWait_time())+"分钟");
                txtWaitMoney.setText("等待费用："+String.valueOf(dd.getWait_money())+"元");
                txtSumMoney.setText("总计："+dd.getOrder_amount()+"元");
                startLocation.setText(dd.getOrigination());
                endLocation.setText(dd.getDestination());
            }

            @Override
            public void onFailure(Call<DriveOrderInfo> call, Throwable t) {

                Toast.makeText(OverOrderActivity.this, "操作失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.btn_go_home)
    public void onViewClicked() {
        Intent intent = new Intent("com.overorder");
        sendBroadcast(intent);      //发送广播
        this.finish();
    }
    @Override
    public void onBackPressed() {
//        自己处理返回按键的内容
//        //super.onBackPressed();

    }

}
