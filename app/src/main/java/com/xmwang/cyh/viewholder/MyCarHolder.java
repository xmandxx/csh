package com.xmwang.cyh.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.xmwang.cyh.R;
import com.xmwang.cyh.application.GlideApp;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.common.event.MyCarEvent;
import com.xmwang.cyh.model.LoveCarModel;
import com.xmwang.cyh.model.OrderModel;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;


public class MyCarHolder extends BaseViewHolder<LoveCarModel.DataBean> {
    TextView txtCarName;
    LinearLayout llEdit;
    LinearLayout llDel;
    TextView txtPlateNumber;
    TextView txtCarJia;
    TextView txtTime;
    TextView txtKm;
    CheckBox cbkDefault;
    public MyCarHolder(ViewGroup parent) {
        super(parent, R.layout.item_my_car);
        txtCarName = $(R.id.txt_car_name);
        txtPlateNumber = $(R.id.txt_plate_number);
        txtCarJia = $(R.id.txt_car_jia);
        txtTime = $(R.id.txt_time);
        txtKm = $(R.id.txt_km);
        cbkDefault = $(R.id.cbk_default);
        llEdit = $(R.id.ll_edit);
        llDel = $(R.id.ll_del);
    }

    @Override
    public void setData(final LoveCarModel.DataBean data) {
        //暂时这样处理
        if (data != null) {
            txtCarName.setText(data.getCar_name());
            txtPlateNumber.setText(data.getCar_card());
            txtCarJia.setText(data.getCar_number());
            txtTime.setText(String.valueOf(data.getCar_year()));
            txtKm.setText(String.valueOf(data.getCar_kilometre()));
            cbkDefault.setChecked(data.getIs_default() == 1);
            if (data.getIs_default() == 1){
                cbkDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        cbkDefault.setChecked(true);
                    }
                });
            }else{
                cbkDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        EventBus.getDefault().post(new MyCarEvent(data.getCar_id(),1));
                    }
                });
            }

            llEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new MyCarEvent(data.getCar_id(),2));
                }
            });
            llDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new MyCarEvent(data.getCar_id(),3));
                }
            });

        }
    }
}
