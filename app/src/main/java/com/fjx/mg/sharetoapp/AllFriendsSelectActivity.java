package com.fjx.mg.sharetoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.friend.chat.ChatActivity;
import com.fjx.mg.sharetoapp.adapter.AllFriendsListAdapter;
import com.fjx.mg.sharetoapp.fragment.SendFriendsInfoDialogFragment;
import com.fjx.mg.sharetoapp.mvp.AllFriendsSelectContract;
import com.fjx.mg.sharetoapp.mvp.AllFriendsSelectPresenter;
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
 * Description：选择所有好友页
 */
public class AllFriendsSelectActivity extends BaseMvpActivity<AllFriendsSelectPresenter> implements AllFriendsSelectContract.View, OnItemClickListener, TextWatcher {

    @BindView(R.id.etSearch)
    EditText mEtSearch;
    @BindView(R.id.rvFriends)
    RecyclerView mRvFriends;
    private AllFriendsListAdapter mAdapter;
    private List<SessionInfo> mSession;

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
        Intent intent = new Intent(context, AllFriendsSelectActivity.class);
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
        Intent intent = new Intent(context, AllFriendsSelectActivity.class);
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
        Intent intent = new Intent(context, AllFriendsSelectActivity.class);
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
        Intent intent = new Intent(context, AllFriendsSelectActivity.class);
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
        Intent intent = new Intent(context, AllFriendsSelectActivity.class);
        intent.putExtra(IntentConstants.MESSAGE_TYPE, messageType);
        intent.putExtra(IntentConstants.TYPE, type);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.act_all_friends_select;
    }

    @Override
    protected AllFriendsSelectPresenter createPresenter() {
        return new AllFriendsSelectPresenter(this);
    }

    @Override
    protected void initView() {
        super.initView();
        mAdapter = new AllFriendsListAdapter();
        mRvFriends.addItemDecoration(new LinearManagerItemDecaration(1, LinearManagerItemDecaration.VERTICAL_LIST));
        mRvFriends.setLayoutManager(new LinearLayoutManager(getCurContext()));
        mRvFriends.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mPresenter.requestAllFriends();

        mAdapter.setEmptyView(R.layout.empty_all_moments_message);
        mEtSearch.addTextChangedListener(this);
    }


    @OnClick({R.id.llBack})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.llBack://返回
                finish();
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
            bundle.putString(IntentConstants.FLAG, model.getContent());
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
        bundle.putString(IntentConstants.USER_AVATAR, sessionInfo.getIconUrl());
        dialogFragment.setArguments(bundle);
        dialogFragment.setonDialogFragmentClickListener(new SendFriendsInfoDialogFragment.onDialogFragmentClickListener() {
            @Override
            public void onClick(DialogFragment dialogFragment, String say) {
//                ChatActivity.startC2CChat(getCurContext(), sessionInfo.getPeer(), sessionInfo.getTitle(), JsonUtil.moderToString(model), say, true);
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
    public void responseAllFriends(List<SessionInfo> datas) {
        mAdapter.setNewInstance(datas);
        this.mSession = datas;
    }

    @Override
    public void responseSearchAllFriends(List<SessionInfo> datas) {
        mAdapter.setList(datas);
    }

    /*------------------------start EditText监听输入内容-----------------------------*/
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        try {
            if (charSequence.length() > 0 && charSequence != null) {
                mPresenter.requestSearchAllFriends(charSequence.toString().trim(), mSession);
            } else {
                mAdapter.setNewInstance(mSession);
            }
        } catch (NullPointerException e) {
            mAdapter.setNewInstance(mSession);
            e.printStackTrace();
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
    /*-----------------------------end EditText监听输入内容-----------------------------*/
}
