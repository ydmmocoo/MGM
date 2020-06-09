package com.fjx.mg.moments.add.pois;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.services.core.PoiItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.StatusBarManager;

import java.util.List;

import butterknife.BindView;

public class AoisActivity extends BaseMvpActivity<AoisPresenter> implements AoisContract.View {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    private AoisAdapter aoisAdapter;
    private String lng = "";//经度
    private String lat = "";//纬度

    public static Intent newInstance(Context context) {
        return new Intent(context, AoisActivity.class);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_aois;
    }

    @Override
    protected AoisPresenter createPresenter() {
        return new AoisPresenter(this);
    }

    @Override
    protected void initView() {

        StatusBarManager.setLightFontColor(this, R.color.colorAccent);
        ToolBarManager.with(this);

        aoisAdapter = new AoisAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.setAdapter(aoisAdapter);
        recycler.addItemDecoration(new SpacesItemDecoration(1));
        mPresenter.locationAddress();

        aoisAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("lng", lng);
                intent.putExtra("lat", lat);
                intent.putExtra("adr", aoisAdapter.getData().get(position).toString());
                setResult(9, intent);
                finish();
            }
        });
    }

    @Override
    public void LocationSuccess(List<PoiItem> pois, String la, String ln) {
        aoisAdapter.setList(pois);
        lng = ln;//经度
        lat = la;//纬度
    }
}
