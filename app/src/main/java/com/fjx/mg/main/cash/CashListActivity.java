package com.fjx.mg.main.cash;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.fjx.mg.R;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.StatusBarManager;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.models.CashListModel;
import com.tencent.qcloud.uikit.common.utils.SoundPlayUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CashListActivity extends BaseMvpActivity<CashListPresenter> implements CashListContract.View {


    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tvTotalMoney)
    TextView tvTotalMoney;
    @BindView(R.id.cvList)
    CardView cvList;
    @BindView(R.id.ivGetAll)
    ImageView ivGetAll;
    @BindView(R.id.ivClose)
    ImageView ivClose;
    @BindView(R.id.ivGig)
    ImageView ivGig;
    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;

    private CashAdapter cashAdapter;
    private int status = 2;

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, CashListActivity.class);
        return intent;
    }

    @Override
    protected int layoutId() {
        mCommonStatusBarEnable = false;
        return R.layout.ac_cash;
    }


    @Override
    protected CashListPresenter createPresenter() {
        return new CashListPresenter(this);
    }

    @Override
    protected void initView() {
        StatusBarManager.transparentNavigationBar(this);
        CommonImageLoader.load(R.drawable.cash).into(ivGig);

        cashAdapter = new CashAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.addItemDecoration(new SpacesItemDecoration(2));
        recycler.setAdapter(cashAdapter);
        cashAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                String id = cashAdapter.getItem(position).getCId();
                mPresenter.reciveCash(id, position);
            }
        });
        refreshView.autoRefresh();
        refreshView.setRefreshListener(new CustomRefreshListener() {


            @Override
            public void onRefreshData(int page, int pageSize) {
                mPresenter.getCashList(page, status);
            }
        });
    }


    @OnClick({R.id.ivGetAll, R.id.ivClose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivGetAll:
                mPresenter.batchReciveCash();
                break;
            case R.id.ivClose:
                onBackPressed();
                break;
        }
    }

    private void showGif() {
        SoundPlayUtils.play(3);
        ivGig.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ivGig.setVisibility(View.GONE);
            }
        }, 1200);
    }

    @Override
    public void showCashList(CashListModel data) {
        refreshView.noticeAdapterData(cashAdapter, data.getCashList(), data.isHasNext());
    }

    @Override
    public void showReciveCashResult(String balance, int position) {
        showGif();
        CashListModel.CashModel model = cashAdapter.getItem(position);
        model.setStatus("1");
        cashAdapter.notifyDataSetChanged();
    }

    @Override
    public void showBatchReciveCashResult(String balance) {
        status = 1;
        refreshView.doRefresh();
        showGif();
    }

    @Override
    public void loadError() {
        refreshView.finishLoading();
    }
}
