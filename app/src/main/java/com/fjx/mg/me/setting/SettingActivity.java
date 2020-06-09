package com.fjx.mg.me.setting;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.friend.blackfriend.BlackFriendActivity;
import com.fjx.mg.me.qr.QrCodeActivity;
import com.fjx.mg.me.safe_center.SafeCenterActivity;
import com.fjx.mg.me.userinfo.UserInfoActivity;
import com.fjx.mg.setting.AboutActivity;
import com.fjx.mg.setting.address.list.AddressListActivity;
import com.fjx.mg.setting.certification.CertificationActivity;
import com.fjx.mg.setting.certification.CertificationInfoActivity;
import com.fjx.mg.setting.companycer.CompanyCerActivity;
import com.fjx.mg.setting.companycer.CompanyCerInfoActivity;
import com.fjx.mg.setting.password.login.ModifyLoginPwdActivity;
import com.fjx.mg.setting.password.pay.ModifyPayPwdActivity;
import com.fjx.mg.setting.privacy.PrivacyActivity;
import com.fjx.mg.utils.DialogUtil;
import com.fjx.mg.utils.SharedPreferencesHelper;
import com.library.common.base.BaseMvpActivity;
import com.library.common.download.UpdateAppManager;
import com.library.common.utils.AppUtil;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.SharedPreferencesUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.data.UserCenter;
import com.library.repository.models.UserInfoModel;
import com.library.repository.models.VersionModel;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 设置
 */
public class SettingActivity extends BaseMvpActivity<SettingPresenter> implements SettingContract.View {

    @BindView(R.id.tvExit)
    TextView tvExit;
    @BindView(R.id.tvCertPerson)
    TextView tvCertPerson;
    @BindView(R.id.tvCertCompany)
    TextView tvCertCompany;
    @BindView(R.id.tvPrivacy)
    TextView tvPrivacy;

    private MaterialDialog updateDliaog;
    private int REQUESTCODE_STORAGE = 1;
    private VersionModel versionModel;

    @Override
    protected SettingPresenter createPresenter() {
        return new SettingPresenter(this);
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_setting;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.setting));
        GradientDrawableHelper.whit(tvExit).setColor(R.color.colorAccent).setCornerRadius(50);

        if (!UserCenter.hasLogin()) {
            tvExit.setVisibility(View.GONE);
        } else {
            UserInfoModel infoModel = UserCenter.getUserInfo();
            tvCertPerson.setText(infoModel.isRealName() == 1 ? getString(R.string.Certified) : getString(R.string.unCertified));
            tvCertCompany.setText(infoModel.isCompanyCert() == 1 ? getString(R.string.Certified) : getString(R.string.unCertified));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    @OnClick({R.id.llCertification, R.id.tvAddress, R.id.tvLoginPassword, R.id.tvPayPassword, R.id.tvUpdate,
            R.id.tvAbout, R.id.tvExit, R.id.llCertCompany, R.id.tv_personal, R.id.tvSaveCenter, R.id.tvshielding_friends,
            R.id.tvPrivacy, R.id.tvClearCache})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llCertification:
                if (UserCenter.needLogin()) return;
                if (UserCenter.getUserInfo().isRealName() == 1) {
                    startActivity(CertificationInfoActivity.newInstance(getCurContext()));
                } else {
                    startActivity(CertificationActivity.newInstance(getCurContext()));
                }
                break;

            case R.id.llCertCompany:
                if (UserCenter.needLogin()) return;
                if (UserCenter.getUserInfo().isCompanyCert() == 1) {
                    startActivity(CompanyCerInfoActivity.newInstance(getCurContext()));
                } else {
                    startActivity(CompanyCerActivity.newInstance(getCurContext()));
                }
                break;
            case R.id.tvAddress:
                if (UserCenter.needLogin()) return;
                startActivity(AddressListActivity.newInstance(getCurContext()));
                break;
            case R.id.tvLoginPassword:
                if (UserCenter.needLogin()) return;
                startActivity(ModifyLoginPwdActivity.newInstance(getCurContext()));
                break;
            case R.id.tvPayPassword:
                if (UserCenter.needLogin()) return;
                startActivity(ModifyPayPwdActivity.newInstance(getCurContext()));
                break;
            case R.id.tvUpdate:
                mPresenter.checkVersion();
                break;
            case R.id.tvAbout:
                startActivity(AboutActivity.newInstance(getCurContext()));
                break;
            case R.id.tv_personal:
                if (UserCenter.needLogin()) return;
                startActivity(UserInfoActivity.newInstance(getCurContext()));
                break;
            case R.id.tvSaveCenter:
                if (UserCenter.needLogin()) return;
                startActivity(SafeCenterActivity.newInstance(getCurContext()));
//                startActivity(IdentityMobileAuthActivity.newInstance(getCurContext(),false));
                break;

            case R.id.tvExit://退出
                mPresenter.logout();
                break;
            case R.id.tvshielding_friends:
                startActivity(BlackFriendActivity.newInstance(getCurContext(), false));
                break;
            case R.id.tvPrivacy:
                startActivity(PrivacyActivity.newInstance(getCurContext()));
                break;
            case R.id.tvClearCache://清楚缓存
                new DialogUtil().showAlertDialog(this, R.string.tips, R.string.clear_cache, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new SharedPreferencesHelper(getCurContext()).remove("QRCodeCollection" + UserCenter.getUserInfo().getIdentifier());
                        dialog.dismiss();
                    }
                });
                break;
        }
    }

    @Override
    public void showUpdateDialog(final VersionModel model) {
        versionModel = model;
        updateDliaog = new MaterialDialog.Builder(this)
                .customView(R.layout.dialog_update, true)
                .build();

        updateDliaog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = updateDliaog.getCustomView();
        if (view == null) return;

        TextView tvUpdate = view.findViewById(R.id.tvUpdate);
        TextView textView2 = view.findViewById(R.id.textView2);
        TextView tvVersion = view.findViewById(R.id.tvVersion);
        if (StringUtil.isNotEmpty(model.getContent())) {
            textView2.setText(model.getContent());
        } else {
            textView2.setText(getString(R.string.new_version));
        }
        if (StringUtil.isNotEmpty(model.getVersionName())) {
            tvVersion.setText("v".concat(model.getVersionName()));
        } else {
            tvVersion.setText("v".concat(AppUtil.getVersionName()));
        }
        view.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDliaog.dismiss();
            }
        });
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDliaog.dismiss();
                requestStoragePermission(model);
//                UpdateAppManager.downloadApk(getCurContext(), model.getDownUrl(), getString(R.string.version_update), getString(R.string.app_name));
            }
        });
        GradientDrawableHelper.whit(tvUpdate).setColor(R.color.colorAccent).setCornerRadius(50);
        updateDliaog.show();
    }

    /**
     * 下载前请求内存卡权限
     *
     * @param data
     */
    private void requestStoragePermission(final VersionModel data) {
        if (!EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_storage), REQUESTCODE_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            UpdateAppManager.downloadApk(getCurContext(), data.getDownUrl(), getString(R.string.version_update), getString(R.string.app_name));
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        UpdateAppManager.downloadApk(getCurContext(), versionModel.getDownUrl(), getString(R.string.version_update), getString(R.string.app_name));
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //弹窗拒绝是调用
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms))
            new AppSettingsDialog.Builder(this).build().show();
    }

}
