package com.fjx.mg.me.level;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.gridlayout.widget.GridLayout;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.paylibrary.model.PayExtModel;
import com.common.paylibrary.model.UsagePayMode;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.me.level.list.InviteListActivity;
import com.fjx.mg.pay.PayActivity;
import com.fjx.mg.view.LevelProgress;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StatusBarManager;
import com.library.common.utils.ViewUtil;
import com.library.repository.models.LevelHomeModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class LevelHomeActivity extends BaseMvpActivity<LevelHomePresenter> implements LevelHomeContract.IView {

    @BindView(R.id.clLevelCard)
    ConstraintLayout clLevelCard;
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.tvNickName)
    TextView tvNickName;
    @BindView(R.id.tvLevel)
    TextView tvLevel;
    @BindView(R.id.tvMyInvite)
    TextView tvMyInvite;
    @BindView(R.id.tvMyInviteNum)
    TextView tvMyInviteNum;
    @BindView(R.id.levelProgress)
    LevelProgress levelProgress;
    @BindView(R.id.tvRedPacketQuota)
    TextView tvRedPacketQuota;
    @BindView(R.id.tvUpgrade2)
    TextView tvUpgrade2;
    @BindView(R.id.tvTransferQuota3)
    TextView tvTransferQuota3;
    @BindView(R.id.tvUpgrade3)
    TextView tvUpgrade3;
    @BindView(R.id.tvTransferQuota4)
    TextView tvTransferQuota4;
    @BindView(R.id.tvUpgrade4)
    TextView tvUpgrade4;
    @BindView(R.id.tvTransferQuota5)
    TextView tvTransferQuota5;
    @BindView(R.id.tvUpgrade5)
    TextView tvUpgrade5;

    @BindView(R.id.glLevel1)
    GridLayout glLevel1;
    @BindView(R.id.glLevel2)
    GridLayout glLevel2;
    @BindView(R.id.glLevel3)
    GridLayout glLevel3;
    @BindView(R.id.glLevel4)
    GridLayout glLevel4;
    @BindView(R.id.glLevel5)
    GridLayout glLevel5;

    private Map<String, Object> map = new HashMap<>();


    private int myLevel = 1;
    private LevelHomeModel levelHomeModel;

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, LevelHomeActivity.class);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_level_home;
    }

    @Override
    protected void initView() {
        StatusBarManager.setLightFontColor(this, R.color.colorBlackBg);
        ToolBarManager.with(this).setTitle(getString(R.string.level_rank), R.color.white)
                .setBackgroundColor(R.color.colorBlackBg).setNavigationIcon(R.drawable.iv_back);
        GradientDrawableHelper.whit(clLevelCard).setColor(R.color.colorAccent)
                .setCornerRadius(20, 20, 0, 0);
        mPresenter.getUserRank();


        GradientDrawableHelper.whit(tvUpgrade2).setColor(R.color.colorAccent).setCornerRadius(50);
        GradientDrawableHelper.whit(tvUpgrade3).setColor(R.color.colorAccent).setCornerRadius(50);
        GradientDrawableHelper.whit(tvUpgrade4).setColor(R.color.colorAccent).setCornerRadius(50);
        GradientDrawableHelper.whit(tvUpgrade5).setColor(R.color.colorAccent).setCornerRadius(50);
    }


    @OnClick({R.id.tvMyInvite, R.id.tvUpgrade2, R.id.tvUpgrade3, R.id.tvUpgrade4, R.id.tvUpgrade5, R.id.ivLevelUpDesc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvMyInvite:
                startActivity(InviteListActivity.newInstance(getCurContext()));
                break;
            case R.id.tvRedPacketQuota:
                break;
            case R.id.tvUpgrade2:
                map.put("rank", "2");
                map.put("price", getPayPrice(2));
                PayExtModel extModel = new PayExtModel(UsagePayMode.levle_recharge, map);
                startActivityForResult(PayActivity.newInstance(getCurContext(), JsonUtil.moderToString(extModel)), 111);

                break;

            case R.id.tvUpgrade3:

                CommonToast.toast(getString(R.string.coming_soon));
//                map.put("rank", "3");
//                map.put("price", getPayPrice(3));
//                extModel = new PayExtModel(UsagePayMode.levle_recharge, map);
//                startActivityForResult(PayActivity.newInstance(getCurContext(), JsonUtil.moderToString(extModel)), 111);

                break;

            case R.id.tvUpgrade4:
                CommonToast.toast(getString(R.string.coming_soon));

//                map.put("rank", "4");
//                map.put("price", getPayPrice(4));
//                extModel = new PayExtModel(UsagePayMode.levle_recharge, map);
//                startActivityForResult(PayActivity.newInstance(getCurContext(), JsonUtil.moderToString(extModel)), 111);

                break;

            case R.id.tvUpgrade5:
                CommonToast.toast(getString(R.string.coming_soon));

//                map.put("rank", "5");
//                map.put("price", getPayPrice(5));
//                extModel = new PayExtModel(UsagePayMode.levle_recharge, map);
//                startActivityForResult(PayActivity.newInstance(getCurContext(), JsonUtil.moderToString(extModel)), 111);

                break;

            case R.id.ivLevelUpDesc:
                startActivity(UpgradeActivity.newInstance(getCurContext(), JsonUtil.moderToString(levelHomeModel)));
                break;
        }
    }

    @Override
    protected LevelHomePresenter createPresenter() {
        return new LevelHomePresenter(this);
    }

    @Override
    public void showLevelHomeModel(LevelHomeModel model) {
        levelHomeModel = model;
        tvNickName.setText(model.getUserInfo().getNickName());
        tvLevel.setText(getString(R.string.lv).concat(model.getUserInfo().getRank()));
        tvMyInviteNum.setText(model.getUserInfo().getInviteNum());
        CommonImageLoader.load(model.getUserInfo().getUImg()).circle().placeholder(R.drawable.user_default).into(ivAvatar);
        myLevel = Integer.parseInt(model.getUserInfo().getRank());
//        myLevel = 2;


        levelProgress.setData(myLevel);
        setLevelShow(glLevel1);
        if (myLevel >= 2) {
            GradientDrawableHelper.whit(tvUpgrade2).setColor(R.color.colorGrayBg2).setCornerRadius(50);
            tvUpgrade2.setEnabled(false);
            setLevelShow(glLevel2);
        }
        if (myLevel >= 3) {
            GradientDrawableHelper.whit(tvUpgrade3).setColor(R.color.colorGrayBg2).setCornerRadius(50);
            tvUpgrade3.setEnabled(false);
            setLevelShow(glLevel3);
        }
        if (myLevel >= 4) {
            GradientDrawableHelper.whit(tvUpgrade4).setColor(R.color.colorGrayBg2).setCornerRadius(50);
            tvUpgrade4.setEnabled(false);
            setLevelShow(glLevel4);
        }
        if (myLevel >= 5) {
            GradientDrawableHelper.whit(tvUpgrade5).setColor(R.color.colorGrayBg2).setCornerRadius(50);
            tvUpgrade5.setEnabled(false);
            setLevelShow(glLevel5);
        }


        LevelHomeModel.RankConfBean level2 = getConfigModel(model.getRankConf(), 2);
        if (level2 != null) {
            tvRedPacketQuota.setText(getString(R.string.send_red_packet).concat(level2.getTransferLimits()));
            tvUpgrade2.setText(getString(R.string.want_level).concat("(").concat(getShowPrice(2)).concat(")"));
        }

        LevelHomeModel.RankConfBean level3 = getConfigModel(model.getRankConf(), 3);
        if (level3 != null) {
//            tvTransferQuota3.setText(getString(R.string.transfer_quota).concat(level3.getTransferLimits()));
            tvUpgrade3.setText(getString(R.string.want_level).concat("(").concat(getShowPrice(3)).concat(")"));

        }

        LevelHomeModel.RankConfBean level4 = getConfigModel(model.getRankConf(), 4);
        if (level4 != null) {
//            tvTransferQuota4.setText(getString(R.string.transfer_quota).concat(level4.getTransferLimits()));
            tvUpgrade4.setText(getString(R.string.want_level).concat("(").concat(getShowPrice(4)).concat(")"));

        }

        LevelHomeModel.RankConfBean level5 = getConfigModel(model.getRankConf(), 5);
        if (level5 != null) {
//            tvTransferQuota5.setText(getString(R.string.transfer_quota).concat(level5.getTransferLimits()));
            tvUpgrade5.setText(getString(R.string.want_level).concat("(").concat(getShowPrice(5)).concat(")"));
        }


        GradientDrawableHelper.whit(tvUpgrade3).setColor(R.color.colorGrayBg2).setCornerRadius(50);
        GradientDrawableHelper.whit(tvUpgrade4).setColor(R.color.colorGrayBg2).setCornerRadius(50);
        GradientDrawableHelper.whit(tvUpgrade5).setColor(R.color.colorGrayBg2).setCornerRadius(50);
    }


    private String getShowPrice(int level) {

        LevelHomeModel.RankConfBean myLevelBean = getConfigModel(levelHomeModel.getRankConf(), myLevel);
        LevelHomeModel.RankConfBean curBean = getConfigModel(levelHomeModel.getRankConf(), level);

        if (level <= myLevel) return curBean.getShortPrice();

        String levelP = curBean.getPrice();
        int levelPrice = Integer.parseInt(levelP);

        String p = myLevelBean.getPrice();
        int myLevelPrice = Integer.parseInt(p);

        int subPrice = levelPrice - myLevelPrice;

        if (subPrice < 1000) {
            return String.valueOf(subPrice);
        } else if (subPrice < 1000000) {
            return String.valueOf(subPrice / 1000).concat("k");
        } else {
            return String.valueOf(subPrice / 10000).concat("M");
        }
    }

    private String getPayPrice(int level) {

        LevelHomeModel.RankConfBean myLevelBean = getConfigModel(levelHomeModel.getRankConf(), myLevel);
        LevelHomeModel.RankConfBean curBean = getConfigModel(levelHomeModel.getRankConf(), level);

        if (level <= myLevel) return curBean.getShortPrice();

        String levelP = curBean.getPrice();
        int levelPrice = Integer.parseInt(levelP);

        String p = myLevelBean.getPrice();
        int myLevelPrice = Integer.parseInt(p);

        int subPrice = levelPrice - myLevelPrice;
        return String.valueOf(subPrice);
    }

    private void setLevelShow(GridLayout glLevel) {
        int glLevel1Count = glLevel.getChildCount();
        for (int i = 0; i < glLevel1Count; i++) {
            View view = glLevel.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                textView.setTextColor(ContextCompat.getColor(getCurContext(), R.color.textColor));
                ViewUtil.setDrawableLeft(textView, R.drawable.circle_black_shape);
            }
        }
    }


    private LevelHomeModel.RankConfBean getConfigModel(List<LevelHomeModel.RankConfBean> list, int level) {
        for (LevelHomeModel.RankConfBean item : list) {
            if (item.getRank() == level) return item;
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == requestCode) mPresenter.getUserRank();
    }
}
