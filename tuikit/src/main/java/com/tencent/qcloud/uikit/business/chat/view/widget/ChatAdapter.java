package com.tencent.qcloud.uikit.business.chat.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.library.common.constant.IntentConstants;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.LogTUtil;
import com.library.common.utils.SharedPreferencesUtil;
import com.library.common.utils.StringUtil;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMFaceElem;
import com.tencent.imsdk.TIMFileElem;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMImage;
import com.tencent.imsdk.TIMImageElem;
import com.tencent.imsdk.TIMImageType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMSnapshot;
import com.tencent.imsdk.TIMSoundElem;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMVideo;
import com.tencent.imsdk.TIMVideoElem;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.imsdk.log.QLog;
import com.tencent.qcloud.uikit.R;
import com.tencent.qcloud.uikit.TUIKit;
import com.tencent.qcloud.uikit.api.chat.IChatAdapter;
import com.tencent.qcloud.uikit.api.chat.IChatProvider;
import com.tencent.qcloud.uikit.business.chat.c2c.TransReceiveListener;
import com.tencent.qcloud.uikit.business.chat.model.CacheUtil;
import com.tencent.qcloud.uikit.business.chat.model.ElemExtModel;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfo;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfoUtil;
import com.tencent.qcloud.uikit.business.chat.view.ChatListView;
import com.tencent.qcloud.uikit.business.session.view.SessionIconView;
import com.tencent.qcloud.uikit.common.BackgroundTasks;
import com.tencent.qcloud.uikit.common.UIKitConstants;
import com.tencent.qcloud.uikit.common.component.audio.UIKitAudioArmMachine;
import com.tencent.qcloud.uikit.common.component.face.FaceManager;
import com.tencent.qcloud.uikit.common.component.picture.imageEngine.impl.GlideEngine;
import com.tencent.qcloud.uikit.common.component.video.VideoViewActivity;
import com.tencent.qcloud.uikit.common.utils.DateTimeUtil;
import com.tencent.qcloud.uikit.common.utils.FileUtil;
import com.tencent.qcloud.uikit.common.utils.UIUtils;
import com.tencent.qcloud.uikit.common.widget.photoview.PhotoViewActivity;
import com.tencent.qcloud.uikit.util.FriendShipUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class ChatAdapter extends IChatAdapter {

    private TransReceiveListener transReceiveListener;

    private boolean mLoading = true;
    private ChatListView mRecycleView;
    private List<MessageInfo> mDataSource = new ArrayList<>();

    private ChatListEvent mListEvent;
    private MessageInterceptor mInterceptor;
    private Context mContext;
    private boolean mState;

    public void setChatListEvent(ChatListEvent mListEvent) {
        this.mListEvent = mListEvent;
    }

    public void setEditor(MessageInterceptor interceptor) {
        this.mInterceptor = interceptor;
    }

    private static final int width = UIUtils.getPxByDp(100);
    private static final int height = UIUtils.getPxByDp(160);
    private static final int normal = UIUtils.getPxByDp(120);

    private static final int audio_min_width = UIUtils.getPxByDp(60);
    private static final int audio_max_width = UIUtils.getPxByDp(200);
    private static final int headerViewType = -99;
    private static List<String> downloadEles = new ArrayList();


    public void setTransReceiveListener(TransReceiveListener transReceiveListener) {
        this.transReceiveListener = transReceiveListener;
    }

    public void isShowNickname(boolean state) {
        this.mState = state;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        if (viewType == headerViewType) {
            LayoutInflater inflater = LayoutInflater.from(TUIKit.getAppContext());
            return new HeaderViewHolder(inflater.inflate(R.layout.chat_adapter_load_more, parent, false));
        }

        LayoutInflater inflater = LayoutInflater.from(TUIKit.getAppContext());
        RecyclerView.ViewHolder holder = new ChatTextHolder(inflater.inflate(R.layout.chat_adapter_text, parent, false));
        switch (viewType) {
            case MessageInfo.MSG_TYPE_TEXT:
                holder = new ChatTextHolder(inflater.inflate(R.layout.chat_adapter_text, parent, false));
                break;
            case MessageInfo.MSG_TYPE_TEXT + 1:
                if (mRecycleView.isDivided())
                    holder = new ChatTextHolder(inflater.inflate(R.layout.chat_adapter_text_self, parent, false));
                else
                    holder = new ChatTextHolder(inflater.inflate(R.layout.chat_adapter_text, parent, false));
                break;
            case MessageInfo.MSG_TYPE_IMAGE:
            case MessageInfo.MSG_TYPE_VIDEO:
            case MessageInfo.MSG_TYPE_CUSTOM_FACE:
                holder = new ChatImageHolder(inflater.inflate(R.layout.chat_adapter_image, parent, false));
                break;
            case MessageInfo.MSG_TYPE_IMAGE + 1:
            case MessageInfo.MSG_TYPE_VIDEO + 1:
            case MessageInfo.MSG_TYPE_CUSTOM_FACE + 1:
                if (mRecycleView.isDivided())
                    holder = new ChatImageHolder(inflater.inflate(R.layout.chat_adapter_image_self, parent, false));
                else
                    holder = new ChatImageHolder(inflater.inflate(R.layout.chat_adapter_image, parent, false));
                break;
            case MessageInfo.MSG_TYPE_AUDIO:
                holder = new ChatAudioHolder(inflater.inflate(R.layout.chat_adapter_audio, parent, false));
                break;
            case MessageInfo.MSG_TYPE_AUDIO + 1:
                if (mRecycleView.isDivided())
                    holder = new ChatAudioHolder(inflater.inflate(R.layout.chat_adapter_audio_self, parent, false));
                else
                    holder = new ChatAudioHolder(inflater.inflate(R.layout.chat_adapter_audio, parent, false));

                break;
            case MessageInfo.MSG_TYPE_FILE:
                holder = new ChatFileHolder(inflater.inflate(R.layout.chat_adapter_file, parent, false));
                break;
            case MessageInfo.MSG_TYPE_FILE + 1:
                if (mRecycleView.isDivided())
                    holder = new ChatFileHolder(inflater.inflate(R.layout.chat_adapter_file_self, parent, false));
                else
                    holder = new ChatFileHolder(inflater.inflate(R.layout.chat_adapter_file, parent, false));
                break;

            case MessageInfo.MSG_TYPE_CUSTOM_TRANSFER:
                holder = new ChatTransferHolder((inflater.inflate(R.layout.chat_adapter_transfer, parent, false)));

                break;
            case MessageInfo.MSG_TYPE_CUSTOM_TRANSFER + 1:
                holder = new ChatTransferHolder((inflater.inflate(R.layout.chat_adapter_transfer_self, parent, false)));
                break;

            case MessageInfo.MSG_TYPE_CUSTOM_RED_PACKET:
                holder = new ChatTransferHolder((inflater.inflate(R.layout.chat_adapter_transfer_red_packet, parent, false)));

                break;
            case MessageInfo.MSG_TYPE_CUSTOM_RED_PACKET + 1:
                holder = new ChatTransferHolder((inflater.inflate(R.layout.chat_adapter_redpacket_self, parent, false)));
                break;
            case MessageInfo.MSG_TYPE_NEARBY_CITY://别人发送的分享信息
                holder = new ShareHolder(inflater.inflate(R.layout.chat_adapter_shared, parent, false));
                break;
            case MessageInfo.MSG_TYPE_NEARBY_CITY + 1://我发送的分享信息
                holder = new ShareHolder(inflater.inflate(R.layout.chat_adapter_shared_self, parent, false));
                break;
        }

        if (viewType == MessageInfo.MSG_TYPE_CUSTOM_TRANSFER_TIP || viewType == MessageInfo.MSG_TYPE_CUSTOM_TRANSFER_TIP + 1) {
            holder = new ChatCustomerTipsHolder((inflater.inflate(R.layout.chat_adapter_customer_tips, parent, false)));
        } else if (viewType == MessageInfo.MSG_TYPE_ADD_FRIEND_TIPS || viewType == MessageInfo.MSG_TYPE_ADD_FRIEND_TIPS + 1) {
            holder = new AddFriendTextHolder(inflater.inflate(R.layout.chat_adapter_add_friend_text, parent, false));
        } else if (viewType >= MessageInfo.MSG_TYPE_TIPS && viewType < MessageInfo.MSG_TYPE_ADD_FRIEND_TIPS) {
            holder = new ChatTipsHolder(inflater.inflate(R.layout.chat_adapter_tips, parent, false));
        }


        return holder;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (position == 0) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            if (mLoading) {
                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                holder.itemView.setVisibility(View.VISIBLE);
            } else {
                holder.itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            holder.itemView.setLayoutParams(param);
            return;
        }


        final MessageInfo msg = getItem(position);
        final TIMMessage timMsg = msg.getTIMMessage();

        final BaseChatHolder chatHolder = (BaseChatHolder) holder;
        if (mRecycleView.getChatTimeBubble() != null) {
            chatHolder.chatTime.setBackground(mRecycleView.getChatTimeBubble());
        }
        if (mRecycleView.getChatTimeColor() != 0) {
            chatHolder.chatTime.setTextColor(mRecycleView.getChatTimeColor());
        }
        if (mRecycleView.getChatTimeSize() != 0) {
            chatHolder.chatTime.setTextSize(mRecycleView.getChatTimeSize());
        }


        if (position > 1) {
            TIMMessage last = getItem(position - 1).getTIMMessage();
            if (last != null) {
                if (timMsg.timestamp() - last.timestamp() >= 5 * 60) {
                    chatHolder.chatTime.setVisibility(View.VISIBLE);
                    chatHolder.chatTime.setText(DateTimeUtil.getTimeFormatText(new Date(timMsg.timestamp() * 1000)));
                } else {
                    chatHolder.chatTime.setVisibility(View.GONE);
                }
            }
        } else {
            chatHolder.chatTime.setVisibility(View.VISIBLE);
            chatHolder.chatTime.setText(DateTimeUtil.getTimeFormatText(new Date(timMsg.timestamp() * 1000)));
        }

        if (msg.getMsgType() == MessageInfo.MSG_TYPE_ADD_FRIEND_TIPS
                || msg.getMsgType() == MessageInfo.MSG_TYPE_ADD_FRIEND_TIPS + 1) {
            AddFriendTextHolder addFriendHolder = (AddFriendTextHolder) holder;
            final TIMUserProfile meProfile = CacheUtil.getInstance().getTIMUser();
            chatHolder.dataView.setBackgroundResource(R.drawable.chat_others);
            if (msg.isSelf() && TextUtils.equals(meProfile.getIdentifier(), msg.getFromUser())) {
                String otherImageUrl = CacheUtil.getInstance().userAvatar(msg.getPeer());
                try {
                    CommonImageLoader.load(URLDecoder.decode(otherImageUrl, "UTF-8"))
                            .placeholder(R.drawable.default_user_image)
                            .error(R.drawable.default_user_image)
                            .into(chatHolder.userIcon);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    CommonImageLoader.load(otherImageUrl)
                            .placeholder(R.drawable.default_user_image)
                            .error(R.drawable.default_user_image)
                            .into(chatHolder.userIcon);
                }
                addFriendHolder.msg.setText(mContext.getString(R.string.we_are_friend_we_can_chat));
            } else {
                String otherImageUrl = CacheUtil.getInstance().userAvatar(msg.getFromUser());
                try {
                    CommonImageLoader.load(URLDecoder.decode(otherImageUrl, "UTF-8"))
                            .placeholder(R.drawable.default_user_image)
                            .error(R.drawable.default_user_image)
                            .into(chatHolder.userIcon);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    CommonImageLoader.load(otherImageUrl)
                            .placeholder(R.drawable.default_user_image)
                            .error(R.drawable.default_user_image)
                            .into(chatHolder.userIcon);
                }
//                CommonImageLoader.load(otherImageUrl).placeholder(R.drawable.default_user_image).into(chatHolder.userIcon);
                addFriendHolder.msg.setText(mContext.getString(R.string.we_are_friend_we_can_chat));
            }

            chatHolder.userIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent("mg_NewImUserDetailActivity");
                    if (msg.isSelf() && TextUtils.equals(meProfile.getIdentifier(), msg.getFromUser())) {
                        intent.putExtra("imUserId", msg.getPeer());
                    } else {
                        intent.putExtra("imUserId", msg.getFromUser());
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
//                    mListEvent.onUserIconClick(v, position, msg);
                }
            });

            return;
        } else if (msg.getMsgType() >= MessageInfo.MSG_TYPE_TIPS) {
            ChatTipsHolder tipsHolder = (ChatTipsHolder) holder;
            if (mRecycleView.getTipsMessageBubble() != null) {
                tipsHolder.tips.setBackground(mRecycleView.getTipsMessageBubble());
            }
            if (mRecycleView.getTipsMessageColor() != 0) {
                tipsHolder.tips.setTextColor(mRecycleView.getTipsMessageColor());
            }
            if (mRecycleView.getTipsMessageSize() != 0) {
                tipsHolder.tips.setTextSize(mRecycleView.getTipsMessageSize());
            }

            if (msg.getStatus() == MessageInfo.MSG_STATUS_REVOKE) {
                if (msg.isSelf())
                    tipsHolder.tips.setText(mContext.getString(R.string.your_withdrew_message));
                else if (msg.isGroup()) {
                    tipsHolder.tips.setText(msg.getFromUser() + mContext.getString(R.string.withdrew_message));
                } else {
                    tipsHolder.tips.setText(mContext.getString(R.string.other_withdrew_message));
                }

            } else if (msg.getMsgType() >= MessageInfo.MSG_TYPE_GROUP_CREATE && msg.getMsgType() <= MessageInfo.MSG_TYPE_GROUP_MODIFY_FACE_URL) {
                if (msg.getExtra() != null)
                    tipsHolder.tips.setText(msg.getExtra().toString());
            }
            return;
        }
        if (msg.getMsgType() == MessageInfo.MSG_TYPE_CUSTOM_TRANSFER_TIP || msg.getMsgType() == MessageInfo.MSG_TYPE_CUSTOM_TRANSFER_TIP + 1) {
            ChatCustomerTipsHolder tipsHolder = (ChatCustomerTipsHolder) chatHolder;
            final TIMCustomElem elem = (TIMCustomElem) msg.getTIMMessage().getElement(0);
            String data = new String(elem.getData());
            final ElemExtModel dataModel = JsonUtil.strToModel(data, ElemExtModel.class);
            String type = dataModel.getMessageType();
//            if (TextUtils.equals(type, MessageInfoUtil.TRANSFER_ACCOUNT_RECEIVED)) {//原本转账的type
            if (TextUtils.equals(type, MessageInfoUtil.TRANSFER_ACCOUNT_NEW_RECEIVED)) {
                if (msg.isSelf()) {
//                    tipsHolder.tips.setText(mContext.getString(R.string.you_accepted_the_transfer));//.concat(msg.getFromUser()).concat("的转账"));
                    tipsHolder.tips.setText(mContext.getString(R.string.your_request_trans));//.concat(msg.getFromUser()).concat("的转账"));
                } else {
//                    tipsHolder.tips.setText(mContext.getString(R.string.your_transfer_was_accepted));
                    tipsHolder.tips.setText(mContext.getString(R.string.your_response_trans));
//                    notifyPacketOrTransferData(dataModel.getTransferId());
                }
                tipsHolder.tips.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent("com.fjx.BillRecord2Activity");
                        //intent.putExtra(IntentConstants.TYPE, "7");
                        mContext.startActivity(intent);
                    }
                });
            } else {
                if (msg.isGroup()) {
                    if (msg.isSelf()) {
                        tipsHolder.tips.setText(mContext.getString(R.string.your_receiver_red_packet));
                    } else {
                        if (msg.getFromUser().contains("fjx") || msg.getFromUser().contains("mg")) {
                            TIMUserProfile timUserProfile = TIMFriendshipManager.getInstance().queryUserProfile(msg.getFromUser());
                            if (timUserProfile != null) {
                                String nickName = timUserProfile.getNickName();
                                if (!TextUtils.isEmpty(nickName)) {
                                    msg.setFromUser(nickName);
                                }
                            }
                        }
                        if (!TextUtils.isEmpty(dataModel.getSendUserName()) &&
                                !TextUtils.isEmpty(dataModel.getReceiveUserName())) {
                            if (dataModel.getSendUserId().equals(TIMManager.getInstance().getLoginUser())) {
                                tipsHolder.tips.setText(dataModel.getReceiveUserName().concat(mContext.getString(R.string.have_receiver)).concat(mContext.getString(R.string.your_red_packet)));
                            } else {
                                tipsHolder.tips.setText(dataModel.getReceiveUserName().concat(mContext.getString(R.string.have_receiver)).concat(dataModel.getSendUserName()).concat(mContext.getString(R.string.rp)));
                            }
                        } else {
                            tipsHolder.tips.setText(msg.getFromUser().concat(mContext.getString(R.string.have_receiver)));
                        }
                    }
//                    notifyPacketOrTransferData(dataModel.getTransferId());
                } else {
                    if (msg.isSelf()) {
                        tipsHolder.tips.setText(mContext.getString(R.string.you_accepted_the_red_package));
                    } else {
                        tipsHolder.tips.setText(mContext.getString(R.string.your_accepted_was_red_package));
//                    notifyPacketOrTransferData(dataModel.getTransferId());
                    }
                }

            }
            return;
        }
        if (msg.isSelf()) {
            if (chatHolder.dataView != null) {
                if (mRecycleView.getSelfBubble() != null) {
                    chatHolder.dataView.setBackground(mRecycleView.getSelfBubble());
                } else {
                    chatHolder.dataView.setBackgroundResource(R.drawable.chat_self);
                }
            }

            final String myImageUrl = CacheUtil.getInstance().getTIMUser().getFaceUrl();

            if (chatHolder.userIcon.getTag() == null) {
                if (TextUtils.isEmpty(myImageUrl)) {
                    chatHolder.userIcon.setImageResource(R.drawable.default_user_image);
                } else {
                    try {
//                        Observable.create(new ObservableOnSubscribe<Bitmap>() {
//                            @Override
//                            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Bitmap> e) throws Exception {
//                                Bitmap bitmap = CommonImageLoader.getBitmapByUrl(myImageUrl);
//                                e.onNext(bitmap);
//                            }
//                        }).observeOn(AndroidSchedulers.mainThread())
//                                .subscribeOn(Schedulers.io())
//                                .subscribe(new Consumer<Bitmap>() {
//                                    @Override
//                                    public void accept(@io.reactivex.annotations.NonNull Bitmap bitmap) throws Exception {
//                                        if (bitmap == null) {
//                                            CommonImageLoader.load(myImageUrl).round().into(chatHolder.userIcon);
//                                            return;
//                                        }
//                                        chatHolder.userIcon.setImageBitmap(bitmap);
//                                    }
//                                });
                        CommonImageLoader.load(myImageUrl)
                                .error(R.drawable.default_user_image)
                                .placeholder(R.drawable.default_user_image)
                                .round()
                                .into(chatHolder.userIcon);
                        chatHolder.userIcon.setTag("myImageUrl");
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                }
            }
        } else {

            if (chatHolder.userIcon.getTag() == null) {
                final String otherImageUrl = CacheUtil.getInstance().userAvatar(msg.getFromUser());
                if (TextUtils.isEmpty(otherImageUrl)) {
                    chatHolder.userIcon.setImageResource(R.drawable.default_user_image);
                } else {
                    Observable.create(new ObservableOnSubscribe<Bitmap>() {
                        @Override
                        public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Bitmap> e) throws Exception {
                            Bitmap bitmap = CommonImageLoader.getBitmapByUrl(otherImageUrl);
                            if (bitmap != null)
                                e.onNext(bitmap);
                        }
                    }).observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Consumer<Bitmap>() {
                                @Override
                                public void accept(@io.reactivex.annotations.NonNull Bitmap bitmap) throws Exception {
                                    if (bitmap == null) {
                                        CommonImageLoader.load(otherImageUrl)
                                                .placeholder(R.drawable.default_user_image)
                                                .error(R.drawable.default_user_image)
                                                .round()
                                                .into(chatHolder.userIcon);
                                        return;
                                    }
                                    chatHolder.userIcon.setImageBitmap(bitmap);
                                }
                            });
                    chatHolder.userIcon.setTag(otherImageUrl);
                }
            }


            if (chatHolder.dataView != null) {
                if (mRecycleView.getOppositeBubble() != null) {
                    chatHolder.dataView.setBackground(mRecycleView.getOppositeBubble());
                } else {
                    chatHolder.dataView.setBackgroundResource(R.drawable.chat_others);
                }
            }
        }

        if (mListEvent != null) {
            chatHolder.contentGroup.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mListEvent.onMessageLongClick(v, position, msg);
                    return true;
                }
            });

            chatHolder.userIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (msg.isSelf()) return;
                    mListEvent.onUserIconClick(v, position, msg);
                }
            });
        }


//        if (mRecycleView.getUserChatIcon() != null) {
//            chatHolder.userIcon.setDynamicChatIconView(mRecycleView.getUserChatIcon());
//        }
//        chatHolder.userIcon.invokeInformation(msg);
//        chatHolder.userIcon.setDefaultImageResId(R.drawable.default_user_image);


        switch (getItemViewType(position)) {
            case MessageInfo.MSG_TYPE_TEXT:
            case MessageInfo.MSG_TYPE_TEXT + 1:
                ChatTextHolder msgHolder = (ChatTextHolder) chatHolder;
                msgHolder.msg.setVisibility(View.VISIBLE);
                if (timMsg.getElement(0) instanceof TIMTextElem) {
                    TIMTextElem textElem = (TIMTextElem) timMsg.getElement(0);
                    FaceManager.handlerEmojiText(msgHolder.msg, textElem.getText());
                }
                if (mRecycleView.getContextSize() != 0) {
                    msgHolder.msg.setTextSize(mRecycleView.getContextSize());
                }
                if (msg.isSelf()) {
                    if (mRecycleView.getSelfContentColor() != 0) {
                        msgHolder.msg.setTextColor(mRecycleView.getSelfContentColor());
                    }
                } else {
                    if (mRecycleView.getOppositeContentColor() != 0) {
                        msgHolder.msg.setTextColor(mRecycleView.getOppositeContentColor());
                    }
                }
                break;
            case MessageInfo.MSG_TYPE_VIDEO:
            case MessageInfo.MSG_TYPE_VIDEO + 1:
            case MessageInfo.MSG_TYPE_IMAGE:
            case MessageInfo.MSG_TYPE_IMAGE + 1:
            case MessageInfo.MSG_TYPE_CUSTOM_FACE:
            case MessageInfo.MSG_TYPE_CUSTOM_FACE + 1:
                final ChatImageHolder imgHolder = (ChatImageHolder) chatHolder;
                imgHolder.imgData.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params;
                //自定义表情的比较特殊，优先处理
                if (msg.getMsgType() == MessageInfo.MSG_TYPE_CUSTOM_FACE) {
                    imgHolder.cover.setVisibility(View.GONE);
                    imgHolder.playBtn.setVisibility(View.GONE);
                    imgHolder.mDuration.setVisibility(View.GONE);
                    params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.CENTER_IN_PARENT);
                    imgHolder.imgData.setLayoutParams(params);
                    if (timMsg.getElementCount() > 0) {
                        TIMFaceElem faceEle = (TIMFaceElem) timMsg.getElement(0);
                        imgHolder.imgData.setImageBitmap(FaceManager.getCustomBitmap(faceEle.getIndex(), new String(faceEle.getData())));
                    }
                    break;
                }

                if (msg.getImgWithd() < 200 && msg.getImgHeight() < 200) {
                    params = new RelativeLayout.LayoutParams(normal, normal);
                } else if (msg.getImgWithd() > msg.getImgHeight()) {
                    params = new RelativeLayout.LayoutParams(height, width);
                } else {
                    params = new RelativeLayout.LayoutParams(width, height);
                }
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                imgHolder.imgData.setLayoutParams(params);

                if (msg.getMsgType() == MessageInfo.MSG_TYPE_IMAGE) {
                    imgHolder.cover.setVisibility(View.GONE);
                    imgHolder.playBtn.setVisibility(View.GONE);
                    imgHolder.mDuration.setVisibility(View.GONE);
                    final TIMImageElem imageEle = (TIMImageElem) timMsg.getElement(0);
                    final List<TIMImage> imgs = imageEle.getImageList();
                    if (!TextUtils.isEmpty(msg.getDataPath())) {
                        GlideEngine.loadImage(imgHolder.imgData, msg.getDataPath(), null);
                    } else {
                        for (int i = 0; i < imgs.size(); i++) {
                            final TIMImage img = imgs.get(i);
                            if (img.getType() == TIMImageType.Thumb) {
                                synchronized (downloadEles) {
                                    if (downloadEles.contains(img.getUuid()))
                                        break;
                                    downloadEles.add(img.getUuid());
                                }
                                final String path = UIKitConstants.IMAGE_DOWNLOAD_DIR + img.getUuid();
                                img.getImage(path, new TIMCallBack() {
                                    @Override
                                    public void onError(int code, String desc) {
                                        downloadEles.remove(img.getUuid());
                                        QLog.e("ChatAdapter img getImage", code + ":" + desc);
                                    }

                                    @Override
                                    public void onSuccess() {
                                        downloadEles.remove(img.getUuid());
                                        msg.setDataPath(path);
                                        GlideEngine.loadImage(imgHolder.imgData, msg.getDataPath(), null);
                                    }
                                });
                                break;
                            }
                        }
                    }

                    imgHolder.contentGroup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int i = 0; i < imgs.size(); i++) {
                                TIMImage img = imgs.get(i);
                                if (img.getType() == TIMImageType.Original) {
                                    PhotoViewActivity.mCurrentOriginalImage = img;
                                    break;
                                }
                            }
                            Intent intent = new Intent(TUIKit.getAppContext(), PhotoViewActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(UIKitConstants.IMAGE_DATA, msg.getDataPath());
                            intent.putExtra(UIKitConstants.SELF_MESSAGE, msg.isSelf());
                            TUIKit.getAppContext().startActivity(intent);
                        }
                    });


                } else if (msg.getMsgType() == MessageInfo.MSG_TYPE_VIDEO) {
                    imgHolder.playBtn.setVisibility(View.VISIBLE);
                    imgHolder.cover.setLayoutParams(params);
                    imgHolder.mDuration.setVisibility(View.VISIBLE);
                    final TIMVideoElem videoEle = (TIMVideoElem) timMsg.getElement(0);
                    final TIMVideo video = videoEle.getVideoInfo();

                    if (!TextUtils.isEmpty(msg.getDataPath())) {
                        GlideEngine.loadImage(imgHolder.imgData, msg.getDataPath(), null);
                    } else {
                        final TIMSnapshot shotInfo = videoEle.getSnapshotInfo();
                        synchronized (downloadEles) {

                            if (downloadEles.contains(shotInfo.getUuid()))
                                break;
                            downloadEles.add(shotInfo.getUuid());
                        }

                        final String path = UIKitConstants.IMAGE_DOWNLOAD_DIR + videoEle.getSnapshotInfo().getUuid();
                        videoEle.getSnapshotInfo().getImage(path, new TIMCallBack() {
                            @Override
                            public void onError(int code, String desc) {
                                downloadEles.remove(shotInfo.getUuid());
                                QLog.e("ChatAdapter video getImage", code + ":" + desc);
                            }

                            @Override
                            public void onSuccess() {
                                downloadEles.remove(shotInfo.getUuid());
                                msg.setDataPath(path);
                                GlideEngine.loadImage(imgHolder.imgData, msg.getDataPath(), null);
                            }
                        });
                    }

                    String durations = "00:" + video.getDuaration();
                    if (video.getDuaration() < 10)
                        durations = "00:0" + video.getDuaration();
                    imgHolder.mDuration.setText(durations);
                    if (msg.isSelf()) {
                        imgHolder.cover.setVisibility(View.GONE);
                        registerVideoPlayClickListener(imgHolder.contentGroup, msg);
                    } else {

                        final String videoPath = UIKitConstants.VIDEO_DOWNLOAD_DIR + video.getUuid();
                        File videoFile = new File(videoPath);
                        //如果视频还未下载
                        if (!videoFile.exists()) {
                            imgHolder.cover.setVisibility(View.VISIBLE);
                            imgHolder.contentGroup.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ((BaseChatHolder) holder).progress.setVisibility(View.VISIBLE);
                                    video.getVideo(videoPath, new TIMCallBack() {
                                        @Override
                                        public void onError(int code, String desc) {
                                            UIUtils.toastLongMessage("下载视频失败:" + code + "=" + desc);
                                            ((BaseChatHolder) holder).progress.setVisibility(View.GONE);
                                            msg.setStatus(MessageInfo.MSG_STATUS_DOWNLOADED);
                                        }

                                        @Override
                                        public void onSuccess() {
                                            ((BaseChatHolder) holder).progress.setVisibility(View.GONE);
                                            registerVideoPlayClickListener(imgHolder.contentGroup, msg);
                                            imgHolder.cover.setVisibility(View.GONE);
                                            //下载完后自动播放
                                            Intent intent = new Intent(TUIKit.getAppContext(), VideoViewActivity.class);
                                            intent.putExtra(UIKitConstants.CAMERA_IMAGE_PATH, msg.getDataPath());
                                            intent.putExtra(UIKitConstants.CAMERA_VIDEO_PATH, msg.getDataUri());
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                                            TUIKit.getAppContext().startActivity(intent);
                                        }
                                    });
                                }
                            });
                        } else {
                            registerVideoPlayClickListener(imgHolder.contentGroup, msg);
                        }
                    }
                }
                break;

            case MessageInfo.MSG_TYPE_AUDIO:
            case MessageInfo.MSG_TYPE_AUDIO + 1:
                final ChatAudioHolder audioHolder = (ChatAudioHolder) chatHolder;
                audioHolder.imgPlay.setImageResource(R.drawable.voice_msg_playing_3);
                final TIMSoundElem soundElem = (TIMSoundElem) msg.getTIMMessage().getElement(0);
                int duration = (int) soundElem.getDuration();
                if (duration == 0)
                    duration = 1;
                LinearLayout.LayoutParams audioParams = (LinearLayout.LayoutParams) chatHolder.dataView.getLayoutParams();
                audioParams.width = audio_min_width + UIUtils.getPxByDp(duration * 10);
                if (audioParams.width > audio_max_width)
                    audioParams.width = audio_max_width;
                // chatHolder.dataView.setLayoutParams(audioParams);
                audioHolder.time.setText(duration + "''");
                audioHolder.contentGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (UIKitAudioArmMachine.getInstance().isPlayingRecord()) {
                            UIKitAudioArmMachine.getInstance().stopPlayRecord();
                            return;
                        }

                        if (TextUtils.isEmpty(msg.getDataPath())) {
                            UIUtils.toastLongMessage("语音文件还未下载完成");
                            return;
                        }
                        audioHolder.imgPlay.setImageResource(R.drawable.play_voice_message);
                        final AnimationDrawable animationDrawable = (AnimationDrawable) audioHolder.imgPlay.getDrawable();
                        animationDrawable.start();
                        UIKitAudioArmMachine.getInstance().playRecord(msg.getDataPath(), new UIKitAudioArmMachine.AudioPlayCallback() {
                            @Override
                            public void playComplete() {
                                audioHolder.imgPlay.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        animationDrawable.stop();
                                        audioHolder.imgPlay.setImageResource(R.drawable.voice_msg_playing_3);
                                    }
                                });
                            }
                        });


                    }
                });
                break;

            case MessageInfo.MSG_TYPE_FILE:
            case MessageInfo.MSG_TYPE_FILE + 1:
                final ChatFileHolder fileHolder = (ChatFileHolder) chatHolder;
                final TIMFileElem fileElem = (TIMFileElem) msg.getTIMMessage().getElement(0);
                final String path = msg.getDataPath();
                fileHolder.fileName.setText(fileElem.getFileName());
                String size = FileUtil.FormetFileSize(fileElem.getFileSize());
                fileHolder.fileSize.setText(size);
                fileHolder.contentGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UIUtils.toastLongMessage("文件路径:" + path);
                    }
                });
                if (msg.isSelf()) {

                    if (msg.getStatus() == MessageInfo.MSG_STATUS_SENDING) {
                        fileHolder.fileStatus.setText(R.string.sending);
                    } else if (msg.getStatus() == MessageInfo.MSG_STATUS_SEND_SUCCESS || msg.getStatus() == MessageInfo.MSG_STATUS_NORMAL) {
                        fileHolder.fileStatus.setText(R.string.sended);
                    }


                } else {
                    if (msg.getStatus() == MessageInfo.MSG_STATUS_DOWNLOADING) {
                        fileHolder.fileStatus.setText(R.string.downloading);
                    } else if (msg.getStatus() == MessageInfo.MSG_STATUS_DOWNLOADED) {
                        fileHolder.fileStatus.setText(R.string.downloaded);
                    } else if (msg.getStatus() == MessageInfo.MSG_STATUS_UN_DOWNLOAD) {
                        fileHolder.fileStatus.setText(R.string.un_download);
                        fileHolder.contentGroup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                msg.setStatus(MessageInfo.MSG_STATUS_DOWNLOADING);
                                fileHolder.fileStatus.setText(R.string.downloading);
                                fileElem.getToFile(path, new TIMCallBack() {
                                    @Override
                                    public void onError(int code, String desc) {
                                        UIUtils.toastLongMessage("getToFile fail:" + code + "=" + desc);
                                        chatHolder.progress.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onSuccess() {
                                        //FileUtil.reNameFile(new File(path), fileElem.getFileName());
                                        msg.setDataPath(path);
                                        fileHolder.fileStatus.setText(R.string.downloaded);
                                        msg.setStatus(MessageInfo.MSG_STATUS_DOWNLOADED);
                                        chatHolder.progress.setVisibility(View.GONE);
                                        fileHolder.contentGroup.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                UIUtils.toastLongMessage("文件路径:" + path);

                                            }
                                        });
                                    }
                                });
                            }
                        });

                    }
                }
                break;
            case MessageInfo.MSG_TYPE_CUSTOM_TRANSFER:
            case MessageInfo.MSG_TYPE_CUSTOM_TRANSFER + 1:
                showTranferInfo(msg, (ChatTransferHolder) chatHolder);
                break;
            case MessageInfo.MSG_TYPE_CUSTOM_RED_PACKET:
            case MessageInfo.MSG_TYPE_CUSTOM_RED_PACKET + 1:
                showRedpacketInfo(msg, (ChatTransferHolder) chatHolder);
                break;
            case MessageInfo.MSG_TYPE_NEARBY_CITY:
            case MessageInfo.MSG_TYPE_NEARBY_CITY + 1:
                showShareInfo(msg, (ShareHolder) chatHolder);
                break;
        }


        if (mRecycleView.getNameColor() != 0) {
            chatHolder.userName.setTextColor(mRecycleView.getNameColor());
        }
        if (mRecycleView.getNameSize() != 0) {
            chatHolder.userName.setTextSize(mRecycleView.getNameSize());
        }

        if (msg.isGroup()) {
            if (mState) {
                if (msg.getFromUser().contains("fjx") || msg.getFromUser().contains("mg")) {
                    TIMUserProfile timUserProfile = TIMFriendshipManager.getInstance().queryUserProfile(msg.getFromUser());
                    if (timUserProfile != null) {
                        String nickName = timUserProfile.getNickName();
                        if (!TextUtils.isEmpty(nickName)) {
                            msg.setFromUser(nickName);
                        }
                    }
                }
                chatHolder.userName.setVisibility(View.VISIBLE);
                chatHolder.userName.setText(msg.getFromUser());
            } else {
                chatHolder.userName.setVisibility(View.GONE);
            }


        } else {
            chatHolder.userName.setVisibility(View.GONE);
        }
        if (msg.getStatus() == MessageInfo.MSG_STATUS_SEND_FAIL) {
            chatHolder.status.setVisibility(View.VISIBLE);
        } else {
            chatHolder.status.setVisibility(View.GONE);
        }

        if (msg.getStatus() == MessageInfo.MSG_STATUS_SENDING || msg.getStatus() == MessageInfo.MSG_STATUS_DOWNLOADING) {
            if (chatHolder.progress != null)
                chatHolder.progress.setVisibility(View.VISIBLE);
        } else {
            if (chatHolder.progress != null)
                chatHolder.progress.setVisibility(View.GONE);
        }
    }

    private void showShareInfo(final MessageInfo msg, ShareHolder chatHolder) {
        TIMCustomElem elem = (TIMCustomElem) msg.getTIMMessage().getElement(0);
        String data = new String(elem.getData());
        final ElemExtModel dataModel = JsonUtil.strToModel(data, ElemExtModel.class);
        Log.d("mgmData", JsonUtil.moderToString(dataModel));
//        if (dataModel.getImgs() != null && dataModel.getImgs().size() > 0) {
//            chatHolder.session_icon.setIcon(dataModel.getImgs().get(0));
//        }
//        chatHolder.tvTitle.setText(dataModel.getTitle());
        if (StringUtil.equals(ElemExtModel.SHARE_YELLOWPAGE, dataModel.getMessageType())) {
            chatHolder.tvType.setText(mContext.getString(R.string.yellowpage_share));
            if (dataModel.getImgs() != null && dataModel.getImgs().size() > 0) {
                chatHolder.session_icon.setIcon(dataModel.getImgs().get(0));
            }
            chatHolder.tvTitle.setText(dataModel.getTitle());
        } else if (StringUtil.equals(ElemExtModel.SHARE_NEWS, dataModel.getMessageType())) {
            chatHolder.tvType.setText(mContext.getString(R.string.news_share));
            if (StringUtil.isNotEmpty(dataModel.getImg())) {
                chatHolder.session_icon.setIcon(dataModel.getImg());
            }
            chatHolder.tvTitle.setText(dataModel.getNewsTitle());
        } else if (StringUtil.equals(ElemExtModel.SHARE_CITY, dataModel.getMessageType())) {
            chatHolder.tvType.setText(mContext.getString(R.string.nearby_city_share));
            if (dataModel.getImages() != null && dataModel.getImages().size() > 0) {
                chatHolder.session_icon.setIcon(dataModel.getImages().get(0));
            }
            chatHolder.tvTitle.setText(dataModel.getTypeName().concat(dataModel.getContent()));
        } else if (StringUtil.equals(ElemExtModel.SHARE_PERSONAL_CARD, dataModel.getMessageType())) {
            chatHolder.tvType.setText(mContext.getString(R.string.tim_psersonal_info));
            if (StringUtil.isNotEmpty(dataModel.getFaceUrl())) {
                chatHolder.session_icon.setIcon(dataModel.getFaceUrl());
            }
            chatHolder.tvTitle.setMaxLines(1);
            chatHolder.tvTitle.setEllipsize(TextUtils.TruncateAt.END);
            chatHolder.tvTitle.setText(dataModel.getName());
            if (StringUtil.isNotEmpty(dataModel.getIdentifier()))
                chatHolder.extra.setText(dataModel.getIdentifier());
        }

        chatHolder.rlItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtil.equals(ElemExtModel.SHARE_YELLOWPAGE, dataModel.getMessageType())) {
                    Intent intent = new Intent("mg_RechargeCenterActivityx");
                    intent.putExtra(IntentConstants.CID, dataModel.getcId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);
                } else if (StringUtil.equals(ElemExtModel.SHARE_NEWS, dataModel.getMessageType())) {
                    Intent intent = new Intent("mg_NewsDetailActivity");
                    intent.putExtra("cid", dataModel.getNewsId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);
                } else if (StringUtil.equals(ElemExtModel.SHARE_CITY, dataModel.getMessageType())) {
                    Intent intent = new Intent("com.fjx.action.NearbyCityActivity");
                    intent.putExtra(IntentConstants.CID, dataModel.getcId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);
                } else if (StringUtil.equals(ElemExtModel.SHARE_PERSONAL_CARD, dataModel.getMessageType())) {
                    String profile = dataModel.gettIMUserProfile();
                    TIMFriend friend = FriendShipUtil.getFriend(dataModel.getIdentifier());
                    if (friend == null) {
                        //非好友
                        Intent intent = new Intent("com.fjx.AddFriendActivity");
                        if (profile == null || TextUtils.equals("", profile)) {
                            intent.putExtra("userInfo", dataModel.getIdentifier());
                        } else {
                            intent.putExtra("userInfo", profile);
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);
                    } else {//好友
                        Intent intent = new Intent("mg_NewImUserDetailActivity");
                        intent.putExtra("imUserId", dataModel.getIdentifier());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);
                    }

                }
            }
        });
    }

    private void showRedpacketInfo(final MessageInfo msg, ChatTransferHolder chatHolder) {
        final List<String> IdList = CacheUtil.getInstance().getTransferMessageId();
        ChatTransferHolder redPacketHolder = chatHolder;
        final TIMCustomElem elem = (TIMCustomElem) msg.getTIMMessage().getElement(0);
        String data = new String(elem.getData());
        final ElemExtModel dataModel = JsonUtil.strToModel(data, ElemExtModel.class);

        final boolean isReceived = TextUtils.equals(dataModel.getMessageType(), MessageInfoUtil.TRANSFER_RED_PACKET_RECEIVED);
//        final String ext = new String(elem.getExt());
//        Map<String, Object> object = JsonUtil.jsonToMap(ext);
//        String money = dataModel.getMoney();//红包不直接显示金钱数量
        String remark = dataModel.getRemark();
        redPacketHolder.tvTransferRemark.setText(remark);
        if (msg.isGroup()) {
            if (msg.isSelf()) {
                redPacketHolder.imgData.setImageResource((IdList.contains(msg.getMsgId()) || isReceived) ? R.drawable.self_redpacket_get : R.drawable.self_redpacket_defaukt);
                redPacketHolder.tvTip.setText((IdList.contains(msg.getMsgId()) || isReceived) ? mContext.getString(R.string.already_received) : mContext.getString(R.string.click_receive));
                redPacketHolder.imgData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ((IdList.contains(msg.getMsgId()) || isReceived)) {
                            transReceiveListener.onClickTransferReceiver(msg, true);
                            return;
                        }
                        if (transReceiveListener == null) return;
                        transReceiveListener.onClickTransfer(msg, true);
                    }
                });
            } else {
//                redPacketHolder.tvTip.setText((IdList.contains(msg.getMsgId()) || isReceived) ? "已领取" : "点击领取");
//                redPacketHolder.imgData.setImageResource((IdList.contains(msg.getMsgId()) || isReceived) ? R.drawable.other_redpacket_get : R.drawable.other_redpacket_default);
                redPacketHolder.imgData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ((IdList.contains(msg.getMsgId()) || isReceived)) {
                            transReceiveListener.onClickTransferReceiver(msg, true);
                            return;
                        }
                        if (transReceiveListener == null) return;
                        transReceiveListener.onClickTransfer(msg, true);
                    }
                });
            }
        } else {
            if (msg.isSelf()) {
                redPacketHolder.imgData.setImageResource((IdList.contains(msg.getMsgId()) || isReceived) ? R.drawable.self_redpacket_get : R.drawable.self_redpacket_defaukt);
                redPacketHolder.tvTip.setText((IdList.contains(msg.getMsgId()) || isReceived) ? mContext.getString(R.string.already_received) : mContext.getString(R.string.click_receive));
                redPacketHolder.imgData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            } else {
                redPacketHolder.tvTip.setText((IdList.contains(msg.getMsgId()) || isReceived) ? mContext.getString(R.string.already_received) : mContext.getString(R.string.click_receive));
                redPacketHolder.imgData.setImageResource((IdList.contains(msg.getMsgId()) || isReceived) ? R.drawable.other_redpacket_get : R.drawable.other_redpacket_default);
                redPacketHolder.imgData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ((IdList.contains(msg.getMsgId()) || isReceived)) {
                            transReceiveListener.onClickTransferReceiver(msg, true);
                            return;
                        }
                        if (transReceiveListener == null) return;
                        transReceiveListener.onClickTransfer(msg, true);
//                    MessageInfo messageInfo = MessageInfoUtil.buildTransferCustomMessage(MessageInfoUtil.TRANSFER_RED_PACKET_RECEIVED, "");
//                    TIMMessage timMessage = new TIMMessage();
//                    dataModel.setMessageType(MessageInfoUtil.TRANSFER_RED_PACKET_RECEIVED);
//                    dataModel.setBeReceivedMessageId(msg.getMsgId());
//                    elem.setData(JsonUtil.moderToString(dataModel).getBytes());
//                    CacheUtil.getInstance().saveTransferMessageId(msg.getMsgId());
//                    timMessage.addElement(elem);
//                    messageInfo.setTIMMessage(timMessage);
//                    C2CChatManager.getInstance().sendC2CMessage(messageInfo, false, null);
//                    notifyDataSetChanged();
                    }
                });
            }
        }

    }

    private void showTranferInfo(final MessageInfo msg, ChatTransferHolder chatHolder) {

        final List<String> receiveIdList = CacheUtil.getInstance().getTransferMessageId();
        ChatTransferHolder transferHolder = chatHolder;
        final TIMCustomElem elem = (TIMCustomElem) msg.getTIMMessage().getElement(0);
        String data = new String(elem.getData());
        final ElemExtModel dataModel = JsonUtil.strToModel(data, ElemExtModel.class);

        String type = dataModel.getMessageType();
        final boolean isReceived = TextUtils.equals(type, MessageInfoUtil.TRANSFER_ACCOUNT_RECEIVED);

        String money = dataModel.getMoney().concat("AR");
        String remark = dataModel.getRemark();

        transferHolder.tvTransferMoney.setText(money);
        transferHolder.tvTransferRemark.setText(remark);

        if (msg.isSelf()) {
            transferHolder.imgData.setImageResource((receiveIdList.contains(msg.getMsgId()) || isReceived) ? R.drawable.self_transfer_get : R.drawable.self_transfer_default);
            transferHolder.imgData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        } else {

            transferHolder.imgData.setImageResource((receiveIdList.contains(msg.getMsgId()) || isReceived) ? R.drawable.other_transfer_get : R.drawable.other_transfer_default);
            transferHolder.imgData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((receiveIdList.contains(msg.getMsgId()) || isReceived)) {
                        transReceiveListener.onClickTransferReceiver(msg, false);
                        return;
                    }
                    if (transReceiveListener == null) return;
                    transReceiveListener.onClickTransfer(msg, false);
//                    MessageInfo messageInfo = MessageInfoUtil.buildTransferCustomMessage(MessageInfoUtil.TRANSFER_ACCOUNT_RECEIVED, "");
//                    TIMMessage timMessage = new TIMMessage();
//                    dataModel.setMessageType(MessageInfoUtil.TRANSFER_ACCOUNT_RECEIVED);
//                    dataModel.setBeReceivedMessageId(msg.getMsgId());
//                    elem.setData(JsonUtil.moderToString(dataModel).getBytes());
//                    CacheUtil.getInstance().saveTransferMessageId(msg.getMsgId());
//                    timMessage.addElement(elem);
//                    messageInfo.setTIMMessage(timMessage);
//                    C2CChatManager.getInstance().sendC2CMessage(messageInfo, false, null);
//                    notifyDataSetChanged();
                }
            });
        }
    }

    private void notifyPacketOrTransferData(MessageInfo messageInfo) {
        if (messageInfo.isSelf()) return;
        if (messageInfo.getMsgType() == MessageInfo.MSG_TYPE_CUSTOM_TRANSFER_TIP
                || messageInfo.getMsgType() == MessageInfo.MSG_TYPE_CUSTOM_TRANSFER_TIP + 1) {
//            final TIMCustomElem elem = (TIMCustomElem) messageInfo.getTIMMessage().getElement(0);
//            String data = new String(elem.getData());
//            final ElemExtModel dataModel = JsonUtil.strToModel(data, ElemExtModel.class);
//            if (TextUtils.equals(dataModel.getTransferId(), transferid)) {
////                        notifyItemChanged();
//            }
            notifyDataSetChanged();
        }
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecycleView = (ChatListView) recyclerView;
    }

    public void showLoading() {
        if (mLoading)
            return;
        mLoading = true;
        notifyItemChanged(0);
    }

    public void notifyDataSetChanged(final int type, final int value) {
        BackgroundTasks.getInstance().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoading = false;
                if (type == ChatListView.DATA_CHANGE_TYPE_REFRESH) {
                    notifyDataSetChanged();
                    mRecycleView.scrollToEnd();
                } else if (type == ChatListView.DATA_CHANGE_TYPE_ADD_BACK) {
                    notifyItemRangeInserted(mDataSource.size() + 1, value);
                    mRecycleView.scrollToEnd();

                    if (mDataSource.size() > 0) {
                        MessageInfo info = mDataSource.get(mDataSource.size() - 1);
                        notifyPacketOrTransferData(info);
                    }
                } else if (type == ChatListView.DATA_CHANGE_TYPE_UPDATE) {
                    notifyItemChanged(value + 1);
                } else if (type == ChatListView.DATA_CHANGE_TYPE_LOAD || type == ChatListView.DATA_CHANGE_TYPE_ADD_FRONT) {
                    //加载条目为数0，只更新动画
                    if (value == 0) {
                        notifyItemChanged(0);
                    } else {
                        //加载过程中有可能之前第一条与新加载的最后一条的时间间隔不超过5分钟，时间条目需去掉，所以这里的刷新要多一个条目
                        if (getItemCount() > value) {
                            notifyItemRangeInserted(0, value);
                        } else {
                            notifyItemRangeInserted(0, value);
                        }
                    }
                } else if (type == ChatListView.DATA_CHANGE_TYPE_DELETE) {
                    notifyItemRemoved(value + 1);
                    notifyDataSetChanged();
                    mRecycleView.scrollToEnd();
                }
            }
        }, 100);
    }


    private void registerVideoPlayClickListener(View view, final MessageInfo msg) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TUIKit.getAppContext(), VideoViewActivity.class);
                intent.putExtra(UIKitConstants.CAMERA_IMAGE_PATH, msg.getDataPath());
                intent.putExtra(UIKitConstants.CAMERA_VIDEO_PATH, msg.getDataUri());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                TUIKit.getAppContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSource.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return headerViewType;
        MessageInfo msg = getItem(position);
        return msg.isSelf() ? msg.getMsgType() + 1 : msg.getMsgType();
    }

    @Override
    public void setDataSource(IChatProvider provider) {
        this.mDataSource = provider.getDataSource();
        provider.attachAdapter(this);
        notifyDataSetChanged(ChatListView.DATA_CHANGE_TYPE_REFRESH, getItemCount());
    }

    @Override
    public MessageInfo getItem(int position) {
        if (position == 0 || mDataSource.size() == 0)
            return null;
        MessageInfo info = mDataSource.get(position - 1);
        if (mInterceptor != null)
            mInterceptor.intercept(info);
        return info;
    }

    class BaseChatHolder extends RecyclerView.ViewHolder {
        protected ImageView userIcon;
        protected ImageView status;
        protected TextView userName, chatTime;
        protected ViewGroup contentGroup, dataView;
        protected ProgressBar progress;
        protected View rootView;

        public BaseChatHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            userName = itemView.findViewById(R.id.tv_user_name);
            chatTime = itemView.findViewById(R.id.chat_time);
            userIcon = itemView.findViewById(R.id.iv_user_icon);
            contentGroup = itemView.findViewById(R.id.ll_content_group);
            dataView = itemView.findViewById(R.id.ll_msg_data_group);
            status = itemView.findViewById(R.id.message_status);
            progress = itemView.findViewById(R.id.message_sending);
            if (userIcon != null)
                userIcon.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ChatTextHolder extends BaseChatHolder {
        private TextView msg;

        public ChatTextHolder(View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.tv_user_msg);

        }
    }


    class AddFriendTextHolder extends BaseChatHolder {
        private TextView msg, tv_addFriend_tip;

        public AddFriendTextHolder(View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.tv_user_msg);
            tv_addFriend_tip = itemView.findViewById(R.id.tv_addFriend_tip);

        }
    }

    class ChatImageHolder extends BaseChatHolder {
        private ImageView imgData;
        private ImageView playBtn;
        private View cover;
        private RelativeLayout mDataGroup;
        private TextView mDuration;

        public ChatImageHolder(View itemView) {
            super(itemView);
            imgData = itemView.findViewById(R.id.iv_user_image);
            playBtn = itemView.findViewById(R.id.video_play_btn);
            cover = itemView.findViewById(R.id.video_un_download_cover);
            mDataGroup = itemView.findViewById(R.id.image_data_group);
            mDuration = itemView.findViewById(R.id.video_duration);
        }
    }

    class ShareHolder extends BaseChatHolder {
        SessionIconView session_icon;
        TextView tvTitle;
        TextView tvType;
        RelativeLayout rlItemView;
        TextView extra;

        public ShareHolder(View itemView) {
            super(itemView);
            session_icon = itemView.findViewById(R.id.session_icon);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvType = itemView.findViewById(R.id.tvType);
            rlItemView = itemView.findViewById(R.id.rlItemView);
            extra = itemView.findViewById(R.id.tvExtra);
        }
    }

    class ChatTransferHolder extends BaseChatHolder {
        private ImageView imgData;
        private TextView tvTransferMoney;
        private TextView tvTransferRemark;
        private TextView tvTip;


        public ChatTransferHolder(View itemView) {
            super(itemView);
            imgData = itemView.findViewById(R.id.iv_user_image);
            tvTransferMoney = itemView.findViewById(R.id.tvTransferMoney);
            tvTransferRemark = itemView.findViewById(R.id.tvTransferRemark);
            tvTip = itemView.findViewById(R.id.tvTip);
        }
    }

    class ChatAudioHolder extends BaseChatHolder {
        private ImageView imgPlay;
        private ImageView unread;
        private TextView time;


        public ChatAudioHolder(View itemView) {
            super(itemView);
            imgPlay = itemView.findViewById(R.id.audio_play);
            unread = itemView.findViewById(R.id.unread_flag);
            time = itemView.findViewById(R.id.audio_time);
        }
    }

    class ChatFileHolder extends BaseChatHolder {
        private ImageView fileIcon;
        private TextView fileName, fileSize, fileStatus;

        public ChatFileHolder(View itemView) {
            super(itemView);
            fileIcon = itemView.findViewById(R.id.file_image);
            fileName = itemView.findViewById(R.id.file_name);
            fileSize = itemView.findViewById(R.id.file_size);
            fileStatus = itemView.findViewById(R.id.file_status);
        }
    }

    class ChatTipsHolder extends BaseChatHolder {

        private TextView tips;

        public ChatTipsHolder(View itemView) {
            super(itemView);
            chatTime = itemView.findViewById(R.id.chat_time);
            tips = itemView.findViewById(R.id.chat_tips);
        }
    }

    class ChatCustomerTipsHolder extends BaseChatHolder {

        private TextView tips;

        public ChatCustomerTipsHolder(View itemView) {
            super(itemView);
            tips = itemView.findViewById(R.id.tvTips);
        }
    }
}