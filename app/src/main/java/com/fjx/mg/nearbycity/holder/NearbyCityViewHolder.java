package com.fjx.mg.nearbycity.holder;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.friend.addfriend.AddFriendActivity;
import com.fjx.mg.friend.imuser.NewImUserDetailActivity;
import com.fjx.mg.nearbycity.NearbyCityDetailActivity;
import com.fjx.mg.nearbycity.PublisherNearbyCityActivity;
import com.fjx.mg.nearbycity.adapter.NearbyCityAdapter;
import com.fjx.mg.utils.DialogUtil;
import com.fjx.mg.widget.recyclerview.ItemDecoration;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.common.utils.ViewUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.TotalCityCircleListModel;
import com.library.repository.repository.RepositoryFactory;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.qcloud.uikit.TimConfig;

import java.util.List;

/**
 * Author    by hanlz
 * Date      on 2019/10/17.
 * Description：
 */
public class NearbyCityViewHolder extends RecyclerView.ViewHolder implements OnItemClickListener, OnItemChildClickListener {

    private RecyclerView mRvNearbyCity;
    private NearbyCityAdapter mAdapter;

    public NearbyCityViewHolder(@NonNull View itemView) {
        super(itemView);
        mRvNearbyCity = itemView.findViewById(R.id.rvNearbyCity);
        mAdapter = new NearbyCityAdapter();
        mRvNearbyCity.setAdapter(mAdapter);
        mRvNearbyCity.addItemDecoration(new ItemDecoration(itemView.getContext(), ItemDecoration.VERTICAL_LIST));
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
    }


    public void setData(List<TotalCityCircleListModel> data) {
        if (!data.isEmpty()) {
            mAdapter.setList(data);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        TotalCityCircleListModel model = (TotalCityCircleListModel) adapter.getItem(position);
        view.getContext().startActivity(NearbyCityDetailActivity.newIntent(view.getContext(), model.getcId()));
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        TotalCityCircleListModel model = (TotalCityCircleListModel) adapter.getItem(position);
        if (view.getId() == R.id.tvLike) {
            if (UserCenter.hasLogin()) {
                final TextView praise = (TextView) view;

                if (model.isLike()) {
                    model.setLike(false);
                    ViewUtil.setDrawableLeft(praise, R.drawable.like_gray);
                    String likeNum = model.getLikeNum();
                    model.setLikeNum(StringUtil.subtract(likeNum, "1", 0L));
                    praise.setText(model.getLikeNum());
                    CommonToast.toast(praise.getContext().getString(R.string.cancelPraise));
                    cancelPraise(model, praise);
                } else {
                    model.setLike(true);
                    ViewUtil.setDrawableLeft(praise, R.drawable.like_red);
                    String likeNum = model.getLikeNum();
                    model.setLikeNum(StringUtil.add(likeNum, "1"));
                    praise.setText(model.getLikeNum());
                    CommonToast.toast(praise.getContext().getString(R.string.praise_success));
                    praise(model, praise);
                }
            } else {
                    new DialogUtil().showAlertDialog(view.getContext(), R.string.tips, R.string.not_login_forward_login, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UserCenter.goLoginActivity();
                    }
                });
            }


        } else if (view.getId() == R.id.ivHeaderPic) {
            String uid = "";
            if (TimConfig.isRelease) {
                uid = "MGM".concat(model.getuId());
            } else {
                uid = "fjx".concat(model.getuId());
            }
            TIMFriend friend = UserCenter.getFriend(uid);
            if (friend == null) {
                //非好友
                view.getContext().startActivity(AddFriendActivity.newInstance(view.getContext(), uid));
            } else {
                //好友
                view.getContext().startActivity(NewImUserDetailActivity.newInstance(view.getContext(), uid));
            }
        }
    }

    private void praise(final TotalCityCircleListModel model, final TextView praise) {
        //点赞
        RepositoryFactory.getRemoteNearbyCitysApi()
                .praise("", "", model.getcId())
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
//                        ViewUtil.setDrawableLeft(praise, R.drawable.like_red);
//                        String likeNum = model.getLikeNum();
//                        model.setLikeNum(StringUtil.add(likeNum, "1"));
//                        praise.setText(model.getLikeNum());
//                        CommonToast.toast(praise.getContext().getString(R.string.praise_success));
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        cancelPraise(model, praise);
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    private void cancelPraise(final TotalCityCircleListModel model, final TextView praise) {
        //取消点赞
        RepositoryFactory.getRemoteNearbyCitysApi()
                .cancelPraise("", "", model.getcId())
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
//                        ViewUtil.setDrawableLeft(praise, R.drawable.like_gray);
//                        String likeNum = model.getLikeNum();
//                        model.setLikeNum(StringUtil.subtract(likeNum, "1", 0l));
//                        praise.setText(model.getLikeNum());
//                        CommonToast.toast(praise.getContext().getString(R.string.cancelPraise));
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        praise(model, praise);
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }
}