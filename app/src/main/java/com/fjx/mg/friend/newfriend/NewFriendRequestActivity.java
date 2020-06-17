package com.fjx.mg.friend.newfriend;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.friend.chat.ChatActivity;
import com.fjx.mg.friend.request.RequestDetailActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.repository.db.model.DBFriendRequestModel;

import java.util.List;

import butterknife.BindView;

/**
 * 新朋友首页
 */
public class NewFriendRequestActivity extends BaseMvpActivity<NewFriendRequestPresenter> implements NewFriendRequestContact.View {
    @BindView(R.id.recycler)
    RecyclerView mRvNewFriendRequest;
    private NewFriendAdapter mAdapter;

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, NewFriendRequestActivity.class);
        return intent;
    }

    @Override
    protected NewFriendRequestPresenter createPresenter() {
        return new NewFriendRequestPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_recycler;
    }


    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.new_friend));

        mAdapter = new NewFriendAdapter();
        mAdapter.setHeaderView(getView());
        mRvNewFriendRequest.setLayoutManager(new LinearLayoutManager(getCurContext()));
        mRvNewFriendRequest.addItemDecoration(new SpacesItemDecoration(1));
        mRvNewFriendRequest.setAdapter(mAdapter);

        mAdapter.setEmptyView(R.layout.layout_empty);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            DBFriendRequestModel model = mAdapter.getItem(position);
            if (model == null || model.getStatus() == 2) return;
            if (model.getStatus() == 1) {
                ChatActivity.startC2CChat(getCurContext(), model.getIdentityId(), model.getNickName());
                return;
            }
            startActivityForResult(RequestDetailActivity.newInstance(getCurContext(),
                    model.getIdentityId(), model.getAddWording()), 1212);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getPendencyList();
    }

    private TextView getView() {
        TextView tvSection = (TextView) LayoutInflater.from(getCurContext()).inflate(R.layout.layout_text, null);
        tvSection.setText(getString(R.string.friend_request));
        return tvSection;
    }

    @Override
    public void showPendencyList(List<DBFriendRequestModel> list) {
        mAdapter.setList(list);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1212) {
            if (resultCode == -1) {
                setResult(-1);
                finish();
            }
        }
    }
}
