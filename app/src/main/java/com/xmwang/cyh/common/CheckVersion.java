package com.xmwang.cyh.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.bigkoo.alertview.OnItemClickListener;
import com.xmwang.cyh.MyApplication;
import com.xmwang.cyh.R;
import com.xmwang.cyh.api.ApiBaseService;
import com.xmwang.cyh.common.retrofit.BaseResponse;
import com.xmwang.cyh.common.retrofit.RetrofitUtil;
import com.xmwang.cyh.common.retrofit.SubscriberOnNextListener;
import com.xmwang.cyh.model.VersionModel;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.List;

import rx.Observable;

/**
 * Created by wangxiaoming on 2018/2/3.
 */

public enum  CheckVersion {
    instance;
    private Context context;
    // 下载进度条
    private ProgressBar progressBar;
    // 是否终止下载
    private boolean isInterceptDownload = false;
    //进度条显示数值
    private int progress = 0;
    //检查版本更新
    public void checkVersion(final Context context) {
        CheckVersion.instance.context = context;
        Observable observable = RetrofitUtil.getInstance().getmRetrofit().create(ApiBaseService.class).check_version(Data.instance.AdminId, 2);
        RetrofitUtil.getInstance()
                .setObservable(observable)
                .base(new SubscriberOnNextListener<BaseResponse<VersionModel>>() {
                    @Override
                    public void onNext(BaseResponse<VersionModel> baseResponse) {
                        VersionModel versionModel = baseResponse.getDataInfo();
                        if (versionModel != null) {
                            Data.instance.setVersion(versionModel);
                            if (getVersionCode() < versionModel.getVersion_code()) {
                                if (versionModel.getForce_update() == 1) {
                                    Common.alertForce(context, versionModel.getIntroduce(), new OnItemClickListener() {
                                        @Override
                                        public void onItemClick(Object o, int position) {
                                            checkPermission();
                                        }
                                    });
                                } else {
                                    Common.alert(context, versionModel.getIntroduce(), new OnItemClickListener() {
                                        @Override
                                        public void onItemClick(Object o, int position) {
                                            checkPermission();
                                        }
                                    }, "去更新");
                                }
                            }
                        }
                    }
                });


    }
    private void checkPermission() {
        AndPermission.with(context)
                .requestCode(200)
                .permission(Permission.STORAGE)
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, List<String> grantedPermissions) {
                        // Successfully.
                        if (requestCode == 200) {
                            showDownloadDialog();
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
    }

    /**
     * 弹出下载框
     */
    private void showDownloadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("正在下载");
        builder.setCancelable(false);
        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.activity_update_prgress, null);
        progressBar = (ProgressBar) v.findViewById(R.id.pb_update_progress);
        builder.setView(v);
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                //终止下载
//                isInterceptDownload = true;
//            }
//        });
        builder.create().show();
        //下载apk
        downloadApk();
    }

    /**
     * 下载apk
     */
    private void downloadApk() {
        //开启另一线程下载
        Thread downLoadThread = new Thread(downApkRunnable);
        downLoadThread.start();
    }

    /**
     * 声明一个handler来跟进进度条
     */
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x111:
                    // 更新进度情况
                    progressBar.setProgress(progress);
//                    Log.e("xmwang", "download:" + progress);
                    break;
                case 0x222:
                    progressBar.setVisibility(View.INVISIBLE);
                    // 安装apk文件
                    installApk(context);
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * 从服务器下载新版apk的线程
     */
    private Runnable downApkRunnable = new Runnable() {
        @Override
        public void run() {
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                Log.e("xmwang", "没有内存卡");
            } else {
                try {
                    //服务器上新版apk地址
                    java.net.URL url = new java.net.URL(Data.instance.getVersion().getUrl());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    int length = conn.getContentLength();
                    InputStream is = conn.getInputStream();
//                    File file = new File(Common.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "csh");
                    File file = new File(new File(Environment.DIRECTORY_DOWNLOADS) + "/updateApkFile/");
                    if (!file.exists()) {
                        //如果文件夹不存在,则创建
                        file.mkdir();
                    }
                    //下载服务器中新版本软件（写文件）
                    String apkFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/updateApkFile/" +
                            getVerName();
                    File ApkFile = new File(apkFile);
                    FileOutputStream fos = new FileOutputStream(ApkFile);
                    int count = 0;
                    byte buf[] = new byte[1024];
                    do {
                        int numRead = is.read(buf);
                        count += numRead;
                        //更新进度条
                        progress = (int) (((float) count / length) * 100);
//                        handler.sendEmptyMessage(1);
                        android.os.Message message_ = new android.os.Message();
                        message_.what = 0x111;
                        handler.sendMessage(message_);
                        if (numRead <= 0) {
                            //下载完成通知安装
//                            handler.sendEmptyMessage(0);
                            android.os.Message message = new android.os.Message();
                            message.what = 0x222;
                            handler.sendMessage(message);
                            break;
                        }
                        fos.write(buf, 0, numRead);
                        //当点击取消时，则停止下载
                    } while (!isInterceptDownload);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * 安装apk
     */
    private static void installApk(final Context context) {
        // 获取当前sdcard存储路径
        File apkFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/updateApkFile/" +
                getVerName());
        if (!apkFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uri;
        //版本7.0无法使用uri访问
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, "com.xmwang.cyh", apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(new File(apkFile.getAbsolutePath()));
        }
        // 安装，如果签名不一致，可能出现程序未安装提示
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 获取当前本地apk的版本
     *
     * @return
     */
    public static int getVersionCode() {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = MyApplication.getContext().getPackageManager().
                    getPackageInfo(MyApplication.getContext().getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取版本号名称
     *
     * @return
     */
    public static String getVerName() {
        String verName = "";
        try {
            verName = MyApplication.getContext().getPackageManager().
                    getPackageInfo(MyApplication.getContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

}
