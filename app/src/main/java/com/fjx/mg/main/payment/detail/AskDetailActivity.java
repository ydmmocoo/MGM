package com.fjx.mg.main.payment.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.common.paylibrary.model.PayExtModel;
import com.common.paylibrary.model.UsagePayMode;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.dialog.TipDialog;
import com.fjx.mg.image.ImageActivity;
import com.fjx.mg.main.payment.answer.AnswerActivity;
import com.fjx.mg.main.payment.detail.tippay.TipPayActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StatusBarManager;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.data.UserCenter;
import com.library.repository.models.PriceListModel;
import com.library.repository.models.QuestionInfo;
import com.library.repository.models.QuestionReplyModel;
import com.library.repository.models.UserInfoModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class AskDetailActivity extends BaseMvpActivity<AskDetailPresenter> implements AskDetailContract.View {

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvDetail)
    TextView tvDetail;


    @BindView(R.id.imageRecycler)
    RecyclerView imageRecycler;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @BindView(R.id.tvMoney)
    TextView tvMoney;

    @BindView(R.id.tvBtn)
    TextView tvBtn;

    @BindView(R.id.tvCommentNum)
    TextView tvCommentNum;

    @BindView(R.id.height)
    View height;

    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;

    private String qId;
    private AnswerListAdapter commentAdapter;
    private boolean isMine = false;
    private int status = 3;
    private TipDialog tiplog;
    private List<PriceListModel.PriceListBean> list = new ArrayList<>();
    private QuestionInfo.QuestionInfoBean questionInfo;
    private AskDetailImageAdapter imageAdapter;
    @BindView(R.id.nestScrollView)
    NestedScrollView nestScrollView;

    private int pos = -1;
    private int top = -1;
    private boolean isTop = false;

    private int x;
    private int y;

    public static Intent newInstance(Context context, String qId) {
        Intent intent = new Intent(context, AskDetailActivity.class);
        intent.putExtra("qId", qId);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_ask_detail;
    }

    @Override
    protected AskDetailPresenter createPresenter() {
        return new AskDetailPresenter(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = (int) event.getX();
        y = (int) event.getY();
//        CommonToast.toast("X轴："+x+"   Y轴："+y);
        return super.onTouchEvent(event);
    }

    @Override
    protected void initView() {

        StatusBarManager.setLightFontColor(this, R.color.colorAccent);
        ToolBarManager.with(this).setTitle(getString(R.string.back));


        qId = getIntent().getStringExtra("qId");
        mPresenter.getQuestionInfo(qId);
        mPresenter.getPriceList();


        imageAdapter = new AskDetailImageAdapter();
        imageRecycler.setLayoutManager(new GridLayoutManager(getCurContext(), 4));
        imageRecycler.addItemDecoration(new SpacesItemDecoration(0, 0));
        ((SimpleItemAnimator) imageRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        imageRecycler.setAdapter(imageAdapter);
        imageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                String urls = JsonUtil.moderToString(imageAdapter.getData());
                startActivity(ImageActivity.newInstance(getCurContext(), urls, position));
            }
        });

        commentAdapter = new AnswerListAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recyclerView.setAdapter(commentAdapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(0));
        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                if (refreshView.isRefresh()) {
                    mPresenter.getQuestionInfo(qId);
                    mPresenter.getQuestionReplyList(qId, 1, isMine, status);
                } else {
                    mPresenter.getQuestionReplyList(qId, page, isMine, status);
                }

            }
        });
        commentAdapter.OnCLickOpenListenr(new AnswerListAdapter.OnCLickOpenListenr() {
            @Override
            public void onClickOpen(final int position, QuestionReplyModel.ReplyListBean item) {
                int top = recyclerView.getChildAt(position).getTop();
                int bottom1 = recyclerView.getChildAt(position).getBottom();
                Log.e("top:", "" + top);
                final List<QuestionReplyModel.ReplyListBean> data = commentAdapter.getData();

                for (int i = 0; i < data.size(); i++) {
                    if (i == position) {
                        data.get(i).setOpen(!data.get(i).getOpen());
                    }
                }
                commentAdapter.setList(data);
                if (data.get(position).getPositions() == -1) {
                    data.get(position).setPositions(bottom1);
                } else {
                    nestScrollView.scrollTo(0, data.get(position).getPositions());
                }
            }
        });
        commentAdapter.setPraiseListenr(new AnswerListAdapter.OnCLickPraiseListenr() {


            @Override
            public void onClickPraise(final int position, final QuestionReplyModel.ReplyListBean item, Boolean me) {
//                if (me) {
//                    mPresenter.deletQ(commentAdapter.getData().get(position).getRId());
//                } else
                if (item.getMine() && status == 1) {
                    new MaterialDialog.Builder(getCurActivity())
                            .title(R.string.accept)
                            .content(R.string.is_accept)
                            .positiveText(R.string.ok)
                            .negativeText(R.string.cancel)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    mPresenter.acceptReply(commentAdapter.getData().get(position).getRId());
                                }
                            })
                            .show();


                } else if (list.size() < 5) {
                    CommonToast.toast(R.string.no_reward_list_was_received);
                } else {
                    tiplog = new TipDialog(list, AskDetailActivity.this,
                            R.style.Theme_Dialog_Scale, new TipDialog.TipDialogClickListener() {

                        @Override
                        public void onClick(View view, String s) {
                            switch (view.getId()) {
                                case R.id.tvSend:
                                    Map<String, Object> mapa = new HashMap<>();
                                    mapa.put("price", s);
                                    mapa.put("rId", item.getRId());
                                    String ext = JsonUtil.moderToString(new PayExtModel(UsagePayMode.question_pay, mapa));
                                    startActivityForResult(TipPayActivity.newInstance(getCurContext(), ext), 101);
                                    tiplog.dismiss();
                                    break;
                                case R.id.tvSendCancle:
                                    tiplog.dismiss();
                                    break;
                            }
                        }
                    });
                    tiplog.show();
                }
            }
        });
        commentAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
            }
        });
    }

    @Override
    public void deleteReplySuccess() {
        //删除回复成功
        refreshView.autoRefresh();
    }

    @OnClick(R.id.tvBtn)
    public void clicktvBtn() {
        if (status == 1) {
            if (isMine) {
                new MaterialDialog.Builder(getCurActivity())
                        .title(R.string.close_question)
                        .content(R.string.is_close_question)
                        .positiveText(R.string.ok)
                        .negativeText(R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                mPresenter.closeQuestion(qId);
                            }
                        })
                        .show();


            } else {
                startActivityForResult(AnswerActivity.newInstance(getCurContext(), qId), 18);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 18 && resultCode == 18) {
            refreshView.autoRefresh();
        }
        if (requestCode == 101 && resultCode == 111) {//打赏成功刷新
            refreshView.autoRefresh();
        }
    }

    @Override
    public void ShowInfo(QuestionInfo info) {
        questionInfo = info.getQuestionInfo();
        if (questionInfo == null) {
            return;
        }

        if (questionInfo.getImages() != null) {
            imageAdapter.setList(questionInfo.getImages());
        }


        tvTitle.setText(questionInfo.getQuestion());
        tvDetail.setText(questionInfo.getDesc());
        tvCommentNum.setText(questionInfo.getReply_count().concat(getString(R.string.number)).concat(getString(R.string.question)));

        status = Integer.parseInt(questionInfo.getStatus());

        UserInfoModel userInfo = UserCenter.getUserInfo();
        String uId = userInfo.getUId();
        isMine = uId.equals(questionInfo.getUid());
        mPresenter.getQuestionReplyList(qId, 1, isMine, status);
        switch (status) {
            case 1://进行中
                tvMoney.setText(getString(R.string.money_reward).concat("：").concat(questionInfo.getPrice()));
                tvMoney.setTextColor(getResources().getColor(R.color.colorAccent));
                tvBtn.setText(isMine ? getString(R.string.no_satisfactory_answer) : getString(R.string.i_will_answer));
                ToolBarManager.with(this).setTitle(isMine ? getString(R.string.my_question) : getString(R.string.payment_problem));
                tvBtn.setTextColor(getResources().getColor(R.color.white));
                tvBtn.setBackground(getResources().getDrawable(R.drawable.solid_setamount_shape));
                Drawable nav_up = getResources().getDrawable(R.drawable.ask_pen);
                nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                tvBtn.setCompoundDrawables(isMine ? null : nav_up, null, null, null);
                break;
            case 2://有采纳答案结束
                ToolBarManager.with(this).setTitle(getString(R.string.back));
                tvMoney.setText(getString(R.string.sent_money_raward).concat("：").concat(questionInfo.getPrice()));
                tvMoney.setTextColor(getResources().getColor(R.color.black_text));
                tvBtn.setText(getString(R.string.finished));
                tvBtn.setTextColor(getResources().getColor(R.color.gray_text));
                tvBtn.setBackground(getResources().getDrawable(R.drawable.solid_gray_shape));
                break;
            case 3://无采纳答案结束
                ToolBarManager.with(this).setTitle(getString(R.string.back));
                tvMoney.setText(getString(R.string.back_monet_raward).concat("：").concat(questionInfo.getPrice()));
                tvMoney.setTextColor(getResources().getColor(R.color.gray_text));
                tvBtn.setText(getString(R.string.finished));
                tvBtn.setTextColor(getResources().getColor(R.color.gray_text));
                tvBtn.setBackground(getResources().getDrawable(R.drawable.solid_gray_shape));
                break;
        }
    }

    @Override
    public void CloseSuccess() {
        mPresenter.getQuestionInfo(qId);
    }

    @Override
    public void acceptReplySuccess() {
        finish();
    }

    @Override
    public void ShowList(QuestionReplyModel list) {
        refreshView.noticeAdapterData(commentAdapter, list.getReplyList(), list.isHasNext());
    }

    @Override
    public void ShowPriceList(List<PriceListModel.PriceListBean> datas) {
        list = datas;
    }
}
