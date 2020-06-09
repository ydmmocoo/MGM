package com.library.common.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.library.common.R;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment implements BaseView {

    private boolean isVisible = false;//当前Fragment是否可见
    private boolean isFirstLoad = true;//是否是第一次加载数据
    protected View mView;
    private Unbinder mUnbinder;
    private MaterialDialog mDialog;
    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(layoutId(), container, false);
        mUnbinder = ButterKnife.bind(this, mView);
        initView(savedInstanceState);
        return mView;
    }

    public void initView(Bundle savedInstanceState) {

    }

    @Nullable
    @Override
    public Context getCurContext() {
        return getActivity();
    }

    @Override
    public Activity getCurActivity() {
        return getActivity();
    }

    protected abstract int layoutId();

    @Override
    public void showLoading() {
        createAndShowDialog();
    }

    @Override
    public void hideLoading() {
        destoryAndDismissDialog();
    }

    public void createAndShowDialog() {
        if (dialog == null) {
            dialog = new Dialog(getCurActivity());
        }
        dialog.setContentView(R.layout.dialog_qr_layout);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(false);
        if (!dialog.isShowing() && dialog != null) {
            dialog.show();
        }
    }

    public void destoryAndDismissDialog() {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }

    @Override
    public <T> AutoDisposeConverter<T> bindAutoDispose() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
                .from(this, Lifecycle.Event.ON_DESTROY));
    }

    @Override
    public void onDestroy() {
        if (mUnbinder != null) mUnbinder.unbind();
        super.onDestroy();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        isVisible = isVisibleToUser;
        doLoadFirst();
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void doLoadFirst() {
        if (isVisible && isFirstLoad && mView != null) {
            lazyLoadData();
            isFirstLoad = false;
        }

        if (isVisible && mView != null) {
            doLoadVisible();
        }


    }

    /**
     * 这个方法和lazyLoadDa不能同时重写
     */
    public void doLoadVisible() {

    }


    @Override
    public void onResume() {
        super.onResume();
        doLoadFirst();
    }

    protected void lazyLoadData() {

    }

    public void startForResult(Intent intent, int requestCode) {
        this.startActivityForResult(intent, requestCode);
    }
}
