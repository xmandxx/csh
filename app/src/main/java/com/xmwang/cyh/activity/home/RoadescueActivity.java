package com.xmwang.cyh.activity.home;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.xmwang.cyh.BaseActivity;
import com.xmwang.cyh.R;
import com.xmwang.cyh.api.ApiHomeService;
import com.xmwang.cyh.api.ApiPayService;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.common.SADialog;
import com.xmwang.cyh.common.retrofit.BaseResponse;
import com.xmwang.cyh.common.retrofit.RetrofitUtil;
import com.xmwang.cyh.common.retrofit.SubscriberOnNextListener;
import com.xmwang.cyh.model.BaseModel;
import com.xmwang.cyh.model.CSTelModel;
import com.xmwang.cyh.utils.ToastUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;

public class RoadescueActivity extends BaseActivity implements AMap.OnMyLocationChangeListener, GeocodeSearch.OnGeocodeSearchListener {

    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.txt_qing)
    EditText txtQing;
    @BindView(R.id.txt_phone)
    EditText txtPhone;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    AMap aMap;
    MyLocationStyle myLocationStyle;
    GeocodeSearch geocoderSearch;
    Location currLocation;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_roadescue);
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

    @OnClick({R.id.title_back, R.id.btn_submit, R.id.btn_kf})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.btn_submit:
                submit();
                break;
            case R.id.btn_kf:
                kf();
                break;
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        //解析result获取地址描述信息
        Log.e("amap", "逆地理编码：" + result.getRegeocodeAddress().getFormatAddress());
        address = result.getRegeocodeAddress().getFormatAddress();
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

    @Override
    public void onMyLocationChange(Location location) {
        // 定位回调监听
        if (location != null) {
            //设置希望展示的地图缩放级别
            CameraUpdate mCameraUpdate = CameraUpdateFactory.zoomTo(15);
            aMap.animateCamera(mCameraUpdate);

            Data.instance.setLocation(location);
            Log.e("amap", "onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());
            Bundle bundle = location.getExtras();
            if (bundle != null) {
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
                if (errorCode > 0) {
                    Toast.makeText(this, "定位失败，请打开定位权限", Toast.LENGTH_SHORT).show();
                }
                regeocodeQuery(location.getLatitude(), location.getLongitude());
                currLocation = location;

            } else {
                Log.e("amap", "定位信息， bundle is null ");
            }

        } else {
            Log.e("amap", "定位失败");
            Toast.makeText(this, "定位失败", Toast.LENGTH_SHORT).show();
        }

    }

    //反地理编码
    private void regeocodeQuery(double latitude, double longitude) {
        LatLonPoint point = new LatLonPoint(latitude, longitude);
        RegeocodeQuery query = new RegeocodeQuery(point, 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }

    private void init() {

        //防止穿透
        llBottom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mapView.getMap();
        }

        //设置希望展示的地图缩放级别
        CameraUpdate mCameraUpdate = CameraUpdateFactory.zoomTo(15);
        aMap.animateCamera(mCameraUpdate);

        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        //连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        if (myLocationStyle == null) {
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

        //逆地理
        if (geocoderSearch == null) {
            geocoderSearch = new GeocodeSearch(this);
        }
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    private void submit() {
        if (TextUtils.isEmpty(txtPhone.getText())) {
            ToastUtils.getInstance().toastShow("电话不能为空");
            return;
        }
//        SADialog.instance.showProgress(this);
        String ds = Data.instance.getIsOnline() ? "0" : "2";
        Observable observable = RetrofitUtil.getInstance().getmRetrofit().create(ApiHomeService.class).add_roadescue(
                Data.instance.AdminId,
                Data.instance.getUserId(),
                txtQing.getText().toString(),
                txtPhone.getText().toString(),
                String.valueOf(currLocation.getLongitude()),
                String.valueOf(currLocation.getLatitude()),
                address
        );
        RetrofitUtil.getInstance()
                .setObservable(observable)
                .base(new SubscriberOnNextListener<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        if (baseResponse.isSuccess()) {
                            ToastUtils.getInstance().toastShow(baseResponse.message);
                        }
                    }
                }, this);
    }

    private void kf() {
//        SADialog.instance.showProgress(this);
        Observable observable = RetrofitUtil.getInstance().getmRetrofit().create(ApiHomeService.class).get_cstel(
                Data.instance.AdminId
        );
        RetrofitUtil.getInstance()
                .setObservable(observable)
                .base(new SubscriberOnNextListener<BaseResponse<CSTelModel>>() {
                    @Override
                    public void onNext(BaseResponse<CSTelModel> baseResponse) {
                        if (baseResponse.getDataInfo() != null) {
                            CSTelModel m = baseResponse.getDataInfo();
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + m.getTel()));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                }, this);
    }
}
