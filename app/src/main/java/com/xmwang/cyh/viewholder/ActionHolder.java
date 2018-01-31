package com.xmwang.cyh.viewholder;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.xmwang.cyh.MyApplication;
import com.xmwang.cyh.R;
import com.xmwang.cyh.application.GlideApp;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.model.Utilities;

/**
 * @Description: 首页功能图标holder
 * @suthor: CTS
 * @Date: 2017/6/12 11:11
 */

public class ActionHolder extends BaseViewHolder<Utilities> {
    private ImageView img;
    private TextView tv;

    public ActionHolder(View parent) {
        super(parent);

//        //将宽度设置为屏幕的1/4
//        ViewGroup.LayoutParams params = parent.getLayoutParams();
//        params.width = ViewUtils.getInstance().getPoint((Activity) getContext()).x / 4;
//        parent.setLayoutParams(params);
        img = $(R.id.item_action_img);
        tv = $(R.id.item_action_text);
    }

    @Override
    public void setData(final Utilities data) {
        GlideApp.with(getContext())
                .load(RetrofitHelper.instance.baseAdminUrl + data.getAdmin_img_url())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(img);
        tv.setText(data.getName());
    }
}
