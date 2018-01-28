package com.xmwang.cyh.daijia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xmwang.cyh.BaseActivity;
import com.xmwang.cyh.R;
import com.xmwang.cyh.common.Common;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.model.OtherDriver;
import com.xmwang.cyh.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xmWang on 2017/12/24.
 */

public class CreateDJOrderActivity extends BaseActivity {
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_back)
    LinearLayout titleBack;
    @BindView(R.id.txt_phone)
    EditText txtPhone;
    @BindView(R.id.start_server)
    FancyButton startServer;
//    String origination;
//    String destination;
//    String longitude;
//    String latitude;

    //    MyReceiver myReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_djorder);
        ButterKnife.bind(this);
        init();
//        loadData();
    }

    private void init() {
        //注册广播接收器
//        myReceiver = new MyReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("com.overorder");
//        registerReceiver(myReceiver, intentFilter);

        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.overorder");
        registerReceiver(mReceiver, filter);
    }

    /**
     * 广播接收
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @OnClick({R.id.title_back, R.id.start_server})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                this.finish();
                break;
            case R.id.start_server:
                addOrder();
                break;
        }
    }

    public void addOrder() {
        if (!Common.isMobile(txtPhone.getText().toString())){
            ToastUtils.getInstance().toastShow("请输入正确的手机号");
            return;
        }
        Call<OtherDriver> call = RetrofitHelper.instance.getApiService().addDriveOrder(
                Data.instance.AdminId,
                txtPhone.getText().toString().trim(),
                Data.instance.getUserId(),
                Data.instance.getChargingId(),
                Data.instance.getFormatAddress(),
                "",
                String.valueOf(Data.instance.getLocation().getLongitude()),
                String.valueOf(Data.instance.getLocation().getLatitude()),
                String.valueOf(Data.instance.getPercentage())
        );
        call.enqueue(new Callback<OtherDriver>() {
            @Override
            public void onResponse(Call<OtherDriver> call, Response<OtherDriver> response) {
                OtherDriver otherDriver = response.body();
                if (otherDriver == null || otherDriver.getData().size() == 0) {
                    ToastUtils.getInstance().toastShow(otherDriver.getMessage());
                    return;
                }
                Data.instance.setDriverOrderId(otherDriver.getData().get(0).getOrder_id());
                startActivity(new Intent(CreateDJOrderActivity.this, TaximeterActivity.class));
            }

            @Override
            public void onFailure(Call<OtherDriver> call, Throwable t) {
                Toast.makeText(CreateDJOrderActivity.this, "发起失败", Toast.LENGTH_LONG).show();
            }
        });
    }
}
