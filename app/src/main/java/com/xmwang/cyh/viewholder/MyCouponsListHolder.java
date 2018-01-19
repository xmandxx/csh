package com.xmwang.cyh.viewholder;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.xmwang.cyh.R;
import com.xmwang.cyh.application.GlideApp;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.model.MyCouponsModel;
import com.xmwang.cyh.model.NewGoodsList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * @Description: 找供应
 * @suthor: CTS
 * @Date: 2017/6/14 15:16
 */

public class MyCouponsListHolder extends BaseViewHolder<MyCouponsModel.DataBean> {
    ImageView img;
    TextView txtTitle;
    TextView txtTime;
    TextView txtMoney;
    TextView txtMin;
    LinearLayout llRight;
//    FancyButton btn;
    public MyCouponsListHolder(ViewGroup parent) {
        super(parent, R.layout.item_my_coupons);
        img = $(R.id.img);
        txtTitle = $(R.id.txt_title);
        txtTime = $(R.id.txt_time);
        txtMoney = $(R.id.txt_money);
        txtMin = $(R.id.txt_min);
        llRight = $(R.id.ll_right);
//        btn = $(R.id.btn_shiyong);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

    }

    @Override
    public void setData(final MyCouponsModel.DataBean data) {

        if (data != null) {
            txtTitle.setText(data.getCoupon_name());
            txtTime.setText(data.getStart_time().replace('-', '.') + "-" + data.getEnd_time().replace('-', '.'));
            txtMin.setText("满" + data.getMin_amount() + "使用");
            txtMoney.setText("￥" + data.getDiscount());
            if (data.getIs_default() == 1) {
                Drawable btnDrawable = getContext().getResources().getDrawable(R.mipmap.lose_bk);
                llRight.setBackgroundDrawable(btnDrawable);
            } else {
                Drawable btnDrawable = getContext().getResources().getDrawable(R.mipmap.orange);
                llRight.setBackgroundDrawable(btnDrawable);
            }
            GlideApp.with(getContext())
                    .load(RetrofitHelper.instance.baseAdminUrl + data.getCoupon_pic())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(img);
        }
    }
}
