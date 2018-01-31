package com.xmwang.cyh.activity.home;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.xmwang.cyh.BaseActivity;
import com.xmwang.cyh.R;
import com.xmwang.cyh.api.ApiHomeService;
import com.xmwang.cyh.api.ApiPayService;
import com.xmwang.cyh.api.ApiUserService;
import com.xmwang.cyh.common.Common;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.common.SADialog;
import com.xmwang.cyh.common.retrofit.BaseResponse;
import com.xmwang.cyh.common.retrofit.RetrofitUtil;
import com.xmwang.cyh.common.retrofit.SubscriberOnNextListener;
import com.xmwang.cyh.model.CarFee;
import com.xmwang.cyh.viewholder.CarFeeHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;

public class YangcheActivity extends BaseActivity {

    @BindView(R.id.txt_time)
    TextView txtTime;
    @BindView(R.id.txt_money)
    TextView txtMoney;
    @BindView(R.id.erv_list)
    EasyRecyclerView ervList;
    private Calendar calendar;
    private RecyclerArrayAdapter<CarFee> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_yangche);
        ButterKnife.bind(this);
        // 创建 Calendar 对象
        calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
        String date = sdf.format(new java.util.Date());
        txtTime.setText(date);
        calendar.setTimeInMillis(System.currentTimeMillis());
        init();
    }

    @OnClick({R.id.title_back, R.id.title_right, R.id.ll_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.title_right:
                startActivity(new Intent(this, AddYangcheActivity.class));
                break;
            case R.id.ll_time:
                chooseTime();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        SADialog.instance.hideProgress();
    }

    private void init() {
        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.YangcheActivity");
        registerReceiver(mReceiver, filter);
        //列表处理
        ervList.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }

        });
        DividerDecoration itemDecoration = new DividerDecoration(this.getResources().getColor(R.color.backgroundColor), 1, 0, 0);
        itemDecoration.setDrawLastItem(false);
        ervList.addItemDecoration(itemDecoration);

        ervList.setAdapterWithProgress(adapter = new RecyclerArrayAdapter<CarFee>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new CarFeeHolder(parent);
            }
        });

        getList();
    }

    /**
     * 广播接收
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getList();
        }
    };

    private void chooseTime() {
        DatePickerDialog dp = new DatePickerDialog(new ContextThemeWrapper(this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                txtTime.setText(year + "年" + (monthOfYear + 1) + "月");
                getList();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dp.show();
        //隐藏天数
        if (dp != null) {
            int SDKVersion = Common.getSDKVersionNumber();
            if (SDKVersion < 11) {
                ((ViewGroup) dp.getDatePicker().getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
            } else if (SDKVersion > 14) {
                ((ViewGroup) ((ViewGroup) dp.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
            }
        }
    }

    private void getList() {
//        SADialog.instance.showProgress(this);
        Observable observable = RetrofitUtil.getInstance().getmRetrofit().create(ApiHomeService.class).get_car_fee(
                Data.instance.AdminId,
                Data.instance.getUserId(),
                txtTime.getText().toString().replace("年", "-").replace("月", "")
        );
        RetrofitUtil.getInstance()
                .setObservable(observable)
                .base(new SubscriberOnNextListener<BaseResponse<CarFee>>() {
                    @Override
                    public void onNext(BaseResponse<CarFee> baseResponse) {
                        adapter.addAll(baseResponse.getDataList());
                        Double money = 0.00;
                        for (CarFee m : baseResponse.getDataList()) {
                            money += Double.valueOf(m.getFee_money());
                        }
                        txtMoney.setText(String.valueOf(money));
                    }
                }, this);
    }

}
