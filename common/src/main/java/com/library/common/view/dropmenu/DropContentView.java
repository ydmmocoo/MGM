package com.library.common.view.dropmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.library.common.R;

import java.util.ArrayList;
import java.util.List;

public class DropContentView extends FrameLayout {
    private LinearLayout contentLayout;
    private RecyclerView firstRecycler;
    private RecyclerView secondRecycler;
    private FrameLayout parentView;
    private ContentType contentType;
    private DropMenuCLickClickter dropMenuCLickClickter;
    private DropItemAdapter firstAdapter, secondAapter;
    private List<DropMenuModel> dataList;


    public DropContentView(Context context) {
        super(context);
        init(context);
    }

    public DropContentView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.layout_drop_view, this);
        setVisibility(GONE);
        contentLayout = findViewById(R.id.contentLayout);
        firstRecycler = findViewById(R.id.firstRecycler);
        secondRecycler = findViewById(R.id.secondRecycler);
    }

    public void setDataList(List<DropMenuModel> dataList, ContentType contentType) {
        this.contentType = contentType;
        this.dataList = dataList;
        switch (contentType) {
            case SINGLE:
                setFirstAdapter(dataList, contentType);
                break;
            case DOUBLE:
                setFirstAdapter(dataList, contentType);
                setSecondAdapter(dataList, contentType);
                break;
        }

    }

    private void setFirstAdapter(List<DropMenuModel> dataList, final ContentType contentType) {
        secondRecycler.setVisibility(GONE);
        firstRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        firstAdapter = new DropItemAdapter();
        firstAdapter.setList(dataList);
        firstRecycler.setAdapter(firstAdapter);
        firstAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                selectFirstData(firstAdapter, position);
                if (contentType == ContentType.SINGLE) {
                    dropMenuCLickClickter.onCLickDropItem(ContentType.SINGLE, firstAdapter.getItem(position), null);
                } else {
                    secondAapter.setList(firstAdapter.getItem(position).getChildList());
                }
            }
        });
    }

    private void selectFirstData(DropItemAdapter firstAdapter, int item) {
        for (int i = 0; i < firstAdapter.getData().size(); i++) {
            DropMenuModel model = firstAdapter.getData().get(i);
            model.setSelect(i == item);
        }
        if (contentType == ContentType.DOUBLE) firstAdapter.notifyDataSetChanged();
    }

    private void selectecondData(DropItemAdapter secondAapter, int item) {
        for (int i = 0; i < secondAapter.getData().size(); i++) {
            DropMenuModel model = secondAapter.getData().get(i);
            model.setSelect(i == item);
        }
        secondAapter.notifyDataSetChanged();
    }


    private void setSecondAdapter(List<DropMenuModel> dataList, ContentType contentType) {
        if (dataList == null) return;
        secondRecycler.setVisibility(VISIBLE);
        secondRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        secondAapter = new DropItemAdapter();
        secondAapter.setList(dataList);
        secondRecycler.setAdapter(secondAapter);
        if (dataList.isEmpty()) {
            secondAapter.setList(new ArrayList<DropMenuModel>());
        } else {
            secondAapter.setList(dataList.get(0).getChildList());
        }

        secondAapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                selectecondData(secondAapter, position);
                dropMenuCLickClickter.onCLickDropItem(ContentType.DOUBLE, null, secondAapter.getItem(position));
            }
        });

    }

    public void setDropMenuCLickClickter(DropMenuCLickClickter dropMenuCLickClickter) {
        this.dropMenuCLickClickter = dropMenuCLickClickter;
    }
}
