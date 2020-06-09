package com.fjx.mg.me.comment.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.house.comment.HouseCommentActivity;
import com.fjx.mg.house.detail.HouseDetailActivity;
import com.fjx.mg.main.payment.detail.AskDetailActivity;
import com.fjx.mg.main.yellowpage.comment.YellowCommentActivity;
import com.fjx.mg.main.yellowpage.detail.YellowPageDetailActivity;
import com.fjx.mg.me.comment.adapter.HouseCommentAdapter;
import com.fjx.mg.me.comment.adapter.NearbyCityMyCommentAdapter;
import com.fjx.mg.me.comment.adapter.NewsCommentAdapter;
import com.fjx.mg.me.comment.adapter.QuestionCommentAdapter;
import com.fjx.mg.me.comment.adapter.YellowCommentAdapter;
import com.fjx.mg.nearbycity.NearbyCityCommentActivity;
import com.fjx.mg.nearbycity.NearbyCityDetailActivity;
import com.fjx.mg.news.comment.NewsCommentActivity;
import com.fjx.mg.news.detail.NewsDetailActivity;
import com.library.common.base.BaseMvpFragment;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.data.UserCenter;
import com.library.repository.models.MyCommentListModel;
import com.library.repository.models.MyCommentModel;
import com.library.repository.models.MyCompanyCommentModel;
import com.library.repository.models.MyHouseCommentModel;
import com.library.repository.models.MyNewsCommentModel;
import com.library.repository.models.MyReplyListCommentModel;
import com.library.repository.models.NearbyCityCommentListModel;
import com.library.repository.models.NewsCommentModel;
import com.library.repository.models.QuestionReplyModel;

import java.util.List;

import butterknife.BindView;

public class MyCommentFragment extends BaseMvpFragment<MyCommentFragmentPresenter> implements MyCommentFragmentContract.View {


    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;
    private HouseCommentAdapter houseCommentAdapter;
    private NewsCommentAdapter newsCommentAdapter;
    private YellowCommentAdapter yellowCommentAdapter;
    private QuestionCommentAdapter questionCommentAdapter;
    private NearbyCityMyCommentAdapter mNearbyCityMyCommentAdapter;
    private MyCommentModel mData;

    @Override
    protected MyCommentFragmentPresenter createPresenter() {
        return new MyCommentFragmentPresenter(this);
    }

    public static MyCommentFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        MyCommentFragment fragment = new MyCommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_common_refresh_list;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        refreshView.autoRefresh();
        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.addItemDecoration(new SpacesItemDecoration(10));

        final int type = getArguments().getInt("type");
        //"生活", "头条", "酒店", "房源"
        switch (type) {
            case 1:
                break;
            case 2:
                initNewsCommentAdapter();
                break;
            case 3:
                break;
            case 4:
                initHouseCommentAdapter();
                break;
            case 5:
                initYellowCommentAdapter();
                break;
            case 6:
                initQuestionCommentAdapter();
                break;
            case 7:
                initNearbyCityMyCommentAdapter();
                break;
            default:
        }
        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                switch (type) {
                    case 1:
                        break;
                    case 2:
                        mPresenter.getMyNewsComment(page);
                        break;
                    case 3:
                        break;
                    case 4:
                        mPresenter.getMyHouseComment(page);
                        break;
                    case 5:
                        mPresenter.getMyCompanyComment(page);
                        break;
                    case 6:
                        mPresenter.getMyQuestionComment(page);
                        break;
                    case 7:
                        mPresenter.requestMyCommentList(page + "");
                        break;
                }
            }
        });
    }

    private void initQuestionCommentAdapter() {
        questionCommentAdapter = new QuestionCommentAdapter(getActivity());
        recycler.setAdapter(questionCommentAdapter);
        questionCommentAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                switch (view.getId()) {
                    case R.id.tvDelete://删除
                        if (questionCommentAdapter.getItem(position).getStatus().equals("1")) {
                            mPresenter.deletQ(questionCommentAdapter.getItem(position).getrId());
                        } else {
                            CommonToast.toast(R.string.question_finished_no_delete_review);
                        }
                        break;
                    case R.id.tvQuestion://问题详情
                        startActivity(AskDetailActivity.newInstance(getCurContext(), questionCommentAdapter.getItem(position).getqId()));
                        break;
                    case R.id.tvBtn://展开收起
                        List<MyReplyListCommentModel.ReplyListBean> data = questionCommentAdapter.getData();
                        for (int i = 0; i < data.size(); i++) {
                            if (i == position) {
                                data.get(i).setOpen(!data.get(i).getOpen());
                            }
                        }
                        questionCommentAdapter.setList(data);
                        recycler.scrollToPosition(position);
                        break;
                }
            }
        });
        questionCommentAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
            }
        });
    }

    private void initYellowCommentAdapter() {
        yellowCommentAdapter = new YellowCommentAdapter();
        recycler.setAdapter(yellowCommentAdapter);
        yellowCommentAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                switch (view.getId()) {
                    case R.id.tvDelete://删除
                        mPresenter.deletY(yellowCommentAdapter.getItem(position).getCommentId(), true);
                        break;
                    case R.id.lltitle:
                        startActivity(YellowPageDetailActivity.newInstance(getCurContext(),
                                yellowCommentAdapter.getItem(position).getcId(), "",false,
                                "-1"));
                        break;
                }
            }
        });
        yellowCommentAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                MyCompanyCommentModel.CommentListBean bean = yellowCommentAdapter.getItem(position);
                NewsCommentModel.CommentListBean model = new NewsCommentModel.CommentListBean();
                model.setComentId(bean.getCommentId());
                model.setUserNickName(UserCenter.getUserInfo().getUNick());
                model.setContent(bean.getContent());
                model.setCreateTime(model.getCreateTime());
                model.setUserAvatar(UserCenter.getUserInfo().getUImg());
                model.setReplyNum(bean.getReplyNum());
                startActivityForResult(YellowCommentActivity.newInstance(getCurContext(),
                        JsonUtil.moderToString(model)), 11);
            }
        });
    }

    private void initNewsCommentAdapter() {
        newsCommentAdapter = new NewsCommentAdapter();
        recycler.setAdapter(newsCommentAdapter);
        newsCommentAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                switch (view.getId()) {
                    case R.id.tvDelete:
                        mPresenter.deleteN(newsCommentAdapter.getItem(position).getCommentId(), true);
                        break;
                    case R.id.lltitle:
                        startActivity(NewsDetailActivity.newInstance(getCurContext(), newsCommentAdapter.getItem(position).getNewsId()));
                        break;
                }
            }
        });

        newsCommentAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                MyNewsCommentModel.CommentListBean bean = newsCommentAdapter.getItem(position);
                NewsCommentModel.CommentListBean model = new NewsCommentModel.CommentListBean();
                model.setLikeNum(bean.getLikeNum());
                model.setComentId(bean.getCommentId());
                model.setUserNickName(UserCenter.getUserInfo().getUNick());
                model.setContent(bean.getContent());
                model.setCreateTime(model.getCreateTime());
                model.setUserAvatar(UserCenter.getUserInfo().getUImg());
                model.setReplyNum(bean.getReplyNum());
                startActivityForResult(NewsCommentActivity.newInstance(getCurContext(),
                        JsonUtil.moderToString(model)), 11);
            }
        });
    }

    private void initHouseCommentAdapter() {
        houseCommentAdapter = new HouseCommentAdapter();
        recycler.setAdapter(houseCommentAdapter);
        houseCommentAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                switch (view.getId()) {
                    case R.id.tvDelete:
                        mPresenter.deleteh(houseCommentAdapter.getItem(position).getCommentId(), true);
                        break;
                    case R.id.lltitle:
                        startActivity(HouseDetailActivity.newInstance(getCurContext(), houseCommentAdapter.getItem(position).getHouseId()));

                        break;
                }
            }
        });
        houseCommentAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                MyHouseCommentModel.CommentListBean bean = houseCommentAdapter.getItem(position);
                NewsCommentModel.CommentListBean model = new NewsCommentModel.CommentListBean();
                model.setComentId(bean.getCommentId());
                model.setUserNickName(UserCenter.getUserInfo().getUNick());
                model.setContent(bean.getContent());
                model.setCreateTime(model.getCreateTime());
                model.setUserAvatar(UserCenter.getUserInfo().getUImg());
                model.setReplyNum(bean.getReplyNum());
                startActivityForResult(HouseCommentActivity.newInstance(getCurContext(),
                        JsonUtil.moderToString(model)), 11);
            }
        });
    }

    private void initNearbyCityMyCommentAdapter() {
        mNearbyCityMyCommentAdapter = new NearbyCityMyCommentAdapter();
        recycler.setAdapter(mNearbyCityMyCommentAdapter);

        mNearbyCityMyCommentAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                switch (view.getId()) {
                    case R.id.tvDelete:
                        MyCommentListModel item = (MyCommentListModel) adapter.getData().get(position);
                        if (UserCenter.hasLogin() && TextUtils.equals(((NearbyCityCommentListModel) adapter.getItem(position)).getUserId(), UserCenter.getUserInfo().getUId())) {
                            final String comentId = item.getCommentId();
                            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                                    .setMessage(R.string.is_delete_review)
                                    .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            mPresenter.delComment(comentId);
                                            showLoading();
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .setNegativeButton(R.string.think_about_it, null)
                                    .create();
                            if (!dialog.isShowing()) {
                                dialog.show();
                            }
                        }
                        break;
                    case R.id.lltitle:
                        MyCommentListModel data = (MyCommentListModel) adapter.getData().get(position);
                        startActivity(NearbyCityDetailActivity.newIntent(getCurContext(),data.getcId()));
                        break;
                }
            }
        });
        mNearbyCityMyCommentAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                MyCommentListModel data = (MyCommentListModel) adapter.getData().get(position);
                startActivity(NearbyCityDetailActivity.newIntent(getCurContext(),data.getcId()));
            }
        });
    }

    @Override
    public void deleteReplySuccess() {
        //删除回复成功
        refreshView.autoRefresh();
    }

    @Override
    public void responseMyCommentList(MyCommentModel data) {
        this.mData = data;
        refreshView.noticeAdapterData(mNearbyCityMyCommentAdapter, data.getCommentList(), data.isHasNext());
    }

    @Override
    public void showMyHouseCommentModel(MyHouseCommentModel model) {
        refreshView.noticeAdapterData(houseCommentAdapter, model.getCommentList(), model.isHasNext());
    }

    @Override
    public void showMyNewsCommentModel(MyNewsCommentModel model) {
        refreshView.noticeAdapterData(newsCommentAdapter, model.getCommentList(), model.isHasNext());
    }

    @Override
    public void ShowMyCompanyComment(MyCompanyCommentModel model) {
        refreshView.noticeAdapterData(yellowCommentAdapter, model.getCommentList(), model.isHasNext());
    }

    @Override
    public void ShowMyReplyListComment(MyReplyListCommentModel model) {
        refreshView.noticeAdapterData(questionCommentAdapter, model.getReplyList(), model.isHasNext());
    }

    @Override
    public void loadError() {
        refreshView.finishLoading();
    }


}
