package com.fjx.mg.me.qr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.utils.DESDemo;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CacheManager;
import com.library.common.utils.CommonImageLoader;
import com.library.repository.Constant;
import com.library.repository.data.UserCenter;
import com.library.repository.models.UserInfoModel;
import com.tencent.qcloud.uikit.TimConfig;

import butterknife.BindView;

public class QrCodeActivity extends BaseMvpActivity<QrCodePresenter> implements QrCodeContract.View {
    @BindView(R.id.ivQrCode)
    ImageView ivQrCode;
    @BindView(R.id.ivWord1)
    ImageView ivWord1;
    @BindView(R.id.tvHint2)
    TextView tvHint2;
    private UserInfoModel userInfo;

    @Override
    protected QrCodePresenter createPresenter() {
        return new QrCodePresenter(this);
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, QrCodeActivity.class);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_qr_code;
    }

    @Override
    protected void initView() {//2是女士
        ToolBarManager.with(this).setTitle(getString(R.string.user_qrcode)).setBackgroundColor(R.color.textColorYellow5);
        userInfo = UserCenter.getUserInfo();
        CommonImageLoader.load(userInfo.getUImg()).circle().placeholder(R.drawable.user_default).into(ivWord1);
        tvHint2.setText("" + userInfo.getUNick());
        Intent intent = getIntent();
        String tvGender = intent.getStringExtra("tvGender");
        Drawable drawable_n = getResources().getDrawable(tvGender.equals("男") ? R.drawable.gender_man : R.drawable.gender_woman);
        drawable_n.setBounds(0, 0, drawable_n.getMinimumWidth(), drawable_n.getMinimumHeight());  //此为必须写的
        tvHint2.setCompoundDrawables(null, null, drawable_n, null);
        if (mPresenter == null) {
        } else {
            try {

                String plaintext = userInfo.getIdentifier();


                String str = userInfo.getIdentifier();
                //密钥，长度要是8的倍数
                String ssss = DESDemo.encodeEBC(TimConfig.getKey(), str.getBytes());
                byte[] bytes = DESDemo.decodeEBC(TimConfig.getKey(), Base64.decode(ssss, 0));
                Log.e("加密后：" + ssss, "解密：" + new String(bytes));
                Bitmap bitmap1 = CacheManager.getInstance(this).getBitmap(Constant.getHost() + "invite/index/" + ssss);
                if (bitmap1 != null) {
                    Log.e("bitmap1","!= null");
                    ivQrCode.setImageBitmap(bitmap1);
                }
                mPresenter.getInviteCode(userInfo.getUImg(), Constant.getHost() + "invite/index/" + ssss, this);
            } catch (Exception e) {
                Log.e("(Exception)", "" + e.toString());
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();
//

    }


    @Override
    public void showQrBitmap(Bitmap bitmap) {
        ivQrCode.setImageBitmap(bitmap);
    }


}