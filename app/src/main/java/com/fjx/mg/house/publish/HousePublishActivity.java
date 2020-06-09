package com.fjx.mg.house.publish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.dialog.PickImageDialog;
import com.fjx.mg.setting.feedback.FeedbackImageAdapter;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.SoftInputUtil;
import com.library.repository.data.UserCenter;
import com.library.repository.models.HouseConfigModel;
import com.library.repository.models.HouseDetailModel;
import com.library.repository.models.UserInfoModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

public class HousePublishActivity extends BaseMvpActivity<HousePublishPresenter> implements HousePublishContract.View {

    //类型,1:求租，2：求购，3：出租，4：出售
    public static final int TYPE_QZ = 1;
    public static final int TYPE_QS = 2;
    public static final int TYPE_CZ = 3;
    public static final int TYPE_CS = 4;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.etTitle)
    EditText etTitle;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.etArea)
    EditText etArea;

    @BindView(R.id.etMoney)
    EditText etMoney;

    @BindView(R.id.tvExpire)
    TextView tvExpire;
    @BindView(R.id.tvHtype)
    TextView tvHtype;
    @BindView(R.id.tvLanguage)
    TextView tvLanguage;
    @BindView(R.id.etContact)
    EditText etContact;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.tvMgCode)
    TextView tvMgCode;
    @BindView(R.id.etDesc)
    EditText etDesc;
    @BindView(R.id.tvCommit)
    TextView tvCommit;
    @BindView(R.id.imageRecycler)
    RecyclerView imageRecycler;
    private FeedbackImageAdapter imageAdapter;
    private List<String> imageUrls = new ArrayList<>();


    private int mType, publishType;
    private String countryId, cityId, layoutId, laguageId, hid, expireId;
    private Map<String, Object> map = new HashMap<>();


    public static Intent newInstance(Context context, int type) {
        Intent intent = new Intent(context, HousePublishActivity.class);
        intent.putExtra("type", type);
        return intent;
    }

    public static Intent newInstance(Context context, int type, String hid) {
        Intent intent = new Intent(context, HousePublishActivity.class);
        intent.putExtra("mType", type);
        intent.putExtra("hid", hid);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_house_public;
    }


    @Override
    protected HousePublishPresenter createPresenter() {
        return new HousePublishPresenter(this);
    }

    @Override
    protected void initView() {
        hid = getIntent().getStringExtra("hid");
        publishType = getIntent().getIntExtra("type", 0);
        mType = getIntent().getIntExtra("mType", 0);
        if (mType == 0) {
            if (publishType == 0) {
                mType = TYPE_QZ;
            } else {
                mType = TYPE_QS;
            }
        }


        GradientDrawableHelper.whit(tvCommit).setColor(R.color.colorAccent).setCornerRadius(50);
        ToolBarManager.with(this).setTitle(getString(R.string.spublish));
        mPresenter.getConfig();
        initShow();

        imageUrls.add("");
        imageAdapter = new FeedbackImageAdapter();
        imageRecycler.setLayoutManager(new GridLayoutManager(getCurContext(), 3));
        imageRecycler.addItemDecoration(new SpacesItemDecoration(10, 10));
        imageRecycler.setAdapter(imageAdapter);
        imageAdapter.setList(imageUrls);
        imageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                String path = imageAdapter.getItem(position);
                if (TextUtils.isEmpty(path)) {
                    PickImageDialog dialog = new PickImageDialog(getCurContext());
                    dialog.setSelectSingle(false);
                    new XPopup.Builder(getCurContext())
                            .asCustom(dialog)
                            .show();
                    SoftInputUtil.hideSoftInput(getCurActivity());
                }
            }
        });
        mPresenter.houseDetail(hid);
        tvMgCode.setText(UserCenter.getUserInfo().getIdentifier());

        setDefaultData();
    }

    private void setDefaultData() {
        if (!TextUtils.isEmpty(hid)) return;
        HouseConfigModel.CountryListBean countryBean = (HouseConfigModel.CountryListBean) mPresenter.getDefaultData("address");
        if (countryBean != null) {
            tvAddress.setText(countryBean.getCountryName().concat(countryBean.getCityList().get(0).getCityName()));
            countryId = countryBean.getCId();
            cityId = countryBean.getCityList().get(0).getCityId();
            map.put("countryId", countryId);
            map.put("cityId", cityId);
        }

        HouseConfigModel.LayoutConfBean layoutBean = (HouseConfigModel.LayoutConfBean) mPresenter.getDefaultData("hType");
        if (layoutBean != null) {
            tvHtype.setText(layoutBean.getName());
            layoutId = layoutBean.getLayoutId();
            map.put("layout", layoutId);
        }

        HouseConfigModel.LaguageConfBean laguageBean = (HouseConfigModel.LaguageConfBean) mPresenter.getDefaultData("language");
        if (laguageBean != null) {
            tvLanguage.setText(laguageBean.getName());
            laguageId = layoutBean.getLayoutId();
            map.put("language", laguageId);
        }

        HouseConfigModel.ExpireTypeBean expireBean = (HouseConfigModel.ExpireTypeBean) mPresenter.getDefaultData("expire");
        if (expireBean != null) {
            tvExpire.setText(expireBean.getExpireType());
            expireId = expireBean.getExpireType();
            map.put("expireType", expireId);
        }

        UserInfoModel infoModel = UserCenter.getUserInfo();
        if (infoModel == null) return;
        etContact.setText(infoModel.getName());
        etPhone.setText(infoModel.getPhone());
    }

    private void initShow() {

        switch (mType) {
            case TYPE_QZ:
                tvType.setText(getString(R.string.rent_seeking));
                imageRecycler.setVisibility(View.GONE);
                break;
            case TYPE_QS:
                tvType.setText(getString(R.string.ask_for_buy));
                imageRecycler.setVisibility(View.GONE);
                break;
            case TYPE_CZ:
                tvType.setText(getString(R.string.lease));
                imageRecycler.setVisibility(View.VISIBLE);
                break;
            case TYPE_CS:
                tvType.setText(getString(R.string.sell));
                imageRecycler.setVisibility(View.VISIBLE);
                break;
        }

        map.put("type", mType);
    }


    @Override
    public void commitSuccess() {
        CommonToast.toast(getString(R.string.spublish_success));
        setResult(11);
        finish();

    }

    @Override
    public void selectType(String s, int i) {
        mType = i;
        initShow();
    }

    @Override
    public void selecrtAddress(String countryName, String cityName, String countryId, String cityId) {
        this.countryId = countryId;
        this.cityId = cityId;
        tvAddress.setText(countryName.concat(cityName));
        map.put("countryId", countryId);
        map.put("cityId", cityId);
    }

    @Override
    public void selectHType(String name, String layoutId) {
        this.layoutId = layoutId;
        tvHtype.setText(name);
        map.put("layout", layoutId);

    }

    @Override
    public void selectLanguage(String name, String laguageId) {
        this.laguageId = laguageId;
        tvLanguage.setText(name);
        map.put("language", laguageId);
    }

    @Override
    public void selectExpire(String name, String id) {
        expireId = id;
        tvExpire.setText(name);
    }

    @Override
    public int getPublishType() {
        return publishType;
    }

    @Override
    public void showHouseDetail(HouseDetailModel houseModel) {
        if (houseModel == null) return;
        etTitle.setText(houseModel.getTitle());
        tvAddress.setText(houseModel.getCountryName().concat(houseModel.getCityName()));
        etArea.setText(houseModel.getArea());
        etMoney.setText(houseModel.getPrice());
        tvHtype.setText(houseModel.getLayout());
        tvLanguage.setText(houseModel.getLanguage());
        etContact.setText(houseModel.getCountryName());
        etPhone.setText(houseModel.getContactPhone());
        tvMgCode.setText(houseModel.getContactWeixin());

        List<String> remoteImages = houseModel.getImages();
        if (remoteImages == null) remoteImages = new ArrayList<>();
        String images = "";
        for (String url : remoteImages) {
            String[] sps = url.split("Uploads");
            if (sps == null) break;
            images = images.concat("Uploads".concat(sps[1])).concat(",");
        }
        map.put("images", images);
        if (remoteImages.size() < 9) remoteImages.add(0, "");
        imageAdapter.setList(remoteImages);

        countryId = mPresenter.getTypeIdByName("countryId", houseModel.getCountryName());
        cityId = mPresenter.getTypeIdByName("cityId", houseModel.getCityName());
        layoutId = mPresenter.getTypeIdByName("layoutId", houseModel.getLayout());
        laguageId = mPresenter.getTypeIdByName("laguageId", houseModel.getLanguage());
        expireId = mPresenter.getTypeIdByName("expireId", houseModel.getLanguage());
        map.put("countryId", countryId);
        map.put("cityId", cityId);
        map.put("layout", layoutId);
        map.put("language", laguageId);
    }


    @OnClick({R.id.llType, R.id.llAddress, R.id.llHtype, R.id.llLanguage, R.id.tvCommit, R.id.llExpire})
    public void onViewClicked(View view) {
        SoftInputUtil.hideSoftInput(this);
        switch (view.getId()) {
            case R.id.llType:
                if (!TextUtils.isEmpty(hid)) return;
                mPresenter.showTypeDialog();
                break;
            case R.id.llAddress:
                mPresenter.showAddressDialog();
                break;
            case R.id.llHtype:
                mPresenter.showHtypeDialog();
                break;
            case R.id.llLanguage:
                mPresenter.showLanguageDialog();
                break;
            case R.id.llExpire:
                mPresenter.showExpireDialog();
                break;
            case R.id.tvCommit:
                String title = etTitle.getText().toString();
                String price = etMoney.getText().toString();
                String phone = etPhone.getText().toString();
                String weixin = tvMgCode.getText().toString();
                String name = etContact.getText().toString();
                String desc = etDesc.getText().toString();
                String area = etArea.getText().toString();

                map.put("title", title);
                map.put("price", price);
                map.put("phone", phone);
                map.put("name", name);
                map.put("desc", desc);
                map.put("weixin", weixin);
                map.put("area", area);
                if (!TextUtils.isEmpty(hid))
                    map.put("hId", hid);
                map.put("expireType", expireId);


                for (String key : map.keySet()) {
                    if (map.get(key) == null) {
                        CommonToast.toast("请完善租售房信息");
                        return;
                    }

                    if (map.get(key) instanceof String) {
                        if (TextUtils.isEmpty((String) map.get(key))) {
                            CommonToast.toast("请完善租售房信息");
                            return;
                        }
                    }
                }

                if (imageUrls.contains("")) imageUrls.remove("");
                if (imageUrls.size() == 0) {
                    if (TextUtils.isEmpty(hid))
                        mPresenter.commit(map);
                    else
                        mPresenter.edit(map);
                } else {
                    mPresenter.updateImage(map, imageUrls);
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PictureConfig.CHOOSE_REQUEST:
                // 图片、视频、音频选择结果回调
                imageUrls.clear();
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                for (LocalMedia localMedia : selectList) {
                    imageUrls.add(localMedia.getCompressPath());
                }

                if (imageUrls.size() < 9) imageUrls.add(0, "");
                imageAdapter.setList(imageUrls);
                Log.d("onActivityResult", JsonUtil.moderToString(selectList));
                break;
        }

    }


    @OnFocusChange({R.id.etTitle, R.id.etArea, R.id.etMoney, R.id.etContact, R.id.etPhone, R.id.etDesc})
    void onEditFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            EditText editText = ((EditText) view);
            String text = editText.getText().toString();
            editText.setSelection(text.length());
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();

        }
    }
}
