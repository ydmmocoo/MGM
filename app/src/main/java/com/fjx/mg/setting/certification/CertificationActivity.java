package com.fjx.mg.setting.certification;

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
import com.fjx.mg.me.safe_center.question.QuestionSetActivity;
import com.fjx.mg.setting.password.pay.ModifyPayPwdActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.JsonUtil;
import com.library.repository.data.UserCenter;
import com.library.repository.models.PersonCerModel;
import com.library.repository.models.UserInfoModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CertificationActivity extends BaseMvpActivity<CertificationPresenter> implements CertificationContract.View {
    @BindView(R.id.tvCertification)
    TextView tvCertification;
    @BindView(R.id.etRealName)
    EditText etRealName;
    @BindView(R.id.etIdCard)
    EditText etIdCard;
    @BindView(R.id.tvAreaCode)
    TextView tvAreaCode;
    @BindView(R.id.etPhoneNum)
    EditText etPhoneNum;
    @BindView(R.id.tvCardFront)
    TextView tvCardFront;
    @BindView(R.id.ivCardFront)
    ImageView ivCardFront;
    @BindView(R.id.tvCardBack)
    TextView tvCardBack;
    @BindView(R.id.ivCardBack)
    ImageView ivCardBack;
    private String frontImageUrl, backImageUrl;
    private String frontLocalImageUrl, backLocalImageUrl;
    private boolean isFront;

    @Override
    protected CertificationPresenter createPresenter() {
        return new CertificationPresenter(this);
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, CertificationActivity.class);
        return intent;
    }


    @Override
    protected int layoutId() {
        return R.layout.ac_certification;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.Real_name_authentication));
        GradientDrawableHelper.whit(tvCertification).setColor(R.color.colorAccent).setCornerRadius(50);
    }


    @OnClick({R.id.tvAreaCode, R.id.tvCertification, R.id.llCardFront, R.id.llCardBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvAreaCode:
                CommonDialogHelper.showAreaCodeDialog(this, new OnSelectListener() {
                    @Override
                    public void onSelect(int position, String text) {
                        tvAreaCode.setText(text);
                    }
                });
                break;

            case R.id.llCardFront:
                showPickImageDialog(true);
                break;
            case R.id.llCardBack:
                showPickImageDialog(false);
                break;
            case R.id.tvCertification:
                String realeName = etRealName.getText().toString();
                String idCard = etIdCard.getText().toString();
                String phone = etPhoneNum.getText().toString();
                String sn = tvAreaCode.getText().toString().replace("+", "");
                if (TextUtils.isEmpty(frontLocalImageUrl) || TextUtils.isEmpty(backLocalImageUrl)) {
//                    CommonToast.toast(getString(R.string.hint_idcard_image));
                    mPresenter.certification(realeName, idCard, phone, sn, frontImageUrl, backImageUrl);
//                    return;
                } else {
                    if (TextUtils.isEmpty(frontImageUrl) || TextUtils.isEmpty(backImageUrl)) {
                        mPresenter.updateImage(frontLocalImageUrl, "front");
                        mPresenter.updateImage(backLocalImageUrl, "back");
                    } else {
                        mPresenter.certification(realeName, idCard, phone, sn, frontImageUrl, backImageUrl);
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
        if (TextUtils.equals(key, "front")) {
            frontImageUrl = imgeUrl;
        } else {
            backImageUrl = imgeUrl;
        }

        if (!TextUtils.isEmpty(frontImageUrl) && !TextUtils.isEmpty(backImageUrl)) {
            String realeName = etRealName.getText().toString();
            String idCard = etIdCard.getText().toString();
            String phone = etPhoneNum.getText().toString();
            String sn = tvAreaCode.getText().toString().replace("+", "");
            mPresenter.certification(realeName, idCard, phone, sn, frontImageUrl, backImageUrl);
        }

    }

    @Override
    public void commitSuccess() {
        UserInfoModel infoModel = UserCenter.getUserInfo();
        if (!infoModel.isSetSecurityIssues()) {
            startActivity(QuestionSetActivity.newInstance(getCurContext()));
        } else if (infoModel.getIsSetPayPsw() != 1) {
            startActivity(ModifyPayPwdActivity.newInstance(getCurContext()));
        }

        setResult(111);
        finish();
    }

    @Override
    public void showPersonCerInfo(PersonCerModel model) {

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
