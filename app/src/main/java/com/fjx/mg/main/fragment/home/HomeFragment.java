package com.fjx.mg.main.fragment.home;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.dialog.ScanQrDialogx;
import com.fjx.mg.house.HouseHomeActivity;
import com.fjx.mg.job.JobHomeActivity;
import com.fjx.mg.main.MainActivity;
import com.fjx.mg.main.fragment.news.tab.NewsAdapter;
import com.fjx.mg.main.more.MoreActivity;
import com.fjx.mg.main.netspeed.NetSpeedActivity;
import com.fjx.mg.main.rate.RateActivity;
import com.fjx.mg.main.scan.QRCodeCollectionActivity;
import com.fjx.mg.main.scan.paymentcode.PaymentCodeActivity;
import com.fjx.mg.main.search.SearchActivity;
import com.fjx.mg.main.translate.TranslateActivity;
import com.fjx.mg.main.yellowpage.YellowPageActivityV1;
import com.fjx.mg.nearbycity.NearbyCityActivity;
import com.fjx.mg.network.KiosqueNetworkActivity;
import com.fjx.mg.news.detail.NewsDetailActivity;
import com.fjx.mg.recharge.center.RechargeCenterActivityx;
import com.fjx.mg.setting.feedback.FeedBackActivity;
import com.fjx.mg.utils.AdClickHelper;
import com.fjx.mg.utils.DialogUtil;
import com.fjx.mg.utils.HttpUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.library.common.base.BaseMvpFragment;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.CommonToast;
import com.library.common.utils.DimensionUtil;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.MulLanguageUtil;
import com.library.common.utils.SharedPreferencesUtil;
import com.library.common.utils.StringUtil;
import com.library.common.utils.ViewUtil;
import com.library.common.view.BannerView;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.Constant;
import com.library.repository.data.UserCenter;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.models.AdListModel;
import com.library.repository.models.AdModel;
import com.library.repository.models.NewsItemModel;
import com.library.repository.models.NewsListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.util.LogUtil;
import com.lxj.xpopup.XPopup;
import com.stx.xmarqueeview.XMarqueeView;
import com.tencent.qcloud.uikit.TimConfig;
import com.tencent.qcloud.uikit.common.utils.ScreenUtil;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Response;

import static java.util.Locale.ENGLISH;
import static java.util.Locale.FRENCH;
import static java.util.Locale.SIMPLIFIED_CHINESE;
import static java.util.Locale.TRADITIONAL_CHINESE;

public class HomeFragment extends BaseMvpFragment<HomePresenter> implements HomeContract.View {

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;

    @BindView(R.id.XMarqueeView)
    XMarqueeView marqueeView;

    @BindView(R.id.tvHint)
    TextView tvHint;
    @BindView(R.id.tvLocation)
    TextView tvLocation;

    @BindView(R.id.homeBanner)
    BannerView bannerView;
    @BindView(R.id.cvBanner)
    CardView cvBanner;
    @BindView(R.id.tvMore)
    TextView tvMore;

    @BindView(R.id.ivRightIcon)
    ImageView ivRightIcon;
    private NewsAdapter adapter;
    private ScanQrDialogx scanQrDialogx;
    private String mJson;
    private MaterialDialog contactDialog;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewUtil.setDrawableLeft(tvLocation, 0);
        ViewUtil.drawableRight(tvLocation, R.drawable.arrow_down);
        Locale locale = MulLanguageUtil.getLocalLanguage();
        if (locale.equals(SIMPLIFIED_CHINESE)) {
            tvLocation.setText("简体");
        } else if (locale.equals(TRADITIONAL_CHINESE)) {
            tvLocation.setText("繁体");
        } else if (locale.equals(FRENCH)) {
            tvLocation.setText("Français");
        } else if (locale.equals(ENGLISH)) {
            tvLocation.setText("English");
        }else {
            tvLocation.setText("English");
        }

        mPresenter.getRecommendStore();
        tvLocation.setVisibility(View.VISIBLE);
        adapter = new NewsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(1));
        try {
            String json = SharedPreferencesUtil.name("BannerList".concat(String.valueOf(TimConfig.isRelease))).getString("jsonHomeshowBanners", "");
            if (StringUtil.isNotEmpty(json)) {
                LogUtil.printJson("HomeFragment", json, "AdListModel form HomeFragment 154line");
                AdListModel statusLs = JsonUtil.strToModel(json, AdListModel.class);
                if (statusLs != null) {
                    if (statusLs.getAdList().size() > 0) {
                        showBanners(statusLs);
                    }
                }
                mPresenter.getNewsList(1);
                mPresenter.getAd();
            } else {
                refreshView.autoRefresh();
            }
        } catch (Exception e) {
            Log.e("Exception:", "" + e.toString());
        }

        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                mPresenter.getNewsList(page);
                mPresenter.getAd();
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                startActivity(NewsDetailActivity.newInstance(getCurContext(), ((NewsListModel)adapter.getItem(position)).getNewsId()));
            }
        });

        //宽高5：1
        CardView.LayoutParams params = (CardView.LayoutParams) bannerView.getLayoutParams();
        int width = DimensionUtil.getScreenWith() - DimensionUtil.dip2px(20);
        params.width = width;
        params.height = width / 4;
        bannerView.setLayoutParams(params);

        getUnReadPoint();

        List<NewsListModel> dataList = DBDaoFactory.getNewsListDao().queryList(1);
        if (dataList.isEmpty()) return;
        adapter.setNewInstance(dataList);
    }

    private void getUnReadPoint() {
        if (!UserCenter.hasLogin()) {
            return;
        }
        new HttpUtil().sendPost(Constant.HOST + "stat/unReadCount", new HttpUtil.OnRequestListener() {
            @Override
            public void onSuccess(String json) {
                //此处拿到红点json通过不同类型手动解析
                //{"code":10000,"msg":"\u6210\u529f","data":{"momentsFriendCount":"0","momentsRecCount":"3","cityCircleUnReadList":{"1":0,"2":0,"3":3,"4":3,"7":1,"8":0,"9":2,"11":0}}}
                mJson = json;
                LogUtil.printJson("netLog", json, "HomeFragment 190row stat/unReadCount");
            }

            @Override
            public void onFailed() {

            }

            @Override
            public void onSuccess(Response response) {

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    boolean change = true;

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }

    @Override
    public void responseFailed(ResponseModel data) {
        refreshView.finishLoading();//请求失败关闭刷新动画
        hideLoading();
    }

    @Override
    public void showRecommendStore() {

    }

    @Override
    public void doLoadVisible() {
        ((MainActivity) getCurActivity()).getPendencyList();
    }

    @Override
    public void showMarqueeView(List<String> datas) {
    }

    @Override
    public void showNewsList(NewsItemModel newsItemModel) {
        refreshView.noticeAdapterData(adapter, newsItemModel.getNewsList(), newsItemModel.isHasNext());
        tvHint.setVisibility(adapter.getData().size() == 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showCheckLanguage(int position, String text) {
        if (position == 1) {
            tvLocation.setText("繁体");
            boolean need = MulLanguageUtil.updateLocale(Locale.TAIWAN);
            if (need) recreateActivity();
        } else if (position == 2) {
            tvLocation.setText("English");
            boolean need = MulLanguageUtil.updateLocale(Locale.ENGLISH);
            if (need) recreateActivity();
        } else if (position == 3) {
            tvLocation.setText("Français");
            boolean need = MulLanguageUtil.updateLocale(Locale.FRENCH);
            if (need) recreateActivity();
        } else {
            tvLocation.setText("简体");
            boolean need = MulLanguageUtil.updateLocale(Locale.CHINA);
            if (need) recreateActivity();
        }

    }

    @Override
    public void showBanners(final AdListModel data) {
        refreshView.finishLoading();
        try {
            if (data == null) return;
            List<String> imageUrl = new ArrayList<>();
            if (data.getAdList() != null && data.getAdList().size() > 0) {
                for (AdModel model : data.getAdList()) {
                    imageUrl.add(model.getImg());
                }

                if (imageUrl.isEmpty()) return;
                cvBanner.setVisibility(View.VISIBLE);
                bannerView.showImages(imageUrl);
                bannerView.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        AdModel adModel = data.getAdList().get(position);
                        AdClickHelper.clickAd(adModel);
                    }
                });
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    private void recreateActivity() {
        /*getActivity().recreate();
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.lan_anim_enter, R.anim.lan_anim_exit);
        startActivity(MainActivity.newInstance(getCurActivity()).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));*/

        getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().overridePendingTransition(R.anim.activity_alpha_in, R.anim.activity_alpha_out);
        getActivity().finish();
    }

    private void showScanQrDialog() {
        scanQrDialogx = new ScanQrDialogx(getActivity(), new ScanQrDialogx.MenuDeleteDialogClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tvQrScan:
                        scanQrDialogx.dismiss();
                        Intent intent = new Intent(MainActivity.broadcast_tocpatureac);
                        getActivity().sendBroadcast(intent);
                        break;
                    case R.id.tvQrCode:
                        getContext().startActivity(QRCodeCollectionActivity.newInstance(getContext()));
                        scanQrDialogx.dismiss();
                        break;
                    case R.id.tvBarCode:
                        getContext().startActivity(PaymentCodeActivity.newIntent(getContext()));
                        scanQrDialogx.dismiss();
                        break;
                    default:
                }
            }
        });
        new XPopup.Builder(getCurContext()).atView(ivRightIcon).asCustom(scanQrDialogx).show();
    }

    //应用的id,1:充值中心,2:房屋租售,3:求职招聘,4:企业黄页,5:汇率查询,6在线翻译,
    // 7意见反馈,8有偿回答,9担保交易,10,酒店住宿,11机票旅游'，12：营业网点，13：马岛服务
    @OnClick({R.id.tvVoucherCenter, R.id.tvHousingSale, R.id.tvTravel, R.id.tvHotel, R.id.tvJobHunting, R.id.tvLocation,
            R.id.tvYellowPage, R.id.tvRateQuery, R.id.tvTranslate, R.id.tvfeedback, R.id.tvMore, R.id.tvSearch
            , R.id.ivRightIcon, R.id.tvNearbyCity, R.id.tvNetwork, R.id.tvNetSpeed})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvVoucherCenter://充值中心
                mPresenter.recUseApp("1");
                startActivity(RechargeCenterActivityx.newInstance(getCurContext()));
                break;
            case R.id.tvHousingSale://房屋租赁
                mPresenter.recUseApp("2");
                startActivity(HouseHomeActivity.newInstance(getCurContext()));
                break;
            case R.id.tvTravel:
                break;
            case R.id.tvHotel:
                break;
            case R.id.tvJobHunting://求职招聘
                mPresenter.recUseApp("3");
                startActivity(JobHomeActivity.newInstance(getCurContext()));
                break;
            case R.id.tvYellowPage://企业黄页
                mPresenter.recUseApp("4");
                startActivity(YellowPageActivityV1.newInstance(getCurContext()));
                break;
            case R.id.tvRateQuery://汇率查询
                mPresenter.recUseApp("5");
                startActivity(RateActivity.newInstance(getCurActivity()));
                break;
            case R.id.tvTranslate://在线翻译
                mPresenter.recUseApp("6");
                startActivity(TranslateActivity.newInstance(getCurActivity()));
                break;
            case R.id.tvfeedback://意见反馈
                mPresenter.recUseApp("7");
                startActivity(FeedBackActivity.newInstance(getCurContext()));
                break;
            case R.id.tvMore:
                startActivity(MoreActivity.newInstance(getCurContext()));
                break;
            case R.id.tvSearch:
                startActivity(SearchActivity.newInstance(getCurContext()));
                break;

            case R.id.tvLocation:
//                if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.READ_PHONE_STATE,
//                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
//                    location();
//                }
                mPresenter.showLanguageDialog(tvLocation);
                break;
            case R.id.ivRightIcon:
                if (UserCenter.hasLogin()) {
                    showScanQrDialog();
                } else {
                    new DialogUtil().showLoginAlertDialog(getCurActivity());
                }
                break;
            case R.id.tvNetwork://营业网点
                mPresenter.recUseApp("12");
                GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
                int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(getActivity());
                if (resultCode != ConnectionResult.SUCCESS) {
//                    if (googleApiAvailability.isUserResolvableError(resultCode)) {
//                        googleApiAvailability.getErrorDialog(getActivity(), resultCode, 2404).show();
//                    }
                    //未安装谷歌服务
                    final Dialog dialog = new Dialog(getActivity());
                    View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_gms_tips_layout, null);
                    v.findViewById(R.id.tvOk).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();

                        }
                    });
                    v.findViewById(R.id.tvContactService).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            showContactUsDialog();
                        }
                    });
                    dialog.setContentView(v);
                    dialog.show();
                    WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                    int w = ScreenUtil.getScreenWidth(getActivity());
                    params.width = w / 5 * 4;
                    dialog.getWindow().setAttributes(params);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                } else {
                    startActivity(KiosqueNetworkActivity.newIntent(getActivity(), "4"));
                }

//                startActivity(SalesNetworkActivity.newIntent(getActivity(), "4"));
                break;
            case R.id.tvNearbyCity://同城/马岛服务
                mPresenter.recUseApp("13");
                startActivityForResult(NearbyCityActivity.newIntent(getCurContext(), mJson), 233);
                break;
            case R.id.tvNetSpeed://网络测速
                startActivity(new Intent(getActivity(), NetSpeedActivity.class));
                break;
            default:
                break;
        }
    }

    public void showContactUsDialog() {
        contactDialog = new MaterialDialog.Builder(getCurActivity())
                .customView(R.layout.dialog_contact_us, true)
                .backgroundColor(ContextCompat.getColor(getCurContext(), R.color.trans))
                .build();
        contactDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = contactDialog.getCustomView();
        if (view == null) return;
        TextView textEmail = view.findViewById(R.id.textEmail);
        final TextView tvTel = view.findViewById(R.id.tvTel);
        final TextView tvWechat = view.findViewById(R.id.tvWechat);
        textEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactDialog.dismiss();
                StringUtil.copyClip(getString(R.string.mg_mail));
                CommonToast.toast(getString(R.string.copy_success));
            }
        });
        tvTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactDialog.dismiss();
                StringUtil.copyClip(tvTel.getText().toString());
                CommonToast.toast(getString(R.string.copy_success));
            }
        });
        tvWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactDialog.dismiss();
                StringUtil.copyClip(tvWechat.getText().toString());
                CommonToast.toast(getString(R.string.copy_success));
            }
        });
        contactDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 233) {
            getUnReadPoint();
        }
        if (data == null) return;
        if (resultCode == -1 && requestCode == 1) {
        }

    }
}
