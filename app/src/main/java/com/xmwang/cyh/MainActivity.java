package com.xmwang.cyh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xmwang.cyh.activity.user.LoginActivity;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.activity.home.IndexActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Data.instance.getIsLogin()){
//            Intent intent = new Intent(MainActivity.this, IndexActivity.class);
            Intent intent = new Intent(MainActivity.this, com.xmwang.cyh.daijia.IndexActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://api.itopv.com/base/api/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        ApiService apiService = retrofit.create(ApiService.class);
//        Call<BestNews> call = apiService.bestNews("1");
//        call.enqueue(new Callback<BestNews>() {
//            @Override
//            public void onResponse(Call<BestNews> call, Response<BestNews> response) {
//                BestNews bestNews = response.body();
//
//            }
//
//            @Override
//            public void onFailure(Call<BestNews> call, Throwable t) {
//
//            }
//        });


    }
}
