package com.fjx.mg.me.safe_center.bind;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.dialog.CommonDialogHelper;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.lxj.xpopup.interfaces.OnSelectListener;

import butterknife.BindView;
import butterknife.OnClick;

public class RebindMobileActivity extends BaseMvpActivity<RebindMobilePresenter> implements RebindMobileContract.View {


    @BindView(R.id.etMobile)
    EditText etMobile;
    @BindView(R.id.etSmsCode)
    EditText etSmsCode;
    @BindView(R.id.confirm)
    TextView confirm;
    @BindView(R.id.tvAreaCode)
    TextView tvAreaCode;

    @BindView(R.id.tvGetSmsCode)
    TextView tvGetSmsCode;
    @Override
    public void setMobile(String text) {
        etMobile.setText(text);
    }
    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, RebindMobileActivity.class);
        return intent;
    }

    @Override
    protected RebindMobilePresenter createPresenter() {
        return new RebindMobilePresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_rebind_mobile;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.bind_new_mobile));
        GradientDrawableHelper.whit(confirm).setColor(R.color.colorAccent).setCornerRadius(50);
    }


    @OnClick({R.id.confirm, R.id.tvGetSmsCode, R.id.tvAreaCode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                String phone = etMobile.getText().toString();
                String smsCode = etSmsCode.getText().toString();
                String areaCode = tvAreaCode.getText().toString();
                areaCode = areaCode.replace("+", "");
                mPresenter.reBindMobile(phone, areaCode, smsCode);
                break;
            case R.id.tvGetSmsCode:
                phone = etMobile.getText().toString();
                areaCode = tvAreaCode.getText().toString();
                areaCode = areaCode.replace("+", "");
                mPresenter.sendSmsCode(areaCode, phone);
                break;

            case R.id.tvAreaCode:
                CommonDialogHelper.showAreaCodeDialog(this, new OnSelectListener() {
                    @Override
                    public void onSelect(int position, String text) {
                        tvAreaCode.setText(text);
                    }
                });
                break;
        }
    }


    @Override
    public void showTimeCount(String s) {
        tvGetSmsCode.setText(s);
    }

    @Override
    public void reBindSuccess() {
        CommonToast.toast(getString(R.string.set_success));
        finish();
    }

    @Override
    protected void onDestroy() {
        mPresenter.releaseTimer();
        super.onDestroy();
    }
}
