package com.xmwang.cyh.activity.person;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.xmwang.cyh.BaseActivity;
import com.xmwang.cyh.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyOrderActivity extends BaseActivity {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    public List<Fragment> ls_fragment = new ArrayList();
    private MyOrderFragment myOrderFragment;
    private MyOrderFragment daifuOrderFragment;//待付款
    private MyOrderFragment daishiOrderFragment;//待使用
    private MyOrderFragment tuiOrderFragment;
//    private Map<Integer, ViewHolder> map_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_my_order);
        ButterKnife.bind(this);
        init();
    }

    @OnClick(R.id.title_back)
    public void onViewClicked() {
        finish();
    }

    private void init(){
        tabLayout.addTab(tabLayout.newTab().setText("全部"));
        tabLayout.addTab(tabLayout.newTab().setText("待付款"));
        tabLayout.addTab(tabLayout.newTab().setText("待使用"));
        tabLayout.addTab(tabLayout.newTab().setText("退款"));

        //初始化fragment
        myOrderFragment = new MyOrderFragment(this,1);
        daifuOrderFragment = new MyOrderFragment(this,2);
        daishiOrderFragment = new MyOrderFragment(this,3);
        tuiOrderFragment = new MyOrderFragment(this,4);

        ls_fragment.add(myOrderFragment);
        ls_fragment.add(daifuOrderFragment);
        ls_fragment.add(daishiOrderFragment);
        ls_fragment.add(tuiOrderFragment);

        viewPager.setAdapter(new MyFrageStatePagerAdapter(
                getFragmentManager()));
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(5);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    /**
     * viewpager适配器
     */
    class MyFrageStatePagerAdapter extends FragmentPagerAdapter {
        public MyFrageStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ls_fragment.get(position);
        }

        @Override
        public int getCount() {
            return ls_fragment.size();
        }
    }
}
