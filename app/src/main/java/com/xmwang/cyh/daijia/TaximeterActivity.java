package com.xmwang.cyh.daijia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceListener;
import com.amap.api.trace.TraceLocation;
import com.amap.api.trace.TraceStatusListener;
import com.xmwang.cyh.BaseActivity;
import com.xmwang.cyh.R;
import com.xmwang.cyh.common.Common;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.MyDialog;
import com.xmwang.cyh.common.MyDialog.OnConfirmListener;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.model.BaseModel;
import com.xmwang.cyh.model.OtherDriver;
import com.xmwang.cyh.model.TempInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.amap.api.maps.AMapUtils.calculateLineDistance;

/**
 * Created by xmWang on 2017/12/25.
 */

public class TaximeterActivity extends BaseActivity implements OnConfirmListener,GeocodeSearch.OnGeocodeSearchListener {
    @BindView(R.id.end_location)
    TextView endLocation;
    @BindView(R.id.txt_km)
    TextView txtKm;
    @BindView(R.id.txt_money)
    TextView txtMoney;
    @BindView(R.id.txt_wait_time)
    TextView txtWaitTime;
    @BindView(R.id.btn_wait)
    FancyButton btnWait;
    @BindView(R.id.btn_over)
    FancyButton btnOver;
    boolean isWait = false;
    boolean isOver = false;
    double startAmount = 0;
    double km = 0;
    int secondsCountDown = 0;
    double sumMoney = 0;
    double waitMoney = 0;
    private String destination;
    GeocodeSearch geocoderSearch;
    LBSTraceClient lbsTraceClient;
    PowerManager.WakeLock m_wklk;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taximeter);
        ButterKnife.bind(this);
        init();
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


    @OnClick({R.id.end_location, R.id.btn_wait, R.id.btn_over})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.end_location:
                break;
            case R.id.btn_wait:
                MyDialog.show(this, "您确定要" + btnWait.getText(),this);
                break;
            case R.id.btn_over:
                if (isWait){
                    Toast.makeText(this,"您还有没有结束等待",Toast.LENGTH_SHORT).show();
                    return;
                }
                MyDialog.show(this, "您确定要结束服务", new OnConfirmListener() {
                    @Override
                    public void onConfirmClick() {
                        lbsTraceClient.stopTrace();
                        overOrder();
                    }
                });
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        自己处理返回按键的内容
//        //super.onBackPressed();

    }

    @Override
    public void onConfirmClick() {
        isWait = !isWait;
        if (isWait){
            btnWait.setText("停止等待");
        }else{
            btnWait.setText("开始等待");
        }
        if (isWait){
            timer.start();
        }else{
            timer.cancel();
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {
        if (regeocodeResult != null && rCode == 1000){
            destination = regeocodeResult.getRegeocodeAddress().getFormatAddress();
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    private void init(){
        PowerManager pm = (PowerManager)getSystemService(POWER_SERVICE);
        m_wklk = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "cn");
        m_wklk.acquire(); //设置保持唤醒

        //初始化集合
//        mTraceList = new ArrayList<TraceLocation>();

        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.overorder");
        registerReceiver(mReceiver, filter);

        Call<TempInfo> call = RetrofitHelper.instance.getApiService().getTempInfo(Data.instance.getUserId(),Data.instance.getChargingId());
        call.enqueue(new Callback<TempInfo>() {
            @Override
            public void onResponse(Call<TempInfo> call, Response<TempInfo> response) {
                TempInfo tempInfo = response.body();
                if (tempInfo == null || tempInfo.getData().size() == 0) {
                    Toast.makeText(TaximeterActivity.this, tempInfo.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                TempInfo.DataBean data = tempInfo.getData().get(0);
                Data.instance.setTempInfo(data);
                reckon();
            }

            @Override
            public void onFailure(Call<TempInfo> call, Throwable t) {
                Toast.makeText(TaximeterActivity.this, "操作失败", Toast.LENGTH_LONG).show();
            }
        });

        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);

        lbsTraceClient = LBSTraceClient.getInstance(this);
        lbsTraceClient.startTrace(new TraceStatusListener() {
            @Override
            public void onTraceStatus(List<TraceLocation> list, List<LatLng> rectifications, String errorInfo) {
                Log.e("xmwang","发起一次轨迹纠偏,msg:"+errorInfo);
                //发起逆地理编码
                if (list.size() > 0){
                    TraceLocation tl = list.get(list.size() - 1);
                    LatLonPoint llp = new LatLonPoint(tl.getLatitude(),tl.getLongitude());
                    RegeocodeQuery query = new RegeocodeQuery(llp, 100,GeocodeSearch.AMAP);
                    geocoderSearch.getFromLocationAsyn(query);
                }
                //
                LatLng sLL = null;
                double distance = 0;
                for (LatLng ll : rectifications){
                    if (sLL != null) {
                        distance += AMapUtils.calculateLineDistance(sLL,ll);
                    }
                    sLL = ll;
                }
                km = distance / 1000 * Data.instance.getPercentage();
                km = (double)Math.round(km*100)/100;    //转化为2位小数
                reckon();
                addTrajectory(rectifications);
            }
        });
    }

    //    计算当前的价格
    private void reckon(){
        int hour = Common.getCalendar().get(Calendar.HOUR);
        int minute = Common.getCalendar().get(Calendar.MINUTE);
        TempInfo.DataBean.TimesBean timesBean = null;
        for (TempInfo.DataBean.TimesBean tb : Data.instance.getTempInfo().getTimes()){
            int startTime = tb.getStart_time() / 60;
            int endTime = (tb.getEnd_time() + 1) / 60;
            if (hour >= startTime && hour < endTime){
                timesBean = tb;
                break;
            }
        }
        if (timesBean != null){
            if (startAmount == 0){
                startAmount = Double.valueOf(timesBean.getStart_amount());
            }
            sumMoney = startAmount;
            //计算当前里程与起步公里的差值
            double diffKm = km - timesBean.getStart_kilometre();
            if (diffKm > 0){
                //把超出的公里数除以超出后多少公里加价的值 向上取整 乘以 超出后加价的单价 得到之后加到 起步价上面去
                sumMoney += Math.ceil(diffKm / timesBean.getAverage_kilometre()) * Double.valueOf(timesBean.getAverage_amount());
            }

            double diffTime = secondsCountDown - Data.instance.getTempInfo().getWait_time() * 60;
            if (diffTime > 0){
                waitMoney = Math.ceil(diffTime / (Data.instance.getTempInfo().getAve_time() * 60)) * Double.valueOf(Data.instance.getTempInfo().getAve_minute_money());
                sumMoney += waitMoney;
            }
        }
        txtMoney.setText(String.valueOf(sumMoney));
        txtKm.setText(String.valueOf(km));
    }

    public CountDownTimer timer = new CountDownTimer(999999999, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            //重新计算 时/分/秒
            secondsCountDown++;
            String str_hour = String.format("%02d",secondsCountDown / 3600);
            String str_minute = String.format("%02d",(secondsCountDown % 3600) / 60);
            String str_second = String.format("%02d",secondsCountDown % 60);
            txtWaitTime.setText(str_hour + ":" + str_minute + ":" + str_second);
            if (secondsCountDown % 60 == 1){
                reckon();
            }
        }

        @Override
        public void onFinish() {
//            mTvShow.setEnabled(true);
//            mTvShow.setText("获取验证码");
        }
    };

    private void addTrajectory(List<LatLng> linepoints){
        StringBuilder trajectory = new StringBuilder();
        trajectory.append("[");

        int i = 0;
        for (LatLng ll : linepoints){
            if (i > 0){
                trajectory.append(",");
            }
            trajectory.append("{\"longitude\":\"");
            trajectory.append(ll.longitude);
            trajectory.append("\",\"latitude\":\"");
            trajectory.append(ll.latitude);
            trajectory.append("\"}");
            i++;
        }
        trajectory.append("]");
        Call<BaseModel> call = RetrofitHelper.instance.getApiService().addTrajectory(
                Data.instance.AdminId,
                Data.instance.getUserId(),
                Data.instance.getDriverOrderId(),
                trajectory.toString()
        );
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {

            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {

            }
        });
    }
    private void overOrder(){
        Call<BaseModel> call = RetrofitHelper.instance.getApiService().submitDriveOrder(
                Data.instance.AdminId,
                Data.instance.getUserId(),
                Data.instance.getDriverOrderId(),
                String.valueOf(sumMoney),
                this.destination,
                String.valueOf(km),
                String.valueOf(secondsCountDown),
                String.valueOf(waitMoney),
                String.valueOf(sumMoney - waitMoney)
        );
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                BaseModel baseModel = response.body();
                if (baseModel == null || baseModel.getCode() != 200) {
                    Toast.makeText(TaximeterActivity.this, baseModel.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(TaximeterActivity.this, OverOrderActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                Toast.makeText(TaximeterActivity.this, "发起失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        m_wklk.release(); //解除保持唤醒
    }

    @Override
    protected void onPause() {
        super.onPause();
        m_wklk.release();//解除保持唤醒
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_wklk.acquire(); //设置保持唤醒
    }

}
