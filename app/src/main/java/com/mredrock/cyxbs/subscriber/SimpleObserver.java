package com.mredrock.cyxbs.subscriber;

import android.content.Context;
import android.support.annotation.CallSuper;

import com.mredrock.cyxbs.component.task.progress.ProgressCancelListener;
import com.mredrock.cyxbs.component.task.progress.ProgressDialogHandler;
import com.mredrock.cyxbs.network.ErrorHandler;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by cc on 16/3/19.
 */
public class SimpleObserver<T>extends DisposableObserver<T> implements ProgressCancelListener  {
    private Context context;
    protected SubscriberListener<T> listener;
    private ProgressDialogHandler mProgressDialogHandler;
    private ErrorHandler mErrorHandler;

    public SimpleObserver(Context context, SubscriberListener<T> listener) {
        this(context, false, listener);
    }

    public SimpleObserver(Context context, boolean shouldShowProgressDialog, SubscriberListener<T> listener) {
        this(context, shouldShowProgressDialog, false, listener);
    }

    public SimpleObserver(Context context, boolean shouldShowProgressDialog, boolean isProgressDialogCancelable, SubscriberListener<T> listener) {
        this.context = context;
        this.listener = listener;
        mErrorHandler = new ErrorHandler(context);
        if (shouldShowProgressDialog) {
            mProgressDialogHandler = new ProgressDialogHandler(context, this, isProgressDialogCancelable);
        }
    }

    @CallSuper
    @Override
    public void onError(Throwable e) {
        mErrorHandler.handle(e, listener);
        dismissProgressDialog();
    }

    @CallSuper
    @Override
    public void onComplete() {
        dismissProgressDialog();
        if (listener != null) {
            listener.onComplete();
        }
    }

    @CallSuper
    @Override
    protected void onStart() {
        showProgressDialog();
        if (listener != null) {
            listener.onStart();
        }
    }

    @CallSuper
    @Override
    public void onNext(T t) {
        if (listener != null) {
            listener.onNext(t);
        }
    }

    @Override
    public void onCancelProgress() {
        if (isDisposed()) {
            dispose();
        }
    }

    protected Context getContext() {
        return this.context;
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG)
                    .sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG)
                    .sendToTarget();
            mProgressDialogHandler = null;
        }
    }

}
