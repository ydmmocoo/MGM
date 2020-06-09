package com.fjx.mg.main.fragment.news.tab;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fjx.mg.R;
import com.fjx.mg.utils.AdClickHelper;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.library.common.base.BaseFragment;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.LogTUtil;
import com.library.common.utils.MulLanguageUtil;
import com.library.common.utils.StringUtil;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.models.AdModel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import butterknife.BindView;

import static java.util.Locale.SIMPLIFIED_CHINESE;

/**
 * Author    by hanlz
 * Date      on 2020/3/20.
 * Description：新闻页部分web类型的分类页面
 */
public class NewsWebFragment extends BaseFragment {

    @BindView(R.id.refreshView)
    CustomRefreshView mRefreshView;
    @BindView(R.id.llWbRoot)
    LinearLayout mLlWbRoot;
    @BindView(R.id.webView)
    BridgeWebView mWebView;
    @BindView(R.id.pbLoading)
    ProgressBar mPbLoading;

    public static NewsWebFragment getInstance(String url) {
        NewsWebFragment fragment = new NewsWebFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IntentConstants.URLS, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_news_web;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindWebContent();
        mRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mWebView.reload();
                mRefreshView.finishLoading();
            }
        });

    }

    private void bindWebContent() {
        if (getArguments().containsKey(IntentConstants.URLS)) {
            String url = getArguments().getString(IntentConstants.URLS);
            WebSettings webSetting = mWebView.getSettings();
            int index = url.indexOf("=");
            if (index != -1) {
                String languageStart = url.substring(0, index + 1);
                StringBuilder sbUrl = new StringBuilder();
                sbUrl.append(languageStart);
                try {
                    Locale locale = MulLanguageUtil.getLocalLanguage();
                    if (locale.getLanguage().equals(SIMPLIFIED_CHINESE.getLanguage())) {
                        sbUrl.append("zh-cn");
                    } else if (locale.getLanguage().equals(Locale.TRADITIONAL_CHINESE.getLanguage())) {
                        sbUrl.append("zh-tw");
                    } else if (locale.getLanguage().equals(Locale.FRENCH.getLanguage())) {
                        sbUrl.append("fr");
                    } else if (locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
                        sbUrl.append("en-us");
                    }
                    url = sbUrl.toString();
                    Log.d("hanlz", url);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
//            webSetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webSetting.setJavaScriptEnabled(true);
            webSetting.setDomStorageEnabled(true);
            webSetting.setDatabaseEnabled(true);
            webSetting.setBuiltInZoomControls(true);
            webSetting.setSupportZoom(false);
            webSetting.setUseWideViewPort(true);
            webSetting.setLoadWithOverviewMode(true);
            mWebView.setWebChromeClient(chromeClient);
            mWebView.loadUrl(url);
            mWebView.registerHandler("WebToLocal", new BridgeHandler() {
                @Override
                public void handler(String data, CallBackFunction function) {
                    LogTUtil.d("", "registerHandler：" + data);
                    if (StringUtil.isNotEmpty(data)) {
                        parseJsonData(data);
                    }
                }
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mWebView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                        if (scrollY == 0) {
                            //开启下拉刷新
                            mRefreshView.setEnabled(true);
                        } else {
                            //关闭下拉刷新
                            mRefreshView.setEnabled(false);
                        }
                    }
                });
            }
        }
    }

    private WebChromeClient chromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);

        }

        @Override
        public void onProgressChanged(WebView webView, int i) {
            super.onProgressChanged(webView, i);
            if (mPbLoading != null) {
                if (i >= 100) {
                    mPbLoading.setVisibility(View.GONE);
                } else {
                    mPbLoading.setVisibility(View.VISIBLE);
                }
                mPbLoading.setProgress(i);
            }
        }
    };

    private void parseJsonData(String jsonStr) {
        try {
            JSONObject object = new JSONObject(jsonStr);
            JSONObject parameters = object.optJSONObject("parameters");
            JSONObject data = parameters.optJSONObject("data");
            JSONArray adList = data.optJSONArray("adList");
            if (adList.length() > 0) {
                JSONObject adObj = adList.optJSONObject(0);
                String aId = adObj.optString("aId");
                String img = adObj.optString("img");
                String jumpType = adObj.optString("jumpType");
                String path = adObj.optString("path");
                String androidPath = adObj.optString("androidPath");
                String url = adObj.optString("url");
                String title = adObj.optString("title");
                AdModel adModel = new AdModel();
                adModel.setaId(aId);
                adModel.setJumpType(jumpType);
                adModel.setImg(img);
                adModel.setPath(path);
                adModel.setAndroidPath(androidPath);
                adModel.setUrl(url);
                adModel.setTitle(title);
                AdClickHelper.clickAd(adModel);
            }
        } catch (NullPointerException | JSONException e) {
            e.printStackTrace();
        }
    }
}
