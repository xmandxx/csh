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
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.common.SADialog;
import com.xmwang.cyh.model.BaseModel;
import com.xmwang.cyh.model.DriveOrderInfo;
import com.xmwang.cyh.utils.ToastUtils;
import com.xmwang.cyh.viewholder.DJOrderHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {

    @BindView(R.id.erv_list)
    EasyRecyclerView ervList;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    private RecyclerArrayAdapter<DriveOrderInfo.DataBean> adapter;
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


    private void setCarStatus(int id,int type){
        retrofit2.Call<BaseModel> call = RetrofitHelper.instance.getApiUserService().user_cars_status(
                Data.instance.AdminId,
                Data.instance.getUserId(),
                id,
                type
        );
        SADialog.instance.showProgress(this);
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(retrofit2.Call<BaseModel> call, Response<BaseModel> response) {
                SADialog.instance.hideProgress();
                BaseModel model = response.body();
                ToastUtils.getInstance().toastShow(model.getMessage());
                if (model.getCode() != RetrofitHelper.instance.SUCCESS_CODE) {
                    return;
                }
                getList();
            }

            @Override
            public void onFailure(retrofit2.Call<BaseModel> call, Throwable t) {
                SADialog.instance.hideProgress();
            }
        });
    }
    private void init(){
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

        ervList.setAdapterWithProgress(adapter = new RecyclerArrayAdapter<DriveOrderInfo.DataBean>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new DJOrderHolder(parent);
            }
        });
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                String oid = String.valueOf(adapter.getAllData().get(position).getOrder_id());
                Intent intent=new Intent();
                intent.setClass(OrderActivity.this,OrderDetailActivity.class);
                intent.putExtra("driverOrderId", oid);
                startActivity(intent);
            }
        });
        getList();
    }
    private void getList(){
        RetrofitHelper.instance.getApiService().getDriveOrderList(
                Data.instance.AdminId,
                Data.instance.getUserId()
        ).enqueue(new Callback<DriveOrderInfo>() {
            @Override
            public void onResponse(retrofit2.Call<DriveOrderInfo> call, Response<DriveOrderInfo> response) {
                swipeContainer.setRefreshing(false);
                adapter.clear();
                DriveOrderInfo model = response.body();
                if (model.getCode() != RetrofitHelper.instance.SUCCESS_CODE) {
                    return;
                }
                if (model.getData().size() > 0) {
                    adapter.addAll(model.getData());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<DriveOrderInfo> call, Throwable t) {
                swipeContainer.setRefreshing(false);
            }
        });
    }
}
