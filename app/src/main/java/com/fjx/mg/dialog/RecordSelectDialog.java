package com.fjx.mg.dialog;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.me.record.BillType1Adapter;
import com.fjx.mg.me.record.BillTypeAdapter;
import com.library.common.utils.DimensionUtil;
import com.library.repository.models.BillRecordModel;
import com.lxj.xpopup.core.AttachPopupView;

import java.util.List;

public class RecordSelectDialog extends AttachPopupView {

    private RecyclerView typeRecycler;
    private RecyclerView otherRecycler;
    private BillTypeAdapter accountAdapter;
    private BillType1Adapter otherAdapter;
    private Context context;
    private List<BillRecordModel.BillTypeBean> billTypeList;
    private List<BillRecordModel.AccountTypeBean> accountTypeList;
    private OnSelectListener onSelectListener;

    public RecordSelectDialog(@NonNull Context context, List<BillRecordModel.BillTypeBean> billTypeList,
                              List<BillRecordModel.AccountTypeBean> accountTypeList, final OnSelectListener onSelectListener) {
        super(context);
        this.context=context;
        this.billTypeList=billTypeList;
        this.accountTypeList=accountTypeList;
        this.onSelectListener=onSelectListener;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        typeRecycler = findViewById(R.id.typeRecycler);
        typeRecycler.setLayoutManager(new GridLayoutManager(context, 3));
        accountAdapter = new BillTypeAdapter();
        typeRecycler.setAdapter(accountAdapter);
        accountAdapter.setNewInstance(accountTypeList);
        accountAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                accountAdapter.setSelect(position);
                BillRecordModel.AccountTypeBean b = accountAdapter.getSellectItem();
                if (b == null) {
                    onSelectListener.onSelect("", "", false);
                } else {
                    onSelectListener.onSelect(b.getAId(), b.getName(), false);
                }
                dismiss();
            }
        });


        otherRecycler = findViewById(R.id.otherRecycler);
        otherRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
        otherAdapter = new BillType1Adapter();
        otherRecycler.setAdapter(otherAdapter);
        otherAdapter.setList(billTypeList);
        otherAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                otherAdapter.setSelect(position);
                BillRecordModel.BillTypeBean b = otherAdapter.getSellectItem();
                if (b == null) {
                    onSelectListener.onSelect("", "", true);
                } else {
                    onSelectListener.onSelect(b.getBId(), b.getName(), true);
                }
                dismiss();
            }
        });

        attachPopupContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.trans));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            attachPopupContainer.setElevation(1);
        }
    }

    //    @Override
//    protected Drawable getPopupBackground() {
//        return getResources().getDrawable(R.drawable.solid_nomal_shape);
//    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_record_select;
    }


    @Override
    protected int getPopupWidth() {
        return DimensionUtil.getScreenWith();
    }

    @Override
    protected int getMaxWidth() {
        return DimensionUtil.getScreenWith();
    }

    @Override
    protected void onShow() {
        super.onShow();
    }

    public interface OnSelectListener {
        void onSelect(String typeId, String typeName, boolean isOther);
    }

}
