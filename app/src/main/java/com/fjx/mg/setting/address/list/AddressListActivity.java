package com.fjx.mg.setting.address.list;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.setting.address.edit.EditAddressActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.JsonUtil;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.models.AddressModel;

import butterknife.BindView;

public class AddressListActivity extends BaseMvpActivity<AddressListPresenter> implements AddressListContract.View {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;

    private AddressAdapter mAdapter;

    @Override
    protected AddressListPresenter createPresenter() {
        return new AddressListPresenter(this);
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, AddressListActivity.class);
    }

    public static Intent newInstance(Context context,boolean isFood) {
        Intent intent=new Intent(context, AddressListActivity.class);
        intent.putExtra("isFood",isFood);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_common_refresh_list_toolbar;
    }

    @Override
    protected void initView() {
        boolean isFood=getIntent().getBooleanExtra("isFood",false);
        TextView tvRightText = ToolBarManager.with(this).setTitle(getString(R.string.my_address))
                .setRightText(getString(R.string.new_address),
                        v -> startActivityForResult(EditAddressActivity.newInstance(getCurContext(), null), 1));
        tvRightText.setTextColor(ContextCompat.getColor(getCurContext(), R.color.textColorAccent));
        mAdapter = new AddressAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.addItemDecoration(new SpacesItemDecoration(10));
        recycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent=new Intent();
            intent.putExtra("addressId",mAdapter.getItem(position).getAddressId());
            intent.putExtra("phone",mAdapter.getItem(position).getPhone());
            intent.putExtra("name",mAdapter.getItem(position).getName());
            intent.putExtra("address",mAdapter.getItem(position).getAddress());
            intent.putExtra("roomNo",mAdapter.getItem(position).getRoomNo());
            intent.putExtra("sex",mAdapter.getItem(position).getSex());
            setResult(RESULT_OK,intent);
            finish();
        });
        mAdapter.setOnClickEditListener(position -> {
            String address = JsonUtil.moderToString(mAdapter.getItem(position));
            startActivityForResult(EditAddressActivity.newInstance(getCurContext(), address), 1);
        });

        mAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            mPresenter.showDeleteDialog(mAdapter.getItem(position).getAddressId(), position);
            return true;
        });
        refreshView.setRefreshListener((page, pageSize) -> mPresenter.getAddressList(page));
        refreshView.autoRefresh();
    }

    @Override
    public void showAddressList(AddressModel addressModel) {
        refreshView.noticeAdapterData(mAdapter, addressModel.getAddressList(), addressModel.isHasNext());
    }

    @Override
    public void loadAddressError() {
        refreshView.finishLoading();
    }

    @Override
    public void deleteSuccess(int position) {
        mAdapter.remove(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode) {
            refreshView.autoRefresh();
        }
    }
}
