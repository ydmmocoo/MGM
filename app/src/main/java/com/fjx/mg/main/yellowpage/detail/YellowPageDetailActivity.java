package com.fjx.mg.main.yellowpage.detail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.dialog.ShareDialog;
import com.fjx.mg.friend.addfriend.AddFriendActivity;
import com.fjx.mg.friend.chat.ChatActivity;
import com.fjx.mg.image.ImageActivity;
import com.fjx.mg.main.yellowpage.comment.YellowCommentActivity;
import com.fjx.mg.main.yellowpage.publish.YellowPagePublicActivity;
import com.fjx.mg.news.detail.AdAdapter;
import com.fjx.mg.news.detail.CommentAdapter;
import com.fjx.mg.news.detail.MJavascriptInterface;
import com.fjx.mg.utils.AdClickHelper;
import com.fjx.mg.utils.DialogUtil;
import com.fjx.mg.utils.SharedPreferencesHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.DeviceInfoUtils;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.IntentUtil;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.NetworkUtil;
import com.library.common.utils.StringUtil;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.Constant;
import com.library.repository.data.UserCenter;
import com.library.repository.models.AdListModel;
import com.library.repository.models.AdModel;
import com.library.repository.models.NewsCommentModel;
import com.library.repository.models.YellowPageDetailModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.qcloud.uikit.TimConfig;
import com.tencent.qcloud.uikit.business.chat.model.ElemExtModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 企业黄页详情页
 */
public class YellowPageDetailActivity extends BaseMvpActivity<YellowPageDetailPresenter> implements YellowPageDetailContract.View {

    @BindView(R.id.tvCompanyName)
    TextView mTvCompanyName;
    @BindView(R.id.tvType)
    TextView mTvType;
    @BindView(R.id.tvContact)
    TextView mTvContact;
    @BindView(R.id.tvPhone)
    TextView mTvPhone;
    @BindView(R.id.tvAddress)
    TextView mTvAddress;
    @BindView(R.id.tvReadNum)
    TextView mTvReadnum;
    @BindView(R.id.imageRecycler)
    RecyclerView mRvImage;
    @BindView(R.id.tvDesc)
    TextView mTvDesc;
    @BindView(R.id.webView)
    WebView mWebView;
    private ImageAdapter mImageAdapter;
    private String cid;
    private String change;
    private String uId;
    private String shareUrl, shareTitle, shareDesc, shareImage;
    private boolean isMyPublish;
    private YellowPageDetailModel detailModel;
    private ImageView ivRigntImage;
    private String mgCode;
    private boolean isChange = false;
    @BindView(R.id.llAd)
    LinearLayout mLlAd;
    @BindView(R.id.adRecycler)
    RecyclerView mRvAd;
    private AdAdapter mAdAdapter;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;
    private CommentAdapter commentAdapter;
    @BindView(R.id.tvCommentNum)
    TextView tvCommentNum;
    @BindView(R.id.ivCollect)
    ImageView ivCollect;
    @BindView(R.id.tvSend)
    TextView tvSend;
    @BindView(R.id.etComment)
    EditText etComment;
    @BindView(R.id.nestScrollView)
    NestedScrollView nestScrollView;
    private final int REQUEST_CODE = 86;
    private YellowPageDetailModel data;

    public static Intent newInstance(Context context, String cid, String change) {
        return newInstance(context, cid, change, false);
    }

    public static Intent newInstance(Context context, String cid, boolean isMyPublish) {
        return newInstance(context, cid, "", isMyPublish);
    }

    public static Intent newInstance(Context context, String cid, String change, boolean isMyPublish) {
        return newInstance(context, cid, change, isMyPublish, "");
    }

    public static Intent newInstance(Context context, String cid, String change, boolean isMyPublish, String uId) {
        Intent intent = new Intent(context, YellowPageDetailActivity.class);
        intent.putExtra(IntentConstants.CID, cid);
        intent.putExtra(IntentConstants.IS_MY_PUBLISH, isMyPublish);
        intent.putExtra(IntentConstants.CHANGE, change);
        intent.putExtra(IntentConstants.UID, uId);
        return intent;
    }


    @Override
    protected int layoutId() {
        return R.layout.ac_yellow_page_detail;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {
        GradientDrawableHelper.whit(etComment).setCornerRadius(50).setColor(R.color.colorGrayBg);
        GradientDrawableHelper.whit(tvSend).setColor(R.color.colorAccent).setCornerRadius(50);
        initWebview();
        commentAdapter = new CommentAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recyclerView.setAdapter(commentAdapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(1));
        mPresenter.getAd();
        refreshView.setRefreshListener((page, pageSize) -> {
            if (refreshView.isRefresh()) {
                mPresenter.getCommentList(cid, page);
            } else {
                mPresenter.getCommentList(cid, page);
            }
        });
        refreshView.autoRefresh();
        uId = getIntent().getStringExtra(IntentConstants.UID);
        //isMyPublish = getIntent().getBooleanExtra(IntentConstants.IS_MY_PUBLISH, false);
        cid = getIntent().getStringExtra(IntentConstants.CID);
        mPresenter.updateReadNumCompany(cid);
        change = getIntent().getStringExtra(IntentConstants.CHANGE);

        mAdAdapter = new AdAdapter();
        mRvAd.setLayoutManager(new LinearLayoutManager(getCurContext()));
        mRvAd.setAdapter(mAdAdapter);
        mAdAdapter.setOnItemClickListener((adapter, view, position) -> AdClickHelper.clickAd(mAdAdapter.getItem(position)));
        try {
            SharedPreferencesHelper sp = new SharedPreferencesHelper(this);
            Gson gson = new Gson();
            String json = sp.getString("jsonshowYellowPageBanners");
            if (!json.equals("")) {
                Log.e("json:", "" + json);
                AdListModel statusLs = gson.fromJson(json, new TypeToken<AdListModel>() {
                }.getType());
                Log.e("statusLs:", "" + statusLs.getAdList());
                mLlAd.setVisibility(View.VISIBLE);
                if (statusLs != null) {
                    showAds(statusLs);
                }
            }
            sp.close();

        } catch (Exception e) {
            Log.e("Exception:", "" + e.toString());
        }
        commentAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (UserCenter.hasLogin()) {
                startActivityForResult(YellowCommentActivity.newInstance(getCurContext(),
                        JsonUtil.moderToString(commentAdapter.getData().get(position))), REQUEST_CODE);
            } else {
                new DialogUtil().showAlertDialog(YellowPageDetailActivity.this, R.string.tips, R.string.not_login_forward_login, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UserCenter.goLoginActivity();
                    }
                });
            }
        });

        commentAdapter.setPraiseListenr((position, item, tvFavNum) -> {
            if (UserCenter.hasLogin()) {
                if (StringUtil.isNotEmpty(item.getComentId()))
                    mPresenter.toggleCommentPraise(position, item, tvFavNum);
            } else {
                new DialogUtil().showAlertDialog(YellowPageDetailActivity.this, R.string.tips, R.string.not_login_forward_login, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UserCenter.goLoginActivity();
                    }
                });
            }
        });
        commentAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            if (UserCenter.hasLogin() && TextUtils.equals(commentAdapter.getItem(position).getUserId(), UserCenter.getUserInfo().getUId()))
                mPresenter.delete(commentAdapter.getItem(position).getComentId(), position);
            return true;
        });
        etComment.setOnTouchListener((v, event) -> {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    nestScrollView.scrollTo(0, tvCommentNum.getTop());

                    etComment.setFocusable(true);
                    etComment.setFocusableInTouchMode(true);
                    etComment.requestFocus();
                }
            }, 300);
            return false;

        });
        etComment.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                String content = etComment.getText().toString();
                mPresenter.addComment(cid, content);
                return true;
            }
            return false;
        });
    }

    @OnClick(R.id.ivCollect)
    public void clickCollect() {
//        if (newsDetailModel == null) return;
//        if (UserCenter.needLogin()) return;
//        mPresenter.toggleCollectNews(newsId, newsDetailModel.isCollect());
    }

    private void initWebview() {
        WebSettings webSetting = mWebView.getSettings();
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setSupportZoom(false);
        webSetting.setUseWideViewPort(false);
        webSetting.setLoadWithOverviewMode(true);
        String screenRes = DeviceInfoUtils.getScreenRes();
        try {
            JSONObject object = new JSONObject(screenRes);
            int screenWidth = object.optInt("screenWidth");
            int density = object.optInt("density");
            WebSettings settings = mWebView.getSettings();
            int screenDensity = getResources().getDisplayMetrics().densityDpi;
            WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM;
            switch (density) {
                case DisplayMetrics.DENSITY_LOW://120
                    zoomDensity = WebSettings.ZoomDensity.CLOSE;
                    break;
                case DisplayMetrics.DENSITY_MEDIUM://160
                    zoomDensity = WebSettings.ZoomDensity.MEDIUM;
                    break;
                case DisplayMetrics.DENSITY_XHIGH://240
                    zoomDensity = WebSettings.ZoomDensity.FAR;
                    break;
                default:
                    zoomDensity = WebSettings.ZoomDensity.FAR;
                    break;

            }
            settings.setDefaultZoom(zoomDensity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);//关闭硬件加速 画面会卡顿
        mWebView.setDrawingCacheEnabled(false);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                addImageClickListener(view);//待网页加载完全后设置图片点击的监听方法
            }
        });
    }

    @Override
    protected YellowPageDetailPresenter createPresenter() {
        return new YellowPageDetailPresenter(this);
    }

    @Override
    public void loadError() {
        refreshView.finishLoading();
    }

    @Override
    public void deleteCommentSuccese(int position) {
        commentAdapter.remove(position);
    }

    @Override
    public void showCommentList(NewsCommentModel model) {
        refreshView.noticeAdapterData(commentAdapter, model.getCommentList(), model.isHasNext());
        tvCommentNum.setText(getString(R.string.comment).concat("(").concat(model.getCommentNum()).concat(")"));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (down) {
                    down = false;
                    nestScrollView.scrollTo(0, tvCommentNum.getTop());
                }
            }
        }, 300);
    }

    @Override
    public void toggleCommentPraiseSuccess(int position) {
//        commentAdapter.notifyItemChanged(position);
    }

    @OnClick(R.id.tvSend)
    public void clickSend() {
        String content = etComment.getText().toString();
        if (UserCenter.hasLogin()) {
            mPresenter.addComment(cid, content);
            if (StringUtil.isNotEmpty(content) && new NetworkUtil().isNetworkConnected(this)) {
                addComment();
            }
        } else {
            new DialogUtil().showAlertDialog(this, R.string.tips, R.string.not_login_forward_login, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    UserCenter.goLoginActivity();
                }
            });
        }
    }


    /**
     * 本地先添加数据 不过本地先添加的评论的commentid是空的 不能对这条信息点赞和评论
     */
    private void addComment() {
        //增加评论数
        String substring = tvCommentNum.getText().toString().substring(tvCommentNum.getText().toString().indexOf("(") + 1, tvCommentNum.getText().toString().length() - 1);
        String commentNum = StringUtil.add(substring, "1");
        tvCommentNum.setText(getString(R.string.comment).concat("(").concat(commentNum).concat(")"));
        List<NewsCommentModel.CommentListBean> data = commentAdapter.getData();
        if (data == null && data.size() < 0) {
            data = new ArrayList<>();
        } else {
            NewsCommentModel.CommentListBean commentListBean = new NewsCommentModel.CommentListBean();
            commentListBean.setContent(etComment.getText().toString());
            //获取当前时间
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
//            Date date = new Date(System.currentTimeMillis());
//            commentListBean.setCreateTime(simpleDateFormat.format(date));
            commentListBean.setCreateTime("评论中...");
            commentListBean.setUserNickName(UserCenter.getUserInfo().getUNick());
            commentListBean.setUserAvatar(UserCenter.getUserInfo().getUImg());
            commentListBean.setReplyNum("0");
            data.add(0, commentListBean);
        }
        if (!data.isEmpty()) {
            commentAdapter.setList(data);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                nestScrollView.scrollTo(0, tvCommentNum.getTop());
            }
        }, 300);
    }


    private Boolean down = false;

    @Override
    public void commentSuccess() {
        down = true;
        etComment.setText("");
        mPresenter.getCommentList(cid, 1);
    }

    @Override
    public void commentFailed() {
        if (commentAdapter != null) {
            List<NewsCommentModel.CommentListBean> data = commentAdapter.getData();
            if (data != null && data.size() > 0) {
                data.remove(0);
                commentAdapter.setList(data);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showCompanyDetail(YellowPageDetailModel data) {
        this.data = data;
        detailModel = data;

        if (UserCenter.hasLogin()) {
            String userUId = "";
            if (TimConfig.isRelease) {
                userUId = "MGM".concat(UserCenter.getUserInfo().getUId());
            } else {
                userUId = "fjx".concat(UserCenter.getUserInfo().getUId());
            }
            isMyPublish = StringUtil.equals(userUId, data.getIdentifier());
        } else {
            isMyPublish = false;
        }
        int drawable = isMyPublish ? R.drawable.more_black : R.drawable.share;
        ivRigntImage = ToolBarManager.with(this).setTitle(getResources().getString(R.string.yellow_page)).setRightImage(drawable,
                v -> {
                    if (UserCenter.hasLogin()) {
                        if (isMyPublish) {
                            if (StringUtil.equals(uId, UserCenter.getUserInfo().getUId())) {
                                mPresenter.showPublishDialog(ivRigntImage, true, true, true, isMyPublish);
                            } else {
                                mPresenter.showPublishDialog(ivRigntImage, true, true, true, isMyPublish);
                            }
                        } else {
                            showShareDialog();
                        }
                    } else {
                        new DialogUtil().showAlertDialog(YellowPageDetailActivity.this, R.string.tips, R.string.not_login_forward_login, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                UserCenter.goLoginActivity();
                            }
                        });
                    }
                });

        if (data.getIdentifier() != null) {
            mgCode = data.getIdentifier();
        } else {
            Log.e("getIdentifier", "data.getIdentifier() != null");
        }
        this.detailModel = data;
        mTvCompanyName.setText(data.getTitle());
        mTvType.setText(data.getServiceName() + " " + data.getSecondServiceName());
        mTvContact.setText(data.getContactName());
        mTvPhone.setText(data.getContactPhone());
//        mTvAddress.setText(data.getAddress());
        mTvAddress.setText(data.getCountryName() + data.getCityName() + data.getAddress());
        mTvReadnum.setText(getString(R.string.read, data.getReadCount()));
//        mTvReadnum.setText(data.getReadCount() + getString(R.string.read));
        mTvDesc.setText(data.getDesc());
        String desc = data.getDesc();
        desc = desc.replace("<img", "<img style=max-width:100%;height:auto");
        mWebView.loadDataWithBaseURL(null, desc, "text/html", "utf-8", null);
        String[] images = returnImageUrlsFromHtml(desc);
        mWebView.addJavascriptInterface(new MJavascriptInterface(this, images), "imagelistener");

        mTvDesc.setVisibility(View.GONE);

        mImageAdapter = new ImageAdapter(this);
        mRvImage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRvImage.addItemDecoration(new SpacesItemDecoration(0, 0));
        ((SimpleItemAnimator) mRvImage.getItemAnimator()).setSupportsChangeAnimations(false);
        mRvImage.setAdapter(mImageAdapter);
        mImageAdapter.setList(data.getImgs());
        shareUrl = Constant.HOST.concat("invite/companydetail?id=").concat(cid).concat("&l=").concat(RepositoryFactory.getLocalRepository().getLangugeType());
        shareTitle = data.getTitle();
        shareDesc = data.getSimpleDesc();
        if (data.getImgs() != null && !data.getImgs().isEmpty())
            shareImage = data.getImgs().get(0);

        mImageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                String urls = JsonUtil.moderToString(mImageAdapter.getData());
                startActivity(ImageActivity.newInstance(getCurContext(), urls, position));
            }
        });
//        mPresenter.getAd();
    }

    @Override
    public void getImUserSuccess(TIMUserProfile profile) {
        startActivityForResult(AddFriendActivity.newInstance(getCurContext(), JsonUtil.moderToString(profile)), 1);
    }

    @Override
    public void getImUserSuccess(TIMFriend friend) {
//        startActivity(ImUserDetailActivity.newInstance(getCurContext(),
//                friend.getTimUserProfile().getIdentifier()));

        String string = friend.getTimUserProfile().getNickName();
        if (!TextUtils.isEmpty(friend.getRemark()))
            string = friend.getRemark();
        ChatActivity.startC2CChat(getCurContext(), friend.getIdentifier(), string);

    }

    @Override
    public void editCompanyInfo() {
        startActivityForResult(YellowPagePublicActivity.newInstance(getCurContext(), cid), 11);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode) {
            mPresenter.getCompanyDetail(cid, change, this);
            isChange = true;
        }
        if (requestCode == REQUEST_CODE) {
            if (resultCode == 1) refreshView.autoRefresh();
        }
    }

    @Override
    public void deleteCompany() {
        mPresenter.deleteCompany(cid);
    }

    @Override
    public void deleteSuccess() {
        setResult(111);
        finish();
    }

    @Override
    public void showAds(AdListModel data) {
        if (data == null || data.getAdList().isEmpty()) return;
        SharedPreferencesHelper sp = new SharedPreferencesHelper(this);
        Gson gson = new Gson();
        sp.putString("jsonshowYellowPageBanners", gson.toJson(data));
        sp.close();
        mLlAd.setVisibility(View.VISIBLE);
        List<String> imageUrl = new ArrayList<>();
        for (AdModel model : data.getAdList()) {
            imageUrl.add(model.getImg());
        }
//        if (mAdAdapter == null) {
//            Log.e("mAdAdapter", "null");
//        }
        mAdAdapter.setList(data.getAdList());
//        List<AdModel> adList = data.getAdList();
//        if (!adList.isEmpty()) {
//            for (int i = 0; i < adList.size(); i++) {
//                initAdImageView(adList.get(i));
//            }
//        }
    }

    @Override
    public void share() {
        ShareDialog shareDialog = new ShareDialog(getCurContext());
        shareDialog.setShareParams(shareTitle, shareDesc, shareImage, shareUrl, cid, ElemExtModel.SHARE_YELLOWPAGE, data.getTitle(), (ArrayList<String>) data.getImgs());
        shareDialog.setShareType(ShareDialog.ShareType.web);
        new XPopup.Builder(getCurContext())
                .asCustom(shareDialog)
                .show();
    }


    @OnClick(R.id.tvPhone)
    public void onViewClicked() {
        String phone = mTvPhone.getText().toString();
        IntentUtil.callPhone(phone);
    }

    @OnClick(R.id.tvMG)
    public void onViewClickMg() {
        if (TextUtils.isEmpty(mgCode)) return;
        mPresenter.findImUser(mgCode);
    }


    private void showShareDialog() {
        ShareDialog shareDialog = new ShareDialog(getCurContext());
        shareDialog.setShareParams(shareTitle, shareDesc, shareImage, shareUrl);
        shareDialog.setShareType(ShareDialog.ShareType.web);
        new XPopup.Builder(getCurContext())
                .asCustom(shareDialog)
                .show();
    }

//    /**
//     * 添加广告
//     */
//    private void initAdImageView(final AdModel item) {
//        View adView = LayoutInflater.from(this).inflate(R.layout.item_ad_image, null);
//        ImageView ivImage = adView.findViewById(R.id.ivImage);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(0,0,0,8);
//        adView.setLayoutParams(lp);
//        ivImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AdClickHelper.clickAd(item);
//            }
//        });
//        CommonImageLoader.load(item.getImg()).into(ivImage);
//        ViewGroup viewGroup = (ViewGroup) adView.getParent();
//        if (viewGroup != null) {
//            viewGroup.removeView(adView);
//        }
//        mLlAdContainer.addView(adView);
//    }

    private void addImageClickListener(WebView webView) {
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistener.openImage(this.src);  " +//通过js代码找到标签为img的代码块，设置点击的监听方法与本地的openImage方法进行连接
                "    }  " +
                "}" +
                "})()");
    }

    public static String[] returnImageUrlsFromHtml(String htmlCode) {
        List<String> imageSrcList = new ArrayList<String>();
        Pattern p = Pattern.compile("<img\\b[^>]*\\bsrc\\b\\s*=\\s*('|\")?([^'\"\n\r\f>]+(\\.jpg|\\.bmp|\\.eps|\\.gif|\\.mif|\\.miff|\\.png|\\.tif|\\.tiff|\\.svg|\\.wmf|\\.jpe|\\.jpeg|\\.dib|\\.ico|\\.tga|\\.cut|\\.pic|\\b)\\b)[^>]*>", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(htmlCode);
        String quote = null;
        String src = null;
        while (m.find()) {
            quote = m.group(1);
            src = (quote == null || quote.trim().length() == 0) ? m.group(2).split("//s+")[0] : m.group(2);
            imageSrcList.add(src);
        }
        if (imageSrcList.size() == 0) {
            return null;
        }
        return imageSrcList.toArray(new String[imageSrcList.size()]);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getCompanyDetail(cid, change, this);
    }
}
