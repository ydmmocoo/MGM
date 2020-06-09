package com.fjx.mg.friend.contacts;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.friend.addfriend.SearchFriendActivity;
import com.fjx.mg.friend.blackfriend.BlackFriendActivity;
import com.fjx.mg.friend.chat.GroupChatListActivity;
import com.fjx.mg.friend.imuser.NewImUserDetailActivity;
import com.fjx.mg.friend.newfriend.NewFriendRequestActivity;
import com.fjx.mg.friend.search.FriendSearchActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.JsonUtil;
import com.library.repository.models.FriendContactSectionModel;
import com.tencent.imsdk.friendship.TIMFriendPendencyResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import butterknife.BindView;

/**
 * 通讯录
 */
public class FriendContactsActivity extends BaseMvpActivity<FriendContactsPresenter> implements FriendContactsContract.View {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    private FriendContactsAdapter contactsAdapter;
    private View headerView;
    private boolean isSelect;
    private boolean isTransfer;
    private CopyOnWriteArraySet<View> mViewContainer = new CopyOnWriteArraySet<>();
    @BindView(R.id.etSearch)
    EditText etSearch;
    TextView textView;

    @Override
    protected FriendContactsPresenter createPresenter() {
        return new FriendContactsPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_friend_contacts;
    }

    public static Intent newInstance(Context context, boolean isSelect) {
        return newInstance(context, isSelect, false);
    }

    public static Intent newInstance(Context context, boolean isSelect, boolean transfer) {
        Intent intent = new Intent(context, FriendContactsActivity.class);
        intent.putExtra(IntentConstants.IS_SELECT, isSelect);
        intent.putExtra(IntentConstants.IS_TRANSFER, transfer);
        return intent;
    }

    @Override
    protected void initView() {
        etSearch.setHint(getString(R.string.friends_search_tips));
        if (getIntent().getBooleanExtra(IntentConstants.IS_TRANSFER, false)) isTransfer = true;
        else isTransfer = false;
        isSelect = getIntent().getBooleanExtra(IntentConstants.IS_SELECT, false);
        GradientDrawableHelper.whit(etSearch).setColor(R.color.white);
        mPresenter.searchWatcher(etSearch);
        etSearch.setFocusable(false);
        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(FriendSearchActivity.newInstance(FriendContactsActivity.this, isTransfer), 1);
            }
        });

        contactsAdapter = new FriendContactsAdapter(new ArrayList<FriendContactSectionModel>());
        if (isTransfer) {
            ToolBarManager.with(this).setTitle(getString(R.string.payment_list));
        } else {
            ToolBarManager.with(this).setTitle(getString(R.string.contacts)).setRightText(getString(R.string.add_friend), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(SearchFriendActivity.newInstance(getCurContext()));
                }
            }).setTextColor(ContextCompat.getColor(getCurContext(), R.color.textColorAccent));
            initHeaderView();
            contactsAdapter.setHeaderView(headerView);
        }

        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.addItemDecoration(new SpacesItemDecoration(1));
        recycler.setAdapter(contactsAdapter);
//        addFootView();TODO 暂时屏蔽
        contactsAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                FriendContactSectionModel model = contactsAdapter.getItem(position);
                if (model.isHeader()) return;
                if (isSelect) {
                    String phone = "";
                    byte[] bytes = model.getData().getTimUserProfile().getCustomInfo().get("phone");
                    if (bytes != null)
                        phone = new String(bytes);
                    Log.e("55555",phone);
                    Intent intent = new Intent();
                    intent.putExtra("mobile", phone);
                    setResult(1, intent);
                    finish();
                } else {
                    startActivity(NewImUserDetailActivity.newInstance(getCurContext(), model.getData().getIdentifier()));
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getAllFriend();
        mPresenter.getPendencyList();
    }

    private void initHeaderView() {
        headerView = LayoutInflater.from(getCurContext()).inflate(R.layout.item_friend_contact_headerx, null);
        headerView.findViewById(R.id.new_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(NewFriendRequestActivity.newInstance(getCurContext()), 1212);
            }
        });
        headerView.findViewById(R.id.black_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(BlackFriendActivity.newInstance(getCurContext(), false));
            }
        });
        headerView.findViewById(R.id.group_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(GroupChatListActivity.newInstance(getCurContext()));
            }
        });
    }

    @Override
    public void showContactList(List<FriendContactSectionModel> datas) {
        Log.e("55555","data:"+JsonUtil.moderToString(datas));
        contactsAdapter.setList(datas);
//        if (datas != null) {
//            List<TIMFriend> counts = new ArrayList<>();
//            for (FriendContactSectionModel model : datas) {
//                counts.add(model.t);
//            }
//            textView.setText(getString(R.string.x_contacts, counts.size() + ""));
//        }
    }

    private void addFootView() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(lp);
        textView = new TextView(this);
        linearLayout.addView(textView);
        linearLayout.setGravity(Gravity.CENTER);
        contactsAdapter.addFooterView(linearLayout);
    }

    @Override
    public void getPendencyListSuccess(TIMFriendPendencyResponse response) {
        if (!isTransfer) {
            long count = response.getUnreadCnt();
            TextView tvNewFeiend = headerView.findViewById(R.id.tvNewFriendNum);
            tvNewFeiend.setText(String.valueOf(count));
            tvNewFeiend.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
            GradientDrawableHelper.whit(tvNewFeiend).setColor(R.color.colorAccent).setCornerRadius(30);
        }

        //TODO 屏蔽原因：此处数据和适配器无关 造成重复刷新 在部分机型会闪
//        contactsAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1212) {
            if (resultCode == -1) {
                setResult(-1);
                finish();
            }
        } else if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                //转账页只需要返回联系人信息
                String phone = data.getStringExtra("mobile");
                Intent intent = new Intent();
                intent.putExtra("mobile", phone);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mViewContainer != null || mViewContainer.size() > 0) {
            mViewContainer.clear();
        }
        super.onDestroy();
    }
}
