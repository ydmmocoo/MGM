package com.fjx.mg.me.userinfo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.dialog.PickImageDialog;
import com.fjx.mg.me.qr.QrCodeActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.SoftInputUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.data.UserCenter;
import com.library.repository.models.UserInfoModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class UserInfoActivity extends BaseMvpActivity<UserInfoPresenter> implements UserInfoContract.View {

    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.tvNickName)
    EditText tvNickName;
    @BindView(R.id.tvMobile)
    TextView tvMobile;
    @BindView(R.id.tvGender)
    TextView tvGender;
    @BindView(R.id.etArea)
    TextView tvArea;

    @BindView(R.id.etInviteCode)
    EditText etInviteCode;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.rbMan)
    RadioButton rbMan;
    @BindView(R.id.rbWoman)
    RadioButton rbWoman;
    private BasePopupView popupView;

    private UserInfoModel infoModel;

    private String filePath, nickName, originSex = "", sex = "", longitude = "1", latitude = "1", inviteCode;
    private String address;

    @Override
    protected UserInfoPresenter createPresenter() {
        return new UserInfoPresenter(this);
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_user_info;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.personal_message));
        GradientDrawableHelper.whit(tvConfirm).setColor(R.color.colorAccent).setCornerRadius(50);
        mPresenter.getUserInfo();
        showUserInfo(UserCenter.getUserInfo());

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbMan:
                        sex = "1";
                        break;
                    case R.id.rbWoman:
                        sex = "2";
                        break;
                }
            }
        });
        tvNickName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    SoftInputUtil.hideSoftInput(getCurActivity());
                }
                return false;
            }
        });
        //首次进入先显示登录时获取的用户信息
        setUserInfo();
    }

    private void setUserInfo() {
        if (UserCenter.getUserInfo() == null) {
            return;
        }
        if (StringUtil.isEmpty(UserCenter.getUserInfo().getUSex())) {
            return;
        }
        UserInfoModel userInfo = UserCenter.getUserInfo();
        setUserInfos(userInfo);
    }

    private void setUserInfos(UserInfoModel userInfoModel) throws NullPointerException {
        tvGender.setText(UserCenter.getUserInfo().getUSex());
        tvNickName.setText(userInfoModel.getUNick());
        tvNickName.setSelection(tvNickName.getText().length());
        tvMobile.setText(userInfoModel.getPhone());
        tvGender.setText(userInfoModel.getUSex());
        nickName = userInfoModel.getUNick();
        sex = String.valueOf(getSex());
        originSex = sex;
        address = userInfoModel.getAddress();
        tvArea.setText(address);


        if (!TextUtils.isEmpty(userInfoModel.getRecommendInviteCode())) {
            etInviteCode.setText(userInfoModel.getRecommendInviteCode());
            etInviteCode.setEnabled(false);
        }
    }

    @Override
    public void showUserInfo(UserInfoModel userInfoModel) {
        this.infoModel = userInfoModel;
        CommonImageLoader.load(userInfoModel.getUImg()).circle().placeholder(R.drawable.user_default).into(ivAvatar);
        tvNickName.setText(userInfoModel.getUNick());
        tvNickName.setSelection(tvNickName.getText().length());
        tvMobile.setText(userInfoModel.getPhone());
        tvGender.setText(userInfoModel.getUSex());
        nickName = userInfoModel.getUNick();
        sex = String.valueOf(getSex());
        originSex = sex;
        address = userInfoModel.getAddress();
        tvArea.setText(address);


        if (!TextUtils.isEmpty(userInfoModel.getRecommendInviteCode())) {
            etInviteCode.setText(userInfoModel.getRecommendInviteCode());
            etInviteCode.setEnabled(false);
        }
    }

    @Override
    public void upateSuccess(boolean isImage) {
        //finish();
    }

    @Override
    public void selecrtAddress(String countryName, String cityName, String countryId, String cityId) {
        address = countryName + cityName;
        tvArea.setText(address);
    }


    @OnClick({R.id.llUserName, R.id.ivAvatar, R.id.llGender, R.id.llArea, R.id.tv_confirm})
    public void onViewClicked(View view) {
        SoftInputUtil.hideSoftInput(this);
        switch (view.getId()) {
            case R.id.llUserName:

                break;
            case R.id.ivAvatar:

                if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    mPresenter.getDefaultAvatar();
                } else {
                    EasyPermissions.requestPermissions(this, getString(R.string.permission_camata_sd),
                            1, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }


                break;
            case R.id.llGender:
//
//                // 这种弹窗从 1.0.0版本开始实现了优雅的手势交互和智能嵌套滚动
//                new XPopup.Builder(getCurContext())
//                        .asBottomList("请选择性别", new String[]{
//                                        getString(R.string.man),
//                                        getString(R.string.woman),
////                                        getString(R.string.unknow)
//                                },
//                                new OnSelectListener() {
//                                    @Override
//                                    public void onSelect(int position, String text) {
//                                        tvGender.setText(text);
//                                        sex = String.valueOf(++position);
//                                    }
//                                })
//                        .setCheckedPosition(getSex())
//                        .show();

                break;
            case R.id.llArea:
                mPresenter.showAddressDialog();
                break;
            case R.id.tv_confirm:
                nickName = tvNickName.getText().toString();
                inviteCode = etInviteCode.getText().toString();
                if (hasEdit(nickName, sex, address, inviteCode)) {
                    mPresenter.doUpdate(filePath, nickName, sex, address, inviteCode);
                } else {
                    finish();
                }

                break;

        }
    }


    private boolean hasEdit(String nickName, String sex, String address, String inviteCode) {
        String originNuicName = infoModel.getUNick();
        String originS = originSex;
        String originAddress = infoModel.getAddress();
        String originInviteCode = infoModel.getRecommendInviteCode();

        if (!TextUtils.equals(nickName, originNuicName)) {
            return true;
        }
        if (!TextUtils.equals(originS, sex)) {
            return true;
        }
        if (!TextUtils.equals(originAddress, address)) {
            return true;
        }
        if (!TextUtils.equals(originInviteCode, inviteCode)) {
            return true;
        }
        return false;
    }

    public void showPickPhotoDialog(List<String> avatars) {
        PickImageDialog dialog = new PickImageDialog(getCurContext());
        dialog.withAspectRatio(1, 1);
        dialog.setSelectAvatarListener(imageUrl -> {
            CommonImageLoader.load(imageUrl).circle().into(ivAvatar);
            mPresenter.updateProfile(imageUrl, nickName, sex, address, inviteCode);
        });
        dialog.setDefaultAvatars(avatars);
        popupView = new XPopup.Builder(getCurContext())
                .asCustom(dialog)
                .show();
    }

    private int getSex() {
        String gender = tvGender.getText().toString();
        if (TextUtils.equals(gender, "男")) {
            rbMan.setChecked(true);
            return 1;
        } else if (TextUtils.equals(gender, "女")) {
            rbWoman.setChecked(true);
            return 2;
        }
        return 3;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PictureConfig.CHOOSE_REQUEST:
                // 图片、视频、音频选择结果回调
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);

                if (selectList.size() > 0) {
                    String path = selectList.get(0).getCompressPath();
                    filePath = path;
                    CommonImageLoader.load(path).circle().placeholder(R.drawable.user_default).into(ivAvatar);
                    mPresenter.doUpdateImage(filePath);
                }
                break;
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        mPresenter.getDefaultAvatar();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //弹窗拒绝是调用
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms))
            new AppSettingsDialog.Builder(this).build().show();
    }
}
