package com.fjx.mg.food;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fjx.mg.R;
import com.fjx.mg.food.adapter.RvGoodsSearchAdapter;
import com.fjx.mg.food.adapter.RvTakeOutFoodSearchAdapter;
import com.gyf.immersionbar.ImmersionBar;
import com.library.common.base.BaseActivity;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GoodsSearchActivity extends BaseActivity {

    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    private RvGoodsSearchAdapter mAdapter;
    private List<String> mList=new ArrayList<>();

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, GoodsSearchActivity.class);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_goods_search;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .transparentStatusBar()
                .statusBarDarkFont(true)
                .init();
        //初始化RecyclerView
        mList.add("item");
        mList.add("item");
        mList.add("item");
        mList.add("item");
        mList.add("item");
        LinearLayoutManager manager = new LinearLayoutManager(getCurContext());
        mRvContent.setLayoutManager(manager);
        mAdapter = new RvGoodsSearchAdapter(R.layout.item_store_goods,mList);
        mRvContent.setAdapter(mAdapter);
    }

    @OnClick({R.id.iv_back, R.id.tv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.tv_search://搜索
                break;
        }
    }
}