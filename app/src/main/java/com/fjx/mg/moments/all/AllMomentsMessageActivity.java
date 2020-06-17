package com.fjx.mg.moments.all;

import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.moments.all.adapter.AllMomentsMessageAdapter;
import com.fjx.mg.moments.event.AllMomentFinish2FriendsMomentEvent;
import com.fjx.mg.widget.recyclerview.LinearManagerItemDecaration;
import com.library.common.base.BaseMvpActivity;
import com.library.repository.models.MomentsInfoModel;
import com.library.repository.models.MomentsReplyListModel;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * create by hanlz
 * 2019/9/23
 * Describe:用户朋友圈动态回复的消息
 */
public class AllMomentsMessageActivity extends BaseMvpActivity<AllMomentsMessagePresenter> implements AllMomentsContract.MessageView {


    @BindView(R.id.rvMessage)
    RecyclerView mRvMessage;
    AllMomentsMessageAdapter mAdapter;

    public static void launch(Context context) {
        Intent intent = new Intent(context, AllMomentsMessageActivity.class);
        context.startActivity(intent);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, AllMomentsMessageActivity.class);
    }

    @Override
    protected int layoutId() {
        return R.layout.act_all_moments_message;
    }

    @Override
    protected AllMomentsMessagePresenter createPresenter() {
        return new AllMomentsMessagePresenter(this);
    }

    @Override
    protected void initView() {
        super.initView();
        ToolBarManager.with(this).setTitle(getString(R.string.all_moments_message));
        mAdapter = new AllMomentsMessageAdapter();
        mRvMessage.setAdapter(mAdapter);
        mRvMessage.addItemDecoration(new LinearManagerItemDecaration(1, LinearManagerItemDecaration.VERTICAL_LIST));
        mRvMessage.setLayoutManager(new LinearLayoutManager(getCurContext()));
        mRvMessage.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.empty_all_moments_message);
        mPresenter.MomentsReplyList("1", "", "2");
    }

    @Override
    public void showReplyList(MomentsReplyListModel data) {
        mAdapter.setList(data.getReplyList());
        mPresenter.momentsRead();
    }

    @Override
    public void showInfo(MomentsInfoModel data) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(new AllMomentFinish2FriendsMomentEvent());
    }
}
