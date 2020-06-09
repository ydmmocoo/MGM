package com.fjx.mg.food;

import android.widget.ListView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.food.adapter.LvBusinessQualificationAdapter;
import com.library.common.base.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 商家资质
 */
public class BusinessQualificationActivity extends BaseActivity {

    @BindView(R.id.lv_content)
    ListView mLvContent;

    private LvBusinessQualificationAdapter mAdapter;
    private ArrayList<String> mList;

    @Override
    protected int layoutId() {
        return R.layout.activity_business_qualification;
    }

    @Override
    protected void initView() {
        mList=getIntent().getStringArrayListExtra("img");
        //设置标题
        ToolBarManager.with(this).setTitle(getResources().getString(R.string.business_qualification));

        mAdapter=new LvBusinessQualificationAdapter(getCurContext(),mList);
        mLvContent.setAdapter(mAdapter);
    }
}