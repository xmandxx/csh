package com.xmwang.cyh.viewholder;

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
import com.xmwang.cyh.model.QiangCouponsModel;

import butterknife.BindView;
import butterknife.OnClick;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * @Description: 找供应
 * @suthor: CTS
 * @Date: 2017/6/14 15:16
 */

public class QiangCouponsListHolder extends BaseViewHolder<QiangCouponsModel> {

    ImageView img;
    TextView txtTitle;
    TextView txtTime;
    TextView txtMan;
    TextView txtBai;
    FancyButton btnShiyong;
    LinearLayout llRight;

    //    FancyButton btn;
    public QiangCouponsListHolder(ViewGroup parent) {
        super(parent, R.layout.item_qiang_coupons);
        img = $(R.id.img);
        txtTitle = $(R.id.txt_title);
        txtTime = $(R.id.txt_time);
        txtMan = $(R.id.txt_man);
        txtBai = $(R.id.txt_bai);
        llRight = $(R.id.ll_right);
        btnShiyong = $(R.id.btn_shiyong);
        btnShiyong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public void setData(final QiangCouponsModel data) {

        if (data != null) {
            txtTitle.setText(data.getCoupon_name());
            txtTime.setText(data.getStart_time().replace('-', '.') + "-" + data.getEnd_time().replace('-', '.'));
            txtMan.setText(data.getDiscount()+"满" + data.getMin_amount() + "使用");
            txtBai.setText(data.getBai());

            GlideApp.with(getContext())
                    .load(RetrofitHelper.instance.baseAdminUrl + data.getCoupon_pic())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(img);
        }
    }
}
