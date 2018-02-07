package com.xmwang.cyh.common.retrofit;

import com.xmwang.cyh.MyApplication;
import com.xmwang.cyh.utils.ToastUtils;
import android.content.Context;
import android.text.TextUtils;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import rx.Subscriber;
/**
 * @author nanchen
 * @fileName RetrofitRxDemoo
 * @packageName com.nanchen.retrofitrxdemoo
 * @date 2016/12/12  14:48
 */

public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private SubscriberOnNextListener<T> mListener;
    private Context mContext;
    private ProgressDialogHandler mHandler;

    public ProgressSubscriber(SubscriberOnNextListener<T> listener, Context context) {
        this.mListener = listener;
        this.mContext = context;
        mHandler = new ProgressDialogHandler(context, this, true);
    }
    public ProgressSubscriber(SubscriberOnNextListener<T> listener) {
        this.mListener = listener;
        mHandler = new ProgressDialogHandler(this, true);
    }

    private void showProgressDialog() {
        if (mHandler != null && mContext != null) {
            mHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mHandler != null && mContext != null) {
            mHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mHandler = null;
        }
    }


    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        super.onStart();
        if (mContext != null) {
            showProgressDialog();
        }
    }

    @Override
    public void onCompleted() {
        if (mContext != null) {
            dismissProgressDialog();
        }
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
            BaseResponse baseResponse = (BaseResponse)t;
            if (baseResponse.isSuccess()){
                mListener.onNext(t);
            }else{
                if (!TextUtils.isEmpty(baseResponse.message)){
                    ToastUtils.getInstance().toastShow(baseResponse.message);
                }
            }
        }
    }

    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}