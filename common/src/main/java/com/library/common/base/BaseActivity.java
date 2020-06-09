package com.library.common.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.library.common.R;
import com.library.common.receiver.ForbiddenReceiver;
import com.library.common.receiver.RankPermissionReceiver;
import com.library.common.utils.MulLanguageUtil;
import com.library.common.utils.SoftInputUtil;
import com.library.common.utils.StatusBarManager;
import com.library.common.utils.StringUtil;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import pub.devrel.easypermissions.EasyPermissions;

public abstract class BaseActivity extends AppCompatActivity implements BaseView,
        EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {

    private Unbinder mUnbinder;

    private MaterialDialog mDialog;
    private Dialog dialog;
    protected boolean mCommonStatusBarEnable = true;
    private RankPermissionReceiver rankPermissionReceiver;
    private ForbiddenReceiver forbiddenReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (layoutId() != 0) {
            setContentView(layoutId());
            if (mCommonStatusBarEnable)
                StatusBarManager.setColor(this, R.color.colorPrimary);
            mUnbinder = ButterKnife.bind(this);
            initView();
        }
    }

    @Override
    public void applyOverrideConfiguration(Configuration overrideConfiguration) {
        overrideConfiguration.setLocale(MulLanguageUtil.getLocalLanguage());
        super.applyOverrideConfiguration(overrideConfiguration);
    }

    @Override
    protected void onResume() {
        registReceiver();
        super.onResume();
    }

    @Override
    protected void onStop() {
        try {
            if (rankPermissionReceiver != null)
                unregisterReceiver(rankPermissionReceiver);

            if (forbiddenReceiver != null)
                unregisterReceiver(forbiddenReceiver);
        } catch (Exception e) {

        }

        super.onStop();
    }

    private void registReceiver() {
        rankPermissionReceiver = new RankPermissionReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RankPermissionReceiver.MG_RANK_ACTION);
        registerReceiver(rankPermissionReceiver, intentFilter);

        forbiddenReceiver = new ForbiddenReceiver();
        IntentFilter forbiddenFilter = new IntentFilter();
        forbiddenFilter.addAction(ForbiddenReceiver.MG_FORBIDDEN_ACTION);
        registerReceiver(forbiddenReceiver, forbiddenFilter);


    }

    @Override
    public Context getCurContext() {
        return this;
    }

    @Override
    public Activity getCurActivity() {
        return this;
    }

    protected abstract int layoutId();


    protected void initView() {
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void showLoading() {
//        if (mDialog == null) {
//            mDialog = new MaterialDialog.Builder(this)
//                    .backgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
//                    .customView(R.layout.dialog_loading, false)
//                    .build();
//            Window window = mDialog.getWindow();
//            if (window == null) return;
//            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            window.setDimAmount(0);
//        }
//
//        if (mDialog.isShowing()) return;
//        mDialog.show();
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

    @Override
    public void hideLoading() {
//        if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
        if (dialog != null && dialog.isShowing() && !isFinishing()) dialog.dismiss();

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
        if (dialog != null && dialog.isShowing() && !isFinishing()) dialog.dismiss();
    }

    @Override
    public <T> AutoDisposeConverter<T> bindAutoDispose() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
                .from(this));
    }

    @Override
    protected void onDestroy() {
        hideLoading();
        if (mUnbinder != null) mUnbinder.unbind();
        super.onDestroy();
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        //(弹出系统框时，用户点击接受)
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //(弹出系统框时，用户点击拒绝按钮)
    }

    @Override
    public void onRationaleAccepted(int requestCode) {
        //(弹出自定义dialog时，用户点击接受按钮)
    }

    @Override
    public void onRationaleDenied(int requestCode) {
        // (弹出自定义dialog时，用户点击拒绝按钮)
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        Context context = languageWork(newBase);
        super.attachBaseContext(context);

    }

    private Context languageWork(Context context) {
        // 8.0及以上使用createConfigurationContext设置configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return updateResources(context);
        } else {
            return context;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Context updateResources(Context context) {
        Resources resources = context.getResources();
        Locale locale = MulLanguageUtil.getLocalLanguage();
        if (locale == null || StringUtil.isEmpty(locale.toString())) {
            Locale loc = getResources().getConfiguration().locale;
            if (loc == null) {
                return context;
            }
            locale = loc;
            MulLanguageUtil.updateLocale(locale);
        }
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
    }

    @Override
    protected void onPause() {
        SoftInputUtil.hideSoftInput(this);
        super.onPause();
    }
}
