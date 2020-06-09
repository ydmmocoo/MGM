package com.fjx.mg.me.wallet.detail;

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
import com.fjx.mg.friend.notice.detail.IMNoticeDetailActivity;
import com.fjx.mg.utils.SharedPreferencesHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.models.BalanceDetailModel;
import com.library.repository.models.BillRecordModel;

import butterknife.BindView;

public class BalanceDetailActivity extends BaseMvpActivity<BalanceDetailPresenter> implements BalanceDetailContract.View {


    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;
    private BalanceDetailAdapter mAdapter;

    @Override
    protected BalanceDetailPresenter createPresenter() {
        return new BalanceDetailPresenter(this);
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, BalanceDetailActivity.class);
        return intent;
    }


    @Override
    protected int layoutId() {
        return R.layout.ac_common_refresh_list_toolbar;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.Balance_details));


        mAdapter = new BalanceDetailAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.addItemDecoration(new SpacesItemDecoration(1));
        recycler.setAdapter(mAdapter);
        refreshView.autoRefresh();
        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                mPresenter.getBalanceList(page);
            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                String id = mAdapter.getItem(position).getBalanceId();
                startActivity(IMNoticeDetailActivity.newInstance(getCurContext(), id, IMNoticeDetailActivity.TYPE_BALANCE_DETAIL));
            }
        });
        try {
            SharedPreferencesHelper sp = new SharedPreferencesHelper(this);
            Gson gson = new Gson();
            String json = sp.getString("BalanceDetailModel");
            if (!json.equals("")) {
                Log.e("BalanceDetailModel:", "" + json);
                BalanceDetailModel statusLs = gson.fromJson(json, new TypeToken<BalanceDetailModel>() {
                }.getType());

                showBalanceDetail(statusLs, 1);
            }
            sp.close();
        } catch (Exception e) {
            Log.e("Exception:", "" + e.toString());
        }

    }

    @Override
    public void showBalanceDetail(BalanceDetailModel detailModel, int page) {
        if (page == 1) {
            SharedPreferencesHelper sp = new SharedPreferencesHelper(this);
            Gson gson = new Gson();
            sp.putString("BalanceDetailModel", gson.toJson(detailModel));
            sp.close();
        }
        refreshView.noticeAdapterData(mAdapter, detailModel.getBalanceList(), detailModel.isHasNext());
    }

    @Override
    public void loadBalanceError() {
        refreshView.finishLoading();

    }
}
