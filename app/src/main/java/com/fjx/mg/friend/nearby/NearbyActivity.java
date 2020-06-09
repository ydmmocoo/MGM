package com.fjx.mg.friend.nearby;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.friend.addfriend.AddFriendActivity;
import com.fjx.mg.friend.imuser.NewImUserDetailActivity;
import com.fjx.mg.widget.loading.XLoadingLayout;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.JsonUtil;
import com.library.repository.models.NearbyUserModel;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.friendship.TIMFriend;

import java.util.List;

import butterknife.BindView;

public class NearbyActivity extends BaseMvpActivity<NearbyPresenter> implements NearbyContact.View {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    private NearbyAdapter nearbyAdapter;
    private XLoadingLayout mXLoadingLayour;
    private List<NearbyUserModel> list;

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, NearbyActivity.class);
        return intent;
    }

    @Override
    protected NearbyPresenter createPresenter() {
        return new NearbyPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_recycler;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.nearby_user));
        mXLoadingLayour = new XLoadingLayout(this);
        mXLoadingLayour.showLoading();
        mPresenter.locationAddress();
        nearbyAdapter = new NearbyAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.addItemDecoration(new SpacesItemDecoration(1));
        recycler.setAdapter(nearbyAdapter);
        nearbyAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                mPresenter.findImUser(list.get(position).getIdentifier());
            }
        });
    }

    @Override
    public void showAroundUsers(List<NearbyUserModel> datas) {
        list = datas;
        nearbyAdapter.setList(list);
        mXLoadingLayour.hideLoading();
    }

    @Override
    public void getImUserSuccess(TIMUserProfile profile) {
        startActivity(AddFriendActivity.newInstance(getCurContext(), JsonUtil.moderToString(profile)));
    }

    @Override
    public void getImUserSuccess(TIMFriend friend) {
        startActivity(NewImUserDetailActivity.newInstance(getCurContext(),
                friend.getTimUserProfile().getIdentifier()));
    }
}
