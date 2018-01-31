package com.xmwang.cyh.daijia;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.xmwang.cyh.R;
import com.xmwang.cyh.api.ApiService;
import com.xmwang.cyh.api.ApiUserService;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.common.SADialog;
import com.xmwang.cyh.common.retrofit.BaseResponse;
import com.xmwang.cyh.common.retrofit.RetrofitUtil;
import com.xmwang.cyh.common.retrofit.SubscriberOnNextListener;
import com.xmwang.cyh.model.BaseModel;
import com.xmwang.cyh.model.DriveOrderInfo;
import com.xmwang.cyh.utils.ToastUtils;
import com.xmwang.cyh.viewholder.DJOrderHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;

public class OrderActivity extends AppCompatActivity {

    @BindView(R.id.erv_list)
    EasyRecyclerView ervList;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    private RecyclerArrayAdapter<DriveOrderInfo> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daijia_order);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SADialog.instance.hideProgress();
    }

    @OnClick({R.id.title_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
        }
    }


    private void setCarStatus(int id, int type) {
        Observable observable = RetrofitUtil.getInstance().getmRetrofit().create(ApiUserService.class).user_cars_status(
                Data.instance.AdminId,
                Data.instance.getUserId(),
                id,
                type
        );
        RetrofitUtil.getInstance()
                .setObservable(observable)
                .base(new SubscriberOnNextListener<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        if (baseResponse.isSuccess()) {
                            getList();
                        }

                    }
                });
    }

    private void init() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getList();
            }
        });

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

        ervList.setAdapterWithProgress(adapter = new RecyclerArrayAdapter<DriveOrderInfo>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new DJOrderHolder(parent);
            }
        });
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                String oid = String.valueOf(adapter.getAllData().get(position).getOrder_id());
                Intent intent = new Intent();
                intent.setClass(OrderActivity.this, OrderDetailActivity.class);
                intent.putExtra("driverOrderId", oid);
                startActivity(intent);
            }
        });
        getList();
    }

    private void getList() {
        Observable observable = RetrofitUtil.getInstance().getmRetrofit().create(ApiService.class).getDriveOrderList(
                Data.instance.AdminId,
                Data.instance.getUserId()
        );
        RetrofitUtil.getInstance()
                .setObservable(observable)
                .base(new SubscriberOnNextListener<BaseResponse<DriveOrderInfo>>() {
                    @Override
                    public void onNext(BaseResponse<DriveOrderInfo> baseResponse) {
                        if (baseResponse.isSuccess()) {
                            adapter.clear();
                            if (baseResponse.getDataList().size() > 0) {
                                adapter.addAll(baseResponse.getDataList());
                            }
                        }

                    }
                });
    }
}
