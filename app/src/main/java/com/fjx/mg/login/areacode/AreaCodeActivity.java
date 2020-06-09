package com.fjx.mg.login.areacode;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.quicksidebar.QuickSideBarTipsView;
import com.bigkoo.quicksidebar.QuickSideBarView;
import com.bigkoo.quicksidebar.listener.OnQuickSideBarTouchListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.GradientDrawableHelper;
import com.library.repository.models.AreaCodeModel;
import com.library.repository.models.AreaCodeSectionModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class AreaCodeActivity extends BaseMvpActivity<AreaCodePresenter> implements AreaCodeContract.View, OnQuickSideBarTouchListener {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.quickSideBarTipsView)
    QuickSideBarTipsView quickSideBarTipsView;
    @BindView(R.id.quickSideBarView)
    QuickSideBarView quickSideBarView;
    @BindView(R.id.etSearch)
    EditText etSearch;


    private AreaCodeAdapter areaCodeAdapter;
    private LinearLayoutManager layoutManager;

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, AreaCodeActivity.class);
        return intent;
    }


    @Override
    protected AreaCodePresenter createPresenter() {
        return new AreaCodePresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_area_code;
    }


    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.areacode_select));
        quickSideBarView.setOnQuickSideBarTouchListener(this);
        areaCodeAdapter = new AreaCodeAdapter(new ArrayList<AreaCodeSectionModel>());
        GradientDrawableHelper.whit(etSearch).setColor(R.color.white);
        mPresenter.getNationList();
        layoutManager = new LinearLayoutManager(getCurContext());
        recycler.setLayoutManager(layoutManager);
        recycler.addItemDecoration(new SpacesItemDecoration(1));
        recycler.setAdapter(areaCodeAdapter);
        mPresenter.getAreaCodeList(null);
        mPresenter.searchWatcher(etSearch);

        areaCodeAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                AreaCodeSectionModel model = areaCodeAdapter.getItem(position);
                if (model.isHeader()) return;
                if (model == null) return;
                String areaCode = model.getData().getTel();
                Intent intent = new Intent();
                intent.putExtra("areaCode", areaCode);
                setResult(111, intent);
                finish();
            }
        });

    }

    @Override
    public void showAreaCodeList(List<AreaCodeSectionModel> datas, List<String> heads) {
        areaCodeAdapter.setList(datas);
        quickSideBarView.setLetters(heads);
    }

    @Override
    public void showNationList(List<AreaCodeModel> datas) {
        if (datas == null || datas.isEmpty()) return;
        View view = View.inflate(getCurContext(), R.layout.layout_list, null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getCurContext()));
        final AreaCodeHeaderAdapter headerAdapter = new AreaCodeHeaderAdapter();
        headerAdapter.setList(datas);
        recyclerView.setAdapter(headerAdapter);
        headerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                String areaCode = headerAdapter.getItem(position).getTel();
                Intent intent = new Intent();
                intent.putExtra("areaCode", areaCode);
                setResult(111, intent);
                finish();
            }
        });
        areaCodeAdapter.addHeaderView(view);
        layoutManager.scrollToPosition(0);

    }

    @Override
    public void onLetterChanged(String letter, int position, float y) {
        quickSideBarTipsView.setText(letter, position, y);
        layoutManager.scrollToPositionWithOffset(index(letter), 0);
    }

    private int index(String letter) {
        List<AreaCodeSectionModel> list = areaCodeAdapter.getData();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isHeader()) {
                if (TextUtils.equals(list.get(i).getHeader(), letter)) {
                    Log.d("onLetterChanged", list.get(i).getHeader() + "--" + letter + "--" + i);
                    return i;

                }
            }
        }
        return 0;
    }


    @Override
    public void onLetterTouching(boolean touching) {
        quickSideBarTipsView.setVisibility(touching ? View.VISIBLE : View.GONE);
    }
}
