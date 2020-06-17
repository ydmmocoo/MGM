package com.fjx.mg.friend.chat;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.friend.chat.adapter.GroupChatListAdapter;
import com.fjx.mg.widget.recyclerview.LinearManagerItemDecaration;
import com.library.common.base.BaseActivity;
import com.library.common.utils.CommonToast;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupBaseInfo;

import java.util.List;

import butterknife.BindView;

/**
 * Author    by hanlz
 * Date      on 2019/12/5.
 * Description：
 */
public class GroupChatListActivity extends BaseActivity implements OnItemClickListener {

    @BindView(R.id.rvGroupChatList)
    RecyclerView mRvGroupChatList;
    private GroupChatListAdapter mAdapter;

    public static Intent newInstance(Context curContext) {
        Intent intent = new Intent(curContext, GroupChatListActivity.class);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.act_group_chat_list;
    }

    @Override
    protected void initView() {
        super.initView();
        ToolBarManager.with(this).setTitle(getString(R.string.group_chat_list));
        mAdapter = new GroupChatListAdapter();
        mRvGroupChatList.setAdapter(mAdapter);
        mRvGroupChatList.setLayoutManager(new LinearLayoutManager(getCurContext()));

        mAdapter.setEmptyView(R.layout.empty_all_moments_message);
        mAdapter.setOnItemClickListener(this);
        mRvGroupChatList.addItemDecoration(new LinearManagerItemDecaration(1, LinearManagerItemDecaration.VERTICAL_LIST));
        getGroupList();
    }

    /**
     * 获取已加入的群组列表
     */
    public void getGroupList() {
        showLoading();
        TIMGroupManager.getInstance().getGroupList(new TIMValueCallBack<List<TIMGroupBaseInfo>>() {
            @Override
            public void onError(int i, String s) {
                hideLoading();
                CommonToast.toast(s);
            }

            @Override
            public void onSuccess(List<TIMGroupBaseInfo> timGroupBaseInfos) {
                hideLoading();
                mAdapter.setList(timGroupBaseInfos);
            }
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        TIMGroupBaseInfo item = (TIMGroupBaseInfo) adapter.getItem(position);
        ChatActivity.startGroupChat(this, item.getGroupId());
    }
}
