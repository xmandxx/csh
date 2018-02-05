package com.xmwang.cyh.activity.person;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.xmwang.cyh.BaseActivity;
import com.xmwang.cyh.R;
import com.xmwang.cyh.application.GlideApp;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.rxgalleryfinal.RxGalleryFinalApi;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
import cn.finalteam.rxgalleryfinal.ui.activity.MediaActivity;
import cn.finalteam.rxgalleryfinal.utils.Logger;
import cn.finalteam.rxgalleryfinal.utils.MediaScanner;
import cn.finalteam.rxgalleryfinal.utils.PermissionCheckUtils;

public class DriverAuthActivity extends BaseActivity {

    @BindView(R.id.txt_real_name)
    EditText txtRealName;
    @BindView(R.id.txt_id_number)
    EditText txtIdNumber;
    @BindView(R.id.img_id_front)
    ImageView imgIdFront;
    @BindView(R.id.img_id_back)
    ImageView imgIdBack;
    @BindView(R.id.img_driving)
    ImageView imgDriving;
    private ActionSheetDialog dialog;
    final String[] stringItems = {"拍照", "从相册中选择"};
    int chooseImageType = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_driver_auth);
        ButterKnife.bind(this);
        dialog = new ActionSheetDialog(this, stringItems, null);
    }

    @OnClick({R.id.title_back, R.id.img_id_front, R.id.img_id_back, R.id.img_driving, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.img_id_front:
                chooseImage(1);
                break;
            case R.id.img_id_back:
                chooseImage(2);
                break;
            case R.id.img_driving:
                chooseImage(3);
                break;
            case R.id.btn_submit:
                submit();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        SimpleRxGalleryFinal.get().onActivityResult(requestCode, resultCode, data);
        if (requestCode == RxGalleryFinalApi.TAKE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Logger.i("拍照OK，图片路径:" + RxGalleryFinalApi.fileImagePath.getPath());
            //刷新相册数据库
            RxGalleryFinalApi.openZKCameraForResult(this, new MediaScanner.ScanCallback() {
                @Override
                public void onScanCompleted(String[] strings) {
                    Logger.i(String.format("拍照成功,图片存储路径:%s", strings[0]));
                    Logger.d("演示拍照后进行图片裁剪，根据实际开发需求可去掉上面的判断");

                }
            });
        } else {
            Logger.i("失敗");
        }
    }

    private void chooseImage(final int type) {
        chooseImageType = type;
        dialog.layoutAnimation(null);
        dialog.isTitleShow(false).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://拍照
//                        camera(type);
                        break;
                    case 1://从相册中选择
//                        album(type);
                        break;
                }
                dialog.dismiss();
            }
        });
    }

    private void camera() {
        //打开单选图片，默认参数
        if (PermissionCheckUtils.checkCameraPermission(this, "", MediaActivity.REQUEST_CAMERA_ACCESS_PERMISSION)) {
            RxGalleryFinalApi.openZKCamera(this);
        }
    }

    private void album() {
        RxGalleryFinalApi instance = RxGalleryFinalApi.getInstance(this);
        instance.setImageRadioResultEvent(new RxBusResultDisposable<ImageRadioResultEvent>() {
            @Override
            protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                Logger.i("单选图片的回调");
                imageRadioResultEvent.getResult().getOriginalPath();
            }
        }).open();
    }
    private void submitImg(final int i, File file) {
        //构建body
//        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                .addFormDataPart("other_path", "profile");
//
//        builder.addFormDataPart("upload", file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
//        RequestBody requestBody = builder.build();
//
//        Observable ob = Api.getDefault().upload(requestBody);
//        HttpUtil.getInstance().toSubscribe(ob, new ProgressSubscriber<List<UploadModel>>(this) {
//            @Override
//            protected void _onNext(List<UploadModel> ls) {
//                if (ls.size() > 0) {
//                    params_img1 = ls.get(0).getPath();
////                    switch (i) {
////                        case 1:
////                            params_img1 = ls.get(0).getPath();
////                            break;
////                        case 2:
////                            params_img2 = ls.get(0).getPath();
////                            break;
////                        case 3:
////                            params_img3 = ls.get(0).getPath();
////                            break;
////                    }
//                }
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                dismissProgressDialog();
//                Log.e("测试上传", e.getMessage());
//                if (!NetworkUtils.getInstance().isNetworkAvailable(MyApplication.getContext())) {
//                    ToastUtils.getInstance().toastShow("请检查网络");
//                } else if (e instanceof ApiException) {
//                    new SweetAlertDialog(MyInfoActivity.this, SweetAlertDialog.WARNING_TYPE)
//                            .setTitleText("操作失败")
//                            .setContentText(e.getMessage())
//                            .setConfirmText("返回")
//                            .setTiming(2000)
//                            .show();
//                } else {
//                    ToastUtils.getInstance().toastShow("上传图片失败，请重试...");
//                }
//            }
//        }, "upload", ActivityLifeCycleEvent.DESTROY, lifecycleSubject, false, false, false);
    }
    private void submit() {

    }

}
