package com.xmwang.cyh.daijia;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.xmwang.cyh.BaseActivity;
import com.xmwang.cyh.R;
import com.xmwang.cyh.api.ApiService;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.common.event.UserDriverInfoEvent;
import com.xmwang.cyh.common.retrofit.BaseResponse;
import com.xmwang.cyh.common.retrofit.RetrofitUtil;
import com.xmwang.cyh.common.retrofit.SubscriberOnNextListener;
import com.xmwang.cyh.model.KilometerModel;
import com.xmwang.cyh.model.MoneyModel;
import com.xmwang.cyh.model.TempInfo;
import com.xmwang.cyh.model.TempModel;
import com.xmwang.cyh.model.TimeModel;
import com.xmwang.cyh.utils.ToastUtils;
import com.xmwang.cyh.viewholder.TempHolder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chihane.jdselector.BottomDialog;
import chihane.jdselector.DataProvider;
import chihane.jdselector.ISelectAble;
import chihane.jdselector.SelectedListener;
import chihane.jdselector.Selector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by xmWang on 2017/12/25.
 */

public class EditTempActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @BindView(R.id.et_temp_name)
    EditText etTempName;
    @BindView(R.id.ln1)
    LinearLayout ln1;
    @BindView(R.id.tv_wait_time)
    TextView tvWaitTime;
    @BindView(R.id.tv_ave_time)
    TextView tvAveTime;
    @BindView(R.id.tv_ave_money)
    TextView tvAveMoney;

    private RecyclerArrayAdapter<TempModel> adapter_temp;
    //    public static List<List<EditText>> ls_et = new ArrayList<>();
    private List<TempModel> ls = new ArrayList<>();

    //弹窗所用数据
    private List<ISelectAble> ls_money = new ArrayList<>();//元
    private List<ISelectAble> ls_money2 = new ArrayList<>();//角
    private List<ISelectAble> ls_km = new ArrayList<>();
    private List<ISelectAble> ls_time = new ArrayList<>();//分钟
    private BottomDialog dialog_km;
    private BottomDialog dialog_money;
    private TimePickerDialog dialog_time;
    private Calendar calendar;

    private PopupWindow popupWindow;

    //公里弹窗数据记录
    private TextView tv1, tv2, tv3, tv4;
    private LinearLayout ln_popup_bg;
    public int popup_position;//记录调出弹窗的item的position
    public int charging_id = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daijia_edit_temp);
        ButterKnife.bind(this);

        charging_id = getIntent().getIntExtra("charging_id",0);

        recyclerView.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        recyclerView.setAdapter(adapter_temp = new RecyclerArrayAdapter<TempModel>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new TempHolder(parent);
            }
        });

        // 创建 Calendar 对象
        calendar = Calendar.getInstance();

        //list初始数据
        ls.clear();
        ls.add(new TempModel("0", "0", "0", "0", "0", "0"));
        ls.add(new TempModel("0", "0", "0", "0", "0", "0"));
        ls.add(new TempModel("0", "0", "0", "0", "0", "0"));
        ls.add(new TempModel("0", "0", "0", "0", "0", "0"));
        ls.add(new TempModel("0", "0", "0", "0", "0", "0"));
        adapter_temp.addAll(ls);

        //初始化弹窗
        dialog_km = new BottomDialog(this);
        dialog_money = new BottomDialog(this);

        popupWindow = new PopupWindow(this);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        View view = LayoutInflater.from(this).inflate(R.layout.popup_km, null);
        tv1 = view.findViewById(R.id.popup_t1);
        tv2 = view.findViewById(R.id.popup_t2);
        tv3 = view.findViewById(R.id.popup_t3);
        tv4 = view.findViewById(R.id.popup_t4);
        ln_popup_bg = view.findViewById(R.id.popup_bg);

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogKm(popup_position, 1);
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogKm(popup_position, 2);
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogMoney(popup_position, 1);
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击完成后需要将选择的起步公里数显示到list上
                adapter_temp.notifyDataSetChanged();
                dissmissPopup();
            }
        });
        ln_popup_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dissmissPopup();
            }
        });
        popupWindow.setContentView(view);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x22000000));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);


        initDialogData();

        initData();

    }
    private void initData(){
        Observable observable = RetrofitUtil.getInstance().getmRetrofit().create(ApiService.class).getTempInfo(Data.instance.getUserId(),charging_id);
        RetrofitUtil.getInstance()
                .setObservable(observable)
                .base(new SubscriberOnNextListener<BaseResponse<TempInfo>>() {
                    @Override
                    public void onNext(BaseResponse<TempInfo> baseResponse) {
                        if (baseResponse.getDataInfo() != null) {
                            TempInfo tempModel = baseResponse.getDataInfo();
                            etTempName.setText(tempModel.getCharging());
                            tvWaitTime.setText(String.valueOf(tempModel.getWait_time()));
                            tvAveTime.setText(String.valueOf(tempModel.getAve_time()));
                            tvAveMoney.setText(String.valueOf(tempModel.getAve_minute_money()));
                            adapter_temp.removeAll();
                            ls.clear();
                            for (TempInfo.TimesBean timesBean: tempModel.getTimes()) {
                                //list初始数据

                                String startTime =Integer.valueOf(timesBean.getStart_time() / 60) + ":" + Integer.valueOf(timesBean.getStart_time() - (Integer.valueOf(timesBean.getStart_time() / 60) * 60));
                                String endTime = Integer.valueOf(timesBean.getEnd_time() / 60) + ":" + Integer.valueOf(timesBean.getEnd_time() - (Integer.valueOf(timesBean.getEnd_time() / 60) * 60));

                                ls.add(new TempModel(startTime,
                                        endTime,
                                        timesBean.getStart_amount(),
                                        String.valueOf(timesBean.getStart_kilometre()),
                                        String.valueOf(timesBean.getAverage_kilometre()),
                                        String.valueOf(timesBean.getAverage_amount()))
                                );
//                    ls.add(new TempModel("0", "0", "0", "0", "0", "0"));
//                    ls.add(new TempModel("0", "0", "0", "0", "0", "0"));
//                    ls.add(new TempModel("0", "0", "0", "0", "0", "0"));
//                    ls.add(new TempModel("0", "0", "0", "0", "0", "0"));
//                    ls.add(new TempModel("0", "0", "0", "0", "0", "0"));

                            }
                            adapter_temp.addAll(ls);
                        }
                    }
                }, this);
    }

    /**
     * 隐藏弹窗(先清空缓存数据)
     */
    private void dissmissPopup() {
        tv1.setText("");
        tv2.setText("");
        tv3.setText("");
        popupWindow.dismiss();
    }

    /**
     * 显示复合公里选择
     */
    public void showPopup() {
        popupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        tv1.setText(ls.get(popup_position).getS4());
        tv2.setText(ls.get(popup_position).getS5());
        tv3.setText(ls.get(popup_position).getS6());
    }

    /**
     * 时间弹窗
     *
     * @param position
     * @param position2
     */
    public void showDialogTime(final int position, final int position2) {
        //初始化并显示TimePickerDialog
        new TimePickerDialog(EditTempActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        if (position2 == 0) {
                            ls.get(position).setS1(hour + ":" + minute);
                        } else {
                            ls.get(position).setS2(hour + ":" + minute);
                        }
                        adapter_temp.notifyDataSetChanged();
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true).show();
    }

    /**
     * 公里弹窗
     *
     * @param position
     * @param tag      标识  0：list中的公里选择；  >=1：popup中的公里选择(序号对应tv号码，例：1对应tv1)
     */
    public void showDialogKm(final int position, final int tag) {
        Selector selector = new Selector(this, 1);

        selector.setDataProvider(new DataProvider() {
            @Override
            public void provideData(int currentDeep, int preId, int position, DataReceiver receiver) {
                //根据tab的深度和前一项选择的id，获取下一级菜单项
                receiver.send(ls_km);
            }
        });
        selector.setSelectedListener(new SelectedListener() {
            @Override
            public void onAddressSelected(ArrayList<ISelectAble> selectAbles) {
                if (tag == 0) {
                    ls.get(position).setS4(selectAbles.get(0).getName().substring(0, selectAbles.get(0).getName().length() - 2));
                    adapter_temp.notifyDataSetChanged();
                } else {
                    switch (tag) {
                        case 1:
                            tv1.setText(selectAbles.get(0).getName().substring(0, selectAbles.get(0).getName().length() - 2));
                            ls.get(position).setS4(selectAbles.get(0).getName().substring(0, selectAbles.get(0).getName().length() - 2));
//                            s_p1 = selectAbles.get(0).getName().substring(0, selectAbles.get(0).getName().length() - 2);
                            break;
                        case 2:
                            tv2.setText(selectAbles.get(0).getName().substring(0, selectAbles.get(0).getName().length() - 2));
                            ls.get(position).setS5(selectAbles.get(0).getName().substring(0, selectAbles.get(0).getName().length() - 2));
//                            s_p2 = selectAbles.get(0).getName().substring(0, selectAbles.get(0).getName().length() - 2);
                            break;
//                        case 3:
//                            tv3.setText(selectAbles.get(0).getName());
//                            s_p3 = selectAbles.get(0).getName().substring(0, selectAbles.get(0).getName().length() - 2);
//                            break;
                    }
                }
                dialog_km.dismiss();
            }
        });

        dialog_km.init(this, selector);
        dialog_km.show();
    }

    /**
     * 价钱弹窗
     *
     * @param position
     * @param tag      标识  0：list中的价钱选择；  1：popup中的价钱选择;  2最下面的价钱选择
     */
    public void showDialogMoney(final int position, final int tag) {
        Selector selector = new Selector(this, 2);

        selector.setDataProvider(new DataProvider() {
            @Override
            public void provideData(int currentDeep, int preId, int position, DataReceiver receiver) {
                //根据tab的深度和前一项选择的id，获取下一级菜单项
                switch (currentDeep) {
                    case 0:
                        receiver.send(ls_money);
                        break;
                    case 1:
                        receiver.send(ls_money2);
                        break;
                }
            }
        });
        selector.setSelectedListener(new SelectedListener() {
            @Override
            public void onAddressSelected(ArrayList<ISelectAble> selectAbles) {
                if (tag == 0) {
                    //拼接价钱并添加在list
                    ls.get(position).setS3(selectAbles.get(0).getName().substring(0, selectAbles.get(0).getName().length() - 1) + "." +
                            selectAbles.get(selectAbles.size() - 1).getName().substring(0, selectAbles.get(selectAbles.size() - 1).getName().length() - 1));
                    adapter_temp.notifyDataSetChanged();
                } else if (tag == 1) {
                    tv3.setText(selectAbles.get(0).getName().substring(0, selectAbles.get(0).getName().length() - 1) + "." +
                            selectAbles.get(selectAbles.size() - 1).getName().substring(0, selectAbles.get(selectAbles.size() - 1).getName().length() - 1));
//                    s_p3 = selectAbles.get(0).getName().substring(0, selectAbles.get(0).getName().length() - 1) + "." +
//                            selectAbles.get(selectAbles.size() - 1).getName().substring(0, selectAbles.get(selectAbles.size() - 1).getName().length() - 1);
                    ls.get(position).setS6(selectAbles.get(0).getName().substring(0, selectAbles.get(0).getName().length() - 1) + "." +
                            selectAbles.get(selectAbles.size() - 1).getName().substring(0, selectAbles.get(selectAbles.size() - 1).getName().length() - 1));
                } else {
                    tvAveMoney.setText(selectAbles.get(0).getName().substring(0, selectAbles.get(0).getName().length() - 1) + "." +
                            selectAbles.get(selectAbles.size() - 1).getName().substring(0, selectAbles.get(selectAbles.size() - 1).getName().length() - 1));
                }
                dialog_money.dismiss();
            }
        });

        dialog_money.init(this, selector);
        dialog_money.show();
    }

    /**
     * 时间弹窗（分钟）
     *
     * @param tag （页面下面两个分钟框，此字段仅用于区分这两个框）
     */
    public void showDialogMinute(final int tag) {
        Selector selector = new Selector(this, 1);

        selector.setDataProvider(new DataProvider() {
            @Override
            public void provideData(int currentDeep, int preId, int position, DataReceiver receiver) {
                //根据tab的深度和前一项选择的id，获取下一级菜单项
                receiver.send(ls_time);
            }
        });
        selector.setSelectedListener(new SelectedListener() {
            @Override
            public void onAddressSelected(ArrayList<ISelectAble> selectAbles) {
                switch (tag) {
                    case 0://第一个
                        tvWaitTime.setText(selectAbles.get(0).getName().substring(0, selectAbles.get(0).getName().length() - 2));
                        break;
                    case 1:
                        tvAveTime.setText(selectAbles.get(0).getName().substring(0, selectAbles.get(0).getName().length() - 2));
                        break;
                }
                dialog_money.dismiss();
            }
        });

        dialog_money.init(this, selector);
        dialog_money.show();
    }


    /**
     * 初始化弹窗数据
     */
    private void initDialogData() {
        for (int i = 1; i <= 120; i++) {
            ls_money.add(new MoneyModel(i + "元"));
            ls_km.add(new KilometerModel(i + "公里"));
            ls_time.add(new TimeModel(i + "分钟"));
        }

        for (int i = 0; i < 10; i++) {
            ls_money2.add(new MoneyModel(i + "角"));
        }
    }

    /**
     * 删除指定item
     */
    public void deleteItem(int position) {
        if (ls.size() <= 1) {
            ToastUtils.getInstance().toastShow("最少必须有一项");
            return;
        }

        ls.remove(position);
//        ls_et.remove(position);
//        //删除时数据会错乱，需要保存并重新加载
//        for (int i = 0; i < ls_et.size(); i++) {
//            ls.get(i).setS1(ls_et.get(i).get(0).getText().toString());
//            ls.get(i).setS2(ls_et.get(i).get(1).getText().toString());
//            ls.get(i).setS3(ls_et.get(i).get(2).getText().toString());
//            ls.get(i).setS4(ls_et.get(i).get(3).getText().toString());
//        }

        adapter_temp.clear();
        adapter_temp.addAll(ls);


    }

    @OnClick({R.id.title_back, R.id.title_right, R.id.edittemp_bt_add, R.id.tv_wait_time, R.id.tv_ave_time, R.id.tv_ave_money})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.title_right:
                break;
            case R.id.btn_save_temp:
                break;
            case R.id.edittemp_bt_add:
                if (ls.size() >= 5) {
                    ToastUtils.getInstance().toastShow("最多只能添加5个");
                } else {
//                    //删除时数据会错乱，需要保存并重新加载
//                    for (int i = 0; i < ls_et.size(); i++) {
//                        ls.get(i).setS1(ls_et.get(i).get(0).getText().toString());
//                        ls.get(i).setS2(ls_et.get(i).get(1).getText().toString());
//                        ls.get(i).setS3(ls_et.get(i).get(2).getText().toString());
//                        ls.get(i).setS4(ls_et.get(i).get(3).getText().toString());
//                    }
                    ls.add(new TempModel("0", "0", "0", "0", "0", "0"));
                    adapter_temp.clear();
                    adapter_temp.addAll(ls);
                }
                break;
            case R.id.tv_wait_time:
                showDialogMinute(0);
                break;
            case R.id.tv_ave_time:
                showDialogMinute(1);
                break;
            case R.id.tv_ave_money:
                showDialogMoney(0, 2);
                break;

        }
    }

//    @OnClick({R.id.title_back, R.id.title_right})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.title_back:
//                this.finish();
//                break;
//            case R.id.title_right:
//                startActivity(new Intent(this, EditTempActivity.class));
//                break;
//        }
//    }
}
