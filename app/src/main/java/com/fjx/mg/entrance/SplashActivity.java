package com.fjx.mg.entrance;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fjx.mg.R;
import com.fjx.mg.main.MainActivity;
import com.library.common.utils.StringUtil;
import com.library.repository.data.UserCenter;
import com.library.repository.models.AdModel;
import com.library.repository.repository.RepositoryFactory;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!this.isTaskRoot()) {
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                finish();
                return;
            }
        }
        UserCenter.imLogin();
        getPrepareData();
    }

    private void getPrepareData() {
        AdModel adModel = RepositoryFactory.getLocalRepository().getEntranceAd();
        if (adModel != null) {
            if (StringUtil.isNotEmpty(adModel.getImg())) {
                startActivity(new Intent(SplashActivity.this, AdActivity.class));
                overridePendingTransition(R.anim.ad_enter_anim, R.anim.ad_exit_anim);
//                    overridePendingTransition(0,0);
            } else {

                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        } else {

            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        finish();
    }

}
