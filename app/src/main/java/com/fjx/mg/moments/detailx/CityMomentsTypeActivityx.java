package com.fjx.mg.moments.detailx;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.fjx.mg.R;
import com.fjx.mg.friend.addfriend.AddFriendActivity;
import com.fjx.mg.friend.imuser.NewImUserDetailActivity;
import com.fjx.mg.image.ImageActivity;
import com.fjx.mg.moments.add.NewMomentsActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.SoftInputUtil;
import com.library.common.utils.StringUtil;
import com.library.common.view.popupwindow.CommonPopupWindow;
import com.library.common.view.popupwindow.Utils;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.models.CityCircleListModel;
import com.library.repository.models.ImUserRelaM;
import com.luck.picture.lib.PictureSelector;

import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

public class CityMomentsTypeActivityx extends BaseMvpActivity<CityMomentsTypePresenterx> implements CityMomentsTypeContractx.View, CommonPopupWindow.ViewInterface {
    private CollapsingToolbarLayoutState state = CollapsingToolbarLayoutState.EXPANDED;
    private boolean hasMeasured = false;
    private int height;


    @BindView(R.id.coordinatorlayout)
    CoordinatorLayout coordinatorlayout;

    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;

    @BindView(R.id.recycler)
    RecyclerView recycler;


    @BindView(R.id.head_img)
    ImageView head_img;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    private TypeMomentsAdapterx cityCircleAdapter;
    private String mId;
    private String rid = "";
    private int mPage = 1;
    private CommonPopupWindow popWindow;
    private EditText comments;

    @SuppressLint("WrongConstant")
    public void SetWhoCanWatch() {//全屏弹出增加照片
        if (popWindow != null && popWindow.isShowing()) return;
        View upView = LayoutInflater.from(this).inflate(R.layout.popup_up_comment, null);
        //测量View的宽高
        Utils.measureWidthAndHeight(upView);
        popWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.popup_up_comment)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, upView.getMeasuredHeight())
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setAnimationStyle(R.style.AnimUp)
                .setViewOnclickListener(this)
                .create();
        popWindow.showAtLocation(this.findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
        popWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        comments.requestFocus();//弹出键盘
        SoftInputUtil.showSoftInput(this, comments);
    }

    private void popupInputMethodWindow() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 0);
    }

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    @Override
    public void getChildView(View view, int layoutResId) {

        if (layoutResId == R.layout.popup_up_comment) {
            comments = view.findViewById(R.id.circleEt);
            ImageView popsendIv = view.findViewById(R.id.sendIv);
            popsendIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popWindow != null) {
                        mPresenter.addReplyMid(mId, comments.getText().toString(), "", rid);
                        popWindow.dismiss();
                    }
                }
            });

        }
    }

    public static Intent newInstance(Context context, String type, String img, String name) {
        Intent intent = new Intent(context, com.fjx.mg.moments.city.type.CityMomentsTypeActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("img", img);
        intent.putExtra("name", name);
        return intent;
    }

    @Override
    protected int layoutId() {
        mCommonStatusBarEnable = false;
        return R.layout.activity_moments_type;
    }

    @SuppressLint({"ClickableViewAccessibility", "NewApi"})
    @Override
    protected void initView() {
        com.library.common.utils.Utils.setStatusBar(this, false, true);
        height = head_img.getHeight();
        toolbar.setNavigationIcon(R.drawable.iv_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setAppBarListener();


        //类型列表
        cityCircleAdapter = new TypeMomentsAdapterx();
        cityCircleAdapter.setCirclePresenter(mPresenter);
        CommonImageLoader.load(getIntent().getStringExtra("img")).noAnim().placeholder(R.drawable.moment_back).into(head_img);
        tvType.setText(getIntent().getStringExtra("name"));
        titleTv.setText(getIntent().getStringExtra("name"));

        recycler.setLayoutManager(new LinearLayoutManager(getCurContext(), LinearLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(new SpacesItemDecoration(0, 0));
        ((SimpleItemAnimator) recycler.getItemAnimator()).setSupportsChangeAnimations(false);
        recycler.setAdapter(cityCircleAdapter);


        recycler.setOnTouchListener(new View.OnTouchListener() {//列表触碰收回输入框
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                updateEditTextBodyVisible(View.GONE);
                return false;
            }
        });

        refreshView.autoRefresh();
        refreshView.setRefreshListener((page, pageSize) -> {
            mPage = page;
            String v1 = getIntent().getStringExtra("type");
            if (StringUtil.isNotEmpty(v1)) {
                int type = Integer.parseInt(v1);
                mPresenter.getCityCircleList(type, "1");//请求数据
            }
        });
//        mPresenter.getCityCircleList(Integer.parseInt(getIntent().getStringExtra("type")), "1");//请求数据
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setAppBarListener() {
        measureHeight();
        appbar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset == 0) {
                if (state != CollapsingToolbarLayoutState.EXPANDED) {
                    state = CollapsingToolbarLayoutState.EXPANDED;//修改为展开状态
                    titleTv.setVisibility(View.GONE);
                    toolbar.setNavigationIcon(R.drawable.iv_back);
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_VISIBLE);
                }
            } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                titleTv.setVisibility(View.VISIBLE);
                toolbar.setNavigationIcon(R.drawable.ic_back_black);
                state = CollapsingToolbarLayoutState.COLLAPSED;//修改为折叠状态
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            } else {
                if (Math.abs(verticalOffset) > height) {
                    titleTv.setVisibility(View.VISIBLE);
                    float scale = 1 - height / (float) Math.abs(verticalOffset);
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if (state == CollapsingToolbarLayoutState.COLLAPSED && scale < 0.55) {//由折叠变为展开
                            toolbar.setNavigationIcon(R.drawable.ic_back_black);
                            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_VISIBLE);
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
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
    protected CityMomentsTypePresenterx createPresenter() {
        return new CityMomentsTypePresenterx(this);
    }

    @OnClick(R.id.floatbutton)
    public void onAddClicked() {
        startActivity(NewMomentsActivity.newInstance(getCurContext()));
    }

    @Override
    public void ShowCityCircleList(CityCircleListModel data) {
        refreshView.finishLoading();

        if (mPage == 1) {//直接替换数据
            cityCircleAdapter.replaceData(data.getMomentsList());
        } else {//刷新数据
            refreshView.noticeAdapterData(cityCircleAdapter, data.getMomentsList(), data.isHasNext());//考虑实际，不显示无更多数据，hasNext=true
            recycler.invalidateItemDecorations();
        }
    }

    @Override
    public void PraiseSuccess() {
        mPage = 1;
        mPresenter.getCityCircleList(Integer.parseInt(getIntent().getStringExtra("type")), "1");
    }


    @Override
    public void updateEditTextBodyVisible(int visibility) {
        if (popWindow != null) {
            popWindow.dismiss();
        }
    }


    @Override
    public void ToUserInfo(String identifier, String userAvatar) {
        mPresenter.findUser(identifier);
    }

    @Override
    public void showUserInfo(ImUserRelaM userRelaM) {
        if (userRelaM.isFriend()) {
            startActivity(NewImUserDetailActivity.newInstance(getCurContext(), userRelaM.getUserProfile().getIdentifier()));
        } else {
            startActivity(AddFriendActivity.newInstance(getCurContext(), JsonUtil.moderToString(userRelaM.getUserProfile())));
        }
    }


    @Override
    public void MomentsDelSuccess() {
        mPage = 1;
        mPresenter.getCityCircleList(Integer.parseInt(getIntent().getStringExtra("type")), "1");
    }

    @Override
    public void BodyVisible(String mid, String rid) {
        mId = mid;
        rid = rid;
        SetWhoCanWatch();
        popupInputMethodWindow();
    }

    @Override
    public void ShowDetail(int position, List<String> item) {
        String url = item.get(0);
        String reg = "(mp4|flv|avi|rm|rmvb|wmv)";
        Pattern p = Pattern.compile(reg);
        if (p.matcher(url).find()) {
            PictureSelector.create(this).externalPictureVideo(url);// 预览视频
        } else {// 预览图片 可自定长按保存路径
            startActivity(ImageActivity.newInstance(getCurActivity(), JsonUtil.moderToString(item), position));
        }
    }

    @Override
    public void ShowCommentDialog(String replyId, String content, boolean showDele) {
        TypeMomentsCommentDialogx dialog = new TypeMomentsCommentDialogx(CityMomentsTypeActivityx.this, mPresenter, replyId, content, showDele);
        dialog.show();
    }

    @Override
    public void delReplySuccess() {
        mPresenter.getCityCircleList(Integer.parseInt(getIntent().getStringExtra("type")), "1");
    }

    @Override
    public void GetNewData() {
        mPage = 1;
        mPresenter.getCityCircleList(Integer.parseInt(getIntent().getStringExtra("type")), "1");
    }
}
