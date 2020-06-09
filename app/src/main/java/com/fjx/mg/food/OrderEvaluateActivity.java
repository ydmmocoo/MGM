package com.fjx.mg.food;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.dialog.PickImageDialog;
import com.fjx.mg.food.adapter.GvAddImageAdapter;
import com.fjx.mg.food.contract.OrderEvaluateContract;
import com.fjx.mg.food.presenter.OrderEvaluatePresenter;
import com.fjx.mg.view.RoundImageView;
import com.fjx.mg.view.WrapContentGridView;
import com.library.common.base.BaseActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.DimensionUtil;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.SoftInputUtil;
import com.library.common.view.materialratingbar.MaterialRatingBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderEvaluateActivity extends BaseMvpActivity<OrderEvaluatePresenter> implements OrderEvaluateContract.View {

    @BindView(R.id.iv_store_logo)
    RoundImageView mIvStoreLogo;
    @BindView(R.id.tv_store_name)
    TextView mTvStoreName;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.rb_in_general)
    MaterialRatingBar mRbInGeneral;
    @BindView(R.id.rb_flavor)
    MaterialRatingBar mRbFlavor;
    @BindView(R.id.tv_flavor_score)
    TextView mTvFlavorScore;
    @BindView(R.id.rb_packing)
    MaterialRatingBar mRbPacking;
    @BindView(R.id.tv_packing_score)
    TextView mTvPackingScore;
    @BindView(R.id.rb_distribution)
    MaterialRatingBar mRbDistribution;
    @BindView(R.id.tv_distribution_score)
    TextView mTvDistributionScore;
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.gv_pic)
    WrapContentGridView mGvPic;

    private GvAddImageAdapter mAdapter;
    private List<String> mList = new ArrayList<>();

    private String mStoreId;
    private String mOrderId;
    private String mInGeneralScore, mFlavorScore, mPackingScore, mDistributionScore;

    @Override
    protected int layoutId() {
        return R.layout.activity_order_evaluate;
    }

    @Override
    protected void initView() {
        mOrderId = getIntent().getStringExtra("order_id");
        mStoreId = getIntent().getStringExtra("store_id");
        String storeName = getIntent().getStringExtra("store_name");
        String storeLogo = getIntent().getStringExtra("store_logo");
        String goodsName = getIntent().getStringExtra("goods_name");
        String price = getIntent().getStringExtra("price");
        ToolBarManager.with(this).setTitle(getResources().getString(R.string.evaluate));

        //设置logo
        mIvStoreLogo.setRectAdius(DimensionUtil.dip2px(5));
        CommonImageLoader.load(storeLogo)
                .placeholder(R.drawable.big_image_default).into(mIvStoreLogo);
        //设置店名
        mTvStoreName.setText(storeName);
        //设置商品名
        mTvName.setText(goodsName);
        //设置价格
        mTvPrice.setText(getResources().getString(R.string.goods_price, price));
        //默认评分都为5分
        mRbInGeneral.setProgress(5);
        mInGeneralScore = "5";
        mRbFlavor.setProgress(5);
        mFlavorScore = "5";
        setScoreText(mTvFlavorScore,5);
        mRbPacking.setProgress(5);
        mPackingScore = "5";
        setScoreText(mTvPackingScore,5);
        mRbDistribution.setProgress(5);
        mDistributionScore = "5";
        setScoreText(mTvDistributionScore,5);

        //初始化图片
        mList.add("");
        mAdapter = new GvAddImageAdapter(getCurContext(), mList);
        mGvPic.setAdapter(mAdapter);

        setListener();
    }

    private void setListener() {
        //监听总体评分
        mRbInGeneral.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                int score = (int) rating;
                mInGeneralScore = String.valueOf(score);
            }
        });
        //监听口味评分
        mRbFlavor.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                int score = (int) rating;
                mFlavorScore = String.valueOf(score);
                setScoreText(mTvFlavorScore, score);
            }
        });
        //监听包装评分
        mRbPacking.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                int score = (int) rating;
                mPackingScore = String.valueOf(score);
                setScoreText(mTvPackingScore, score);
            }
        });
        //监听配送评分
        mRbDistribution.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                int score = (int) rating;
                mDistributionScore = String.valueOf(score);
                setScoreText(mTvDistributionScore, score);
            }
        });
        //图片添加点击事件
        mGvPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PickImageDialog dialog = new PickImageDialog(getCurContext());
                dialog.setSelectSingle(false);
                new XPopup.Builder(getCurContext())
                        .asCustom(dialog)
                        .show();
                SoftInputUtil.hideSoftInput(getCurActivity());
            }
        });
    }

    @OnClick({R.id.tv_store_name, R.id.tv_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_store_name://点击店铺名
                Intent intent = new Intent(getCurContext(), StoreDetailActivity.class);
                intent.putExtra("id", mStoreId);
                startActivity(intent);
                break;
            case R.id.tv_commit://提交
                String content=mEtContent.getText().toString();
                mPresenter.updateImage(mList,mOrderId,mInGeneralScore,mFlavorScore,mPackingScore,mDistributionScore,content);
                break;
        }
    }

    @Override
    public void addEvaluateSuccess() {
        finish();
    }

    @Override
    protected OrderEvaluatePresenter createPresenter() {
        return new OrderEvaluatePresenter(this);
    }

    private void setScoreText(TextView tv, int rating) {
        switch (rating) {
            case 1:
                tv.setTextColor(ContextCompat.getColor(getCurContext(),R.color.colorAccent));
                tv.setText(getResources().getString(R.string.difference));
                break;
            case 2:
                tv.setTextColor(ContextCompat.getColor(getCurContext(),R.color.gray_text));
                tv.setText(getResources().getString(R.string.commonly));
                break;
            case 3:
                tv.setTextColor(ContextCompat.getColor(getCurContext(),R.color.textColorYellow5));
                tv.setText(getResources().getString(R.string.not_bad));
                break;
            case 4:
                tv.setTextColor(ContextCompat.getColor(getCurContext(),R.color.textColorYellow5));
                tv.setText(getResources().getString(R.string.very_good));
                break;
            case 5:
                tv.setTextColor(ContextCompat.getColor(getCurContext(),R.color.textColorYellow5));
                tv.setText(getResources().getString(R.string.very_nice));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PictureConfig.CHOOSE_REQUEST:
                // 图片、视频、音频选择结果回调
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                mList.clear();
                for (LocalMedia localMedia : selectList) {
                    mList.add(localMedia.getCompressPath());
                }
                if (mList.size() < 3) {
                    mList.add("");
                }
                mAdapter.setData(mList);
                break;
        }
    }
}