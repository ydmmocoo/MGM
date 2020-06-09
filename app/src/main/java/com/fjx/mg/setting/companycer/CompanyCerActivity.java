package com.fjx.mg.setting.companycer;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.dialog.CommonDialogHelper;
import com.fjx.mg.dialog.PickImageDialog;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.JsonUtil;
import com.library.repository.models.CompanyCerModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CompanyCerActivity extends BaseMvpActivity<CompanyCerPresenter> implements CompanyCerContract.View {
    @BindView(R.id.tvCertification)
    TextView tvCertification;
    @BindView(R.id.etRealName)
    EditText etRealName;
    @BindView(R.id.etIdCard)
    EditText etIdCard;
    @BindView(R.id.tvCardFront)
    TextView tvCardFront;
    @BindView(R.id.ivCardFront)
    ImageView ivCardFront;
    @BindView(R.id.tvCardBack)
    TextView tvCardBack;
    @BindView(R.id.ivCardBack)
    ImageView ivCardBack;
    private String businessImg, employImg;
    private String frontLocalImageUrl, backLocalImageUrl;
    private boolean isFront;

    @Override
    protected CompanyCerPresenter createPresenter() {
        return new CompanyCerPresenter(this);
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, CompanyCerActivity.class);
        return intent;
    }


    @Override
    protected int layoutId() {
        return R.layout.ac_certification_com;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.Enterprise_Certification));
        GradientDrawableHelper.whit(tvCertification).setColor(R.color.colorAccent).setCornerRadius(50);
    }


    @OnClick({R.id.tvCertification, R.id.llCardFront, R.id.llCardBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.llCardFront:
                showPickImageDialog(true);
                break;
            case R.id.llCardBack:
                showPickImageDialog(false);
                break;
            case R.id.tvCertification:
                String realeName = etRealName.getText().toString();
                String idCard = etIdCard.getText().toString();
                if (TextUtils.isEmpty(frontLocalImageUrl) || TextUtils.isEmpty(backLocalImageUrl)) {
                    CommonToast.toast(getString(R.string.hint_upload_image));
                    return;
                } else {
                    if (TextUtils.isEmpty(businessImg) || TextUtils.isEmpty(employImg)) {
                        mPresenter.updateImage(frontLocalImageUrl, "businessImg");
                        mPresenter.updateImage(backLocalImageUrl, "employImg");
                    } else {
                        mPresenter.certification(realeName, idCard, businessImg, employImg);
                    }

                }
                break;
        }


    }

    private void showPickImageDialog(boolean b) {
        isFront = b;
        new XPopup.Builder(getCurContext())
                .asCustom(new PickImageDialog(getCurContext()))
                .show();
    }

    @Override
    public void uploadImageSucces(String key, String imgeUrl) {
        if (TextUtils.equals(key, "businessImg")) {
            businessImg = imgeUrl;
        } else {
            employImg = imgeUrl;
        }

        if (!TextUtils.isEmpty(businessImg) && !TextUtils.isEmpty(employImg)) {
            String name = etRealName.getText().toString();
            String licenseNo = etIdCard.getText().toString();
            mPresenter.certification(name, licenseNo, businessImg, employImg);
        }

    }

    @Override
    public void commitSuccess() {
        finish();
    }

    @Override
    public void showCompanyCerInfo(CompanyCerModel model) {

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
                    if (isFront) {
                        frontLocalImageUrl = path;
                        ivCardFront.setVisibility(View.VISIBLE);
                        tvCardFront.setVisibility(View.GONE);
                        CommonImageLoader.load(path).placeholder(R.drawable.user_default).into(ivCardFront);
                    } else {
                        backLocalImageUrl = path;
                        ivCardBack.setVisibility(View.VISIBLE);
                        tvCardBack.setVisibility(View.GONE);
                        CommonImageLoader.load(path).placeholder(R.drawable.user_default).into(ivCardBack);
                    }

                }
                Log.d("onActivityResult", JsonUtil.moderToString(selectList));
                break;
        }
    }


}
