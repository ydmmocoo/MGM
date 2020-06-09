package com.tencent.qcloud.uikit.business.session.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.library.common.utils.BitmapUtil;
import com.library.common.utils.CacheManager;
import com.library.common.utils.CommonImageLoader;
import com.tencent.qcloud.uikit.R;
import com.tencent.qcloud.uikit.business.chat.model.CacheUtil;
import com.tencent.qcloud.uikit.business.session.model.SessionInfo;
import com.tencent.qcloud.uikit.common.utils.UIUtils;
import com.tencent.qcloud.uikit.common.widget.gatherimage.SynthesizedImageView;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SessionIconView extends RelativeLayout {
    private ImageView mIconView;

    private static final int icon_size = UIUtils.getPxByDp(50);


    public SessionIconView(Context context) {
        super(context);
        init();
    }

    public SessionIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SessionIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void invokeInformation(SessionInfo sessionInfo, DynamicSessionIconView infoView) {
        infoView.setLayoutContainer(this);
        infoView.setMainViewId(R.id.profile_icon_group);
        infoView.parseInformation(sessionInfo);
    }

    private void init() {
        inflate(getContext(), R.layout.profile_icon_view, this);
        mIconView = findViewById(R.id.profile_icon);
        ((SynthesizedImageView) mIconView).defaultImage(R.drawable.default_user_image);
    }

    public void setProfileImageView(ImageView iconView) {
        mIconView = iconView;
        LayoutParams params = new LayoutParams(icon_size, icon_size);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(mIconView, params);

    }

    /**
     * 设置会话头像的url
     *
     * @param iconUrls 头像url,最多只取前9个
     */
    public void setIconUrls(List<String> iconUrls) {
        if (mIconView instanceof SynthesizedImageView) {
            ((SynthesizedImageView) (mIconView)).displayImage(iconUrls).load();
        }
    }

    @SuppressLint("CheckResult")
    public void setIcon(final String url) {
        if (TextUtils.isEmpty(url)) {
            mIconView.setImageResource(R.drawable.default_user_image);
        } else {
            Bitmap bitmap = CacheManager.getInstance(getContext()).getBitmap(url);
            if (bitmap != null) {
                mIconView.setImageBitmap(bitmap);
            } else {
                CommonImageLoader.load(url).placeholder(R.drawable.default_user_image).error(R.drawable.default_user_image).into(mIconView);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CommonImageLoader.getThumbBitmapByUrl(url);
                    }
                }).start();
            }
        }
    }

    public void setIconResId(int resId) {
        if (resId == -1) {
            mIconView.setImageResource(R.drawable.default_user_image);
        }else {
            mIconView.setImageResource(resId);
        }
    }

    public void setDefIcon(){
        mIconView.setImageResource(R.drawable.default_user_image);
    }

    public void setDefaultImageResId(int resId) {
        ((SynthesizedImageView) mIconView).defaultImage(resId);
    }
}

