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
import com.xmwang.cyh.model.CarType;
import com.xmwang.cyh.model.LoveCarModel;

import org.greenrobot.eventbus.EventBus;


public class CarTypeHolder extends BaseViewHolder<CarType> {
    TextView txtName;
    ImageView img;
    public CarTypeHolder(ViewGroup parent) {
        super(parent, R.layout.item_car_type);
        txtName = $(R.id.txt_name);
        img = $(R.id.img);
    }

    @Override
    public void setData(final CarType data) {
        //暂时这样处理
        if (data != null) {
            txtName.setText(data.getName());
            GlideApp.with(getContext())
                    .load(RetrofitHelper.instance.baseAdminUrl + data.getLogo())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(img);
        }
    }
}
