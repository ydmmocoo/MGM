package com.fjx.mg.me.level;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.library.common.base.BaseActivity;
import com.library.common.utils.JsonUtil;
import com.library.repository.models.LevelHomeModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpgradeActivity extends BaseActivity {

    @BindView(R.id.tvHint2)
    TextView tvHint2;
    @BindView(R.id.tvHint3)
    TextView tvHint3;
    @BindView(R.id.tvHint4)
    TextView tvHint4;
    @BindView(R.id.tvHint5)
    TextView tvHint5;
    private LevelHomeModel homeModel;

    public static Intent newInstance(Context context, String model) {
        Intent intent = new Intent(context, UpgradeActivity.class);
        intent.putExtra("model", model);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_upgrade;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.upgrade_desc));
        String mod = getIntent().getStringExtra("model");
        homeModel = JsonUtil.strToModel(mod, LevelHomeModel.class);
        if (homeModel == null) return;


        LevelHomeModel.RankConfBean level2 = getConfigModel(homeModel.getRankConf(), 2);
        tvHint2.setText(getString(R.string.upgrade_hint2).concat(level2.getShortPrice()).concat(")"));

        LevelHomeModel.RankConfBean level3 = getConfigModel(homeModel.getRankConf(), 3);
        tvHint3.setText(getString(R.string.upgrade_hint3).concat(level3.getShortPrice()).concat(")"));

        LevelHomeModel.RankConfBean level4 = getConfigModel(homeModel.getRankConf(), 4);
        tvHint4.setText(getString(R.string.upgrade_hint4).concat(level4.getShortPrice()).concat(")"));

        LevelHomeModel.RankConfBean level5 = getConfigModel(homeModel.getRankConf(), 5);
        tvHint5.setText(getString(R.string.upgrade_hint5).concat(level5.getShortPrice()).concat(")"));


    }


    private LevelHomeModel.RankConfBean getConfigModel(List<LevelHomeModel.RankConfBean> list, int level) {
        for (LevelHomeModel.RankConfBean item : list) {
            if (item.getRank() == level) return item;
        }
        return null;
    }

}
