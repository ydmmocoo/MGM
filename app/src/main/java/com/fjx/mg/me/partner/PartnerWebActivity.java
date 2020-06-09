package com.fjx.mg.me.partner;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.dialog.WebShareDialog;
import com.fjx.mg.main.MainActivity;
import com.library.common.base.BaseActivity;
import com.library.common.callback.CActivityManager;
import com.library.common.utils.JsonUtil;
import com.library.repository.Constant;
import com.library.repository.data.UserCenter;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;

import butterknife.BindView;

public class PartnerWebActivity extends BaseActivity {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.webView)
    WebView webView;
    private TextView tvTitle;

    private String shareTitle, shareDesc, shareImage, shareUrl;

    public static Intent newInstance(Context context, String option) {
        Intent intent = new Intent(context, PartnerWebActivity.class);
        intent.putExtra("Options", option);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_partner_web;
    }

    @Override
    protected void initView() {
        String str = getIntent().getStringExtra("Options");
        Options options = JsonUtil.strToModel(str, Options.class);
        String newurl = GetNewUrl(options.getLoadUrl());
        idToolbar.setVisibility(options.enableShowToolbar ? View.VISIBLE : View.GONE);
        ToolBarManager toolBarManager = ToolBarManager.with(this).setNavigationIcon(R.color.white)
                .setTitle(options.getTitle(), R.color.white);
        tvTitle = toolBarManager.getTitleTextView();

        if (options.enableShare) {
            final String host = Constant.getHost();
            shareUrl = options.getLoadUrl();
            toolBarManager.setBackgroundColor(R.color.me_parner_top_bg).setRightText("关闭", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        WebSettings webSetting = webView.getSettings();

        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setSupportZoom(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setLoadWithOverviewMode(true);

        webView.setWebChromeClient(chromeClient);
        if (TextUtils.isEmpty(options.loadUrl)) return;
        if (options.loadUrl.startsWith("http")) {
//            webView.loadUrl(options.getLoadUrl());
            webView.loadUrl(newurl);
        } else {
            webView.loadDataWithBaseURL(null, options.loadUrl, "text/html", "utf-8", null);
        }
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.goBack();
            }
        });
    }

    private String GetNewUrl(String loadUrl) {
        StringBuilder sb = new StringBuilder();
        sb.append(Constant.HOST);
        if (loadUrl.contains("invite/flowAdv")) {
            sb.append("invite/flowAdv?ispType=");
        } else if (loadUrl.contains(Constant.getSearch())) {
            sb.append(Constant.getSearch());
        } else {
            return loadUrl;
        }
        UserInfoModel userInfo = UserCenter.getUserInfo();
        sb.append(userInfo.getPhone().substring(0, 3));
        sb.append("&l=");
        sb.append(RepositoryFactory.getLocalRepository().getLangugeType());
        sb.append("&appOpen=1");
        Log.e("资费套餐链接:", sb.toString());
        return sb.toString();
    }

    public static class Options {
        String title = "";
        String loadUrl = null;
        int backIcon = R.drawable.ic_back_black;
        int toolbarColor = R.color.white;
        int statusBarColor = R.color.white;
        int titleTextColor = R.color.textColor;
        boolean enableShowToolbar = true;
        boolean enableShare = true;
        String fromActivity;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLoadUrl() {
            return loadUrl;
        }

        public void setLoadUrl(String loadUrl) {
            this.loadUrl = loadUrl;
        }

        public int getBackIcon() {
            return backIcon;
        }

        public void setBackIcon(int backIcon) {
            this.backIcon = backIcon;
        }

        public int getToolbarColor() {
            return toolbarColor;
        }

        public void setToolbarColor(int toolbarColor) {
            this.toolbarColor = toolbarColor;
        }

        public int getStatusBarColor() {
            return statusBarColor;
        }

        public void setStatusBarColor(int statusBarColor) {
            this.statusBarColor = statusBarColor;
        }

        public int getTitleTextColor() {
            return titleTextColor;
        }

        public void setTitleTextColor(int titleTextColor) {
            this.titleTextColor = titleTextColor;
        }

        public boolean isEnableShowToolbar() {
            return enableShowToolbar;
        }

        public void setEnableShowToolbar(boolean enableShowToolbar) {
            this.enableShowToolbar = enableShowToolbar;
        }

        public void setEnableShare(boolean enableShare) {
            this.enableShare = enableShare;
        }

        public String getFromActivity() {
            return fromActivity;
        }

        public void setFromActivity(String fromActivity) {
            this.fromActivity = fromActivity;
        }
    }


    private WebChromeClient chromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            shareTitle = title;
            shareDesc = title;
            tvTitle.setText(title);
        }
    };

    private void showShareDialog(String title, String desc, String imageUrl, String webview) {

        WebShareDialog shareDialog = new WebShareDialog(getCurContext());
        shareDialog.setShareParams(title, desc, imageUrl, webview);
        shareDialog.setShareType(WebShareDialog.ShareType.web);
        new XPopup.Builder(getCurContext())
                .asCustom(shareDialog)
                .show();
    }

    @Override
    public void onBackPressed() {
        boolean hasMainCreate = CActivityManager.getAppManager().hasCreateActivity(MainActivity.class);
        if (!hasMainCreate) {
            startActivity(MainActivity.newInstance(getCurContext()));
        }
        super.onBackPressed();
    }
}
