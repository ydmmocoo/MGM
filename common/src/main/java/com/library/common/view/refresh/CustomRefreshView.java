package com.library.common.view.refresh;

import android.content.Context;
import android.util.AttributeSet;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.base.adapter.BaseMulAdapter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

public class CustomRefreshView extends SmartRefreshLayout implements OnRefreshListener, OnLoadMoreListener {

    private int pageNo = DEFAULT_PAGENO;
    private int pageSize = 20;

    public static final int DEFAULT_PAGENO = 1;
    private CustomRefreshListener refreshListener;

    public CustomRefreshView(Context context) {
        super(context);
        init();
    }

    public CustomRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEnableLoadMoreWhenContentNotFull(false);
//        setEnableFooterTranslationContent(false);
        setEnableAutoLoadMore(false);
        setDragRate(0.9f);
        setOnRefreshListener(this);
        setOnLoadMoreListener(this);
    }

    public void setRefreshListener(CustomRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        pageNo = DEFAULT_PAGENO;
        setNoMoreData(false);
        if (refreshListener == null) return;
        refreshListener.onRefreshData(pageNo, pageSize);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        pageNo++;
        if (refreshListener == null) return;
        refreshListener.onRefreshData(pageNo, pageSize);
    }

    /**
     * 取消加载
     */
    public void finishLoading() {
        if (isRefreshing()) finishRefresh();
        if (isLoading()) finishLoadMore();
    }


    /**
     * 下啦刷新
     *
     * @return
     */
    public boolean isRefresh() {
        return pageNo == DEFAULT_PAGENO;
    }


    /**
     * 通知适配器修改数据
     */
    public <T> void noticeAdapterData(BaseAdapter<T> adapter, List<T> list) {
        finishLoading();
        if (pageNo > DEFAULT_PAGENO) {
            adapter.addData(list);
            if (list.size() < pageSize) {
                finishLoadMoreWithNoMoreData();
            }
        } else {
            adapter.setList(list);
        }
    }

    public <T> void noticeAdapterData(BaseAdapter<T> adapter, List<T> list, boolean hasNext) {
        finishLoading();
        if (pageNo > DEFAULT_PAGENO) {
            if (!hasNext) {
                finishLoadMoreWithNoMoreData();
            } else {
                adapter.addData(list);
            }
        } else {
            adapter.setList(list);
        }
    }

    public <T extends MultiItemEntity> void noticeAdapterData(BaseMulAdapter<T> adapter, List<T> list, boolean hasNext) {
        finishLoading();
        if (pageNo > DEFAULT_PAGENO) {
            adapter.addData(list);
            if (!hasNext) {
                finishLoadMoreWithNoMoreData();
            }
        } else {
            adapter.setList(list);
        }
    }

    public void doRefresh() {
        initData();
        autoRefresh();
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageNO() {
        return this.pageNo;
    }

    public void initData() {
        pageNo = DEFAULT_PAGENO;
    }

}
