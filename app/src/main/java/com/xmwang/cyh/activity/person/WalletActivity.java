package com.xmwang.cyh.activity.person;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xmwang.cyh.BaseActivity;
import com.xmwang.cyh.R;
import com.xmwang.cyh.common.Data;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WalletActivity extends BaseActivity {

    @BindView(R.id.txt_money)
    TextView txtMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_wallet);
        ButterKnife.bind(this);
        txtMoney.setText(Data.instance.getUserInfo().getUser_money());
    }

    @OnClick({R.id.title_back, R.id.ll_cz, R.id.ll_tx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.ll_cz:
                startActivity(new Intent(this, RechargeActivity.class));
                break;
            case R.id.ll_tx:
                startActivity(new Intent(this, CashActivity.class));
                break;
        }
    }
}
