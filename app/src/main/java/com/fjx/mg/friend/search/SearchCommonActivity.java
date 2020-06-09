package com.fjx.mg.friend.search;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.friend.chat.ChatActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.JsonUtil;
import com.library.repository.models.SearchTimFriendModel;
import com.tencent.imsdk.ext.group.TIMGroupBaseInfo;
import com.tencent.imsdk.friendship.TIMFriend;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author hanlz
 * @date 2020年2月5日15:41:25
 * @description：搜索结果页(超过三人跳转此页)
 */
public class SearchCommonActivity extends BaseMvpActivity<SearchCommonPresenter> implements SearchComonContract.View {

    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private SearchFeiendAdapter friendAdapter;
    @BindView(R.id.rvGroup)
    RecyclerView mRvGroup;
    private SearchGroupAdapter searchGroupAdapter;
    private String type;

    public static Intent newIntent(Context context, String type, String data) {
        Intent intent = new Intent(context, SearchCommonActivity.class);
        intent.putExtra(IntentConstants.TYPE, type);
        intent.putExtra(IntentConstants.EXT, data);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_search_common;
    }

    @Override
    protected void initView() {
        super.initView();
        type = getIntent().getStringExtra(IntentConstants.TYPE);
        String data = getIntent().getStringExtra(IntentConstants.EXT);

        friendAdapter = new SearchFeiendAdapter();
        recycler.setAdapter(friendAdapter);
        recycler.addItemDecoration(new SpacesItemDecoration(1));
        friendAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                SearchTimFriendModel item = friendAdapter.getItem(position);
                TIMFriend friend = item.getTimFriend();
                String remark = friend.getRemark();
                if (TextUtils.isEmpty(remark)) {
                    remark = friend.getTimUserProfile().getNickName();
                }
                ChatActivity.startC2CChat(getCurContext(), friend.getIdentifier(), remark);
            }
        });

        searchGroupAdapter = new SearchGroupAdapter();
        mRvGroup.setAdapter(searchGroupAdapter);
        mRvGroup.addItemDecoration(new SpacesItemDecoration(1));
        searchGroupAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                TIMGroupBaseInfo timGroupBaseInfo = searchGroupAdapter.getItem(position);
                ChatActivity.startGroupChat(getCurContext(), timGroupBaseInfo.getGroupId());
            }
        });
        if (TextUtils.equals("1", type) || TextUtils.equals("2", type)) {
            List<SearchTimFriendModel> friends = JsonUtil.jsonToList(data, SearchTimFriendModel.class);
            friendAdapter.setList(friends);
        } else if (TextUtils.equals("3", type)) {
            List<TIMGroupBaseInfo> timGroupBaseInfos = JsonUtil.jsonToList(data, TIMGroupBaseInfo.class);
            searchGroupAdapter.setList(timGroupBaseInfos);
        }

        etContent.setInputType(InputType.TYPE_CLASS_TEXT);
        etContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch();
                }
                return true;
            }
        });
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    doSearch();
                } else {
                    searchGroupAdapter.setList(null);
                    friendAdapter.setList(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void doSearch() {
        String content = etContent.getText().toString();
        switch (type) {
            case "1":
                mPresenter.searchConverser(content);
                break;
            case "4":
            case "2":
                mPresenter.searchFriend(content);
                break;
            case "3":
                mPresenter.searchGroup(content);
                break;
            default:
                break;

        }
    }

    @Override
    protected SearchCommonPresenter createPresenter() {
        return new SearchCommonPresenter(this);
    }

    @Override
    public void showFriends(List<SearchTimFriendModel> friendList) {
        friendAdapter.setList(friendList);
    }

    @Override
    public void showGroups(List<TIMGroupBaseInfo> mTimGroupBaseInfos) {
        searchGroupAdapter.setList(mTimGroupBaseInfos);
    }

    @Override
    public void showConverser(List<SearchTimFriendModel> timConversations) {
        friendAdapter.setList(timConversations);
    }

    @OnClick(R.id.ivBack)
    public void back() {
        finish();
    }
}
