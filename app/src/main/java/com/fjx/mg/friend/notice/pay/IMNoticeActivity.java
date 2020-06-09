package com.fjx.mg.friend.notice.pay;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.friend.notice.detail.IMNoticeDetailActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StatusBarManager;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.models.IMNoticeListModel;

import butterknife.BindView;

public class IMNoticeActivity extends BaseMvpActivity<IMNoticePresenter> implements IMNoticeContract.View {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;

    private IMNoticeAdapter noticeAdapter;

    public static Intent newInstance(Context context, String listModel) {
        Intent intent = new Intent(context, IMNoticeActivity.class);
        intent.putExtra("listModel", listModel);
        return intent;
    }


    @Override
    protected int layoutId() {
        return R.layout.ac_common_refresh_list_toolbar;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setNavigationIcon(R.drawable.iv_back).setTitle(getString(R.string.pay_notice), R.color.white)
                .setBackgroundColor(R.color.colorAccent);
        StatusBarManager.setLightFontColor(this, R.color.colorAccent);

        String str = getIntent().getStringExtra("listModel");
        IMNoticeListModel model = JsonUtil.strToModel(str, IMNoticeListModel.class);


        noticeAdapter = new IMNoticeAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.setAdapter(noticeAdapter);
        recycler.addItemDecoration(new SpacesItemDecoration(15));
        if (model != null) {
            noticeAdapter.setList(model.getRecordList());
            if (!model.isHasNext()) {
                refreshView.finishLoadMoreWithNoMoreData();
            } else {
                refreshView.setPageNo(2);
            }
        } else {
            refreshView.autoRefresh();
        }

        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                mPresenter.getNoticeList(page);
            }
        });

        noticeAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                //                if (TextUtils.equals(noticeAdapter.getItem(position).getSecondType(), "1")
//                        || TextUtils.equals(noticeAdapter.getItem(position).getSecondType(), "2"))
//                    return;
                startActivity(IMNoticeDetailActivity.newInstance(getCurContext(),
                        noticeAdapter.getItem(position).getBillId(), IMNoticeDetailActivity.TYPE_PAY_NOTICE));
            }
        });

    }

    @Override
    protected IMNoticePresenter createPresenter() {
        return new IMNoticePresenter(this);
    }


    @Override
    public void showNoticeList(IMNoticeListModel data) {
        refreshView.noticeAdapterData(noticeAdapter, data.getRecordList(), data.isHasNext());
    }

    @Override
    public void loadError() {
        refreshView.finishLoading();
    }
}
