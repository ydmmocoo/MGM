package com.fjx.mg.news.detail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.JavascriptInterface;
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
import com.fjx.mg.news.adapter.NewsRecommendReadAdapter;
import com.fjx.mg.news.comment.NewsCommentActivity;
import com.fjx.mg.utils.AdClickHelper;
import com.fjx.mg.utils.DialogUtil;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.DeviceInfoUtils;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.NetworkUtil;
import com.library.common.utils.StringUtil;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.Constant;
import com.library.repository.data.UserCenter;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.models.AdListModel;
import com.library.repository.models.AdModel;
import com.library.repository.models.NewsCommentModel;
import com.library.repository.models.NewsDetailModel;
import com.library.repository.models.NewsRecommendReadModel;
import com.library.repository.models.RecommentListModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.tencent.qcloud.uikit.business.chat.model.ElemExtModel;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

public class NewsDetailActivity extends BaseMvpActivity<NewsDetailPresenter> implements NewsDetailContract.View {

    @BindView(R.id.llContent)
    LinearLayout llContent;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;
    @BindView(R.id.tvNewsTitle)
    TextView tvNewsTitle;
    @BindView(R.id.ivUserAvatar)
    ImageView ivUserAvatar;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvReadNum)
    TextView tvReadNum;
    @BindView(R.id.tvCommentNum)
    TextView tvCommentNum;
    @BindView(R.id.etComment)
    EditText etComment;
    @BindView(R.id.tvOrigin)
    TextView tvOrigin;
    @BindView(R.id.tvAuthTag)
    TextView tvAuthTag;
    @BindView(R.id.ivCollect)
    ImageView ivCollect;
    //
//    @BindView(R.id.bannerView)
//    BannerView bannerView;
    @BindView(R.id.llAd)
    LinearLayout llAd;


    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.tvSend)
    TextView tvSend;
    //    @BindView(R.id.llAdContainer)
//    LinearLayout mLlAdContainer;
    @BindView(R.id.rvRecommendRead)
    RecyclerView mRvRecommendRead;
    NewsRecommendReadAdapter mNewsRecommendReadAdapter;
    @BindView(R.id.llRecommendNews)
    LinearLayout mLlRecommendNews;

    @BindView(R.id.adRecycler)
    RecyclerView adRecycler;
    private AdAdapter adAdapter;


    private CommentAdapter commentAdapter;
    private String newsId;

    private NewsDetailModel newsDetailModel;
    private final int REQUEST_CODE = 86;
    public String shareImageUrl;
    private String shareTitle;
    private String shareDesc;
    private String shareWebUrl;
    @BindView(R.id.nestScrollView)
    NestedScrollView nestScrollView;
    private NewsDetailModel model;
    private DialogUtil.Builder mBuilder;

    @Override
    protected NewsDetailPresenter createPresenter() {
        return new NewsDetailPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_news_detail;
    }

    public static Intent newInstance(Context context, String newsId) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra("cid", newsId);
        return intent;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {
        newsId = getIntent().getStringExtra("cid");
        if (newsId == null) {
            Uri uri = getIntent().getData();
            Log.e("url:" + uri, "newsId:" + newsId);
            if (uri != null) {
                // 完整的url信息
                String url = uri.toString();
                newsId = uri.getQueryParameter("id");
            }
        }
        Log.e("newsId", "" + newsId);
        newsDetailModel = DBDaoFactory.getNewsDetailDao().queryModel(newsId);

        GradientDrawableHelper.whit(tvSend).setColor(R.color.colorAccent).setCornerRadius(50);
        ToolBarManager.with(this).setTitle(getString(R.string.news_content)).setRightImage(R.drawable.ic_right_setting,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (UserCenter.hasLogin()) {
                            if (newsDetailModel == null) return;
                            mPresenter.showSettingDialog(v, newsDetailModel.isCollect());
                        } else {
                            new DialogUtil().showLoginAlertDialog(getCurActivity());
                        }
                    }
                });

        initWebview();
        mPresenter.updateReadNum(newsId);
        GradientDrawableHelper.whit(etComment).setCornerRadius(50).setColor(R.color.colorGrayBg);
        GradientDrawableHelper.whit(tvAuthTag).setCornerRadius(50).setColor(R.color.colorAccent);
        GradientDrawableHelper.whit(textView2).setCornerRadius(0).setColor(R.color.colorGrayBg2);
        llContent.setVisibility(View.GONE);

        commentAdapter = new CommentAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recyclerView.setAdapter(commentAdapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(1));
        mPresenter.getAd();
        mPresenter.getNewsDetail(newsId);
        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
//                if (refreshView.isRefresh()) {
//
//                    mPresenter.getCommentList(newsId, page);
//                } else {
//                    mPresenter.getCommentList(newsId, page);
//                }
                mPresenter.getCommentList(newsId, page);
                mPresenter.requestRecommendNews(newsId);
            }
        });

        commentAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if (UserCenter.hasLogin()) {
                    if (StringUtil.isNotEmpty(commentAdapter.getItem(position).getComentId()))
                        startActivityForResult(NewsCommentActivity.newInstance(getCurContext(),
                                JsonUtil.moderToString(commentAdapter.getData().get(position))), REQUEST_CODE);
                } else {
                    new DialogUtil().showAlertDialog(NewsDetailActivity.this, R.string.tips, R.string.not_login_forward_login, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            UserCenter.goLoginActivity();
                        }
                    });
                }
            }
        });

        commentAdapter.setPraiseListenr(new CommentAdapter.OnCLickPraiseListenr() {
            @Override
            public void onClickPraise(int position, NewsCommentModel.CommentListBean item, TextView tvFavNum) {
                if (UserCenter.hasLogin()) {
                    if (StringUtil.isNotEmpty(item.getComentId()))
                        mPresenter.toggleCommentPraise(position, item, tvFavNum);
                } else {
                    new DialogUtil().showAlertDialog(NewsDetailActivity.this, R.string.tips, R.string.not_login_forward_login, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            UserCenter.goLoginActivity();
                        }
                    });
                }
            }
        });
        commentAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (UserCenter.hasLogin() && TextUtils.equals(commentAdapter.getItem(position).getUserId(), UserCenter.getUserInfo().getUId()))
                    mPresenter.delete(commentAdapter.getItem(position).getComentId(), position);
                return true;
            }
        });
        etComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            nestScrollView.scrollTo(0, tvCommentNum.getTop());
                            etComment.setFocusable(true);
                            etComment.setFocusableInTouchMode(true);
                            etComment.requestFocus();
                        } catch (NullPointerException e) {

                        }

                    }
                }, 300);
                return false;

            }
        });
        //软键盘的搜索点击事件
        etComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String content = etComment.getText().toString();
                    mPresenter.addComment(newsId, content);
                    return true;
                }
                return false;
            }
        });

//        if (newsDetailModel == null) {
//
//            return;
//        }
        refreshView.autoRefresh();
//        mPresenter.getCommentList(newsId, 1);
        //showNewsDetail(newsDetailModel);


        adAdapter = new AdAdapter();
        adRecycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        adRecycler.setAdapter(adAdapter);
        adAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                AdClickHelper.clickAd(adAdapter.getItem(position));
            }
        });
        mNewsRecommendReadAdapter = new NewsRecommendReadAdapter();
        mRvRecommendRead.setAdapter(mNewsRecommendReadAdapter);
        mRvRecommendRead.setLayoutManager(new LinearLayoutManager(this));
        mNewsRecommendReadAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                RecommentListModel model = (RecommentListModel) adapter.getItem(position);
                String newsId = model.getNewsId();
                startActivity(NewsDetailActivity.newInstance(NewsDetailActivity.this, newsId));
                finish();
            }
        });

    }

    private int nestedScrollViewTop;

    public void scrollByDistance(int dy) {
        if (nestedScrollViewTop == 0) {
            int[] intArray = new int[2];
            nestScrollView.getLocationOnScreen(intArray);
            nestedScrollViewTop = intArray[1];
        }
        int distance = dy - nestedScrollViewTop;//必须算上nestedScrollView本身与屏幕的距离
        nestScrollView.fling(distance);//添加上这句滑动才有效
        nestScrollView.smoothScrollBy(0, distance);
    }

    private void showShareDialog(String title, String desc, String imageUrl, String webview) {

        ShareDialog shareDialog = new ShareDialog(getCurContext());
        shareDialog.setShareParams(title, desc, imageUrl, webview, newsId, ElemExtModel.SHARE_NEWS, model.getNewsTitle(), model.getImg());
        shareDialog.setShareType(ShareDialog.ShareType.web);
        new XPopup.Builder(getCurContext())
                .asCustom(shareDialog)
                .show();
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
    public void loadError() {
        refreshView.finishLoading();
    }

    @Override
    public void showNewsDetail(NewsDetailModel model) {
        if (model == null) return;
        this.model = model;
        shareTitle = model.getNewsTitle();
//        shareDesc = model.getNewsContent().replace("<p>", "").replace("</p>", "");
        shareDesc = deleteHtml(model.getNewsContent()).replace(" ", "");
        try {
            String inviteCode = UserCenter.getUserInfo().getInviteCode();
            if (StringUtil.isEmpty(inviteCode)) {
                shareWebUrl = "";
            } else {
                shareWebUrl = Constant.HOST + "invite/newsdetail?id=" + model.getNewsId() + "&iv=" + inviteCode + "&l=" + RepositoryFactory.getLocalRepository().getLangugeType();
            }
        } catch (NullPointerException e) {
            shareWebUrl = "";
        }

        newsDetailModel = model;
        llContent.setVisibility(View.VISIBLE);
        refreshView.finishLoading();
        tvNewsTitle.setText(model.getNewsTitle());
        String content = getNewContent(model.getNewsContent());
        content = content.replace("<img", "<img style=max-width:100%;height:auto");
        Log.e("55555","content:"+content);
        mWebView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
//        mWebView.loadDataWithBaseURL(null, model.getNewsContent(), "text/html", "utf-8", null);
        String[] images=returnImageUrlsFromHtml(content);
        mWebView.addJavascriptInterface(new MJavascriptInterface(this,images), "imagelistener");
        tvDate.setText(model.getCreateTime());
        tvReadNum.setText(model.getReadNum().concat(getString(R.string.Read)));
        tvOrigin.setText(model.getNewsAuth());
        ivCollect.setImageResource(model.isCollect() ? R.drawable.news_collect : R.drawable.news_no_collect);
        NewsDetailModel.AuthorInfoBean author = model.getAuthorInfo();
        if (author == null) return;
        tvUserName.setText(model.getAuthorInfo().getNickname());
        CommonImageLoader.load(model.getAuthorInfo().getAvatar()).placeholder(R.drawable.user_default1).into(ivUserAvatar);
    }

    private Boolean down = false;

    @Override
    public void commentSuccess() {
        down = true;
        etComment.setText("");
        mPresenter.getCommentList(newsId, 1);
//        refreshView.autoRefresh();
    }

    @Override
    public void commentFail() {
        if (commentAdapter != null) {
            List<NewsCommentModel.CommentListBean> data = commentAdapter.getData();
            if (data != null && data.size() > 0) {
                data.remove(0);
                commentAdapter.setList(data);
            }
        }
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
    public void toggleCollectSuccess(boolean collect) {
        newsDetailModel.setCollect(collect);
        showNewsDetail(newsDetailModel);
    }

    @Override
    public void toggleCommentPraiseSuccess(int position) {
//        commentAdapter.notifyItemChanged(position);
    }

    @Override
    public void deleteCommentSuccese(int position) {
        commentAdapter.remove(position);
    }

    @Override
    public void selectSettingPosition(int position) {
        if (position == 0) {
            showShareDialog(shareTitle, shareDesc, shareImageUrl, shareWebUrl);
        } else {
            clickCollect();
        }
    }

    @Override
    public void showBanners(final AdListModel data) {
        try {
            if (data == null || data.getAdList().isEmpty()) return;
            llAd.setVisibility(View.VISIBLE);


            List<String> imageUrl = new ArrayList<>();
            for (AdModel model : data.getAdList()) {
                imageUrl.add(model.getImg());
            }
//        bannerView.showImages(imageUrl);
//        bannerView.setOnBannerListener(new OnBannerListener() {
//            @Override
//            public void OnBannerClick(int position) {
//                AdModel adModel = data.getAdList().get(position);
//                AdClickHelper.clickAd(adModel);
//            }
//        });

            adAdapter.addData(data.getAdList());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void responseRecommendNews(NewsRecommendReadModel model) {
        List<RecommentListModel> recommentList = model.getRecommentList();
        mNewsRecommendReadAdapter.setList(recommentList);
        mLlRecommendNews.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.ivCollect)
    public void clickCollect() {
        if (newsDetailModel == null) return;
        if (UserCenter.needLogin()) return;
        mPresenter.toggleCollectNews(newsId, newsDetailModel.isCollect());
    }

    @OnClick(R.id.tvSend)
    public void clickSend() {
        String content = etComment.getText().toString();
//        if (UserCenter.needLogin()) {//未登錄不讓評論
//            return;
//        }
        if (UserCenter.hasLogin()) {
            mPresenter.addComment(newsId, content);
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
        if (data == null && data.size() <= 0) {
            data = new ArrayList<>();
        } else {
            NewsCommentModel.CommentListBean commentListBean = new NewsCommentModel.CommentListBean();
            commentListBean.setContent(etComment.getText().toString());
            //获取当前时间
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
//            Date date = new Date(System.currentTimeMillis());
//            commentListBean.setCreateTime(simpleDateFormat.format(date));
            commentListBean.setCreateTime(getString(R.string.commenting));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == 1) refreshView.autoRefresh();
        }
    }

    // 高版本webView屏幕适配
    private String getNewContent(String htmlText) {
        Document doc = Jsoup.parse(htmlText);
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            if (TextUtils.isEmpty(shareImageUrl))
                shareImageUrl = element.attr("src");
            element.attr("width", "100%").attr("height", "auto");
        }
        return doc.toString();
    }


    /**
     * 去除html标签
     *
     * @param data
     * @return
     */
    public static String deleteHtml(String data) {
        try {
            String regEx_script = "<[^>]+>"; // 定义script的正则表达式
            Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            Matcher m_script = p_script.matcher(data);
            String a = m_script.replaceAll(""); // 过滤script标签
            return a;
        } catch (Exception e) {
            return "";
        }
    }

//    /**
//     * 添加广告
//     */
//    private void initAdImageView(final AdModel item) {
//        View adView = LayoutInflater.from(this).inflate(R.layout.item_ad_image, null);
//        ImageView ivImage = adView.findViewById(R.id.ivImage);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(0, 0, 0, 8);
//        adView.setLayoutParams(lp);
//
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

    public static String [] returnImageUrlsFromHtml(String htmlCode) {
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
}
