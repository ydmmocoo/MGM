package com.fjx.mg.friend.search;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.friend.chat.ChatActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.SoftInputUtil;
import com.library.repository.models.SearchTimFriendModel;
import com.tencent.imsdk.ext.group.TIMGroupBaseInfo;
import com.tencent.imsdk.friendship.TIMFriend;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FriendSearchActivity extends BaseMvpActivity<FriendSearchPresenter> implements FriendSearchContract.View {

    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private SearchFeiendAdapter friendAdapter;
    @BindView(R.id.tvSearch)
    TextView mTvSearch;
    @BindView(R.id.rvGroup)
    RecyclerView mRvGroup;
    SearchGroupAdapter mSearchGroupAdapter;
    @BindView(R.id.rvConverser)
    RecyclerView mRvConverser;
    private SearchFeiendAdapter mConverser;
    @BindView(R.id.llConverser)
    LinearLayout mLlConverser;
    @BindView(R.id.tvMoreConverser)
    TextView mTvMoreConverser;
    @BindView(R.id.llContact)
    LinearLayout mLlContact;
    @BindView(R.id.tvMoreContact)
    TextView mTvMoreContact;
    @BindView(R.id.llGroup)
    LinearLayout mLlGroup;
    @BindView(R.id.tvMoreGroup)
    TextView mTvMoreGroup;
    private List<TIMGroupBaseInfo> mTimGroupBaseInfos;
    private List<SearchTimFriendModel> timConversations;
    private List<SearchTimFriendModel> friendList;
    private boolean isTransfer;

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, FriendSearchActivity.class);
        return intent;
    }

    public static Intent newInstance(Context context, boolean isTransfer) {
        Intent intent = new Intent(context, FriendSearchActivity.class);
        intent.putExtra(IntentConstants.IS_TRANSFER, isTransfer);
        return intent;
    }

    @Override
    protected FriendSearchPresenter createPresenter() {
        return new FriendSearchPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_searchfriend;
    }

    @Override
    protected void initView() {
        if (getIntent() != null) {
            isTransfer = getIntent().getBooleanExtra(IntentConstants.IS_TRANSFER, false);
        }
        etContent.setInputType(InputType.TYPE_CLASS_TEXT);
        friendAdapter = new SearchFeiendAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(getCurContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recycler.setLayoutManager(manager);
        recycler.setAdapter(friendAdapter);
        recycler.addItemDecoration(new SpacesItemDecoration(1));

        etContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch();
                }
                return true;
            }
        });

        friendAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                SearchTimFriendModel item = friendAdapter.getItem(position);
                TIMFriend friend = item.getTimFriend();
                String remark = friend.getRemark();
                if (TextUtils.isEmpty(remark)) {
                    remark = friend.getTimUserProfile().getNickName();
                }
                if (isTransfer) {
                    //转账页只需要返回联系人信息
                    String phone = "";
                    byte[] bytes = friend.getTimUserProfile().getCustomInfo().get("phone");
                    if (bytes != null)
                        phone = new String(bytes);
                    Intent intent = new Intent();
                    intent.putExtra("mobile", phone);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    ChatActivity.startC2CChat(getCurContext(), friend.getIdentifier(), remark);
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SoftInputUtil.showSoftInputView(getCurActivity(), etContent);
            }
        }, 100);
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String trim = charSequence.toString().trim();
                mPresenter.searchFriend(trim);
                if (TextUtils.isEmpty(trim)) {
                    mConverser.setList(null);
                    friendAdapter.setList(null);
                    mSearchGroupAdapter.setList(null);
                    mLlContact.setVisibility(View.GONE);
                    mLlConverser.setVisibility(View.GONE);
                    mLlGroup.setVisibility(View.GONE);
                    mTvMoreContact.setVisibility(View.GONE);
                    mTvMoreConverser.setVisibility(View.GONE);
                    mTvMoreGroup.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mTvSearch.setVisibility(View.GONE);
        initGroup();
        initConverser();
        mTvMoreContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(SearchCommonActivity.newIntent(getCurContext(), "2", JsonUtil.moderToString(friendList)));
            }
        });
    }

    private void initConverser() {
        mConverser = new SearchFeiendAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRvConverser.setLayoutManager(manager);
        mRvConverser.addItemDecoration(new SpacesItemDecoration(1));
        mRvConverser.setAdapter(mConverser);
        mConverser.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                SearchTimFriendModel item = mConverser.getItem(position);
                TIMFriend friend = item.getTimFriend();
                String remark = friend.getRemark();
                if (TextUtils.isEmpty(remark)) {
                    remark = friend.getTimUserProfile().getNickName();
                }
                if (isTransfer) {
                    //转账页只需要返回联系人信息
                    String phone = "";
                    byte[] bytes = friend.getTimUserProfile().getCustomInfo().get("phone");
                    if (bytes != null)
                        phone = new String(bytes);
                    Intent intent = new Intent();
                    intent.putExtra("mobile", phone);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    ChatActivity.startC2CChat(getCurContext(), friend.getIdentifier(), remark);
                }
            }
        });
        mTvMoreConverser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(SearchCommonActivity.newIntent(getCurContext(), "1", JsonUtil.moderToString(timConversations)));
            }
        });
    }

    private void initGroup() {
        mSearchGroupAdapter = new SearchGroupAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRvGroup.setLayoutManager(manager);
        mRvGroup.addItemDecoration(new SpacesItemDecoration(1));
        mRvGroup.setAdapter(mSearchGroupAdapter);
        mSearchGroupAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                TIMGroupBaseInfo timGroupBaseInfo = mSearchGroupAdapter.getItem(position);
                ChatActivity.startGroupChat(getCurContext(), timGroupBaseInfo.getGroupId());
            }
        });
        mTvMoreGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(SearchCommonActivity.newIntent(getCurContext(), "3", JsonUtil.moderToString(mTimGroupBaseInfos)));
            }
        });
    }

    private void doSearch() {
        String content = etContent.getText().toString();
        mPresenter.searchFriend(content);
    }

    @Override
    public void showFriends(final List<SearchTimFriendModel> friendList) {
        this.friendList = friendList;
        if (friendList != null && friendList.size() > 0) {
            if (friendList.size() > 3) {
                friendAdapter.setList(friendList.subList(0, 3));
                mTvMoreContact.setVisibility(View.VISIBLE);
            } else {
                friendAdapter.setList(friendList);
                mTvMoreContact.setVisibility(View.GONE);
            }
            mLlContact.setVisibility(View.VISIBLE);
            recycler.setVisibility(View.VISIBLE);

        } else {
            mTvMoreContact.setVisibility(View.GONE);
            mLlContact.setVisibility(View.GONE);
            recycler.setVisibility(View.GONE);
        }
    }

    @Override
    public void showGroups(final List<TIMGroupBaseInfo> mTimGroupBaseInfos) {
        this.mTimGroupBaseInfos = mTimGroupBaseInfos;
        if (mTimGroupBaseInfos != null && mTimGroupBaseInfos.size() > 0) {
            if (mTimGroupBaseInfos.size() > 3) {
                mSearchGroupAdapter.setList(mTimGroupBaseInfos.subList(0, 3));
                mTvMoreGroup.setVisibility(View.VISIBLE);
            } else {
                mSearchGroupAdapter.setList(mTimGroupBaseInfos);
                mTvMoreGroup.setVisibility(View.GONE);
            }
            mRvGroup.setVisibility(View.VISIBLE);
            mLlGroup.setVisibility(View.VISIBLE);
        } else {
            mTvMoreGroup.setVisibility(View.GONE);
            mRvGroup.setVisibility(View.GONE);
            mLlGroup.setVisibility(View.GONE);
        }
    }

    @Override
    public void showConverser(final List<SearchTimFriendModel> timConversations) {
        this.timConversations = timConversations;
        if (timConversations != null && timConversations.size() > 0) {
            if (timConversations.size() > 3) {
                mConverser.setList(timConversations.subList(0, 3));
                mTvMoreConverser.setVisibility(View.VISIBLE);
            } else {
                mConverser.setList(timConversations);
                mTvMoreConverser.setVisibility(View.GONE);
            }
            mRvConverser.setVisibility(View.VISIBLE);
            mLlConverser.setVisibility(View.VISIBLE);
        } else {
            mRvConverser.setVisibility(View.GONE);
            mLlConverser.setVisibility(View.GONE);
            mTvMoreConverser.setVisibility(View.GONE);
        }
    }


    @OnClick({R.id.ivBack, R.id.tvSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.tvSearch:
                doSearch();
                break;
        }
    }
}
