package com.xmwang.cyh.activity.person;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xmwang.cyh.R;
import com.xmwang.cyh.api.ApiUserService;
import com.xmwang.cyh.application.GlideApp;
import com.xmwang.cyh.common.Common;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.common.event.MyCarEvent;
import com.xmwang.cyh.common.retrofit.BaseResponse;
import com.xmwang.cyh.common.retrofit.RetrofitUtil;
import com.xmwang.cyh.common.retrofit.SubscriberOnNextListener;
import com.xmwang.cyh.model.CarType;
import com.xmwang.cyh.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

public class EditCarActivity extends AppCompatActivity {

    @BindView(R.id.img_car_type_log)
    ImageView imgCarTypeLog;
    @BindView(R.id.txt_car_type_name)
    TextView txtCarTypeName;
    @BindView(R.id.txt_car_year)
    TextView txtCarYear;
    @BindView(R.id.txt_car_card)
    TextView txtCarCard;
    @BindView(R.id.txt_car_kilometre)
    EditText txtCarKilometre;
    @BindView(R.id.txt_car_number)
    EditText txtCarNumber;
    int carId = 0;
    String imgSrc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_edit_car);
        ButterKnife.bind(this);
        carId = getIntent().getIntExtra("carId",0);
        //注册事件
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//解除订阅
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(final CarType event) {
        if (event != null) {
            txtCarTypeName.setText(event.getName());
            imgSrc = event.getLogo();
            GlideApp.with(this)
                    .load(RetrofitHelper.instance.baseAdminUrl + event.getLogo())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(imgCarTypeLog);
        }
    }

    @OnClick({R.id.title_back, R.id.ll_choose_car_type, R.id.btn_save, R.id.txt_car_year})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.ll_choose_car_type:
                startActivity(new Intent(this, CarTypeListActivity.class));
                break;
            case R.id.btn_save:
                save();
                break;
            case R.id.txt_car_year:
                chooseYear();
                break;
        }
    }

    private void chooseYear() {
        Calendar.getInstance().setTimeInMillis(System.currentTimeMillis());
        DatePickerDialog dp = new DatePickerDialog(new ContextThemeWrapper(this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                txtCarYear.setText(year + "-" + (monthOfYear + 1));
            }
        },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        dp.show();
        //隐藏天数
        if (dp != null) {
            int SDKVersion = Common.getSDKVersionNumber();
            if (SDKVersion < 11) {
                ((ViewGroup) dp.getDatePicker().getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
            } else if (SDKVersion > 14) {
                ViewGroup viewGroup = (ViewGroup) (((ViewGroup) ((ViewGroup) dp.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(2));
                if (viewGroup != null) {
                    viewGroup.setVisibility(View.GONE);
                }

            }
        }
    }

    private void save() {
        if (TextUtils.isEmpty(txtCarTypeName.getText())){
            ToastUtils.getInstance().toastShow("请选择车辆类型");
            return;
        }
        if (TextUtils.isEmpty(txtCarYear.getText())){
            ToastUtils.getInstance().toastShow("请选择车辆年限");
            return;
        }
        if (TextUtils.isEmpty(txtCarCard.getText())){
            ToastUtils.getInstance().toastShow("请输入车牌号");
            return;
        }
        if (TextUtils.isEmpty(txtCarKilometre.getText())){
            ToastUtils.getInstance().toastShow("请输入公里数");
            return;
        }
        if (TextUtils.isEmpty(txtCarNumber.getText())){
            ToastUtils.getInstance().toastShow("请输入发动机号");
            return;
        }

        Map<String, String> param = RetrofitUtil.getInstance().baseParam();
        param.put("car_id",carId+"");
        param.put("car_name",txtCarTypeName.getText().toString());
        param.put("car_year",txtCarYear.getText().toString());
        param.put("car_card",txtCarCard.getText().toString());
        param.put("car_kilometre",txtCarKilometre.getText().toString());
        param.put("car_number",txtCarNumber.getText().toString());
        param.put("cars_imgs",imgSrc);
        Observable observable = RetrofitUtil.getInstance().getmRetrofit().create(ApiUserService.class).edit_user_car(param);
        RetrofitUtil.getInstance()
                .setObservable(observable)
                .base(new SubscriberOnNextListener<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        if (baseResponse.isSuccess()){
                            ToastUtils.getInstance().toastShow(baseResponse.message);
                            EventBus.getDefault().post(new MyCarEvent(1,4));
                            finish();
                        }
                    }
                });
    }
}
