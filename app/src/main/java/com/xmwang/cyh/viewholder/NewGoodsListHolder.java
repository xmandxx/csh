package com.xmwang.cyh.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.xmwang.cyh.R;
import com.xmwang.cyh.model.NewGoodsList;

/**
 * @Description: 找供应
 * @suthor: CTS
 * @Date: 2017/6/14 15:16
 */

public class NewGoodsListHolder extends BaseViewHolder<NewGoodsList> {
    private TextView tv_pro, tv_gys, tv_place, tv_time, tv_price;

    public NewGoodsListHolder(ViewGroup parent) {
        super(parent, R.layout.item_action);
//        tv_pro = $(R.id.item_pro);
//        tv_gys = $(R.id.item_company);
//        tv_place = $(R.id.item_place);
//        tv_time = $(R.id.item_time);
//        tv_price = $(R.id.item_price);
    }

    @Override
    public void setData(final NewGoodsList data) {
//        tv_pro.setText(data.getCategory_name() + "  " + data.getMass());
//        tv_gys.setText(data.getSupplier());
//        tv_place.setText(data.getCity());
//        tv_price.setText(data.getPrice());
//        if (getContext() instanceof MyReleaseActivity) {
//            if (((MyReleaseActivity) getContext()).getType() != 2) {
//                tv_time.setText(data.getCreate_time());
//            } else {
//                tv_time.setText("浏览" + data.getCount() + "次");
//            }
//        } else {
//            tv_time.setText(data.getCreate_time());
//        }
    }
}
