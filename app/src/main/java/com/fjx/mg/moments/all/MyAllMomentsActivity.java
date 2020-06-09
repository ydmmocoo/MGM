package com.fjx.mg.moments.all;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.moments.detail.MomentsDetailActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.Utils;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.models.PersonalMomentListModel;

import butterknife.BindView;

/**
 * 用户的朋友圈动态
 */
public class MyAllMomentsActivity extends BaseMvpActivity<AllMomentsPresenter> implements AllMomentsContract.View, MenuItem.OnMenuItemClickListener {
    private CollapsingToolbarLayoutState state = CollapsingToolbarLayoutState.EXPANDED;
    private boolean hasMeasured = false;
    private MomentAllAdapter adapter;
    private String identifier;
    private int height;
    private boolean isFristAdd;

    @BindView(R.id.coordinatorlayout)
    CoordinatorLayout coordinatorlayout;

    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @BindView(R.id.headimg)
    ImageView headView;
    @BindView(R.id.backdrop)
    ImageView backdrop;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    public static Intent newInstance(Context context, String identifier, String Img, String nickName) {
        Intent intent = new Intent(context, MyAllMomentsActivity.class);
        intent.putExtra("identifier", identifier);
        intent.putExtra("nickName", nickName);
        intent.putExtra("Img", Img);
        return intent;
    }


    @Override
    protected int layoutId() {
        mCommonStatusBarEnable = false;
        return R.layout.activity_all_moments;
    }

    @Override
    protected AllMomentsPresenter createPresenter() {
        return new AllMomentsPresenter(this);
    }

    @SuppressLint("NewApi")
    @Override
    protected void initView() {
        Utils.setStatusBar(this, false, true);
        height = backdrop.getHeight();
        toolbar.setNavigationIcon(R.drawable.iv_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setAppBarListener();
        titleTv.setText(getIntent().getStringExtra("nickName"));
        identifier = getIntent().getStringExtra("identifier");
        toolbar.inflateMenu(R.menu.all_moments_activity_toolbar_menu);
        MenuItem item = toolbar.getMenu().findItem(R.id.allMomentMessage);
        item.setOnMenuItemClickListener(this);
        adapter = new MomentAllAdapter();

        CommonImageLoader.load(getIntent().getStringExtra("Img")).placeholder(R.drawable.default_user_image).round().into(headView);


        recyclerView.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recyclerView.addItemDecoration(new SpacesItemDecoration(1));
        recyclerView.setAdapter(adapter);



        //adapter.bindToRecyclerView(recyclerView);






        refreshView.autoRefresh();
        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                mPresenter.personalMomentList(identifier, page);
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                PersonalMomentListModel.MomentsListBean momentsListBean = (PersonalMomentListModel.MomentsListBean) adapter.getItem(position);
                startActivity(MomentsDetailActivity.newInstance(getCurContext(), ((PersonalMomentListModel.MomentsListBean)adapter.getItem(position)).getMId()
                        , JsonUtil.moderToString(momentsListBean)));
            }
        });

    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        AllMomentsMessageActivity.launch(MyAllMomentsActivity.this);
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setAppBarListener() {
        measureHeight();
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED;//修改为展开状态
                        titleTv.setVisibility(View.GONE);
                        toolbar.setNavigationIcon(R.drawable.iv_back);
                        MyAllMomentsActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    titleTv.setVisibility(View.VISIBLE);
                    toolbar.setNavigationIcon(R.drawable.ic_back_black);
                    state = CollapsingToolbarLayoutState.COLLAPSED;//修改为折叠状态
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        MyAllMomentsActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    }
                } else {
                    if (Math.abs(verticalOffset) > height) {
                        titleTv.setVisibility(View.VISIBLE);
                        float scale = 1 - height / (float) Math.abs(verticalOffset);
                        if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                            if (state == CollapsingToolbarLayoutState.COLLAPSED && scale < 0.55) {//由折叠变为展开
                                toolbar.setNavigationIcon(R.drawable.ic_back_black);
                                MyAllMomentsActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_VISIBLE);
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    MyAllMomentsActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                                }
                            }
                            state = CollapsingToolbarLayoutState.INTERNEDIATE;
                        }
                        toolbar.setNavigationIcon(R.drawable.ic_back_black);
                    } else {
                        titleTv.setVisibility(View.GONE);
                        toolbar.setNavigationIcon(R.drawable.ic_back_black);
                    }
                }
            }
        });
    }

    private void measureHeight() {
        ViewTreeObserver vto = coordinatorlayout.getViewTreeObserver();

        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                if (hasMeasured == false) {

                    height = toolbar.getMeasuredHeight();
                    hasMeasured = true;

                }
                return true;
            }
        });
    }


    @Override
    public void showPersonalMomentList(PersonalMomentListModel model) {
        refreshView.finishLoading();
        refreshView.noticeAdapterData(adapter, model.getMomentsList(), model.isHasNext());
        recyclerView.invalidateItemDecorations();
        if (!isFristAdd) {
            addFootView(getString(R.string.no_more_loading_data));
            isFristAdd = true;
        }
    }

    @Override
    public void loadError() {
        refreshView.finishLoading();
    }


    public void addFootView(String msg) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_foot_msg, null);
        TextView tvFootMsg = view.findViewById(R.id.tvFootMsg);
        tvFootMsg.setText(msg);
        adapter.addFooterView(view);
    }


}


