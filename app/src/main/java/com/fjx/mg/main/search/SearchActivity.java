package com.fjx.mg.main.search;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.house.detail.HouseDetailActivity;
import com.fjx.mg.house.fragment.HouseLeaseAdapter;
import com.fjx.mg.job.detail.JobDetailActivity;
import com.fjx.mg.job.fragment.JobHuntinAdapter;
import com.fjx.mg.main.fragment.news.tab.NewsAdapter;
import com.fjx.mg.main.yellowpage.YellowPageAdapter;
import com.fjx.mg.main.yellowpage.detail.YellowPageDetailActivity;
import com.fjx.mg.news.detail.NewsDetailActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.SoftInputUtil;
import com.library.common.utils.StatusBarManager;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.models.CompanyListModel;
import com.library.repository.models.HouseListModel;
import com.library.repository.models.JobListModel;
import com.library.repository.models.NewsItemModel;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.qcloud.uikit.business.contact.view.adapter.ContactAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseMvpActivity<SearchPresenter> implements SearchContract.View {


    @BindView(R.id.tvType)
    TextView tvType;

    @BindView(R.id.tvSearch)
    TextView tvSearch;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.ivClear)
    ImageView ivClear;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;
    private String content;
    private int mType;
    private NewsAdapter newsAdapter;
    private HouseLeaseAdapter houseAdapter;
    private JobHuntinAdapter jobAdapter;
    private ContactAdapter friendAdaper;
    private YellowPageAdapter companyAdapter;

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_search;
    }

    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter(this);
    }

    @Override
    protected void initView() {
        StatusBarManager.setLightFontColor(this, R.color.colorAccent);
        mPresenter.bindTextWatcher(etSearch);

        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                mPresenter.search(mType, content, page);
            }
        });


        //软键盘的搜索点击事件
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch();
                    return true;
                }
                return false;
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!SearchActivity.this.isFinishing()) {
                    SoftInputUtil.showSoftInputView(getCurActivity(), etSearch);
                }
            }
        }, 100);
    }

    private void doSearch() {
        content = etSearch.getText().toString();
        if (TextUtils.isEmpty(content)) {
            CommonToast.toast(getString(R.string.hint_input_search_content));
            return;
        }
        refreshView.doRefresh();
        SoftInputUtil.hideSoftInput(getCurActivity());
    }

    @OnClick({R.id.tvType, R.id.ivClear, R.id.ivBack, R.id.tvSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvType:


                mPresenter.showTypeDialog(tvType);
                break;
            case R.id.ivClear:
                content = "";
                etSearch.setText(content);
                break;
            case R.id.ivBack:
                onBackPressed();
                break;

            case R.id.tvSearch:
                doSearch();
                break;
        }
    }

    @Override
    public void selectType(int position, String text) {
        mType = position;
        tvType.setText(text);
    }

    @Override
    public void showClearImage(boolean enableShow) {
        ivClear.setVisibility(enableShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showNewsList(NewsItemModel newsList) {
        if (newsAdapter == null)
            newsAdapter = new NewsAdapter();
        recycler.setAdapter(newsAdapter);
        refreshView.noticeAdapterData(newsAdapter, newsList.getNewsList(), newsList.isHasNext());
        newsAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                startActivity(NewsDetailActivity.newInstance(getCurContext(), newsAdapter.getItem(position).getNewsId()));
            }
        });
    }

    @Override
    public void showHouseList(HouseListModel houseList) {
        if (houseAdapter == null) {
            houseAdapter = new HouseLeaseAdapter();
            houseAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    String hid = houseAdapter.getItem(position).getHid();
                    startActivity(HouseDetailActivity.newInstance(getCurContext(), hid));
                }
            });
        }
        recycler.setAdapter(houseAdapter);
        refreshView.noticeAdapterData(houseAdapter, houseList.getHouseList(), houseList.isHasNext());
    }

    @Override
    public void showJobList(JobListModel jobList) {
        if (jobAdapter == null) {
            jobAdapter = new JobHuntinAdapter();
            jobAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    int type = jobAdapter.getItem(position).getType();
                    startActivity(JobDetailActivity.newInstance(getCurContext(),
                            jobAdapter.getItem(position).getJobId(), type == 1));
                }
            });
        }
        recycler.setAdapter(houseAdapter);
        refreshView.noticeAdapterData(jobAdapter, jobList.getJobsList(), jobList.isHasNext());
    }

    @Override
    public void showFriendList(List<TIMFriend> friendList) {
//        if (friendAdaper == null) {
//            friendAdaper = new PhoneContactsAdapter(friendList);
//        }
//        recycler.setAdapter(houseAdapter);
//        refreshView.noticeAdapterData(jobAdapter, jobList.getJobsList(), jobList.isHasNext());
    }

    @Override
    public void showCompanyList(CompanyListModel companyList) {
        if (companyAdapter == null) {
            companyAdapter = new YellowPageAdapter();
            companyAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    startActivity(YellowPageDetailActivity.newInstance(getCurContext(),
                            companyAdapter.getItem(position).getCId(), "",
                            false,companyAdapter.getItem(position).getUId()));
                }
            });
        }
        recycler.setAdapter(companyAdapter);
        refreshView.noticeAdapterData(companyAdapter, companyList.getCompanyList(), companyList.isHasNext());
    }

    @Override
    public void loadError() {
        refreshView.finishLoading();
    }
}
