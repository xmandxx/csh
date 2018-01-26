package com.xmwang.cyh.daijia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xmwang.cyh.R;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.model.DriveOrderInfo;
import com.xmwang.cyh.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {
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
    String driverOrderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daijia_order_detail);
        ButterKnife.bind(this);
        init();

    }
    @OnClick({R.id.title_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
        }
    }
    private void init(){
        Intent intent=getIntent();
        driverOrderId = intent.getStringExtra("driverOrderId");

        Call<DriveOrderInfo> call = RetrofitHelper.instance.getApiService().getDriveOrderInfo(
                Data.instance.AdminId,
                Data.instance.getUserId(),
                driverOrderId
        );
        call.enqueue(new Callback<DriveOrderInfo>() {
            @Override
            public void onResponse(Call<DriveOrderInfo> call, Response<DriveOrderInfo> response) {
                DriveOrderInfo driveOrderInfo = response.body();
                if (driveOrderInfo == null || driveOrderInfo.getData().size() == 0) {
                    ToastUtils.getInstance().toastShow(driveOrderInfo.getMessage());
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
                ToastUtils.getInstance().toastShow("获取订单失败");
            }
        });
    }
}
