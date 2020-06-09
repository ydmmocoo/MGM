package com.fjx.mg.main.yellowpage.publish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.gson.Gson;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.SoftInputUtil;
import com.library.repository.data.UserCenter;
import com.library.repository.db.model.CompanyTypeModel;
import com.library.repository.models.CmpanydetaisModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YellowPagePublicActivity extends BaseMvpActivity<YellowPagePublicPresenter> implements YellowPagePublicContract.View {

    @BindView(R.id.etTitle)
    EditText etTitle;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.head)
    TextView head;
    @BindView(R.id.etContact)
    EditText etContact;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.etDesc)
    EditText etDesc;
    @BindView(R.id.imageRecycler)
    RecyclerView imageRecycler;
    @BindView(R.id.tvCommit)
    TextView tvCommit;
    @BindView(R.id.tvArea)
    TextView tvArea;
    @BindView(R.id.iv_title_clear)
    ImageView mIvTitleClear;
    @BindView(R.id.iv_contact_clear)
    ImageView mIvContactClear;
    @BindView(R.id.iv_phone_clear)
    ImageView mIvPhoneClear;
    @BindView(R.id.iv_address_clear)
    ImageView mIvAddressClear;
    private List<String> imageUrls = new ArrayList<>();

    private FeedbackImageAdapter imageAdapter;
    private String cid;
    private String remoteImage;
    private String serviceId, secondServiceId, countryId, cityId;
    private String concat = "";
    private boolean isSelectImage=false;

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, YellowPagePublicActivity.class);
        return intent;
    }

    public static Intent newInstance(Context context, String cid) {
        Intent intent = new Intent(context, YellowPagePublicActivity.class);
        intent.putExtra("cid", cid);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_yellow_page_public;
    }

    @Override
    protected YellowPagePublicPresenter createPresenter() {
        return new YellowPagePublicPresenter(this);
    }

    @Override
    protected void initView() {
        cid = getIntent().getStringExtra("cid");
        mPresenter.getCompanyTypesV1();
        GradientDrawableHelper.whit(tvCommit).setColor(R.color.colorAccent).setCornerRadius(50);
        ToolBarManager.with(this).setTitle(getString(R.string.spublish));

        imageUrls.add("");
        imageAdapter = new FeedbackImageAdapter();
        imageRecycler.setLayoutManager(new GridLayoutManager(getCurContext(), 3));
        imageRecycler.addItemDecoration(new SpacesItemDecoration(10, 10));
        imageRecycler.setAdapter(imageAdapter);
        imageAdapter.setList(imageUrls);
        imageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {

                String path ="";
                if (position!=0){
                    path=imageUrls.size()<9?imageUrls.get(position-1):imageUrls.get(position);
                }
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

        mPresenter.getCompanyDetail(cid);

        etPhone.setText(UserCenter.getUserInfo().getPhone());
        etContact.setText(UserCenter.getUserInfo().getUNick());
        etContact.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus&&etContact.getText().toString().length()>0){
                    mIvContactClear.setVisibility(View.VISIBLE);
                }
            }
        });
        etPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus&&etPhone.getText().toString().length()>0){
                    mIvPhoneClear.setVisibility(View.VISIBLE);
                }
            }
        });
        //监听标题输入
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()>0){
                    mIvTitleClear.setVisibility(View.VISIBLE);
                }else {
                    mIvTitleClear.setVisibility(View.GONE);
                }
            }
        });
        //监听联系人输入
        etContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()>0){
                    mIvContactClear.setVisibility(View.VISIBLE);
                }else {
                    mIvContactClear.setVisibility(View.GONE);
                }
            }
        });
        //监听手机号输入
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()>0){
                    mIvPhoneClear.setVisibility(View.VISIBLE);
                }else {
                    mIvPhoneClear.setVisibility(View.GONE);
                }
            }
        });
        //监听地址输入
        etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()>0){
                    mIvAddressClear.setVisibility(View.VISIBLE);
                }else {
                    mIvAddressClear.setVisibility(View.GONE);
                }
            }
        });
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
                isSelectImage=true;
                if (imageUrls.size() < 9) imageUrls.add(0, "");
                imageAdapter.setList(imageUrls);
                Log.d("onActivityResult", JsonUtil.moderToString(selectList));
                break;
        }
    }

    @Override
    public void commitSuccess() {
        CommonToast.toast(getString(R.string.spublish_success));
        setResult(11);
        finish();
    }

    @Override
    public void showCompanyDetail(CmpanydetaisModel data) {
        etTitle.setText(data.getTitle());
        tvType.setText(data.getServiceName().concat(" ").concat(data.getSecondServiceName()));
        etContact.setText(data.getContactName());
        etPhone.setText(data.getContactPhone());
//        head.setText(data.getCountryName().concat(data.getCityName()));
        concat = data.getCountryName().concat(" ").concat(data.getCityName()).concat(" ");
        etAddress.setText(data.getAddress());
//        etAddress.setText(data.getAddress());
        etDesc.setText(data.getDesc());

        serviceId = data.getService();
        secondServiceId = data.getSecondService();
        cityId = data.getCityId();
        countryId = data.getCountryId();

        imageUrls = data.getImgs();
        remoteImage = "";
        for (String url : imageUrls) {
            String[] sps = url.split("Uploads");
            if (sps == null) break;
            remoteImage = remoteImage.concat("/Uploads".concat(sps[1])).concat(",");
        }
        if (imageUrls.size() < 9) imageUrls.add(0, "");
        imageAdapter.setList(imageUrls);
    }

    @Override
    public String getRemoteImages() {
        return remoteImage;
    }

    @Override
    public String getCid() {
        return cid;
    }

    @Override
    public void showSelectData(CompanyTypeModel model) {
        tvType.setText(model.getName());
        serviceId = model.getCId();
    }

    @Override
    public void selectService(String serviceId, String serviceName, String secondServiceIs, String secondServiceName) {
        this.serviceId = serviceId;
        this.secondServiceId = secondServiceIs;
        tvType.setText(serviceName.concat(" ").concat(secondServiceName));
    }

    @Override
    public void selecrtAddress(String countryName, String cityName, String countryId, String cityId) {
        this.countryId = countryId;
        this.cityId = cityId;
        tvArea.setText(countryName.concat(" ").concat(cityName));
    }

    @OnClick(R.id.tvCommit)
    public void onViewClicked() {
        String title = etTitle.getText().toString();
        String name = etContact.getText().toString();
        String phone = etPhone.getText().toString();
        String address = etAddress.getText().toString();
        String desc = etDesc.getText().toString();
        SoftInputUtil.hideSoftInput(getCurActivity());
        if (imageUrls.contains("")) imageUrls.remove("");
        if (isSelectImage) {
            mPresenter.updateImage(title, serviceId, name, phone, address, desc, imageUrls, secondServiceId, countryId, cityId);
        }else {
            if (TextUtils.isEmpty(getCid())) {
                mPresenter.commit(title, serviceId, name, phone, address, desc, "", secondServiceId, countryId, cityId);
            } else {
                mPresenter.companyEdit(title, serviceId, name, phone, address, desc, getCid(), remoteImage, secondServiceId, countryId, cityId);
            }
        }
    }

    @OnClick({R.id.tvType, R.id.tvArea,R.id.iv_title_clear, R.id.iv_contact_clear, R.id.iv_phone_clear, R.id.iv_address_clear})
    public void onViewType(View view) {
        switch (view.getId()) {
            case R.id.tvArea:
                mPresenter.showAreaDialog();
                break;
            case R.id.tvType:
                SoftInputUtil.hideSoftInput(this);
                mPresenter.showTypeDialog();
                break;
            case R.id.iv_title_clear://标题清除
                etTitle.setText("");
                mIvTitleClear.setVisibility(View.GONE);
                break;
            case R.id.iv_contact_clear://联系人清除
                etContact.setText("");
                mIvContactClear.setVisibility(View.GONE);
                break;
            case R.id.iv_phone_clear://联系电话清除
                etPhone.setText("");
                mIvPhoneClear.setVisibility(View.GONE);
                break;
            case R.id.iv_address_clear://企业地址清除
                etAddress.setText("");
                mIvAddressClear.setVisibility(View.GONE);
                break;
        }
    }
}
