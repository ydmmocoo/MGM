package com.fjx.mg.nearbycity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.fjx.mg.R;
import com.fjx.mg.nearbycity.adapter.NearbyCityCommentListAdapter;
import com.fjx.mg.nearbycity.event.AddCommentEvent;
import com.fjx.mg.nearbycity.event.CommentEvent;
import com.fjx.mg.utils.DialogUtil;
import com.fjx.mg.widget.viewpager.AutoHeightViewPager;
import com.library.common.base.BaseFragment;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StringUtil;
import com.library.common.utils.ViewUtil;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.NearbyCityCommentListModel;
import com.library.repository.models.NearbyCityCommentModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Author    by hanlz
 * Date      on 2019/10/19.
 * Description：同城详情评论列表
 */
@SuppressLint("ValidFragment")
public class NearbyCityCommentListFragment extends BaseFragment implements OnItemLongClickListener, OnItemClickListener, OnItemChildClickListener {

    @BindView(R.id.refreshView)
    CustomRefreshView mRefreshView;
    @BindView(R.id.rvComment)
    RecyclerView mRvComment;
    AutoHeightViewPager viewPager;
    private NearbyCityCommentListAdapter adapter;
    private String mCId;
    private OnRefreshListener mListener;
    private final int REQUEST_CODE = 666;
    private int mPage = 1;

    public static NearbyCityCommentListFragment newFragment(String mCId, AutoHeightViewPager viewPager) {
        NearbyCityCommentListFragment fragment = new NearbyCityCommentListFragment(viewPager);
        Bundle bundle = new Bundle();
        bundle.putString(IntentConstants.CID, mCId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @SuppressLint("ValidFragment")
    public NearbyCityCommentListFragment(AutoHeightViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_nearby_city_comment_list;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnRefreshListener) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        mCId = getArguments().getString(IntentConstants.CID);

        mRefreshView.autoRefresh();
        mRefreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                mPage = page;
                requestCommentList("1", mCId);
            }
        });

        mRvComment.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NearbyCityCommentListAdapter();
        adapter.addFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.item_placeholder_layout, null));
        mRvComment.setAdapter(adapter);
        adapter.setOnItemLongClickListener(this);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemChildClickListener(this);
        viewPager.setObjectForPosition(view, 0);//0代表tab的位置 0,1,2,3
    }


    public void requestCommentList(String page, String cId) {
        RepositoryFactory.getRemoteNearbyCitysApi()
                .getCommentList(page, cId)
                .compose(RxScheduler.<ResponseModel<NearbyCityCommentModel>>toMain())
                .as(this.<ResponseModel<NearbyCityCommentModel>>bindAutoDispose())
                .subscribe(new CommonObserver<NearbyCityCommentModel>() {
                    @Override
                    public void onSuccess(NearbyCityCommentModel data) {
                        mRefreshView.finishLoading();
                        mListener.onRefresh(mCId);
                        if (mPage == 1) {//直接替换数据
                            adapter.replaceData(data.getCommentList());
                        } else {//刷新数据
                            mRefreshView.noticeAdapterData(adapter, data.getCommentList(), data.isHasNext());//考虑实际，不显示无更多数据，hasNext=true
                            mRvComment.invalidateItemDecorations();
                        }

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        mRefreshView.finishLoading();
                        commentFail();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mRefreshView.finishLoading();
                        CommonToast.toast(data.getMsg());
                        commentFail();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mRefreshView.finishLoading();
                        CommonToast.toast(data.getMsg());
                        commentFail();
                    }
                });
    }

    public void commentFail() {
        if (adapter != null) {
            List<NearbyCityCommentListModel> data = adapter.getData();
            if (data != null && data.size() > 0) {
                data.remove(0);
                adapter.setList(data);
            }
        }
    }

    public void delComment(String commentId) {
        RepositoryFactory.getRemoteNearbyCitysApi()
                .delComment(commentId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(this.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mRefreshView.autoRefresh();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        hideLoading();
                    }
                });
    }

    public void praise(String commentId, String replyId, String cId) {
        RepositoryFactory.getRemoteNearbyCitysApi()
                .praise(commentId, replyId, cId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
//                        CommonToast.toast(getActivity().getString(R.string.praise_success));
//                        requestCommentList("1", mCId);
                    }

                    @Override
                    public void onError(ResponseModel data) {
//                        showLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    public void cancelPraise(String commentId, String replyId, String cId) {
        RepositoryFactory.getRemoteNearbyCitysApi()
                .cancelPraise(commentId, replyId, cId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
//                        CommonToast.toast(getActivity().getString(R.string.cancelPraise));
//                        requestCommentList("1", mCId);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommentEvent(CommentEvent event) {
        requestCommentList("1", event.getCid());
    }

    /**
     * @param event 先在本地添加评论数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddCommentEvent(AddCommentEvent event) {
        String content = event.getContent();
        if (adapter != null) {
            List<NearbyCityCommentListModel> data = adapter.getData();
            if (data == null && data.size() <= 0) {
                data = new ArrayList<>();
            }
            NearbyCityCommentListModel model = new NearbyCityCommentListModel();
            model.setContent(content);
            model.setUserAvatar(UserCenter.getUserInfo().getUImg());
            model.setUserNickName(UserCenter.getUserInfo().getUNick());
            model.setCreateTime(getString(R.string.commenting));
            model.setReplyNum("0");
            data.add(0,model);
            if (!data.isEmpty()){
                adapter.setList(data);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        NearbyCityCommentListModel item = (NearbyCityCommentListModel) adapter.getItem(position);
        if (UserCenter.hasLogin() && TextUtils.equals(((NearbyCityCommentListModel) adapter.getItem(position)).getUserId(), UserCenter.getUserInfo().getUId())) {
            final String comentId = item.getComentId();
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setMessage(getString(R.string.is_delete_review))
                    .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            delComment(comentId);
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton(getString(R.string.think_about_it), null)
                    .create();
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }
        return true;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        startActivityForResult(NearbyCityCommentActivity.newInstance(getCurContext(),
                JsonUtil.moderToString(adapter.getData().get(position))), REQUEST_CODE);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (view.getId() == R.id.tvPraise) {//点赞
            if (UserCenter.hasLogin()){
                final TextView praise = (TextView) view;
                NearbyCityCommentListModel item = (NearbyCityCommentListModel) adapter.getData().get(position);
                if (item.isLike()) {
                    item.setLike(false);
                    ViewUtil.setDrawableLeft(praise, R.drawable.like_gray);
                    String likeNum = item.getLikeNum();
                    item.setLikeNum(StringUtil.subtract(likeNum, "1", 0L));
                    praise.setText(item.getLikeNum());
                    CommonToast.toast(praise.getContext().getString(R.string.cancelPraise));
                    cancelPraise(item.getComentId(), "", mCId);
                } else {
                    item.setLike(true);
                    ViewUtil.setDrawableLeft(praise, R.drawable.like_red);
                    String likeNum = item.getLikeNum();
                    item.setLikeNum(StringUtil.add(likeNum, "1"));
                    praise.setText(item.getLikeNum());
                    CommonToast.toast(praise.getContext().getString(R.string.praise_success));
                    praise(item.getComentId(), "", mCId);
                }
            }else {
                new DialogUtil().showAlertDialog(getCurActivity(), R.string.tips, R.string.not_login_forward_login, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UserCenter.goLoginActivity();
                    }
                });
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            mRefreshView.autoRefresh();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRefreshView != null) {
            mRefreshView.finishLoading();
        }
    }
}
