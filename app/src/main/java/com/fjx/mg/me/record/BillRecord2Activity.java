package com.fjx.mg.me.record;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

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
import com.library.common.constant.IntentConstants;
import com.library.common.utils.ViewUtil;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.models.BillRecordModel;

import java.util.List;

import butterknife.BindView;

public class BillRecord2Activity extends BaseMvpActivity<BillRecord2Presenter> implements BillRecord2Contract.View {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;

    private BillRecord1Adapter record1Adapter;
    private List<BillRecordModel.AccountTypeBean> accountTypeList;
    private List<BillRecordModel.BillTypeBean> billTypeList;
    private String billType, accountType, type;
    private TextView tvSelect;

    @Override
    protected BillRecord2Presenter createPresenter() {
        return new BillRecord2Presenter(this);
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, BillRecord2Activity.class);
        return intent;
    }

    public static Intent newInstance(Context context, String type) {
        Intent intent = new Intent(context, BillRecord2Activity.class);
        intent.putExtra(IntentConstants.TYPE, type);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_common_refresh_list_toolbar;
    }

    @Override
    protected void initView() {
        if (getIntent() != null) {
            type = getIntent().getStringExtra(IntentConstants.TYPE);
        }
        if (TextUtils.equals("7", type)) {
            billType = "2";
            accountType = "7";
            ToolBarManager.with(this).setTitle(getString(R.string.transfer_record));
        } else {
            ToolBarManager.with(this).setTitle(getString(R.string.bill_record));
        }

        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.addItemDecoration(new SpacesItemDecoration(1));
        record1Adapter = new BillRecord1Adapter();
        if (!TextUtils.equals("7", type)) {
            record1Adapter.addHeaderView(getHeaderView());
        }
        recycler.setAdapter(record1Adapter);
        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                mPresenter.getBillRecord(page, billType, accountType);
            }
        });
        refreshView.autoRefresh();
        try {
            SharedPreferencesHelper sp = new SharedPreferencesHelper(this);
            Gson gson = new Gson();
            String json = sp.getString("BillRecordModel");
            if (!json.equals("")) {
                Log.e("BillRecordModel:", "" + json);
                BillRecordModel statusLs = gson.fromJson(json, new TypeToken<BillRecordModel>() {
                }.getType());

                showBillRecore(statusLs, 1);
            }
            sp.close();
        } catch (Exception e) {
            Log.e("Exception:", "" + e.toString());
        }
        record1Adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                //                if (TextUtils.equals(record1Adapter.getItem(position).getSecondType(), "1")
//                        || TextUtils.equals(record1Adapter.getItem(position).getSecondType(), "2"))
//                    return;
                String id = record1Adapter.getItem(position).getBillId();
                startActivityForResult(IMNoticeDetailActivity.newInstance(getCurContext(), id, IMNoticeDetailActivity.TYPE_BILL_DETAIL), 1);
                refreshView.finishLoading();
            }
        });
    }

    private View getHeaderView() {
        final View view = LayoutInflater.from(this).inflate(R.layout.layout_record_header, null);
        tvSelect = view.findViewById(R.id.tvSelect);
        tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSelect.setTextColor(ContextCompat.getColor(getCurContext(), R.color.colorAccent));
                ViewUtil.drawableRight(tvSelect, R.drawable.select_up);
                mPresenter.showSelectDialog(view, accountTypeList, billTypeList);
            }
        });

        return view;
    }


    @Override
    public void showBillRecore(BillRecordModel recordModel, int page) {
        if (page == 1) {
            SharedPreferencesHelper sp = new SharedPreferencesHelper(this);
            Gson gson = new Gson();
            sp.putString("BillRecordModel", gson.toJson(recordModel));
            sp.close();
        }
        /*if (accountTypeList == null || billTypeList == null) {
            accountTypeList = recordModel.getAccountType();
            billTypeList = recordModel.getBillType();
        }*/
        accountTypeList = recordModel.getAccountType();
        billTypeList = recordModel.getBillType();
        refreshView.noticeAdapterData(record1Adapter, recordModel.getRecordList(), recordModel.isHasNext());
    }

    @Override
    public void loadDataerror() {
        refreshView.finishLoading();
    }

    @Override
    public void onSelectType(String typeId, String typeName, boolean isOther) {

        if (isOther) {
            billType = typeId;
        } else {
            accountType = typeId;
        }
        refreshView.autoRefresh();
    }

    @Override
    public void onDialogDismiss() {
        tvSelect.setTextColor(ContextCompat.getColor(getCurContext(), R.color.textColor));
        ViewUtil.drawableRight(tvSelect, R.drawable.select_down);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 111) {
                refreshView.autoRefresh();
            }
        }
    }
}
