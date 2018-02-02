package com.xmwang.cyh.daijia;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMyLocationChangeListener;
import com.amap.api.maps.AMap.OnCameraChangeListener;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
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
import com.flyco.dialog.widget.ActionSheetDialog;
import com.xmwang.cyh.BaseActivity;
import com.xmwang.cyh.MyApplication;
import com.xmwang.cyh.R;
import com.xmwang.cyh.api.ApiService;
import com.xmwang.cyh.common.CheckVersion;
import com.xmwang.cyh.common.Common;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.common.retrofit.RetrofitUtil;
import com.xmwang.cyh.common.SADialog;
import com.xmwang.cyh.common.event.UserDriverInfoEvent;
import com.xmwang.cyh.common.retrofit.BaseResponse;
import com.xmwang.cyh.common.retrofit.ProgressSubscriber;
import com.xmwang.cyh.common.retrofit.SubscriberOnNextListener;
import com.xmwang.cyh.model.BaseModel;
import com.xmwang.cyh.model.DriveInfo;
import com.xmwang.cyh.model.DriveInfoModel;
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
import rx.Observable;

/**
 * Created by xmwang on 2017/12/19.
 */

public class IndexActivity extends BaseActivity implements OnCameraChangeListener, GeocodeSearch.OnGeocodeSearchListener,AMapLocationListener {

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
    AMapLocation currLocation;

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
                        if (requestCode == 200) {
                            MyApplication.getInstances().startLocation();//启动全局定位
                            init();
                        }
                    }

                    @Override
                    public void onFailed(int requestCode, List<String> deniedPermissions) {
                        // Failure.
                        if (requestCode == 200) {
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
            Observable observable = RetrofitUtil.getInstance().getmRetrofit().create(ApiService.class).online(
                    Data.instance.AdminId,
                    Data.instance.getUserId(),
                    "2",
                    String.valueOf(currLocation.getLongitude()),
                    String.valueOf(currLocation.getLatitude())
            );
            RetrofitUtil.getInstance()
                    .setObservable(observable)
                    .base(new SubscriberOnNextListener<BaseResponse>() {
                        @Override
                        public void onNext(BaseResponse baseResponse) {

                        }
                    }, this);

            ActivityManager.getInstance().removeAllActivity();
        } else {
            ToastUtils.getInstance().toastShow("再按一次退出程序");
        }
    }
    LocationSource.OnLocationChangedListener mListener;
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;

    private void init() {
        //检查版本
        CheckVersion.instance.checkVersion(this);
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
        // 设置定位监听
        aMap.setLocationSource(new LocationSource() {
            @Override
            public void activate(OnLocationChangedListener onLocationChangedListener) {
                mListener = onLocationChangedListener;
                if (mlocationClient == null) {
                    //初始化定位
                    mlocationClient = new AMapLocationClient(IndexActivity.this);
                    //初始化定位参数
                    mLocationOption = new AMapLocationClientOption();
                    //设置定位回调监听
                    mlocationClient.setLocationListener(IndexActivity.this);
                    //设置为高精度定位模式
                    mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                    mLocationOption.setOnceLocationLatest(true);//获取最近3s内精度最高的一次定位结果：
                    //设置定位参数
                    mlocationClient.setLocationOption(mLocationOption);
                    // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
                    // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
                    // 在定位结束后，在合适的生命周期调用onDestroy()方法
                    // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
                    mlocationClient.startLocation();//启动定位
                    mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
                    if (null != mlocationClient) {
                        mlocationClient.setLocationOption(mLocationOption);
                        //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
                        mlocationClient.stopLocation();
                        mlocationClient.startLocation();
                    }
                    mLocationOption.setOnceLocation(true);//单次定位
                }
            }

            @Override
            public void deactivate() {
                mListener = null;
                if (mlocationClient != null) {
                    mlocationClient.stopLocation();
                    mlocationClient.onDestroy();
                }
                mlocationClient = null;
            }
        });
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
//        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

        //设置希望展示的地图缩放级别
        CameraUpdate mCameraUpdate = CameraUpdateFactory.zoomTo(15);
        aMap.animateCamera(mCameraUpdate);

        //设置移动屏幕监听
        aMap.setOnCameraChangeListener(IndexActivity.this);

        marker = aMap.addMarker(new MarkerOptions());

        //逆地理
        if (geocoderSearch == null) {
            geocoderSearch = new GeocodeSearch(this);
        }
        geocoderSearch.setOnGeocodeSearchListener(this);
    }
    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                Data.instance.setLocation(amapLocation);
                regeocodeQuery(amapLocation.getLatitude(), amapLocation.getLongitude());
                LatLng latLng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                marker.setPosition(latLng);
                currLocation = amapLocation;
                loadNetworkData();
            } else {
                ToastUtils.getInstance().toastShow("定位失败，请打开定位权限");
//                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
//                Log.e("AmapErr",errText);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(final UserDriverInfoEvent event) {
        loadNetworkData();
    }

    private void loadNetworkData() {
        Observable observable = RetrofitUtil.getInstance().getmRetrofit().create(ApiService.class).driveInfo(Data.instance.AdminId, Data.instance.getUserId());
        RetrofitUtil.getInstance()
                .setObservable(observable)
                .base(new SubscriberOnNextListener<BaseResponse<DriveInfo>>() {
                    @Override
                    public void onNext(BaseResponse<DriveInfo> baseResponse) {
                        if (baseResponse.getDataInfo() != null){
                            Data.instance.setDriveInfo(baseResponse.getDataInfo());
                            txtMoney.setText(Data.instance.getDriveInfo().getDriver_money());
                            txtDayMoney.setText(Data.instance.getDriveInfo().getDay_get_money());
                            txtOrderNumber.setText(Data.instance.getDriveInfo().getDay_order_count());
                            if (baseResponse.getDataInfo().getDriver_status() == 2) {
                                Data.instance.setOnline(false);
                            } else {
                                Data.instance.setOnline(true);
                            }
                        }
                        online();
                    }
                });
    }

    @OnClick({R.id.start_location, R.id.ll_end_location, R.id.btn_online, R.id.add_order, R.id.edit_parment, R.id.title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.start_location:
                break;
            case R.id.ll_end_location:
                startActivityForResult(new Intent(this, ChooseLocationActivity.class), 200);
                break;
            case R.id.btn_online:
                Data.instance.setOnline(!Data.instance.getIsOnline());
                online();
                break;
            case R.id.add_order:
//                创建订单
                if (!Data.instance.getIsOnline()) {
                    ToastUtils.getInstance().toastShow("请先上线");
                    return;
                }
                if (Double.valueOf(Data.instance.getDriveInfo().getDriver_money()) < Data.instance.getDriveInfo().getDriver_min_money()){
                    ToastUtils.getInstance().toastShow("余额不足，接单最低需要"+Data.instance.getDriveInfo().getDriver_min_money()+"元");
                    return;
                }
                startActivity(new Intent(this, CreateDJOrderActivity.class));
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

    // 为了获取结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RESULT_OK，判断另外一个activity已经结束数据输入功能，Standard activity result:
        // operation succeeded. 默认值是-1
        if (resultCode == 200) {
            if (requestCode == 200) {
                String destination = data.getStringExtra("destination");
                if (destination != null){
                    endLocation.setText(destination);
                    Data.instance.setTempDestination(destination);
                }
                //设置结果显示框的显示数值
//              result.setText(String.valueOf(three));
            }
        }
    }

    private void online() {
//        SADialog.instance.showProgress(this);
        String ds = Data.instance.getIsOnline() ? "0" : "2";
        Observable observable = RetrofitUtil.getInstance().getmRetrofit().create(ApiService.class).online(
                Data.instance.AdminId,
                Data.instance.getUserId(),
                ds,
                String.valueOf(currLocation.getLongitude()),
                String.valueOf(currLocation.getLatitude())
        );
        RetrofitUtil.getInstance()
                .setObservable(observable)
                .base(new SubscriberOnNextListener<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        if (baseResponse.isSuccess()){
                            if (Data.instance.getIsOnline()) {
                                btnOnline.setText("点击离线");
                                btnOnline.setBackgroundColor(Color.parseColor("#98FB98"));
                            } else {
                                btnOnline.setText("点击上线");
                                btnOnline.setBackgroundColor(Color.parseColor("#B0C4DE"));
                            }
                        }
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
    private void regeocodeQuery(double latitude, double longitude) {
        LatLonPoint point = new LatLonPoint(latitude, longitude);
        RegeocodeQuery query = new RegeocodeQuery(point, 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        regeocodeQuery(cameraPosition.target.latitude, cameraPosition.target.longitude);
        marker.setPosition(cameraPosition.target);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        //解析result获取地址描述信息
        Log.e("amap", "逆地理编码：" + result.getRegeocodeAddress().getFormatAddress());
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
        if(null != mlocationClient){
            mlocationClient.onDestroy();
        }
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
