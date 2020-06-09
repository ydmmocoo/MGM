package com.fjx.mg.recharge.record.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.recharge.detail.RecordDetailActivity;
import com.fjx.mg.utils.SharedPreferencesHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.common.base.BaseMvpFragment;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.Constant;
import com.library.repository.models.PhoneRechargeModel;

import butterknife.BindView;

public class BillRecordTabFragment extends BaseMvpFragment<BillRecordTabPresenter> implements BillRecordTabContract.View {

    public static int TYPE_MOBILE = 1;
    public static int TYPE_ELECT = 2;
    public static int TYPE_NET = 3;
    public static int TYPE_WATER = 4;
    public static int TYPE_DATA = 5;

    private int mType = TYPE_MOBILE;
    private int typeWater = 1;
    private BillRecordAdapter adapter;

    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    public static BillRecordTabFragment newInstance(int type, int typeWater) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putInt("typeWater", typeWater);
        BillRecordTabFragment fragment = new BillRecordTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected BillRecordTabPresenter createPresenter() {
        return new BillRecordTabPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_bill_record_tab;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mType = getArguments().getInt("type");
        typeWater = getArguments().getInt("typeWater");
        adapter = new BillRecordAdapter();
        adapter.setType(mType);
        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new SpacesItemDecoration(1));
        refreshView.autoRefresh();
        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                if (mType == TYPE_MOBILE) {
                    try {
                        int type = types();
                        SharedPreferencesHelper sp = new SharedPreferencesHelper(getActivity());
                        Gson gson = new Gson();
                        String json = sp.getString("PhoneRechargeModel" + typeWater);
                        if (!json.equals("")) {
                            Log.e("PhoneRechargeModel:", "" + json);
                            PhoneRechargeModel statusLs = gson.fromJson(json, new TypeToken<PhoneRechargeModel>() {
                            }.getType());
                            if (statusLs != null && mView != null) {
                                showRecordDatas(statusLs, true);
                            }
                        }
                        sp.close();

                    } catch (Exception e) {
                        Log.e("Exception:", "" + e.toString());
                    }
                    mPresenter.getPhoneRechargeRecord(page, getActivity(),typeWater);
                } else {
                    try {
                        SharedPreferencesHelper sp = new SharedPreferencesHelper(getActivity());
                        Gson gson = new Gson();
                        String json = sp.getString("PhoneRechargeModelx" + typeWater);
                        if (!json.equals("")) {
                            Log.e("PhoneRechargeModelx:", "" + json);
                            PhoneRechargeModel statusLs = gson.fromJson(json, new TypeToken<PhoneRechargeModel>() {
                            }.getType());
                            if (statusLs != null && mView != null) {
                                showRecordDatas(statusLs, true);
                            }
                        }
                        sp.close();

                    } catch (Exception e) {
                        Log.e("Exception:", "" + e.toString());
                    }
                    mPresenter.getRechargeRecord(page, getActivity(),typeWater);
                }

            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                String id = ((PhoneRechargeModel.RecordListBean)adapter.getData().get(position)).getId();
                String type = ((PhoneRechargeModel.RecordListBean)adapter.getData().get(position)).getType();
                startActivity(RecordDetailActivity.newInstance(getCurContext(), id, type, mType == TYPE_MOBILE));
            }
        });

    }


    @Override
    public void showRecordDatas(PhoneRechargeModel model, boolean b) {
        refreshView.noticeAdapterData(adapter, model.getRecordList(), model.isHasNext());
        recycler.invalidateItemDecorations();
        refreshView.finishLoading();
    }

    private int types() {
        int type = getType();
        if (type == TYPE_ELECT) {
            return Constant.RecordType.ELECT;
        } else if (type == TYPE_NET) {
            return Constant.RecordType.NET;
        } else if (type == TYPE_WATER) {
            return Constant.RecordType.WATER;
        }
        return 0;
    }

    private int phoneTypes() {
        int type = getType();
        if (type == TYPE_MOBILE) {
            return Constant.RecordPhoneType.PHONE;
        } else {
            return Constant.RecordPhoneType.DATA;
        }
    }

    @Override
    public int getType() {
        return mType;
    }

}
