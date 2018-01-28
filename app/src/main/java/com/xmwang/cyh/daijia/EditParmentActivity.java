package com.xmwang.cyh.daijia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xmwang.cyh.BaseActivity;
import com.xmwang.cyh.R;
import com.xmwang.cyh.api.ApiService;
import com.xmwang.cyh.common.Common;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.FlexRadioGroup;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.model.Charging;
import com.xmwang.cyh.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xmwang on 2017/12/19.
 */

public class EditParmentActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_back)
    LinearLayout titleBack;
    @BindView(R.id.title_right_img)
    TextView titleRightImg;
    @BindView(R.id.title_right)
    LinearLayout titleRight;
    @BindView(R.id.rdo_100)
    RadioButton rdo100;
    @BindView(R.id.rdo_120)
    RadioButton rdo120;
    @BindView(R.id.rdo_150)
    RadioButton rdo150;
    @BindView(R.id.rdo_gorup)
    RadioGroup rdoGorup;
    //    @BindView(R.id.rdo_1001)
//    RadioButton rdo1001;
//    @BindView(R.id.rdo_1202)
//    RadioButton rdo1202;
//    @BindView(R.id.rdo_1503)
//    RadioButton rdo1503;
    @BindView(R.id.rdo_temp_group)

    FlexRadioGroup rdoTempGroup;
    @BindView(R.id.btn_setting_temp)
    FancyButton btnSettingTemp;
    @BindView(R.id.btn_add_order)
    FancyButton btnAddOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daijia_edit_parment);
        ButterKnife.bind(this);
        init();
        loadData();
    }

    private void init() {
        rdoGorup.setOnCheckedChangeListener(this);

        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.overorder");
        registerReceiver(mReceiver, filter);
        double pvalue = Data.instance.getPercentage() * 100;
        if (pvalue == 100){
            rdoGorup.check(rdo100.getId());
        }
        if (pvalue == 120){
            rdoGorup.check(rdo120.getId());
        }
        if (pvalue == 150){
            rdoGorup.check(rdo150.getId());
        }
    }

    private void loadData() {
        Call<Charging> call = RetrofitHelper.instance
                .getApiService()
                .charging(Data.instance.AdminId, Data.instance.getUserId());
        call.enqueue(new Callback<Charging>() {
            @Override
            public void onResponse(Call<Charging> call, Response<Charging> response) {
                Charging charging = response.body();
                setRaidBtn(charging.getData());
            }

            @Override
            public void onFailure(Call<Charging> call, Throwable t) {

            }
        });
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

    @OnClick({R.id.title_back, R.id.title_right_img, R.id.rdo_gorup, R.id.rdo_temp_group, R.id.btn_setting_temp, R.id.btn_add_order})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                this.finish();
                break;
            case R.id.title_right_img:
//                Log.e("xmwang", String.valueOf(chargingId));
                break;
            case R.id.btn_setting_temp:
                startActivity(new Intent(this, TempListActivity.class));
                break;
            case R.id.btn_add_order:
                //创建订单
                if (!Data.instance.getIsOnline()){
                    ToastUtils.getInstance().toastShow("请先上线");
                    return;
                }
                if (Double.valueOf(Data.instance.getDriveInfo().getDriver_money()) < Data.instance.getDriveInfo().getDriver_min_money()){
                    ToastUtils.getInstance().toastShow("余额不足，接单最低需要"+Data.instance.getDriveInfo().getDriver_min_money()+"元");
                    return;
                }
                startActivity(new Intent(this, CreateDJOrderActivity.class));
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
//        radioGroup.check(i);
//        Log.e("xmwang", String.valueOf(i));
        Log.e("xmwang", "我是rdoGorup");
        RadioButton radioButton = (RadioButton) radioGroup.findViewById(i);
        Data.instance.setPercentage(Double.valueOf(radioButton.getText().toString().replace("%","")) / 100);
    }

    private void setRaidBtn(List<Charging.DataBean> data) {
        if (null == data) {
            return;
        }
        int width = (Common.getWindowWidth(this) - Common.dp2px(this, 40)) / 3 - 10;
        for (int i = 0; i < data.size(); i++) {
            Charging.DataBean model = data.get(i);
            RadioButton radioButton = new RadioButton(this);
            //去除圆点
            radioButton.setButtonDrawable(0);
            RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(width, Common.dp2px(this, 25));
            //设置RadioButton边距 (int left, int top, int right, int bottom)
            lp.setMargins(0, 0, Common.dp2px(this, 10), Common.dp2px(this, 10));
            radioButton.setLayoutParams(lp);
            radioButton.setTextSize(10);
            //设置RadioButton背景
            radioButton.setBackgroundResource(R.drawable.fillet);
            radioButton.setTextColor(getResources().getColorStateList(R.color.btn_black_white));
            radioButton.setGravity(Gravity.CENTER);
            //设置RadioButton的样式
//            radioButton.setButtonDrawable(R.drawable.fillet);
            //设置文字距离四周的距离
//            radioButton.setPadding(80, 0, 0, 0);
            //设置文字
            radioButton.setText(model.getCharging());

            final int finalI = model.getCharging_id();
            //设置radioButton的点击事件
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Data.instance.setChargingId(finalI);
                }
            });
            //将radioButton添加到radioGroup中
            rdoTempGroup.addView(radioButton);

            //要在添加完之后设置状态
            if (model.getCharging_id() == Data.instance.getChargingId()) {
                rdoTempGroup.check(radioButton.getId());
            }

        }
    }
}
