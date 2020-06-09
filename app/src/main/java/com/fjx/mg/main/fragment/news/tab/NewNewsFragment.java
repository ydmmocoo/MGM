package com.fjx.mg.main.fragment.news.tab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.dialog.ScanQrDialogx;
import com.fjx.mg.main.MainActivity;
import com.fjx.mg.main.scan.QRCodeCollectionActivity;
import com.fjx.mg.main.scan.paymentcode.PaymentCodeActivity;
import com.fjx.mg.main.search.SearchActivity;
import com.fjx.mg.news.detail.NewsDetailActivity;
import com.fjx.mg.utils.DialogUtil;
import com.library.common.base.BaseMvpFragment;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.data.UserCenter;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.models.NewsItemModel;
import com.library.repository.models.NewsListModel;
import com.lxj.xpopup.XPopup;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author yedeman
 * @date 2020/6/4.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class NewNewsFragment extends BaseMvpFragment<NewsTabPresenter> implements NewsTabContract.View{

    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    private NewsAdapter adapter;
    @BindView(R.id.ivRightIcon)
    ImageView mIvRightIcon;

    private ScanQrDialogx scanQrDialogx;

    private int typeId;

    public static NewNewsFragment newInstance() {
        Bundle args = new Bundle();
        args.putInt("typeId", 2);
        NewNewsFragment fragment = new NewNewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        typeId = getArguments().getInt("typeId");

        adapter = new NewsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(1));
        //adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.layout_empty);
        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                mPresenter.getNewsList(typeId, page, "");
            }
        });
        refreshView.autoRefresh();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                startActivity(NewsDetailActivity.newInstance(getCurContext(), ((NewsListModel)adapter.getItem(position)).getNewsId()));
            }
        });
        List<NewsListModel> dataList = DBDaoFactory.getNewsListDao().queryList(typeId);
        if (dataList.isEmpty()) return;
        adapter.setList(dataList);

    }

//    @Override
//    protected void lazyLoadData() {
//        refreshView.autoRefresh();
//    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_new_news;
    }


    @Override
    protected NewsTabPresenter createPresenter() {
        return new NewsTabPresenter(this);
    }

    @Override
    public void showNewsList(NewsItemModel model) {
        refreshView.noticeAdapterData(adapter, model.getNewsList(), model.isHasNext());
        recyclerView.invalidateItemDecorations();
    }

    @Override
    public void loadError() {
        refreshView.finishLoading();
    }

    @OnClick({R.id.tvSearch, R.id.ivRightIcon})
    public void onViewClicked(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tvSearch:
                startActivity(SearchActivity.newInstance(getCurContext()));
                break;
            case R.id.ivRightIcon:
                if (UserCenter.hasLogin()) {
                    showScanQrDialog();
                } else {
                    new DialogUtil().showLoginAlertDialog(getCurActivity());
                }
                break;
            default:
                break;
        }
    }

    private void showScanQrDialog() {
        scanQrDialogx = new ScanQrDialogx(getActivity(), view -> {
            switch (view.getId()) {
                case R.id.tvQrScan:
                    scanQrDialogx.dismiss();
                    Intent intent = new Intent(MainActivity.broadcast_tocpatureac);
                    getActivity().sendBroadcast(intent);
                    break;
                case R.id.tvQrCode:
                    getContext().startActivity(QRCodeCollectionActivity.newInstance(getContext()));
                    scanQrDialogx.dismiss();
                    break;
                case R.id.tvBarCode:
                    getContext().startActivity(PaymentCodeActivity.newIntent(getContext()));
                    scanQrDialogx.dismiss();
                    break;
                default:
                    break;
            }
        });
        new XPopup.Builder(getCurContext()).atView(mIvRightIcon).asCustom(scanQrDialogx).show();
    }
}
