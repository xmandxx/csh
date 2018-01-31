package com.xmwang.cyh.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.xmwang.cyh.R;
import com.xmwang.cyh.common.event.MyCarEvent;
import com.xmwang.cyh.model.DriveOrderInfo;
import com.xmwang.cyh.model.LoveCarModel;
import com.xmwang.cyh.model.OrderModel;

import org.greenrobot.eventbus.EventBus;


public class DJOrderHolder extends BaseViewHolder<DriveOrderInfo> {
    TextView txtTime;
    TextView txtStart;
    TextView txtEnd;
    TextView txtMoney;
    public DJOrderHolder(ViewGroup parent) {
        super(parent, R.layout.item_daijia_order);
        txtTime = $(R.id.txt_time);
        txtStart = $(R.id.start_location);
        txtEnd = $(R.id.end_location);
        txtMoney = $(R.id.txt_money);
    }

    @Override
    public void setData(final DriveOrderInfo data) {
        //暂时这样处理
        if (data != null) {
            txtTime.setText(data.getAdd_time());
            txtStart.setText(data.getOrigination());
            txtEnd.setText(data.getDestination());
            txtMoney.setText(data.getOrder_amount());
        }
    }
}
