package com.fjx.mg.me.level.list;

import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.me.invite.InviteActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.models.InviteListModel;

import butterknife.BindView;

public class InviteListActivity extends BaseMvpActivity<InviteListPresenter> implements InviteListContract.IView {


    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;
    private InviteAdapter inviteAdapter;

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, InviteListActivity.class);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_common_refresh_list_toolbar;
    }

    @Override
    protected void initView() {
        TextView tvRightText = ToolBarManager.with(this).setTitle(getString(R.string.my_invite)).setRightText(getString(R.string.invite_friend),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(InviteActivity.newInstance(getCurContext()));
                    }
                });
        tvRightText.setTextColor(ContextCompat.getColor(getCurContext(), R.color.colorAccent));


        inviteAdapter = new InviteAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new SpacesItemDecoration(1));
        recycler.setAdapter(inviteAdapter);
        refreshView.autoRefresh();
        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                mPresenter.myInvite(page);
            }
        });

    }


    @Override
    protected InviteListPresenter createPresenter() {
        return new InviteListPresenter(this);
    }


    @Override
    public void showInviteList(InviteListModel model) {
        refreshView.noticeAdapterData(inviteAdapter, model.getInviteList(), model.isHasNext());
    }

    @Override
    public void loadError() {

    }
}
