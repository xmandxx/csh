package com.xmwang.cyh.activity.person;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.xmwang.cyh.R;
import com.xmwang.cyh.api.ApiUserService;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.LazyLoadFragment;
import com.xmwang.cyh.common.retrofit.BaseResponse;
import com.xmwang.cyh.common.retrofit.RetrofitUtil;
import com.xmwang.cyh.common.retrofit.SubscriberOnNextListener;
import com.xmwang.cyh.model.OrderModel;
import com.xmwang.cyh.viewholder.MyorderHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;

/**
 * Created by xmWang on 2018/1/15.
 */

@SuppressLint("ValidFragment")
public class MyOrderFragment extends LazyLoadFragment {
    @BindView(R.id.erv_list)
    EasyRecyclerView ervList;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    Unbinder unbinder;
    private Context context;
    private int orderType = 1;
    private int pageIndex = 1;
    private RecyclerArrayAdapter<OrderModel> adapter;

    public MyOrderFragment(Context context, int orderType) {
        this.context = context;
        this.orderType = orderType;
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_my_coupons;
    }

    @Override
    protected void initFragmentData() {
        init();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void init() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getList();
            }
        });
        /**
         * 最新优惠
         */
        ervList.setLayoutManager(new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }

        });
        DividerDecoration itemDecoration = new DividerDecoration(this.getResources().getColor(R.color.backgroundColor), 1, 0, 0);
        itemDecoration.setDrawLastItem(false);
        ervList.addItemDecoration(itemDecoration);

        ervList.setAdapterWithProgress(adapter = new RecyclerArrayAdapter<OrderModel>(context) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new MyorderHolder(parent);
            }
        });

        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
//                Intent intent = new Intent(context, FindSupplyDetailActivity.class);
//                intent.putExtra("id", ls_supply.get(position).getSupply_id());
//                startActivity(intent);
            }
        });

        getList();
    }

    private void getList() {
        Observable observable = RetrofitUtil.getInstance().getmRetrofit().create(ApiUserService.class).myOrder(
                Data.instance.AdminId,
                Data.instance.getUserId(),
                1,
                6,
                pageIndex
        );
        RetrofitUtil.getInstance()
                .setObservable(observable)
                .base(new SubscriberOnNextListener<BaseResponse<OrderModel>>() {
                    @Override
                    public void onNext(BaseResponse<OrderModel> baseResponse) {
                        swipeContainer.setRefreshing(false);
                        if (pageIndex == 1) {
                            adapter.clear();
                        }
                        if (baseResponse.getDataList() != null) {
                            adapter.addAll(baseResponse.getDataList());
                        }

                    }
                },context);
    }
}
