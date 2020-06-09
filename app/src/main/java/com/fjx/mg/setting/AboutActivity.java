package com.fjx.mg.setting;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.library.common.base.BaseActivity;
import com.library.common.utils.AppUtil;

import butterknife.BindView;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.tvVersion)
    TextView tvVersion;

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_about;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.abount));

        String versionName = AppUtil.getVersionName();
        tvVersion.setText("v".concat(versionName));

    }
}
