package com.fjx.mg.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;

import com.common.sharesdk.PlatformType;
import com.common.sharesdk.repostory.ShareCallback;
import com.fjx.mg.R;
import com.library.common.callback.CActivityManager;
import com.library.common.utils.IntentUtil;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.core.BottomPopupView;

public class WebShareDialog extends BottomPopupView {

    public enum ShareType {
        image, web
    }

    public String imageUrl;
    private String title;
    private String desc;
    private String webUrl;
    private ShareType shareType;
    private Context mContext;

    public void setShareParams(String title, String desc, String imageUrl, String webUel) {
        this.title = title;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.webUrl = webUel;
    }

    public void showSmsTab() {
        findViewById(R.id.tvSMSCode).setVisibility(VISIBLE);
        findViewById(R.id.tvSMSCode).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.pickContact(CActivityManager.getAppManager().currentActivity());
            }
        });
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setShareType(ShareType shareType) {
        this.shareType = shareType;
    }

    public WebShareDialog(@NonNull final Context context) {
        super(context);
        mContext=context;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.tvShareWechat).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                switch (shareType) {
                    case web:
                        RepositoryFactory.getShareApi().wechareShareWeb(PlatformType.WECHAT, title, desc, imageUrl, webUrl, shareCallback);
                        break;
                    case image:
                        RepositoryFactory.getShareApi().wechatShareImage(PlatformType.WECHAT, imageUrl, shareCallback);
                        break;
                }

            }
        });

        findViewById(R.id.tvShareCircle).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                switch (shareType) {
                    case web:
                        RepositoryFactory.getShareApi().wechareShareWeb(PlatformType.CIRCLE, title, desc, imageUrl, webUrl, shareCallback);
                        break;
                    case image:
                        RepositoryFactory.getShareApi().wechatShareImage(PlatformType.CIRCLE, imageUrl, shareCallback);
                        break;
                }

            }
        });
        findViewById(R.id.tvShareWeb).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
                mContext.startActivity(browserIntent);
                dismiss();
            }
        });
        findViewById(R.id.tvCancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.web_dialog_share;
    }


    public ShareCallback shareCallback = new ShareCallback() {
        @Override
        public void onSucces() {

        }
    };
}
