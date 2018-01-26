package com.xmwang.cyh.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.xmwang.cyh.R;
import com.xmwang.cyh.application.GlideApp;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.model.Utilities;


public class ChooseLocationHolder extends BaseViewHolder<PoiItem> {
    private TextView item_title;
    private TextView item_detail;

    public ChooseLocationHolder(ViewGroup parent) {
        super(parent,R.layout.item_list_view);

        item_title = $(R.id.item_title);
        item_detail = $(R.id.item_detail);
    }

    @Override
    public void setData(final PoiItem data) {
        if (data!=null){
            item_detail.setText(data.getSnippet());
            item_title.setText(data.getTitle());
        }
    }
}
