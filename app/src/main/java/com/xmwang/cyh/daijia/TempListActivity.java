package com.xmwang.cyh.daijia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xmwang.cyh.BaseActivity;
import com.xmwang.cyh.R;
import com.xmwang.cyh.api.ApiService;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.RetrofitHelper;
import com.xmwang.cyh.model.Charging;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xmWang on 2017/12/25.
 */

public class TempListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.list_view)
    ListView listView;
    private List<Charging.DataBean> dataList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daijia_temp_list);
        ButterKnife.bind(this);
        init();

    }

    private void init(){

        final List<String> data = new ArrayList<String>();

        Call<Charging> call = RetrofitHelper.instance.getApiService().charging(Data.instance.AdminId, Data.instance.getUserId());
        call.enqueue(new Callback<Charging>() {
            @Override
            public void onResponse(Call<Charging> call, Response<Charging> response) {
                Charging charging = response.body();
                dataList = charging.getData();
                for (Charging.DataBean cData: charging.getData()) {
                    data.add(cData.getCharging());
                }
                listView.setAdapter(new ArrayAdapter<String>(TempListActivity.this,android.R.layout.simple_expandable_list_item_1,data));
            }

            @Override
            public void onFailure(Call<Charging> call, Throwable t) {

            }
        });
        listView.setOnItemClickListener(this);


    }

    @OnClick({R.id.title_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (dataList.size() > 0){
            Intent intent = new Intent(this,EditTempActivity.class);
            intent.putExtra("charging_id",dataList.get(i).getCharging_id());
            startActivity(intent);
        }
    }
}
