package com.fjx.mg.main.translate;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.SoftInputUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.models.LanTranslateResultM;
import com.library.repository.models.TranslateLanModel;
import com.library.repository.repository.RepositoryFactory;

import butterknife.BindView;
import butterknife.OnClick;

public class TranslateActivity extends BaseMvpActivity<TranslatePresenter> implements TranslateContract.View {


    @BindView(R.id.tvStartLan)
    TextView tvStartLan;
    @BindView(R.id.tvEndLan)
    TextView tvEndLan;
    @BindView(R.id.etTranslateContent)
    EditText etTranslateContent;
    @BindView(R.id.tvTransfer)
    TextView tvTransfer;
    @BindView(R.id.tvTransferResult)
    TextView tvTransferResult;
    @BindView(R.id.tvCopyText)
    TextView mTvCopyText;
    private String from = "fra", to = "zh";

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, TranslateActivity.class);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_translate;
    }

    @Override
    protected TranslatePresenter createPresenter() {
        return new TranslatePresenter(this);
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.translate_online));
        GradientDrawableHelper.whit(tvTransfer).setColor(R.color.colorAccent).setCornerRadius(50);
        mPresenter.initData();
    }


    @OnClick({R.id.tvStartLan, R.id.tvEndLan, R.id.tvTransfer, R.id.tvTransferResult, R.id.ivChange,R.id.ivTranslateDelete})
    public void onViewClicked(View view) {
        SoftInputUtil.hideSoftInput(this);
        switch (view.getId()) {
            case R.id.tvStartLan:
                mPresenter.showlanguageDialog(true);
                break;
            case R.id.tvEndLan:
                mPresenter.showlanguageDialog(false);
                break;
            case R.id.tvTransfer:
                tvTransferResult.setText("");
                String content = etTranslateContent.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    CommonToast.toast(getString(R.string.hint_input_translate));
                    return;
                }
                RepositoryFactory.getLocalRepository().saveLastTranslate(tvStartLan.getText().toString(), from, tvEndLan.getText().toString(), to);
                mPresenter.translate(content, from, to);
                break;

            case R.id.tvTransferResult:
            case R.id.tvCopyText:
                String result = tvTransferResult.getText().toString();
                if (!TextUtils.isEmpty(result)) {
                    StringUtil.copyClip(result);
                    CommonToast.toast(getString(R.string.copy_success));
                }

                break;

            case R.id.ivChange:
                change();
                break;
            case R.id.ivTranslateDelete://删除输入内容
                etTranslateContent.setHint(getResources().getString(R.string.hint_input_translate));
                etTranslateContent.setText("");
                break;
            default:
                break;
        }
    }

    @Override
    public void showTranslateResult(LanTranslateResultM model) {

        if (model == null) return;
        if (model.getTrans_result().isEmpty()) return;
        String result = "";
        for (LanTranslateResultM.TransResultBean b : model.getTrans_result()) {
            result = result.concat(b.getDst()).concat("\n");
        }
        tvTransferResult.setText(result);
    }

    private void change() {
        if (TextUtils.equals("auto", from)) return;
        String temp = from;
        from = to;
        to = temp;
        String start = tvStartLan.getText().toString();
        tvStartLan.setText(tvEndLan.getText());
        tvEndLan.setText(start);

    }

    @Override
    public void showLanguage(boolean isFrom, TranslateLanModel lanModel) {
        if (isFrom) {
            from = lanModel.getName();
            tvStartLan.setText(lanModel.getType().concat("（").concat(lanModel.getName()).concat("）"));
        } else {
            to = lanModel.getName();
            tvEndLan.setText(lanModel.getType().concat("（").concat(lanModel.getName()).concat("）"));
        }
    }

    @Override
    public void initData(String fromText, String from, String toText, String to) {
        this.from = from;
        this.to = to;
        tvStartLan.setText(fromText);
        tvEndLan.setText(toText);
    }
}
