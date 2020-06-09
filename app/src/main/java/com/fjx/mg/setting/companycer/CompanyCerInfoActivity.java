package com.fjx.mg.setting.companycer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonImageLoader;
import com.library.repository.models.CompanyCerModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompanyCerInfoActivity extends BaseMvpActivity<CompanyCerPresenter> implements CompanyCerContract.View {

    @BindView(R.id.tvRealName)
    TextView tvRealName;
    @BindView(R.id.tvIdCard)
    TextView tvIdCard;
    @BindView(R.id.ivImage1)
    ImageView ivImage1;
    @BindView(R.id.ivImage2)
    ImageView ivImage2;

    @Override
    protected CompanyCerPresenter createPresenter() {
        return new CompanyCerPresenter(this);
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, CompanyCerInfoActivity.class);
        return intent;
    }


    @Override
    protected int layoutId() {
        return R.layout.ac_certification_com_info;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.Enterprise_Certification));
        mPresenter.companyAuditInfo();
    }


    @Override
    public void uploadImageSucces(String key, String imgeUrl) {

    }

    @Override
    public void commitSuccess() {

    }

    @Override
    public void showCompanyCerInfo(CompanyCerModel model) {
        tvRealName.setText(model.getCompanyName());
        tvIdCard.setText(model.getBusinessLicense());
        CommonImageLoader.load(model.getBusinessImg()).into(ivImage1);
        CommonImageLoader.load(model.getEmployImg()).into(ivImage2);
    }

}
