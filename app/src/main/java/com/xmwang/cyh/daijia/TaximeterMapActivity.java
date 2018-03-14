package com.xmwang.cyh.daijia;

import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceLocation;
import com.amap.api.trace.TraceOverlay;
import com.amap.api.trace.TraceStatusListener;
import com.xmwang.cyh.R;
import com.xmwang.cyh.api.ApiService;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.retrofit.BaseResponse;
import com.xmwang.cyh.common.retrofit.RetrofitUtil;
import com.xmwang.cyh.common.retrofit.SubscriberOnNextListener;
import com.xmwang.cyh.model.Reckon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaximeterMapActivity extends AppCompatActivity implements TraceStatusListener {

    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.start_bt)
    Button startBt;
    @BindView(R.id.stop_bt)
    Button stopBt;
    @BindView(R.id.txt_money)
    TextView txtMoney;
    @BindView(R.id.txt_km)
    TextView txtKm;

    private AMap mAMap;
    PowerManager.WakeLock m_wklk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daijia_taximeter_map);
        ButterKnife.bind(this);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        m_wklk = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "cn");
        m_wklk.acquire(); //设置保持唤醒
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            mAMap.getUiSettings().setRotateGesturesEnabled(false);
            mAMap.getUiSettings().setZoomControlsEnabled(false);

            //设置中心点和缩放比例
            mAMap.moveCamera(CameraUpdateFactory.changeLatLng(Data.instance.getCurrentLocation()));
            mAMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        }
        startTrace();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("xmwang","退出页面TaximeterMapActivity");
        stopTrace();
        m_wklk.release(); //解除保持唤醒
        traceClient.destroy();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        m_wklk.release();//解除保持唤醒
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_wklk.acquire(); //设置保持唤醒
        mMapView.onResume();
    }

    @OnClick({R.id.title_back, R.id.start_bt, R.id.stop_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.start_bt:
                startTrace();
                break;
            case R.id.stop_bt:
                stopTrace();
                break;
        }
    }


    LBSTraceClient traceClient = null;
    TraceOverlay traceOverlay;

    /**
     * 停止轨迹纠偏
     */
    private void stopTrace() {
        if (traceClient == null) {
            traceClient = new LBSTraceClient(this.getApplicationContext());//LBSTraceClient.getInstance(this);
        }
        traceClient.stopTrace();
    }

    /**
     * 开启轨迹纠偏
     */
    private void startTrace() {

        if (traceClient == null) {
            traceClient = new LBSTraceClient(this.getApplicationContext());//LBSTraceClient.getInstance(this);
        }
        traceClient.startTrace(this);
    }

    @Override
    public void onTraceStatus(List<TraceLocation> locations, List<LatLng> rectifications, String errorInfo) {
        if (!TextUtils.isEmpty(errorInfo)) {
            Log.e("BasicActivity", " source count->" + locations.size() + "   result count->" + rectifications.size());
        }
        Log.e("BasicActivity", " source count->" + locations.size() + "   result count->" + rectifications.size());

        if (traceOverlay != null) {
            traceOverlay.remove();
        }
        //将得到的轨迹点显示在地图上
        traceOverlay = new TraceOverlay(mAMap, rectifications);
        traceOverlay.zoopToSpan();

        //临时存储上一个经纬度
        LatLng tempLocation = null;
        //总里程
        double distance = 0;
        for (LatLng ll : rectifications) {
            if (tempLocation != null) {
                distance += AMapUtils.calculateLineDistance(tempLocation, ll);
            }
            tempLocation = ll;
        }
        km = distance / 1000 * Data.instance.getPercentage();
        km = (double) Math.round(km * 100) / 100;    //转化为2位小数
        txtKm.setText("公里："+km+"km");
        reckon();
//		for (TraceLocation traceLocation:locations)
//		{
//			LogWriter logWriter = new LogWriter();
//			logWriter.writeLog("经度="+traceLocation.getLatitude()+"纬度="+traceLocation.getLongitude());
//		}

    }
    double startAmount = 0;
    double km = 0;
    int secondsCountDown = 0;
    double sumMoney = 0;
    double waitMoney = 0;
    //    计算当前的价格
    private void reckon() {
        Map<String, String> param = new HashMap<>();
        param.put("admin_id", Data.instance.AdminId);
        param.put("user_id", Data.instance.getUserId());
        param.put("charging_id", String.valueOf(Data.instance.getChargingId()));
        param.put("start_amount", String.valueOf(startAmount));
        param.put("km", String.valueOf(km));
        param.put("seconds_count_down", String.valueOf(secondsCountDown));
        RetrofitUtil.getInstance()
                .setObservable(RetrofitUtil.getInstance().getmRetrofit().create(ApiService.class).reckon(param))
                .base(new SubscriberOnNextListener<BaseResponse<Reckon>>() {
                    @Override
                    public void onNext(BaseResponse<Reckon> baseResponse) {
                        if (baseResponse.getDataInfo() != null) {
                            Reckon reckon = baseResponse.getDataInfo();
                            sumMoney = reckon.getSum_money();
                            waitMoney = reckon.getWait_money();
                            startAmount = reckon.getStart_amount();
                            txtMoney.setText("金额："+sumMoney+"元");
                        }
                    }
                });
    }


}
