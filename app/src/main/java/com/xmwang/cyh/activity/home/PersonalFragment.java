package com.xmwang.cyh.activity.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xmwang.cyh.R;
import com.xmwang.cyh.activity.personal.DriverAuthActivity;
import com.xmwang.cyh.activity.personal.MyCarActivity;
import com.xmwang.cyh.activity.personal.MyOrderActivity;
import com.xmwang.cyh.activity.personal.SettingActivity;
import com.xmwang.cyh.activity.personal.WalletActivity;
import com.xmwang.cyh.application.GlideApp;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.LazyLoadFragment;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.model.UserInfo;
import com.xmwang.cyh.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xmwang on 2018/1/4.
 */
@SuppressLint("ValidFragment")
public class PersonalFragment extends LazyLoadFragment {


    Unbinder unbinder;
    @BindView(R.id.img_header)
    ImageView imgHeader;
    @BindView(R.id.txt_realname)
    TextView txtRealname;
    @BindView(R.id.txt_qb)
    TextView txtQb;
    @BindView(R.id.txt_jf)
    TextView txtJf;
    @BindView(R.id.txt_sc)
    TextView txtSc;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    private Context context;

    public PersonalFragment(Context context) {
        this.context = context;
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_personal;
    }

    @Override
    protected void initFragmentData() {

    }
    private void loadUI(){
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        UserInfo.DataBean userInfo = Data.instance.getUserInfo();
        if (userInfo != null){
            txtRealname.setText(userInfo.getReal_name());
            txtQb.setText("￥"+userInfo.getUser_money());
            txtJf.setText(String.valueOf(userInfo.getRank_points()));
            txtSc.setText(String.valueOf(userInfo.getCollection()));
            GlideApp.with(context)
                    .load(RetrofitHelper.instance.baseAdminUrl + userInfo.getHeadimg())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .placeholder(R.mipmap.default_header_image)
                    .into(imgHeader);
        }
    }
    private void loadData(){
        retrofit2.Call<UserInfo> call = RetrofitHelper.instance.getApiUserService().getuserinfo(
                Data.instance.AdminId,
                Data.instance.getUserId()
        );

        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(retrofit2.Call<UserInfo> call, Response<UserInfo> response) {
                swipeContainer.setRefreshing(false);
                UserInfo model = response.body();
                if (model.getCode() != RetrofitHelper.instance.SUCCESS_CODE) {
                    return;
                }
                if (model.getData().size() > 0) {
                    Data.instance.setUserInfo(model.getData().get(0));
                }
                loadUI();
            }

            @Override
            public void onFailure(retrofit2.Call<UserInfo> call, Throwable t) {
                swipeContainer.setRefreshing(false);
            }
        });
    }
    @Override
    protected void lazyLoad() {
        loadUI();
        loadData();
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

    @OnClick({R.id.ll_qb, R.id.ll_jf, R.id.ll_sc,R.id.ll_aiche, R.id.ll_renzheng, R.id.ll_kaquan, R.id.ll_order, R.id.ll_daijia, R.id.ll_yangche, R.id.ll_youhao, R.id.ll_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_qb:
                startActivity(new Intent(context, WalletActivity.class));
                break;
            case R.id.ll_jf:
                break;
            case R.id.ll_sc:
                break;
            case R.id.ll_aiche:
                startActivity(new Intent(context, MyCarActivity.class));
                break;
            case R.id.ll_renzheng:
                if (Data.instance.getUserInfo().getDriver_status() == 0){
                    ToastUtils.getInstance().toastShow("您的申请已提交，请等待审核");
                    return;
                }
                if (Data.instance.getUserInfo().getDriver_status() == 1){
                    startActivity(new Intent(context, DriverAuthActivity.class));
                }
                break;
            case R.id.ll_kaquan:
                startActivity(new Intent(context,CouponsActivity.class));
                break;
            case R.id.ll_order:
                startActivity(new Intent(context,MyOrderActivity.class));
                break;
            case R.id.ll_daijia:
                break;
            case R.id.ll_yangche:
                startActivity(new Intent(context, YangcheActivity.class));
                break;
            case R.id.ll_youhao:
                break;
            case R.id.ll_setting:
                startActivity(new Intent(context, SettingActivity.class));
                break;
        }
    }
}
