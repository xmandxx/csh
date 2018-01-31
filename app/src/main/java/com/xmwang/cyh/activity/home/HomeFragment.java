package com.xmwang.cyh.activity.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.acker.simplezxing.activity.CaptureActivity;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.xmwang.cyh.R;
import com.xmwang.cyh.api.ApiHomeService;
import com.xmwang.cyh.application.GlideApp;
import com.xmwang.cyh.common.Common;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.LazyLoadFragment;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.common.retrofit.BaseResponse;
import com.xmwang.cyh.common.retrofit.RetrofitUtil;
import com.xmwang.cyh.common.retrofit.SubscriberOnNextListener;
import com.xmwang.cyh.daijia.*;
import com.xmwang.cyh.model.Banner;
import com.xmwang.cyh.model.NewGoodsList;
import com.xmwang.cyh.model.TopNews;
import com.xmwang.cyh.model.Utilities;
import com.xmwang.cyh.utils.ToastUtils;
import com.xmwang.cyh.viewholder.ActionHolder;
import com.xmwang.cyh.viewholder.NewGoodsListHolder;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by xmwang on 2018/1/4.
 */
@SuppressLint("ValidFragment")
public class HomeFragment extends LazyLoadFragment {
    @BindView(R.id.tv_toutiao)
    TextView tvToutiao;
    @BindView(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    @BindView(R.id.erv_menu)
    EasyRecyclerView ervMenu;
    @BindView(R.id.erv_list)
    EasyRecyclerView ervList;
    @BindView(R.id.home_scroll)
    ScrollView homeScroll;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    Unbinder unbinder;
    private List<Banner> lsBanner = new ArrayList<>();
    private List<Utilities> lsActionSort = new ArrayList<>();
    private List<NewGoodsList> lsNewGoodsList = new ArrayList<>();
    private RecyclerArrayAdapter<Utilities> adapterUtilities;
    private RecyclerArrayAdapter<NewGoodsList> adapterNewGoodsList;
    private Context context;

    public HomeFragment(Context context) {
        this.context = context;
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initFragmentData() {
//        setEventBus(true);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewGoodsList();
            }
        });
        getTopNews();
        /**
         * 功能图标
         */
        ervMenu.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        ervMenu.setAdapter(adapterUtilities = new RecyclerArrayAdapter<Utilities>(context) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                //将宽度设置为屏幕的1/4
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_action, parent, false);
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.width = Common.getWindowWidth(context) / 5;//ViewUtils.getInstance().getPoint((Activity) getContext()).x / 4;
                view.setLayoutParams(params);
                return new ActionHolder(view);
            }
        });

        adapterUtilities.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                switch (lsActionSort.get(position).getId()) {
                    case 1://汽车金融
//                        Intent intent_1 = new Intent(context, FindSupplyActivity.class);
//                        startActivity(intent_1);
                        break;
                    case 2://违章查询
//                        Intent intent_buy = new Intent(context, FindBuyActivity.class);
//                        startActivity(intent_buy);
                        break;
                    case 3://油卡充值
                        startActivity(new Intent(context, FuelcardActivity.class));
                        break;
                    case 4://道路救援
                        startActivity(new Intent(context, RoadescueActivity.class));
                        break;
                    case 6://车辆股价
//                        Intent intent_buy = new Intent(context, FindBuyActivity.class);
//                        startActivity(intent_buy);
                        break;
                    case 7://车险
//                        Intent intent_buy = new Intent(context, FindBuyActivity.class);
//                        startActivity(intent_buy);
                        break;
                    case 8://油耗
//                        Intent intent_buy = new Intent(context, FindBuyActivity.class);
//                        startActivity(intent_buy);
                        break;
                    case 9://拼车
//                        Intent intent_buy = new Intent(context, FindBuyActivity.class);
//                        startActivity(intent_buy);
                        break;
                    case 10://代驾
                        startActivity(new Intent(context, com.xmwang.cyh.daijia.IndexActivity.class));
                        break;
                    case 11://自驾游

                        break;

                }
            }
        });

        //取功能图标数据
        getAction();

        /**
         * Banner图
         */
        convenientBanner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new LocalImageHolderView();
            }
        }, lsBanner) //mList是图片地址的集合
                .setPointViewVisible(true)    //设置指示器是否可见
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.mipmap.default_home_dot, R.mipmap.current_page_home_dot})
                //设置指示器位置（左、中、右）
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .startTurning(4000)     //设置自动切换（同时设置了切换时间间隔）
                .setManualPageable(true)  //设置手动影响（设置了该项无法手动切换）
        ;
        getBanner();

        /**
         * 最新优惠
         */
        ervList.setLayoutManager(new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        DividerDecoration itemDecoration = new DividerDecoration(this.getResources().getColor(R.color.backgroundColor), 1, 0, 0);
        itemDecoration.setDrawLastItem(false);
        ervList.addItemDecoration(itemDecoration);

        ervList.setAdapterWithProgress(adapterNewGoodsList = new RecyclerArrayAdapter<NewGoodsList>(context) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new NewGoodsListHolder(parent);
            }
        });

        adapterNewGoodsList.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
//                Intent intent = new Intent(context, FindSupplyDetailActivity.class);
//                intent.putExtra("id", ls_supply.get(position).getSupply_id());
//                startActivity(intent);
            }
        });

        getNewGoodsList();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CaptureActivity.REQ_CODE:
                switch (resultCode) {
                    case RESULT_OK:

                        //or do sth
                        break;
                    case RESULT_CANCELED:

                        break;
                }
                break;
        }
    }

    @OnClick({R.id.ll_sao, R.id.ll_ka, R.id.ll_yang, R.id.ll_fujin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_sao:
                AndPermission.with(this)
                        .requestCode(200)
                        .permission(Permission.CAMERA)
                        .callback(new PermissionListener() {
                            @Override
                            public void onSucceed(int requestCode, List<String> grantedPermissions) {
                                // Successfully.
                                if (requestCode == 200) {
                                    startActivityForResult(new Intent(context, CaptureActivity.class), CaptureActivity.REQ_CODE);
                                }
                            }

                            @Override
                            public void onFailed(int requestCode, List<String> deniedPermissions) {
                                // Failure.
                                if (requestCode == 200) {
                                    // TODO ...
                                }
                            }
                        })
                        .start();
                break;
            case R.id.ll_ka:
                startActivity(new Intent(context, CouponsActivity.class));
                break;
            case R.id.ll_yang:
                startActivity(new Intent(context, YangcheActivity.class));
                break;
            case R.id.ll_fujin:
                break;
        }
    }

    /**
     * 获取头条数据
     */
    private void getTopNews() {
        Observable observable = RetrofitUtil.getInstance().getmRetrofit().create(ApiHomeService.class).top_news(
                Data.instance.AdminId
        );
        RetrofitUtil.getInstance()
                .setObservable(observable)
                .base(new SubscriberOnNextListener<BaseResponse<TopNews>>() {
                    @Override
                    public void onNext(BaseResponse<TopNews> baseResponse) {
                        if (baseResponse.getDataInfo() != null) {
                            tvToutiao.setText(baseResponse.getDataInfo().getTitle());
                        }

                    }
                });
    }

    /**
     * 取轮播图数据
     */
    private void getBanner() {
        Observable observable = RetrofitUtil.getInstance().getmRetrofit().create(ApiHomeService.class).banner(
                Data.instance.AdminId
        );
        RetrofitUtil.getInstance()
                .setObservable(observable)
                .base(new SubscriberOnNextListener<BaseResponse<Banner>>() {
                    @Override
                    public void onNext(BaseResponse<Banner> baseResponse) {
                        lsBanner.clear();
                        lsBanner.addAll(baseResponse.getDataList());
                        if (lsBanner.size() == 1) {
                            convenientBanner.stopTurning();
                        }
                        convenientBanner.stopTurning();
                        convenientBanner.notifyDataSetChanged();
                        convenientBanner.setcurrentitem(lsBanner.size() * 10);
                    }
                });

    }

    /**
     * 取功能图标
     */
    private void getAction() {
        Observable observable = RetrofitUtil.getInstance().getmRetrofit().create(ApiHomeService.class).utilities(
                Data.instance.AdminId
        );
        RetrofitUtil.getInstance()
                .setObservable(observable)
                .base(new SubscriberOnNextListener<BaseResponse<Utilities>>() {
                    @Override
                    public void onNext(BaseResponse<Utilities> baseResponse) {
                        if (baseResponse.getDataList().size() > 0) {
                            List<Utilities> list = new ArrayList<Utilities>();
                            int[] intArray = {0, 5, 1, 6, 2, 7, 3, 8, 4, 9};
                            for (int i : intArray) {
                                if (baseResponse.getDataList().get(i) != null) {
                                    list.add(baseResponse.getDataList().get(i));
                                }
                            }
                            adapterUtilities.clear();
                            lsActionSort = list;
                            adapterUtilities.addAll(list);
                        }
                    }
                });
    }

    /**
     * 最新最新优惠
     */
    private void getNewGoodsList() {
        Observable observable = RetrofitUtil.getInstance().getmRetrofit().create(ApiHomeService.class).new_goods_list(
                Data.instance.AdminId
        );
        RetrofitUtil.getInstance()
                .setObservable(observable)
                .base(new SubscriberOnNextListener<BaseResponse<NewGoodsList>>() {
                    @Override
                    public void onNext(BaseResponse<NewGoodsList> baseResponse) {
                        adapterNewGoodsList.clear();
                        adapterNewGoodsList.addAll(baseResponse.getDataList());
                    }
                },context);

    }

    class LocalImageHolderView implements Holder<Banner> {
        private ImageView img;

        @Override
        public View createView(Context context) {
            img = new ImageView(context);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return img;
        }

        @Override
        public void UpdateUI(Context context, int position, Banner data) {
            GlideApp.with(context)
                    .load(RetrofitHelper.instance.baseAdminUrl + data.getAd_code())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(img);
        }
    }
//    /**
//     * viewpager适配器
//     */
//    class MyFrageStatePagerAdapter extends FragmentPagerAdapter {
//        public MyFrageStatePagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return ls_fragment_viewpager.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return ls_fragment_viewpager.size();
//        }
//    }
}
