package com.fjx.mg.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RadioGroup;

import com.fjx.mg.R;
import com.fjx.mg.entrance.SplashActivity;
import com.library.common.base.BaseActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.SharedPreferencesUtil;
import com.library.repository.repository.RepositoryFactory;

public class TestActivity extends BaseActivity {

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, TestActivity.class);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_test;
    }

    @Override
    protected void initView() {
        RadioGroup rg = findViewById(R.id.rg);
        String envit = RepositoryFactory.getLocalRepository().getHostEnvironment();
        if (TextUtils.equals(envit, "1")) {
            rg.check(R.id.rbcs);
        } else {
            rg.check(R.id.rbDefault);
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbDefault:
                        RepositoryFactory.getLocalRepository().saveHostEnvironment("");
                        break;

                    case R.id.rbcs:
                        RepositoryFactory.getLocalRepository().saveHostEnvironment("2");
                        break;
                }
                CommonToast.toast(R.string.environment_has_been_switched_you_need_to_exit_the_app_and_restart);
                finish();
            }
        });
    }
}
