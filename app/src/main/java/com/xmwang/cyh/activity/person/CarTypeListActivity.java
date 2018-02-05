package com.xmwang.cyh.activity.person;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.xmwang.cyh.R;
import com.xmwang.cyh.api.ApiUserService;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.retrofit.BaseResponse;
import com.xmwang.cyh.common.retrofit.RetrofitUtil;
import com.xmwang.cyh.common.retrofit.SubscriberOnNextListener;
import com.xmwang.cyh.model.CarType;
import com.xmwang.cyh.viewholder.CarTypeHolder;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

public class CarTypeListActivity extends AppCompatActivity {

    @BindView(R.id.erv_list)
    EasyRecyclerView ervList;
    private RecyclerArrayAdapter<CarType> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_car_type_list);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        //列表处理
        ervList.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }

        });
        DividerDecoration itemDecoration = new DividerDecoration(this.getResources().getColor(R.color.backgroundColor), 1, 0, 0);
        itemDecoration.setDrawLastItem(false);
        ervList.addItemDecoration(itemDecoration);

        ervList.setAdapterWithProgress(adapter = new RecyclerArrayAdapter<CarType>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new CarTypeHolder(parent);
            }
        });
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                CarType carType = adapter.getItem(position);
                EventBus.getDefault().post(carType);
                finish();
            }
        });
        getList();
    }

    private void getList() {
        Observable observable = RetrofitUtil.getInstance().getmRetrofit().create(ApiUserService.class).car_type_list(
                Data.instance.AdminId
        );
        RetrofitUtil.getInstance()
                .setObservable(observable)
                .base(new SubscriberOnNextListener<BaseResponse<CarType>>() {
                    @Override
                    public void onNext(BaseResponse<CarType> baseResponse) {
                        adapter.clear();
                        if (baseResponse.getDataList() != null) {
                            adapter.addAll(baseResponse.getDataList());
                        }

                    }
                }, this);
    }
}
