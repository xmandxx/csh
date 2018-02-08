package com.xmwang.cyh.common.retrofit;

import com.xmwang.cyh.MyApplication;
import com.xmwang.cyh.utils.ToastUtils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.TextUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;

/**
 * @author nanchen
 * @fileName RetrofitRxDemoo
 * @packageName com.nanchen.retrofitrxdemoo
 * @date 2016/12/12  14:48
 */

public class ProgressSubscriber<T> extends Subscriber<T> {

    private SubscriberOnNextListener<T> mListener;
    private Context mContext;
    SweetAlertDialog pDialog;

    public ProgressSubscriber(SubscriberOnNextListener<T> listener, Context context) {
        this.mListener = listener;
        this.mContext = context;

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
//        pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                onCancelProgress();
//            }
//        });
    }

    public ProgressSubscriber(SubscriberOnNextListener<T> listener) {
        this.mListener = listener;
    }

    private void showProgressDialog() {
        if (pDialog != null && mContext != null) {
            pDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (pDialog != null && mContext != null) {
            pDialog.dismiss();
            pDialog.cancel();
        }
    }


    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        super.onStart();
        showProgressDialog();
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
//        Toast.makeText(MyApplication.getContext(),"获取数据完成！",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            ToastUtils.getInstance().toastShow("网络中断，请检查您的网络状态");
//            Toast.makeText(DemoApplication.getAppContext(), "", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
            ToastUtils.getInstance().toastShow("网络中断，请检查您的网络状态");
//            Toast.makeText(DemoApplication.getAppContext(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
            ToastUtils.getInstance().toastShow("网络错误");
            if (!TextUtils.isEmpty(e.getMessage())) {
//                ToastUtils.getInstance().toastShow(e.getMessage());
            }
//            Toast.makeText(DemoApplication.getAppContext(), "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if (mContext != null) {
            dismissProgressDialog();
        }
    }

    @Override
    public void onNext(T t) {
        if (mListener != null) {
            BaseResponse baseResponse = (BaseResponse) t;
            if (baseResponse.isSuccess()) {
                mListener.onNext(t);
            } else {
                if (!TextUtils.isEmpty(baseResponse.message) && (mContext != null)) {
                    ToastUtils.getInstance().toastShow(baseResponse.message);
                }
            }
        }
    }

    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}