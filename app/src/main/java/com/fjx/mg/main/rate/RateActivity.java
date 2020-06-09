package com.fjx.mg.main.rate;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.utils.AdClickHelper;
import com.fjx.mg.utils.SharedPreferencesHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.DimensionUtil;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.SoftInputUtil;
import com.library.common.utils.StatusBarManager;
import com.library.common.view.BannerView;
import com.library.repository.models.AdListModel;
import com.library.repository.models.AdModel;
import com.library.repository.models.RateModel;
import com.library.repository.repository.RepositoryFactory;
import com.library.repository.repository.translate.model.RateResultModel;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RateActivity extends BaseMvpActivity<RatePresenter> implements RateContract.View {

    @BindView(R.id.cvBanner)
    CardView cvBanner;

    @BindView(R.id.tvCountryValue)
    TextView tvCountryValue;
    @BindView(R.id.tvImage)
    ImageView tvImage;

    @BindView(R.id.tvConvert)
    TextView tvConvert;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.tvCountryValue2)
    TextView tvCountryValue2;
    @BindView(R.id.tvAmounteValue)
    TextView tvAmounteValue;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private String amount;
    @BindView(R.id.homeBanner)
    BannerView bannerView;
    private String from = "MGA";//马达加斯加 阿里亚里
    private String defaultTo = "CNY";//默认固定转人民币
    private RateAdapter rateAdapter;
    private MaterialDialog deleteDialog;
    private RateModel rate;

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, RateActivity.class);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_rate;
    }

    @Override
    public void showBanners(final AdListModel data) {
        SharedPreferencesHelper sp = new SharedPreferencesHelper(this);
        Gson gson = new Gson();
        sp.putString("jsonshowBanners", gson.toJson(data));
        sp.close();

        if (data == null) return;
        List<String> imageUrl = new ArrayList<>();
        for (AdModel model : data.getAdList()) {
            imageUrl.add(model.getImg());
        }

        if (imageUrl.isEmpty()) return;
        bannerView.showImages(imageUrl);
        cvBanner.setVisibility(View.VISIBLE);
        bannerView.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                AdModel adModel = data.getAdList().get(position);
                AdClickHelper.clickAd(adModel);
            }
        });
    }

    @Override
    protected void initView() {
        rate = new RateModel(R.drawable.area_icon_mdjsj, getCurContext().getString(R.string.rate_aly), "MGA");

        String amount = RepositoryFactory.getLocalRepository().getRateAmount();
        etAmount.setText(amount);
        etAmount.setSelection(etAmount.getText().length());
        StatusBarManager.setLightFontColor(this, R.color.colorAccent);
        GradientDrawableHelper.whit(tvConvert).setColor(R.color.colorAccent).setCornerRadius(50);
        ToolBarManager.with(this).setNavigationIcon(R.drawable.iv_back).setTitle(getString(R.string.exchange_rate_inquiry), R.color.white).setBackgroundColor(R.color.colorAccent);
//        mPresenter.getCurrency();
        mPresenter.initData();
        initRecycler();
        mPresenter.getAd();
        //软键盘的搜索点击事件
        etAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doConvert();
                    return true;
                }
                return false;
            }
        });
        CardView.LayoutParams params = (CardView.LayoutParams) bannerView.getLayoutParams();
        int width = DimensionUtil.getScreenWith() - DimensionUtil.dip2px(0);
        params.width = width;
        params.height = width / 4;
        bannerView.setLayoutParams(params);
        try {
            SharedPreferencesHelper sp = new SharedPreferencesHelper(this);
            Gson gson = new Gson();
            String json = sp.getString("jsonshowBanners");
            if (!json.equals("")) {
                Log.e("json:", "" + json);
                AdListModel statusLs = gson.fromJson(json, new TypeToken<AdListModel>() {
                }.getType());
                cvBanner.setVisibility(View.VISIBLE);
                showBanners(statusLs);
            }
            sp.close();

        } catch (Exception e) {
            Log.e("Exception:", "" + e.toString());
        }
    }


    private void initRecycler() {

        rateAdapter = new RateAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.setAdapter(rateAdapter);
        View header = LayoutInflater.from(getCurContext()).inflate(R.layout.header_rate_add, null);
        rateAdapter.addHeaderView(header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.showRateList(rateAdapter.getData(), tvCountryValue.getText().toString());
            }
        });

        rateAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            deleteDialog = new MaterialDialog.Builder(getCurActivity())
                    .title(getString(R.string.delete))
                    .content(R.string.hint_confirm_delete)
                    .positiveText(R.string.ok)
                    .negativeText(R.string.cancel)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            deleteDialog.dismiss();
                            rateAdapter.remove(position);
                            RepositoryFactory.getLocalRepository().saveRateConvertList(rateAdapter.getData());
                        }
                    })
                    .show();
            return true;
        });
    }

    @Override
    protected RatePresenter createPresenter() {
        return new RatePresenter(this);
    }

    @Override
    public void showConvert(RateResultModel model) {
        if (TextUtils.equals(defaultTo, model.getTo())) {
            tvAmounteValue.setText(model.getCamount());
        } else {

            for (RateModel r : rateAdapter.getData()) {
                if (TextUtils.equals(model.getTo(), r.getAmountKey())) {
                    r.setToAmount(model.getCamount());
                    rateAdapter.notifyDataSetChanged();
                    break;
                }

            }

        }
    }

    @Override
    public void addRateItem(RateModel rateModel) {
        rateAdapter.addData(rateModel);
        doConvert();
    }


    @Override
    public void ChangeFrom(RateModel rateModel) {

        tvCountryValue.setText(rateModel.getAmountName().concat("  ").concat(rateModel.getAmountKey()));
        tvImage.setImageResource(rateModel.getDrawableId());
        from = rateModel.getAmountKey();
        defaultTo = rateModel.getAmountKey();
        doConvert();
    }

    private void doConvert() {
        amount = etAmount.getText().toString();
        mPresenter.convert(amount, from, defaultTo);
        SoftInputUtil.hideSoftInput(getCurActivity());
        mPresenter.batchConvert(amount, from, rateAdapter.getData());
    }

    @Override
    public void DeletTo(RateModel rateModel) {

        List<RateModel> data = rateAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getAmountKey().equals(rateModel.getAmountKey())) {
                rateAdapter.remove(i);
                rateAdapter.addData(rate);

            }
        }
        rate = rateModel;
        ChangeFrom(rateModel);
        RepositoryFactory.getLocalRepository().saveRateConvertList(rateAdapter.getData());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DesTo();
    }

    public void DesTo() {

        List<RateModel> data = rateAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getAmountKey().equals("MGA")) {
                rateAdapter.remove(i);

            }
        }
        RepositoryFactory.getLocalRepository().saveRateConvertList(rateAdapter.getData());
    }

    @Override
    public void defaultShow(List<RateModel> datas) {
        amount = etAmount.getText().toString();
        mPresenter.batchConvert(amount, from, datas);
    }

    @Override
    public void showConvertList(List<RateModel> dates) {
        rateAdapter.setList(dates);
    }


    @OnClick(R.id.tvConvert)
    public void onViewClicked() {
        doConvert();
    }

    @OnClick(R.id.tvCountryValue)
    public void FromClicked() {
        mPresenter.shoFromList(rateAdapter.getData());
    }
}
