package com.xmwang.cyh.activity.person;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.xmwang.cyh.BaseActivity;
import com.xmwang.cyh.R;
import com.xmwang.cyh.api.ApiUserService;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.SADialog;
import com.xmwang.cyh.common.event.MyCarEvent;
import com.xmwang.cyh.common.retrofit.BaseResponse;
import com.xmwang.cyh.common.retrofit.RetrofitUtil;
import com.xmwang.cyh.common.retrofit.SubscriberOnNextListener;
import com.xmwang.cyh.model.LoveCarModel;
import com.xmwang.cyh.viewholder.MyCarHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

public class MyCarActivity extends BaseActivity {
    @BindView(R.id.erv_list)
    EasyRecyclerView ervList;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    private RecyclerArrayAdapter<LoveCarModel> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_my_car);
        ButterKnife.bind(this);
        init();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//解除订阅
        SADialog.instance.hideProgress();
    }
    @OnClick({R.id.title_back, R.id.title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.title_right:
                startActivity(new Intent(this,EditCarActivity.class));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(final MyCarEvent event) {
        int type = 1;//1删除2设置默认
        switch (event.getTag()){
            case 1://设为默认
                setCarStatus(event.getId(),2);
                break;
            case 2://编辑
                Intent intent = new Intent(this,EditCarActivity.class);
                intent.putExtra("carId",event.getId());
                startActivity(intent);
                break;
            case 3://删除
                final NormalDialog dialog_call = new NormalDialog(this);
                dialog_call.widthScale(0.8f);
                dialog_call.heightScale(0.3f);
                dialog_call.content("你确定要删除该地址吗？")//
                        .style(NormalDialog.STYLE_TWO)//
                        .titleTextSize(18);

                dialog_call.setOnBtnClickL(new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog_call.dismiss();
                    }
                }, new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog_call.dismiss();
                        setCarStatus(event.getId(),1);
                    }
                });
//            }
                dialog_call.show();
                break;
            case 4://刷新
                getList();
                break;
        }
    }
    private void setCarStatus(int id,int type){
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
    private void init(){
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getList();
            }
        });
        //注册事件
        EventBus.getDefault().register(this);
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

        ervList.setAdapterWithProgress(adapter = new RecyclerArrayAdapter<LoveCarModel>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new MyCarHolder(parent);
            }
        });

        getList();
    }
    private void getList(){
        Observable observable = RetrofitUtil.getInstance().getmRetrofit().create(ApiUserService.class).user_cars_list(
                Data.instance.AdminId,
                Data.instance.getUserId(),
                20,
                1
        );
        RetrofitUtil.getInstance()
                .setObservable(observable)
                .base(new SubscriberOnNextListener<BaseResponse<LoveCarModel>>() {
                    @Override
                    public void onNext(BaseResponse<LoveCarModel> baseResponse) {
                        swipeContainer.setRefreshing(false);
                        adapter.clear();
                        if (baseResponse.getDataList()!=null) {
                            adapter.addAll(baseResponse.getDataList());
                        }

                    }
                },this);
    }
}
