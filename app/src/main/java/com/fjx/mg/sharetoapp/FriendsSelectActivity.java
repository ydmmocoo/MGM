package com.fjx.mg.sharetoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.friend.chat.ChatActivity;
import com.fjx.mg.sharetoapp.adapter.NearChatListAdapter;
import com.fjx.mg.sharetoapp.fragment.SendFriendsInfoDialogFragment;
import com.fjx.mg.sharetoapp.mvp.FriendsSelectContract;
import com.fjx.mg.sharetoapp.mvp.FriendsSelectPresenter;
import com.fjx.mg.widget.recyclerview.LinearManagerItemDecaration;
import com.library.common.base.BaseMvpActivity;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StringUtil;
import com.tencent.qcloud.uikit.business.chat.model.CacheUtil;
import com.tencent.qcloud.uikit.business.chat.model.ElemExtModel;
import com.tencent.qcloud.uikit.business.session.model.SessionInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author    by hanlz
 * Date      on 2019/11/6.
 * Description：好友选择
 */
public class FriendsSelectActivity extends BaseMvpActivity<FriendsSelectPresenter> implements FriendsSelectContract.View, OnItemClickListener, TextWatcher {

    @BindView(R.id.etSearch)
    EditText mEtSearch;
    @BindView(R.id.llSelectFriends)
    LinearLayout mLlSelectFriends;
    @BindView(R.id.rvFriends)
    RecyclerView mRvFriends;
    private NearChatListAdapter mAdapter;
    private List<SessionInfo> mSession;
    private String type;

    /**
     * 来自黄页的分享
     *
     * @param context
     * @param cId
     * @param messageType
     * @param title
     * @param imgs
     * @return
     */
    public static Intent newIntent(Context context, String cId, String messageType, String title, ArrayList<String> imgs) {
        Intent intent = new Intent(context, FriendsSelectActivity.class);
        intent.putExtra(IntentConstants.CID, cId);
        intent.putExtra(IntentConstants.MESSAGE_TYPE, messageType);
        intent.putExtra(IntentConstants.TITLE, title);
        intent.putStringArrayListExtra(IntentConstants.IMGS, imgs);
        return intent;
    }

    /**
     * 来自同城分享
     *
     * @param context
     * @param typeName
     * @param images
     * @param cId
     * @param content
     * @param messageType
     * @return
     */
    public static Intent newIntent(Context context, String typeName, ArrayList<String> images, String cId, String content, String messageType) {
        Intent intent = new Intent(context, FriendsSelectActivity.class);
        intent.putExtra(IntentConstants.CID, cId);
        intent.putExtra(IntentConstants.MESSAGE_TYPE, messageType);
        intent.putExtra(IntentConstants.CONTENT, content);
        intent.putExtra(IntentConstants.TYPE_NAME, typeName);
        intent.putStringArrayListExtra(IntentConstants.IMAGES, images);
        return intent;
    }

    /**
     * 来自新闻的分享
     *
     * @param newsId
     * @param messageType
     * @param newTitle
     * @param img
     * @return
     */
    public static Intent newIntent(Context context, String newsId, String messageType, String newTitle, String img) {
        Intent intent = new Intent(context, FriendsSelectActivity.class);
        intent.putExtra(IntentConstants.NEWSID, newsId);
        intent.putExtra(IntentConstants.MESSAGE_TYPE, messageType);
        intent.putExtra(IntentConstants.NEWS_TITLE, newTitle);
        intent.putExtra(IntentConstants.IMG, img);
        return intent;
    }

    /**
     * 用户名片分享给列表好友 type=1
     *
     * @param context
     * @return
     */
    public static Intent newIntent(Context context, String model, String messageType, String type) {
        Intent intent = new Intent(context, FriendsSelectActivity.class);
        intent.putExtra(IntentConstants.ELEMEXTMODEL, model);
        intent.putExtra(IntentConstants.MESSAGE_TYPE, messageType);
        intent.putExtra(IntentConstants.TYPE, type);
        return intent;
    }

    /**
     * 列表好友名片分享用户type=2
     *
     * @param context
     * @return
     */
    public static Intent newIntent(Context context, String messageType, String type) {
        Intent intent = new Intent(context, FriendsSelectActivity.class);
        intent.putExtra(IntentConstants.MESSAGE_TYPE, messageType);
        intent.putExtra(IntentConstants.TYPE, type);
        return intent;
    }


    @Override
    protected int layoutId() {
        return R.layout.act_friends_select;
    }

    @Override
    protected FriendsSelectPresenter createPresenter() {
        return new FriendsSelectPresenter(this);
    }

    @Override
    protected void initView() {
        super.initView();
        type = getIntent().getStringExtra(IntentConstants.TYPE);
        mAdapter = new NearChatListAdapter();
        mRvFriends.addItemDecoration(new LinearManagerItemDecaration(1, LinearManagerItemDecaration.VERTICAL_LIST));
        mRvFriends.setLayoutManager(new LinearLayoutManager(getCurContext()));
        mRvFriends.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        mAdapter.setEmptyView(R.layout.empty_all_moments_message);
        String messageTyp = getIntent().getStringExtra(IntentConstants.MESSAGE_TYPE);
        if (StringUtil.equals(ElemExtModel.SHARE_PERSONAL_CARD, messageTyp)) {
            mLlSelectFriends.setVisibility(View.GONE);
        }
        mEtSearch.addTextChangedListener(this);

        mPresenter.requestConversationList(type);//请求最近聊天好友列表
    }


    @OnClick({R.id.llBack, R.id.llSelectFriends})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.llBack://返回
                finish();
                break;
            case R.id.llSelectFriends://选择所有好友
                String messageTyp = getIntent().getStringExtra(IntentConstants.MESSAGE_TYPE);
                if (StringUtil.equals(ElemExtModel.SHARE_CITY, messageTyp)) {
                    view.getContext().startActivity(AllFriendsSelectActivity.newIntent(view.getContext(),
                            getIntent().getStringExtra(IntentConstants.TYPE_NAME),
                            getIntent().getStringArrayListExtra(IntentConstants.IMAGES),
                            getIntent().getStringExtra(IntentConstants.CID),
                            getIntent().getStringExtra(IntentConstants.CONTENT), messageTyp));
                } else if (StringUtil.equals(ElemExtModel.SHARE_NEWS, messageTyp)) {
                    view.getContext().startActivity(AllFriendsSelectActivity.newIntent(view.getContext(),
                            getIntent().getStringExtra(IntentConstants.NEWSID), messageTyp,
                            getIntent().getStringExtra(IntentConstants.NEWS_TITLE),
                            getIntent().getStringExtra(IntentConstants.IMG)));
                } else if (StringUtil.equals(ElemExtModel.SHARE_YELLOWPAGE, messageTyp)) {
                    view.getContext().startActivity(AllFriendsSelectActivity.newIntent(view.getContext(),
                            getIntent().getStringExtra(IntentConstants.CID), messageTyp,
                            getIntent().getStringExtra(IntentConstants.TITLE),
                            getIntent().getStringArrayListExtra(IntentConstants.IMGS)));
                }
                break;
            default:
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final SessionInfo sessionInfo = (SessionInfo) adapter.getItem(position);
        final ElemExtModel model = getElemExtModel(sessionInfo);
        SendFriendsInfoDialogFragment dialogFragment = new SendFriendsInfoDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "FriendsSelectActivity");
        Bundle bundle = new Bundle();
        if (StringUtil.equals(ElemExtModel.SHARE_CITY, model.getMessageType())) {
            bundle.putString(IntentConstants.FLAG, model.getTypeName().concat(model.getContent()));
        } else if (StringUtil.equals(ElemExtModel.SHARE_NEWS, model.getMessageType())) {
            bundle.putString(IntentConstants.FLAG, model.getNewsTitle());
        } else if (StringUtil.equals(ElemExtModel.SHARE_YELLOWPAGE, model.getMessageType())) {
            bundle.putString(IntentConstants.FLAG, model.getTitle());
        } else {
            //个人名片
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append(getString(R.string.tim_psersonal_info));
            sb.append("]");
            if (TextUtils.isEmpty(model.getName())) {
                sb.append(model.getIdentifier());
            } else {
                sb.append(model.getName());
            }
            bundle.putString(IntentConstants.FLAG, sb.toString());
        }
        bundle.putString(IntentConstants.NICKNAME, sessionInfo.getTitle());
        bundle.putString(IntentConstants.USER_AVATAR, sessionInfo.getPeer());
        dialogFragment.setArguments(bundle);
        dialogFragment.setonDialogFragmentClickListener(new SendFriendsInfoDialogFragment.onDialogFragmentClickListener() {
            @Override
            public void onClick(DialogFragment dialogFragment, String say) {
                if (TextUtils.equals("2", getIntent().getStringExtra(IntentConstants.TYPE))) {
                    Intent intent = new Intent();
                    intent.putExtra("message", JsonUtil.moderToString(model));
                    setResult(-1, intent);
                    finish();
                } else {
                    ChatActivity.startC2CChat(getCurContext(), sessionInfo.getPeer(), sessionInfo.getTitle(), JsonUtil.moderToString(model), say, true);
                }
            }
        });
    }

    private ElemExtModel getElemExtModel(SessionInfo sessionInfo) {
        ElemExtModel model = new ElemExtModel();
        String messageType = getIntent().getStringExtra(IntentConstants.MESSAGE_TYPE);
        if (StringUtil.equals(ElemExtModel.SHARE_CITY, messageType)) {
            //来自同城分享
            model.setTypeName(getIntent().getStringExtra(IntentConstants.TYPE_NAME));
            model.setcId(getIntent().getStringExtra(IntentConstants.CID));
            model.setContent(getIntent().getStringExtra(IntentConstants.CONTENT));
            model.setImages(getIntent().getStringArrayListExtra(IntentConstants.IMAGES));
            model.setMessageType(getIntent().getStringExtra(IntentConstants.MESSAGE_TYPE));
        } else if (StringUtil.equals(ElemExtModel.SHARE_YELLOWPAGE, messageType)) {
            //来自黄页分享
            model.setTitle(getIntent().getStringExtra(IntentConstants.TITLE));
            model.setcId(getIntent().getStringExtra(IntentConstants.CID));
            model.setImgs(getIntent().getStringArrayListExtra(IntentConstants.IMGS));
            model.setMessageType(getIntent().getStringExtra(IntentConstants.MESSAGE_TYPE));
        } else if (StringUtil.equals(ElemExtModel.SHARE_NEWS, messageType)) {
            //来自新闻分享
            model.setImg(getIntent().getStringExtra(IntentConstants.IMG));
            model.setNewsId(getIntent().getStringExtra(IntentConstants.NEWSID));
            model.setNewsTitle(getIntent().getStringExtra(IntentConstants.NEWS_TITLE));
            model.setMessageType(getIntent().getStringExtra(IntentConstants.MESSAGE_TYPE));
        } else if (StringUtil.equals(ElemExtModel.SHARE_PERSONAL_CARD, messageType)) {
            if (TextUtils.equals("1", getIntent().getStringExtra(IntentConstants.TYPE))) {
                model = JsonUtil.strToModel(getIntent().getStringExtra(IntentConstants.ELEMEXTMODEL), ElemExtModel.class);
            } else {
                model.setIdentifier(sessionInfo.getPeer());
                String url = CacheUtil.getInstance().userAvatar(sessionInfo.getPeer());
                model.setFaceUrl(url);
                model.settIMUserProfile("");
                model.setName(sessionInfo.getTitle());
                model.setRemark(sessionInfo.getTitle());
                model.setMessageType(messageType);
            }
        }
        return model;
    }

    @Override
    public void responseSearchResult(List<SessionInfo> session) {
        mAdapter.setList(session);
    }

    @Override
    public void responseConversationList(List<SessionInfo> session) {
        mAdapter.setList(session);
        this.mSession = session;
    }

    /*------------------------start EditText监听输入内容-----------------------------*/
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() > 0 && charSequence != null) {
            mPresenter.requestSearchResult(charSequence.toString().trim(), mSession);
        } else {
            mAdapter.setList(mSession);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
    /*-----------------------------end EditText监听输入内容-----------------------------*/
}
