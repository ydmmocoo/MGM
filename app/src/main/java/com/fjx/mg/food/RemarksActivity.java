package com.fjx.mg.food;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.view.flowlayout.TagFlowLayout;
import com.library.common.base.BaseActivity;
import com.library.common.utils.CommonToast;

import butterknife.BindView;

/**
 * 备注
 */
public class RemarksActivity extends BaseActivity {

    @BindView(R.id.et_remarks)
    EditText mEtRemarks;
    @BindView(R.id.fl_remarks_tag)
    TagFlowLayout mFlRemarksTag;

    public static Intent newInstance(Context context) {
        return new Intent(context, RemarksActivity.class);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_remarks;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.remark));
        //完成
        ToolBarManager.with(this).setRightText(getString(R.string.confirm), R.color.colorAccent,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remark=mEtRemarks.getText().toString();
                /*if (TextUtils.isEmpty(remark)){
                    CommonToast.toast(getResources().getString(R.string.remark_is_null));
                    return;
                }*/
                Intent intent=new Intent();
                intent.putExtra("content",remark);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
