package com.fjx.mg.main.payment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.main.payment.ask.AskActivity;
import com.fjx.mg.main.payment.detail.AskDetailActivity;
import com.fjx.mg.main.payment.search.PaymentSearchActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.view.dropmenu.ContentType;
import com.library.common.view.dropmenu.DrapMenuTab;
import com.library.common.view.dropmenu.DropContentView;
import com.library.common.view.dropmenu.DropMenuCLickClickter;
import com.library.common.view.dropmenu.DropMenuHelper;
import com.library.common.view.dropmenu.DropMenuModel;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.models.CmpanydetaisModel;
import com.library.repository.models.QuestionListDetailModel;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 有偿问答
 */
public class PayMentActivity extends BaseMvpActivity<PayMentPresenter> implements PayMentContract.View {

    @BindView(R.id.tvzh)
    DrapMenuTab tvzh;

    @BindView(R.id.dmtCom)
    DrapMenuTab dmtCom;

    @BindView(R.id.dmtTime)
    DrapMenuTab dmtTime;

    @BindView(R.id.dmtPrice)
    DrapMenuTab dmtPrice;

    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    @BindView(R.id.dcview)
    DropContentView dcview;


    private QuestionListAdapter mAdapter;

    private String mCom = "", mTime = "", mPrice = "", title = "";

    public static Intent newInstance(Context context) {
        return new Intent(context, PayMentActivity.class);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_pay_ment;
    }

    @Override
    protected PayMentPresenter createPresenter() {
        return new PayMentPresenter(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {


        mAdapter = new QuestionListAdapter();
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                startActivityForResult(AskDetailActivity.newInstance(getCurContext(), mAdapter.getItem(position).getQId()), -1);
            }
        });


        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.addItemDecoration(new SpacesItemDecoration(1));
        recycler.setAdapter(mAdapter);


        mAdapter.setList(DBDaoFactory.getQuestionListDao().queryList());


        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                mPresenter.getQuestionList(title, mTime, mPrice, mCom, page);
            }
        });


        DropMenuHelper.getInstance().add(tvzh).add(dmtCom).add(dmtTime).add(dmtPrice);


        tvzh.attactDropView(dcview, new DropMenuCLickClickter() {//综合
            @Override
            public void onCLickDropItem(ContentType contentType, DropMenuModel firstItem, DropMenuModel secondItem) {
                mCom = firstItem.getTypeId();
                refreshView.doRefresh();
            }
        });
        tvzh.setDataList(mPresenter.getzHComDatalist(), ContentType.SINGLE);


        dmtCom.attactDropView(dcview, new DropMenuCLickClickter() {//综合
            @Override
            public void onCLickDropItem(ContentType contentType, DropMenuModel firstItem, DropMenuModel secondItem) {
                mCom = firstItem.getTypeId();
                refreshView.doRefresh();
            }
        });
        dmtCom.setDataList(mPresenter.getTabComDatalist(), ContentType.SINGLE);


        dmtTime.attactDropView(dcview, new DropMenuCLickClickter() {//时间
            @Override
            public void onCLickDropItem(ContentType contentType, DropMenuModel firstItem, DropMenuModel secondItem) {
                mTime = firstItem.getTypeId();
                refreshView.doRefresh();
            }
        });
        dmtTime.setDataList(mPresenter.getTabTimeDatalist(), ContentType.SINGLE);


        dmtPrice.attactDropView(dcview, new DropMenuCLickClickter() {//价格
            @Override
            public void onCLickDropItem(ContentType contentType, DropMenuModel firstItem, DropMenuModel secondItem) {
                mPrice = firstItem.getTypeId();
                refreshView.doRefresh();
            }
        });
        dmtPrice.setDataList(mPresenter.getTabPriceDatalist(), ContentType.SINGLE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView.autoRefresh();
    }

    @Override
    public void onDestroy() {
        DropMenuHelper.getInstance().release();
        super.onDestroy();
    }

    @Override
    public void showQuestionListModel(List<QuestionListDetailModel> data, Boolean next) {
        refreshView.noticeAdapterData(mAdapter, data, next);

    }

    @Override
    public void loadError() {
        refreshView.finishLoading();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        refreshView.autoRefresh();
    }

    @OnClick({R.id.floatbutton, R.id.etSearch, R.id.ivBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.floatbutton:
                startActivityForResult(AskActivity.newInstance(getCurContext()), -1);
                break;

            case R.id.etSearch:
                startActivityForResult(PaymentSearchActivity.newInstance(getCurContext()), -1);
                break;
        }
    }
}
