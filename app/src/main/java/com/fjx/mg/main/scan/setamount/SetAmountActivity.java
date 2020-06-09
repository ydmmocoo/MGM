package com.fjx.mg.main.scan.setamount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.main.scan.QRCodeCollectionPresenter;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.StatusBarManager;

import butterknife.BindView;

public class SetAmountActivity extends BaseMvpActivity<SetAmountPresenter> implements SetAmountContract.View {
    EditText etComment;
    @BindView(R.id.tvSend)
    TextView tvSend;

    @Override
    protected int layoutId() {
        return R.layout.activity_set_amount;
    }

    @Override
    protected SetAmountPresenter createPresenter() {
        return new SetAmountPresenter(this);
    }

    @Override
    protected void initView() {
        StatusBarManager.setLightFontColor(this, R.color.colorAccent);
        ToolBarManager.with(this).setNavigationIcon(R.drawable.iv_back).setTitle(getString(R.string.qr_code), R.color.white).setBackgroundColor(R.color.colorAccent);
        etComment = findViewById(R.id.etCommentx);
        findViewById(R.id.tvSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = etComment.getText().toString();

                if (!s.equals("")) {
                    Intent intent = new Intent();

                    intent.putExtra("content", s);
                    setResult(11, intent);
                    finish();
                }
            }
        });
    }


}
