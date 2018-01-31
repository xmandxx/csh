package com.xmwang.cyh.viewholder;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.xmwang.cyh.R;
import com.xmwang.cyh.application.GlideApp;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.model.CarFee;
import com.xmwang.cyh.model.MyCouponsModel;

import butterknife.BindView;

/**
 * @Description: 找供应
 * @suthor: CTS
 * @Date: 2017/6/14 15:16
 */

public class CarFeeHolder extends BaseViewHolder<CarFee> {
    TextView txtDate;
    ImageView img;
    TextView txtTitle;
    TextView txtMoney;

    //    FancyButton btn;
    public CarFeeHolder(ViewGroup parent) {
        super(parent, R.layout.item_car_fee);
        img = $(R.id.img);
        txtTitle = $(R.id.txt_title);
        txtDate = $(R.id.txt_date);
        txtMoney = $(R.id.txt_money);
    }

    @Override
    public void setData(final CarFee data) {

        if (data != null) {
            txtTitle.setText(data.getFee_title());
            txtDate.setText(data.getDate());
            txtMoney.setText(data.getFee_money());
            switch (data.getFee_type()) {
                case 1:
                    img.setBackgroundResource(R.mipmap.maintain);
                    break;
                case 2:
                    img.setBackgroundResource(R.mipmap.insurance);
                    break;
                case 3:
                    img.setBackgroundResource(R.mipmap.park);
                    break;
                case 4:
                    img.setBackgroundResource(R.mipmap.car_wash);
                    break;
                case 5:
                    img.setBackgroundResource(R.mipmap.break_rules);
                    break;
                case 6:
                    img.setBackgroundResource(R.mipmap.upkeep);
                    break;
                case 7:
                    img.setBackgroundResource(R.mipmap.road_toll);
                    break;
                case 8:
                    img.setBackgroundResource(R.mipmap.else_s);
                    break;
            }
        }
    }
}
