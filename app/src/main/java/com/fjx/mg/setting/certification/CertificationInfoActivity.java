package com.fjx.mg.setting.certification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.library.common.base.BaseMvpActivity;
import com.library.repository.models.PersonCerModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CertificationInfoActivity extends BaseMvpActivity<CertificationPresenter> implements CertificationContract.View {


    @BindView(R.id.tvRealName)
    TextView tvRealName;
    @BindView(R.id.tvIdCard)
    TextView tvIdCard;
    @BindView(R.id.tvPhoneNum)
    TextView tvPhoneNum;

    @Override
    protected CertificationPresenter createPresenter() {
        return new CertificationPresenter(this);
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, CertificationInfoActivity.class);
        return intent;
    }


    @Override
    protected int layoutId() {
        return R.layout.ac_certification_info;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.Real_name_authentication));
        mPresenter.userAuditInfo();
    }


    @Override
    public void uploadImageSucces(String key, String imgeUrl) {

    }

    @Override
    public void commitSuccess() {

    }

    @Override
    public void showPersonCerInfo(PersonCerModel model) {
        tvRealName.setText(model.getName());
        tvIdCard.setText(model.getIccid());
        tvPhoneNum.setText(model.getPhone());
    }


}
