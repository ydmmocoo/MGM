package com.fjx.mg.entrance;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fjx.mg.R;
import com.fjx.mg.main.MainActivity;
import com.fjx.mg.utils.SharedPreferencesUtils;
import com.fjx.mg.web.CommonWebActivity;
import com.library.common.base.BaseActivity;
import com.library.common.base.BaseApp;
import com.library.common.utils.JsonUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 广告页
 */
public class AdActivity extends BaseActivity {

    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.tvTime)
    TextView tvTime;
    private String url;


    @Override
    protected int layoutId() {
        mCommonStatusBarEnable = false;
        return R.layout.ac_ad;
    }

    @Override
    protected void initView() {
        /*adModel = RepositoryFactory.getLocalRepository().getEntranceAd();
        if (adModel == null) goMain();
        if (StringUtil.isEmpty(adModel.getImg())) goMain();
        try {
            CommonImageLoader.load(adModel.getImg() == null ? R.drawable.splash_bg : adModel.getImg()).placeholder(R.drawable.splash_bg).noAnim().into(ivImage);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (TimConfig.isRelease) {
            timer.start();
        } else {
            goMain();
        }*/
        String img=SharedPreferencesUtils.getString(BaseApp.getInstance().getApplicationContext(), "ad_image", "");
        url=SharedPreferencesUtils.getString(BaseApp.getInstance().getApplicationContext(), "ad_url", "");
        if (TextUtils.isEmpty(img)){
            goMain();
        }else {
            tvTime.setVisibility(View.VISIBLE);
            Glide.with(this).load(img).into(ivImage);
            timer.start();
        }
    }

    private CountDownTimer timer = new CountDownTimer(4000, 1000) {
        @SuppressLint("SetTextI18n")
        @Override
        public void onTick(long millisUntilFinished) {
            long second = millisUntilFinished / 1000;
            tvTime.setText(getString(R.string.skip) + "（" + second + "s）");
        }

        @Override
        public void onFinish() {
            goMain();
        }
    };

    private void goMain() {
        startActivity(MainActivity.newInstance(getCurContext()));
        finish();
    }

    @OnClick({R.id.ivImage, R.id.tvTime})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivImage:
                if (TextUtils.isEmpty(url)) return;
                CommonWebActivity.Options options = new CommonWebActivity.Options();
                options.setLoadUrl(url);
                startActivity(CommonWebActivity.newInstance(getCurContext(), JsonUtil.moderToString(options)));
                timer.cancel();
                finish();
                break;
            case R.id.tvTime:
                timer.cancel();
                goMain();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
    }
}