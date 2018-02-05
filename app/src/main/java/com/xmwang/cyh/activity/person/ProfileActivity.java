package com.xmwang.cyh.activity.person;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xmwang.cyh.BaseActivity;
import com.xmwang.cyh.R;
import com.xmwang.cyh.api.ApiUserService;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.common.retrofit.BaseResponse;
import com.xmwang.cyh.common.retrofit.RetrofitUtil;
import com.xmwang.cyh.common.retrofit.SubscriberOnNextListener;
import com.xmwang.cyh.model.UserInfo;
import com.xmwang.cyh.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

public class ProfileActivity extends BaseActivity {

    @BindView(R.id.btn_img)
    ImageView btnImg;
    @BindView(R.id.txt_real_name)
    EditText txtRealName;
    @BindView(R.id.txt_cy_name)
    EditText txtCyName;
    @BindView(R.id.txt_gender)
    TextView txtGender;
    @BindView(R.id.ll_gender)
    LinearLayout llGender;
    private String gender = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_profile);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
//        RetrofitUtil.getInstance()
//                .setObservable(RetrofitUtil.getInstance().getmRetrofit().create(ApiUserService.class).getuserinfo(Data.instance.AdminId, Data.instance.getUserId()))
//                .base(new SubscriberOnNextListener<BaseResponse<UserInfo>>() {
//                    @Override
//                    public void onNext(BaseResponse<UserInfo> baseResponse) {
//                        UserInfo userInfo = baseResponse.getDataInfo();
//
//                    }
//                });
        Data.instance.reUserInfo(new Data.IUserInfo() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                if (userInfo != null) {
                    txtRealName.setText(userInfo.getReal_name());
                    txtCyName.setText(userInfo.getUser_name());
                    txtGender.setText(userInfo.getSex() == 1 ? "男" : "女");
                    gender = String.valueOf(userInfo.getSex());
                }
            }
        });
    }

    @OnClick({R.id.title_back, R.id.btn_img, R.id.ll_profile, R.id.ll_gender, R.id.ll_qcode, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.btn_img:
                break;
            case R.id.ll_profile:
                break;
            case R.id.ll_gender:
                break;
            case R.id.ll_qcode:
                break;
            case R.id.btn_save:
                save();
                break;
        }
    }

    private void genderAction() {
        String[] items = {"男", "女"};
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("选择性别")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 1) {
                            gender = "1";
                            txtGender.setText("男");
                        } else {
                            gender = "0";
                            txtGender.setText("女");
                        }
                    }
                }).create();
        dialog.show();
    }

    private void save() {
        if (TextUtils.isEmpty(txtRealName.getText())) {
            ToastUtils.getInstance().toastShow("请输入真实姓名");
            return;
        }
        if (TextUtils.isEmpty(txtCyName.getText())) {
            ToastUtils.getInstance().toastShow("请输入车友名");
            return;
        }
        Observable observable = RetrofitUtil.getInstance().getmRetrofit().create(ApiUserService.class).editUser(
                Data.instance.getUserId(),
                "",
                txtRealName.getText().toString(),
                txtCyName.getText().toString(),
                gender
        );
        RetrofitUtil.getInstance()
                .setObservable(observable)
                .base(new SubscriberOnNextListener<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        if (baseResponse.isSuccess()) {
                            ToastUtils.getInstance().toastShow(baseResponse.message);
                            finish();
                        } else {

                        }
                    }
                },this);
    }

}
