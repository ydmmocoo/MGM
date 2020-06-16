package com.fjx.mg.setting.address.edit;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.moments.add.pois.AoisActivity;
import com.fjx.mg.network.KiosqueNetworkActivity;
import com.fjx.mg.utils.SharedPreferencesUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.library.common.base.BaseApp;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.JsonUtil;
import com.library.repository.Constant;
import com.library.repository.models.AddressModel;
import com.library.repository.repository.RepositoryFactory;
import com.tencent.qcloud.uikit.common.utils.ScreenUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class EditAddressActivity extends BaseMvpActivity<EditAddressPresenter> implements EditAddressContract.View {

    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.tvMan)
    TextView tvMan;
    @BindView(R.id.tvWoman)
    TextView tvWoman;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    @BindView(R.id.etDetailAddress)
    EditText etDetailAddress;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;

    private String longitude, latitude;

    private AddressModel.AddressListBean addressModel;

    private String gender = Constant.Gender.man;

    @Override
    protected EditAddressPresenter createPresenter() {
        return new EditAddressPresenter(this);
    }

    public static Intent newInstance(Context context, String address) {
        Intent intent = new Intent(context, EditAddressActivity.class);
        intent.putExtra("address", address);
        return intent;
    }


    @Override
    protected int layoutId() {
        return R.layout.ac_edit_address;
    }

    @Override
    protected void initView() {
        String address = getIntent().getStringExtra("address");
        addressModel = JsonUtil.strToModel(address, AddressModel.AddressListBean.class);
        String title = getString(R.string.add_address);
        if (addressModel != null) {
            title = getString(R.string.modify_address);
            initShow();
        }

        ToolBarManager.with(this).setTitle(title);
        GradientDrawableHelper.whit(tvConfirm).setColor(R.color.colorAccent).setCornerRadius(50);
        initGender(gender);
    }

    private void initShow() {
        etName.setText(addressModel.getName());
        etName.setSelection(etName.getText().length());

        if (TextUtils.equals(addressModel.getSex(), getString(R.string.woman2))) {
            gender = Constant.Gender.woman;
        } else {
            gender = Constant.Gender.man;
        }
        etPhone.setText(addressModel.getPhone());
        etDetailAddress.setText(addressModel.getRoomNo());
        tvLocation.setText(addressModel.getAddress());
        latitude = addressModel.getLatitude();
        longitude = addressModel.getLongitude();
    }

    private void initGender(String gender) {
        if (TextUtils.equals(gender, Constant.Gender.man)) {
            GradientDrawableHelper.whit(tvMan).setColor(R.color.colorAccent).setStroke(0, R.color.text_color_gray).setCornerRadius(0);
            tvMan.setTextColor(ContextCompat.getColor(getCurContext(), R.color.white));
            GradientDrawableHelper.whit(tvWoman).setColor(R.color.trans).setStroke(1, R.color.text_color_gray).setCornerRadius(0);
            tvWoman.setTextColor(ContextCompat.getColor(getCurContext(), R.color.textColorGray));
        } else {
            GradientDrawableHelper.whit(tvWoman).setColor(R.color.colorAccent).setStroke(0, R.color.text_color_gray).setCornerRadius(0);
            tvWoman.setTextColor(ContextCompat.getColor(getCurContext(), R.color.white));
            GradientDrawableHelper.whit(tvMan).setColor(R.color.trans).setStroke(1, R.color.text_color_gray).setCornerRadius(0);
            tvMan.setTextColor(ContextCompat.getColor(getCurContext(), R.color.textColorGray));
        }
    }

    @OnClick({R.id.tvMan, R.id.tvWoman, R.id.tvLocation, R.id.tvConfirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvMan:
                gender = Constant.Gender.man;
                initGender(gender);
                break;
            case R.id.tvWoman:
                gender = Constant.Gender.woman;
                initGender(gender);
                break;
            case R.id.tvLocation:
                location();
                break;
            case R.id.tvConfirm:
                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();
                String address = tvLocation.getText().toString();
                String detailAddress = etDetailAddress.getText().toString();
                if (TextUtils.isEmpty(address)) {
                    CommonToast.toast(getString(R.string.hint_input_address));
                    return;
                }

                if (addressModel == null) {
                    mPresenter.addAddress(name, gender, phone, address, "1", "1", detailAddress);

                } else {
                    mPresenter.modifyAddress(name, gender, phone, address, "1", "1", detailAddress, addressModel.getAddressId());
                }
                break;
        }
    }

    private void location() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(getCurContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            //未安装谷歌服务
            final Dialog dialog = new Dialog(getCurActivity());
            View v = LayoutInflater.from(getCurContext()).inflate(R.layout.dialog_gms_tips, null);
            v.findViewById(R.id.tvOk).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.setContentView(v);
            dialog.show();
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            int w = ScreenUtil.getScreenWidth(getCurActivity());
            params.width = w / 5 * 4;
            dialog.getWindow().setAttributes(params);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        } else {
            XXPermissions.with(getCurActivity())
                    .permission(Permission.Group.LOCATION)
                    .request(new OnPermission() {

                        @Override
                        public void hasPermission(List<String> granted, boolean all) {
                            if (all) {
                                startActivityForResult(AoisActivity.newInstance(getCurContext()), 9);
                            } else {
                            }
                        }

                        @Override
                        public void noPermission(List<String> denied, boolean quick) {
                        }
                    });
        }
    }

    @Override
    public void editSuccess() {
        setResult(1);
        finish();
    }

    @Override
    public void locationResult(String address, String longitude, String latitude) {
        tvLocation.setText(address);
        this.longitude = longitude;
        this.latitude = latitude;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE)
            location();*/

        if (requestCode == 9 && resultCode == 9) {
            String address = data.getStringExtra("adr");
            SharedPreferencesUtils.setString(BaseApp.getInstance(), "address", address);

            String lng = data.getStringExtra("lng");//经度
            String lat = data.getStringExtra("lat");//纬度
            this.longitude = lng;
            this.latitude = lat;
            RepositoryFactory.getLocalRepository().saveLatitude(lat);
            RepositoryFactory.getLocalRepository().saveLongitude(lng);

            tvLocation.setText(address);
        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.stopLocation();
        super.onDestroy();
    }
}
