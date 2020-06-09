package com.library.common.view.dropmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.library.common.R;
import com.library.common.utils.ContextManager;

import java.util.List;

public class DrapMenuTab extends AppCompatTextView {

    private int selectTextColor;
    private int unSelectTextColor;
    private Drawable selectIcon;
    private Drawable unSelectIcon;
    private DropContentView dropContentView;

    private boolean isSelect;
    private ContentType contentType;
    private DropMenuCLickClickter dropMenuCLickClickter;

    private List<DropMenuModel> dataList;

    public DrapMenuTab(Context context) {
        super(context);
    }

    public DrapMenuTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        initResourse(attrs);
    }

    public DrapMenuTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initResourse(attrs);
    }

    private void initResourse(AttributeSet attrs) {
        TypedArray t = getContext().obtainStyledAttributes(attrs, R.styleable.DrapMenuTab);
        selectTextColor = t.getColor(R.styleable.DrapMenuTab_dmt_selectTextColor, Color.RED);
        unSelectTextColor = t.getColor(R.styleable.DrapMenuTab_dmt_un_selectTextColor, Color.BLACK);
        selectIcon = t.getDrawable(R.styleable.DrapMenuTab_dmt_selectIcon);
        selectIcon = t.getDrawable(R.styleable.DrapMenuTab_dmt_selectIcon);
        unSelectIcon = t.getDrawable(R.styleable.DrapMenuTab_dmt_un_selectIcon);
        t.recycle();
        initShow();

        initClickListener();
    }

    private void initClickListener() {

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                List<DrapMenuTab> list = DropMenuHelper.getInstance().getDrapMenuTabs();
                for (DrapMenuTab menuTab : list) {
                    if (v.getId() == menuTab.getId()) continue;
                    menuTab.setSelect(false);
                }


                dropContentView.setDataList(dataList, contentType);
                if (isSelect) {
                    dropContentView.setVisibility(GONE);
                    isSelect = false;
                } else {
                    dropContentView.setVisibility(VISIBLE);
                    isSelect = true;
                }
                initShow();
            }
        });
    }

    public void setSelect(boolean select) {
        this.isSelect = select;
        initShow();
    }


    public void initShow() {

        setTextColor(isSelect ? selectTextColor : unSelectTextColor);
        if (unSelectIcon == null || selectIcon == null) return;
        selectIcon.setBounds(0, 0, selectIcon.getIntrinsicWidth(), selectIcon.getIntrinsicHeight());
        unSelectIcon.setBounds(0, 0, unSelectIcon.getIntrinsicWidth(), unSelectIcon.getIntrinsicHeight());
        setCompoundDrawables(null, null, isSelect ? selectIcon : unSelectIcon, null);
        setCompoundDrawablePadding(dip2px(getContext(), 10));

        if (dropContentView == null) return;
        dropContentView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelect = false;
                dropContentView.setVisibility(GONE);
                initShow();
            }
        });

        dropContentView.setDropMenuCLickClickter(new DropMenuCLickClickter() {
            @Override
            public void onCLickDropItem(ContentType contentType, DropMenuModel firstItem, DropMenuModel secondItem) {
                reShow(contentType, firstItem, secondItem);
            }

        });

    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void attactDropView(DropContentView dropView, final DropMenuCLickClickter dropMenuCLickClickter) {
        this.dropMenuCLickClickter = dropMenuCLickClickter;
        this.dropContentView = dropView;
        dropContentView.setDropMenuCLickClickter(new DropMenuCLickClickter() {
            @Override
            public void onCLickDropItem(ContentType contentType, DropMenuModel firstItem, DropMenuModel secondItem) {
                reShow(contentType, firstItem, secondItem);
            }

        });

        dropContentView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelect = false;
                dropContentView.setVisibility(GONE);
                initShow();
            }
        });
    }

    private void reShow(ContentType contentType, DropMenuModel firstItem, DropMenuModel secondItem) {

        //选择的item替换筛选控件的显示
//        if (contentType == ContentType.SINGLE) {
//            this.setText(firstItem.getTypeName());
//        } else {
//            setText(secondItem.getTypeName());
//        }
        isSelect = false;
        initShow();
        dropContentView.setVisibility(GONE);
        dropMenuCLickClickter.onCLickDropItem(contentType, firstItem, secondItem);
    }

    public void setDataList(List<DropMenuModel> dataList, ContentType type) {
        this.contentType = type;
        this.dataList = dataList;
        dropContentView.setDataList(dataList, contentType);
    }
}
