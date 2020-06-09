package com.fjx.mg.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.common.sharesdk.PlatformType;
import com.common.sharesdk.repostory.ShareCallback;
import com.fjx.mg.R;
import com.fjx.mg.me.invite.InviteActivity;
import com.fjx.mg.utils.ThirdUtils;
import com.library.common.callback.CActivityManager;
import com.library.common.utils.IntentUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.core.BottomPopupView;

public class ShareDialog2 extends BottomPopupView {

    public enum ShareType {
        image, web
    }

    public String imageUrl;
    private String title;
    private String desc;
    private String webUrl;
    private ShareType shareType;
    private Context mContext;

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog2_share;
    }

    public void setShareParams(String title, String desc, String imageUrl, String webUel) {
        this.title = title;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.webUrl = webUel;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setShareType(ShareType shareType) {
        this.shareType = shareType;
    }

    public ShareDialog2(@NonNull final Context context) {
        super(context);
        mContext=context;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.tvSMSCode).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.pickContact(CActivityManager.getAppManager().currentActivity());
            }
        });

        findViewById(R.id.tvShareFacebook).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (ThirdUtils.isFacebookAvilible(getContext())){
                    switch (shareType) {
                        case image:
                            RepositoryFactory.getShareApi().fackbookShareImage(null, imageUrl, shareCallback);
                            break;
                        case web:
                            RepositoryFactory.getShareApi().fackbookShareWeb(null, title, desc, imageUrl, webUrl, shareCallback);
                            break;
                        default:
                            break;
                    }
                }else {
                    //未安装facebook
                    new AlertDialog.Builder(getContext())
                            .setTitle(getContext().getString(R.string.tips))
                            .setMessage(getContext().getString(R.string.uninstalled_facebook))
                            .setPositiveButton(getContext().getString(R.string.confirm1), null)
                            .create()
                            .show();
                }

            }
        });

        findViewById(R.id.tvShareWechat).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                switch (shareType) {
                    case web:
                        if (StringUtil.isEmpty(webUrl)) {
                            AlertDialog dialog = new AlertDialog.Builder(mContext)
                                    .setMessage(R.string.no_invite_code)
                                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            mContext.startActivity(InviteActivity.newInstance(mContext));
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .setNegativeButton(R.string.no_thank_you, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    }).create();
                            dialog.show();
                        } else {
                            RepositoryFactory.getShareApi().wechareShareWeb(PlatformType.WECHAT, title, desc, imageUrl, webUrl, shareCallback);
                        }

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
        findViewById(R.id.tvCancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public ShareCallback shareCallback = new ShareCallback() {
        @Override
        public void onSucces() {
        }
    };
}
