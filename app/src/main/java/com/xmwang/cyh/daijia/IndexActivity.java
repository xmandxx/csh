package com.xmwang.cyh.daijia;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMyLocationChangeListener;
import com.amap.api.maps.AMap.OnCameraChangeListener;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.xmwang.cyh.BaseActivity;
import com.xmwang.cyh.R;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.common.SADialog;
import com.xmwang.cyh.common.event.UserDriverInfoEvent;
import com.xmwang.cyh.model.BaseModel;
import com.xmwang.cyh.model.DriveInfo;
import com.xmwang.cyh.utils.ActivityManager;
import com.xmwang.cyh.utils.GetTime;
import com.xmwang.cyh.utils.ToastUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xmwang on 2017/12/19.
 */

public class IndexActivity extends BaseActivity implements OnMyLocationChangeListener,OnCameraChangeListener,GeocodeSearch.OnGeocodeSearchListener {

    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.txt_money)
    TextView txtMoney;
    @BindView(R.id.txt_order_number)
    TextView txtOrderNumber;
    @BindView(R.id.txt_day_money)
    TextView txtDayMoney;
    @BindView(R.id.start_location)
    TextView startLocation;
    @BindView(R.id.end_location)
    TextView endLocation;
    @BindView(R.id.add_order)
    FancyButton addOrder;
    @BindView(R.id.edit_parment)
    FancyButton editParment;
    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.ll_bottom)
    LinearLayout linearLayout;
    @BindView(R.id.btn_online)
    FancyButton btnOnline;

    AMap aMap;
    MyLocationStyle myLocationStyle;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    Marker marker;
    GeocodeSearch geocoderSearch;
    Location currLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daijia_index);
        ButterKnife.bind(this);

        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);

        AndPermission.with(this)
                .requestCode(200)
                .permission(Permission.LOCATION)
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, List<String> grantedPermissions) {
                        // Successfully.
                        if(requestCode == 200) {
                            init();
                        }
                    }

                    @Override
                    public void onFailed(int requestCode, List<String> deniedPermissions) {
                        // Failure.
                        if(requestCode == 200) {
                            // TODO ...
                        }
                    }
                })
                .start();

    }
    @Override
    public void onBackPressed() {
//        自己处理返回按键的内容
//        //super.onBackPressed();
        // 当popupWindow 正在展示的时候 按下返回键 关闭popupWindow 否则关闭activity

        if (GetTime.getInstance().isFastDoubleClick(2000)) {
            Call<BaseModel> call = RetrofitHelper.instance.getApiService().online(
                    Data.instance.AdminId,
                    Data.instance.getUserId(),
                    "2",
                    String.valueOf(currLocation.getLongitude()),
                    String.valueOf(currLocation.getLatitude())
            );
            call.enqueue(new Callback<BaseModel>() {
                @Override
                public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {

                }

                @Override
                public void onFailure(Call<BaseModel> call, Throwable t) {

                }
            });
            ActivityManager.getInstance().removeAllActivity();
        } else {
            ToastUtils.getInstance().toastShow("再按一次退出程序");
        }
    }
    private void init() {
        //注册事件
        EventBus.getDefault().register(this);
        //防止穿透
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        //设置标题日期
        titleText.setText(stringData());
        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mapView.getMap();
        }

        //设置希望展示的地图缩放级别
        CameraUpdate mCameraUpdate = CameraUpdateFactory.zoomTo(15);
        aMap.animateCamera(mCameraUpdate);

        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        //连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        if(myLocationStyle ==null) {
            myLocationStyle = new MyLocationStyle();
        }
        myLocationStyle.showMyLocation(true);  //隐藏定位蓝点
        myLocationStyle.isMyLocationShowing();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);

        //
        aMap.setMyLocationStyle(myLocationStyle);
        //设置定位监听
        aMap.setOnMyLocationChangeListener(this);



        marker = aMap.addMarker(new MarkerOptions());

        //逆地理
        if (geocoderSearch == null){
            geocoderSearch = new GeocodeSearch(this);
        }
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(final UserDriverInfoEvent event) {
        loadNetworkData();
    }
    private void loadNetworkData(){
        Call<DriveInfo> call = RetrofitHelper.instance.getApiService().driveInfo(Data.instance.AdminId, Data.instance.getUserId());
        call.enqueue(new Callback<DriveInfo>() {
            @Override
            public void onResponse(Call<DriveInfo> call, Response<DriveInfo> response) {
                DriveInfo driveInfo = response.body();
                if (driveInfo != null){
                    if (driveInfo.getData().size() > 0){
                        Data.instance.setDriveInfo(driveInfo.getData().get(0));
                        txtMoney.setText(Data.instance.getDriveInfo().getDriver_money());
                        txtDayMoney.setText(Data.instance.getDriveInfo().getDay_get_money());
                        txtOrderNumber.setText(Data.instance.getDriveInfo().getDay_order_count());
                        if (driveInfo.getData().get(0).getDriver_status() == 2){
                            Data.instance.setOnline(false);
                        }else{
                            Data.instance.setOnline(true);
                        }
                    }
                }
                online();
            }

            @Override
            public void onFailure(Call<DriveInfo> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.start_location, R.id.end_location,R.id.btn_online, R.id.add_order, R.id.edit_parment,R.id.title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.start_location:
                break;
            case R.id.end_location:
                startActivity(new Intent(this,ChooseLocationActivity.class));
                break;
            case R.id.btn_online:
                Data.instance.setOnline(!Data.instance.getIsOnline());
                online();
                break;
            case R.id.add_order:
                //创建订单
                if (!Data.instance.getIsOnline()){
                     ToastUtils.getInstance().toastShow("请先上线");
                     return;
                }
                startActivity(new Intent(this,CreateDJOrderActivity.class));
                break;
            case R.id.edit_parment:
                //编辑参数
                startActivity(new Intent(this, EditParmentActivity.class));
                break;
            case R.id.title_right:
                startActivity(new Intent(this, ManagerActivity.class));
                break;
        }
    }

    private void online(){
        SADialog.instance.showProgress(this);
        String ds = Data.instance.getIsOnline() ? "0" : "2";
        Call<BaseModel> call = RetrofitHelper.instance.getApiService().online(
                Data.instance.AdminId,
                Data.instance.getUserId(),
                ds,
                String.valueOf(currLocation.getLongitude()),
                String.valueOf(currLocation.getLatitude())
        );
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                BaseModel baseModel = response.body();
                SADialog.instance.hideProgress();
                if (baseModel.getCode() != RetrofitHelper.instance.SUCCESS_CODE) {
                    ToastUtils.getInstance().toastShow(baseModel.getMessage());
                    return;
                }
                if (Data.instance.getIsOnline()){
                    btnOnline.setText("点击离线");
                    btnOnline.setBackgroundColor(Color.parseColor("#98FB98"));
                }else{
                    btnOnline.setText("点击上线");
                    btnOnline.setBackgroundColor(Color.parseColor("#B0C4DE"));
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                SADialog.instance.hideProgress();
                ToastUtils.getInstance().toastShow("操作失败");
            }
        });
    }

    public String stringData() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return mMonth + "月" + mDay + "日" + " 星期" + mWay;
    }
    //反地理编码
    private void regeocodeQuery(double latitude,double longitude){
        LatLonPoint point = new LatLonPoint(latitude,longitude);
        RegeocodeQuery query = new RegeocodeQuery(point, 200,GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }
    @Override
    public void onMyLocationChange(Location location) {
        // 定位回调监听
        if(location != null) {
            //设置希望展示的地图缩放级别
            CameraUpdate mCameraUpdate = CameraUpdateFactory.zoomTo(15);
            aMap.animateCamera(mCameraUpdate);

            Data.instance.setLocation(location);
            Log.e("amap", "onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());
            Bundle bundle = location.getExtras();
            if(bundle != null) {
                int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
                String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
                // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
                int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);

                /*
                errorCode
                errorInfo
                locationType
                */
//                Log.e("amap", "定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType );
                if (errorCode > 0){
                    Toast.makeText(this,"定位失败，请打开定位权限",Toast.LENGTH_SHORT).show();
                }
                regeocodeQuery(location.getLatitude(),location.getLongitude());
                LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                marker.setPosition(latLng);
                //判定第一次加载 在线数据
                if (currLocation == null){
                    currLocation = location;
                    loadNetworkData();
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            aMap.setOnCameraChangeListener(IndexActivity.this);
                        }
                    }, 2000);
                }

            } else {
                Log.e("amap", "定位信息， bundle is null ");

            }

        } else {
            Log.e("amap", "定位失败");
            Toast.makeText(this,"定位失败",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        regeocodeQuery(cameraPosition.target.latitude,cameraPosition.target.longitude);
        marker.setPosition(cameraPosition.target);
    }

    @Override
    public void onCameraChangeFinish(CameraPosition position){


    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        //解析result获取地址描述信息
        Log.e("amap", "逆地理编码："+result.getRegeocodeAddress().getFormatAddress());
        startLocation.setText(result.getRegeocodeAddress().getFormatAddress());
        Data.instance.setFormatAddress(result.getRegeocodeAddress().getFormatAddress());
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
        SADialog.instance.hideProgress();
        EventBus.getDefault().unregister(this);//解除订阅
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }
}
