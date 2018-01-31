package com.xmwang.cyh.viewholder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.xmwang.cyh.R;
import com.xmwang.cyh.application.GlideApp;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.model.OrderModel;


public class MyorderHolder extends BaseViewHolder<OrderModel> {
    TextView txtTitle;
    TextView txtTitleRight;
    ImageView img;
    TextView txtGoodsName;
    TextView txtGoodsDetail;
    TextView txtUnitPrice;
    TextView txtUnitNumber;
    TextView txtNumber;
    TextView txtPrice;

    public MyorderHolder(ViewGroup parent) {
        super(parent, R.layout.item_my_order);
        txtTitle = $(R.id.txt_title);
        txtTitleRight = $(R.id.txt_title_right);
        img = $(R.id.img);
        txtGoodsName = $(R.id.txt_goods_name);
        txtGoodsDetail = $(R.id.txt_goods_detail);
        txtUnitPrice = $(R.id.txt_unit_price);
        txtUnitNumber = $(R.id.txt_unit_number);
        txtNumber = $(R.id.txt_number);
        txtPrice = $(R.id.txt_price);
    }

    @Override
    public void setData(final OrderModel data) {
        //暂时这样处理
        if (data != null) {
            txtTitle.setText(data.getSupplier_name());
            switch (data.getOrder_type()){
                case 1:
                    txtTitleRight.setText("已使用");
                    break;
                case 2:
                    txtTitleRight.setText("待付款");
                    break;
                case 3:
                    txtTitleRight.setText("待使用");
                    break;
                case 4:
                    txtTitleRight.setText("退款成功");
                    break;
            }
            txtGoodsName.setText(data.getGoods_name());
            txtGoodsDetail.setText("");
            txtUnitPrice.setText(data.getGoods_price());
            txtUnitNumber.setText(String.valueOf(data.getGoods_number()));
            txtNumber.setText(String.valueOf(data.getGoods_number()));
            txtPrice.setText(data.getGoods_amount());
            GlideApp.with(getContext())
                    .load(RetrofitHelper.instance.baseAdminUrl + data.getOriginal_img())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(img);
        }
    }
}
