package com.xmwang.cyh.activity.home;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xmwang.cyh.BaseActivity;
import com.xmwang.cyh.R;
import com.xmwang.cyh.api.ApiHomeService;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.common.retrofit.BaseResponse;
import com.xmwang.cyh.common.retrofit.RetrofitUtil;
import com.xmwang.cyh.common.retrofit.SubscriberOnNextListener;
import com.xmwang.cyh.model.BaseModel;
import com.xmwang.cyh.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;

public class AddYangcheActivity extends BaseActivity {

    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.txt_t_name)
    TextView txtTName;
    @BindView(R.id.txt_money)
    EditText txtMoney;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_value)
    EditText txtValue;
    @BindView(R.id.txt_name1)
    TextView txtName1;
    @BindView(R.id.txt_value1)
    EditText txtValue1;
    private int index = 1;
    private Calendar calendar;
    private String messageTitle = "维修地点";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_add_yangche);
        ButterKnife.bind(this);
        calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String date = sdf.format(new java.util.Date());
        titleText.setText(date);
        calendar.setTimeInMillis(System.currentTimeMillis());
    }

    @OnClick({R.id.title_back, R.id.title_right, R.id.ll1, R.id.ll2, R.id.ll3, R.id.ll4, R.id.ll5, R.id.ll6, R.id.ll7, R.id.ll8, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.title_right:
                chooseTime();
                break;
            case R.id.ll1:
                updateView(1,R.mipmap.maintain,"维修","维修地点：","故障名称：");
                break;
            case R.id.ll2:
                updateView(2,R.mipmap.insurance,"保险","购买险种：","投保公司：");
                break;
            case R.id.ll3:
                updateView(3,R.mipmap.park,"停车","停车地点：","其他描述：");
                break;
            case R.id.ll4:
                updateView(4,R.mipmap.car_wash,"洗车","洗车地点：","其他描述：");
                break;
            case R.id.ll5:
                updateView(5,R.mipmap.break_rules,"违章","违章地点：","违章名称：");
                break;
            case R.id.ll6:
                updateView(6,R.mipmap.upkeep,"保养","保养地点：","保养情况：");
                break;
            case R.id.ll7:
                updateView(7,R.mipmap.road_toll,"过路费","过路地点：","其他描述：");
                break;
            case R.id.ll8:
                updateView(8,R.mipmap.else_s,"其他","其他花费：","其他描述：");
                break;
            case R.id.btn_save:
                save();
                break;
        }
    }

    private void updateView(int index,int resId,String tName,String name,String name1){
        this.index = index;
        img.setImageResource(resId);
        txtTName.setText(tName);
        txtName.setText(name);
        txtName1.setText(name1);
        messageTitle = name;
    }
    private void save(){
        if (TextUtils.isEmpty(txtMoney.getText())){
            ToastUtils.getInstance().toastShow("金额不能为空");
            return;
        }
        if (TextUtils.isEmpty(txtValue.getText())){
            ToastUtils.getInstance().toastShow(messageTitle+"不能为空");
            return;
        }
        Observable observable = RetrofitUtil.getInstance().getmRetrofit().create(ApiHomeService.class).add_car_fee(
                Data.instance.AdminId,
                Data.instance.getUserId(),
                index,
                txtMoney.getText().toString().trim(),
                txtValue.getText().toString().trim(),
                txtValue1.getText().toString().trim(),
                titleText.getText().toString().replace(".","-")
        );
        RetrofitUtil.getInstance()
                .setObservable(observable)
                .base(new SubscriberOnNextListener<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        if (baseResponse.isSuccess()) {
                            ToastUtils.getInstance().toastShow(baseResponse.message);
                            Intent intent = new Intent("com.YangcheActivity");
                            sendBroadcast(intent);      //发送广播
                            finish();
                        }
                    }
                }, this);

    }
    private void chooseTime() {
        DatePickerDialog dp = new DatePickerDialog(new ContextThemeWrapper(this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                titleText.setText(year + "." + (monthOfYear + 1) + "."+dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dp.show();
    }
}
