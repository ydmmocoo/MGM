package com.fjx.mg.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.common.sharesdk.PlatformType;
import com.common.sharesdk.repostory.ShareCallback;
import com.fjx.mg.R;
import com.fjx.mg.me.invite.InviteActivity;
import com.fjx.mg.moments.add.NewMomentsActivity;
import com.fjx.mg.sharetoapp.FriendsSelectActivity;
import com.fjx.mg.utils.ThirdUtils;
import com.library.common.utils.StringUtil;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.core.BottomPopupView;
import com.tencent.qcloud.uikit.business.chat.model.ElemExtModel;

import java.util.ArrayList;

public class ShareDialog extends BottomPopupView {

    public enum ShareType {
        image, web
    }

    public String imageUrl;
    private String title;
    private String desc;
    private String webUrl;
    private ShareType shareType;
    private String cId;
    private String newsId;
    private String messageTyp;
    private String content;
    private String shareTitle;
    private ArrayList<String> imgs;
    private ArrayList<String> images;
    private String typeName;
    private String img;
    private String newsTitle;
    private Context mContext;

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_share;
    }

    public void setShareParams(String title, String desc, String imageUrl, String webUel) {
        this.title = title;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.webUrl = webUel;
    }

    /**
     * 来自黄页的分享
     *
     * @param title
     * @param desc
     * @param imageUrl
     * @param webUel
     * @param cId
     * @param messageType
     * @param shareTitle
     * @param imgs
     */
    public void setShareParams(String title, String desc, String imageUrl, String webUel, String cId, String messageType, String shareTitle, ArrayList<String> imgs) {
        this.title = title;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.webUrl = webUel;
        this.cId = cId;
        this.messageTyp = messageType;
        this.shareTitle = shareTitle;
        this.imgs = imgs;
    }

    /**
     * 来自新闻分享
     *
     * @param title
     * @param desc
     * @param imageUrl
     * @param webUel
     * @param newsId
     * @param messageType
     * @param newsTitle
     * @param img
     */
    public void setShareParams(String title, String desc, String imageUrl, String webUel, String newsId, String messageType, String newsTitle, String img) {
        this.title = title;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.webUrl = webUel;
        this.newsId = newsId;
        this.messageTyp = messageType;
        this.newsTitle = newsTitle;
        this.img = img;
    }

    /**
     * 来自同城的分享
     *
     * @param title
     * @param desc
     * @param imageUrl
     * @param webUel
     * @param cId
     * @param messageType
     * @param content
     * @param typeName
     * @param images
     */
    public void setShareParams(String title, String desc, String imageUrl, String webUel, String cId, String messageType, String content, String typeName, ArrayList<String> images) {
        this.title = title;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.webUrl = webUel;
        this.cId = cId;
        this.typeName = typeName;
        this.messageTyp = messageType;
        this.content = content;
        this.images = images;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setShareType(ShareType shareType) {
        this.shareType = shareType;
    }

    public ShareDialog(@NonNull final Context context) {
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
                        if (StringUtil.isEmpty(webUrl)) {
                            AlertDialog dialog = new AlertDialog.Builder(mContext)
                                    .setMessage(R.string.no_invite_code)
                                    .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                                        mContext.startActivity(InviteActivity.newInstance(mContext));
                                        dialogInterface.dismiss();
                                    })
                                    .setNegativeButton(R.string.no_thank_you, (dialogInterface, i) -> dialogInterface.dismiss()).create();
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

        findViewById(R.id.tvShareCircle).setOnClickListener(v -> {
            dismiss();
            switch (shareType) {
                case web:
                    RepositoryFactory.getShareApi().wechareShareWeb(PlatformType.CIRCLE, title, desc, imageUrl, webUrl, shareCallback);
                    break;
                case image:
                    RepositoryFactory.getShareApi().wechatShareImage(PlatformType.CIRCLE, imageUrl, shareCallback);
                    break;
            }

        });

        findViewById(R.id.tvShareFacebook).setOnClickListener(v -> {
            dismiss();
            if (ThirdUtils.isFacebookAvilible(getContext())){
                switch (shareType) {
                    case web:
                        RepositoryFactory.getShareApi().fackbookShareWeb(null, title, desc, imageUrl, webUrl, shareCallback);
                        break;
                    case image:
                        RepositoryFactory.getShareApi().fackbookShareImage(null, imageUrl, shareCallback);
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

        });
        findViewById(R.id.tvCancel).setOnClickListener(v -> dismiss());

        TextView tvShareChat = findViewById(R.id.tvShareChat);
        //MG聊天
        tvShareChat.setOnClickListener(view -> {
            dismiss();
            if (StringUtil.equals(ElemExtModel.SHARE_CITY, messageTyp)) {
                view.getContext().startActivity(FriendsSelectActivity.newIntent(view.getContext(),
                        typeName, images, cId, content, messageTyp));
            } else if (StringUtil.equals(ElemExtModel.SHARE_NEWS, messageTyp)) {
                view.getContext().startActivity(FriendsSelectActivity.newIntent(view.getContext(),
                        newsId, messageTyp, newsTitle, img));
            } else if (StringUtil.equals(ElemExtModel.SHARE_YELLOWPAGE, messageTyp)) {
                view.getContext().startActivity(FriendsSelectActivity.newIntent(view.getContext(),
                        cId, messageTyp, shareTitle, imgs));
            }

        });
        TextView tvShareFriendsMoment = findViewById(R.id.tvShareFriendsMoment);
        //MG朋友圈
        tvShareFriendsMoment.setOnClickListener(view -> {
            dismiss();
            if (StringUtil.equals(ElemExtModel.SHARE_CITY, messageTyp)) {
                view.getContext().startActivity(NewMomentsActivity.newIntent(view.getContext(),
                        cId, messageTyp, images, typeName, content));
            } else if (StringUtil.equals(ElemExtModel.SHARE_NEWS, messageTyp)) {
                view.getContext().startActivity(NewMomentsActivity.newIntent(view.getContext(),
                        newsId, messageTyp, newsTitle, img));
            } else if (StringUtil.equals(ElemExtModel.SHARE_YELLOWPAGE, messageTyp)) {
                view.getContext().startActivity(NewMomentsActivity.newIntent(view.getContext(),
                        cId, messageTyp, shareTitle, imgs));
            }
//                view.getContext().startActivity(NewMomentsActivity.newIntent(view.getContext(), cId, newsId, messageTyp, content, shareTitle, imgs, type));
        });
    }

    public ShareCallback shareCallback = new ShareCallback() {
        @Override
        public void onSucces() {

        }
    };
}
