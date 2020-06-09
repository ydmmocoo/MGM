package com.tencent.qcloud.uikit.operation.group;

import com.library.common.base.BaseActivity;
import com.library.common.utils.StatusBarManager;
import com.tencent.qcloud.uikit.R;

/**
 * 群组设置
 */
public class GroupManagerActivity extends BaseActivity {


    @Override
    protected int layoutId() {
        return R.layout.group_manager_activity;
    }

    @Override
    protected void initView() {
        StatusBarManager.setLightFontColor(this, R.color.colorAccent);

        GroupInfoFragment fragment = new GroupInfoFragment();
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.group_manager_base, fragment).commitAllowingStateLoss();
    }

    @Override
    public void finish() {
        super.finish();
        setResult(1001);
    }
}
