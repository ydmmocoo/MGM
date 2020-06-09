package com.fjx.mg.house.detail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.dialog.ShareDialog;
import com.fjx.mg.house.comment.HouseCommentActivity;
import com.fjx.mg.news.detail.CommentAdapter;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StatusBarManager;
import com.library.common.utils.StringUtil;
import com.library.common.view.BannerView;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.Constant;
import com.library.repository.data.UserCenter;
import com.library.repository.models.HouseDetailModel;
import com.library.repository.models.NewsCommentModel;
import com.lxj.xpopup.XPopup;

import butterknife.BindView;
import butterknife.OnClick;

public class HouseDetailActivity extends BaseMvpActivity<HouseDetailPresenter> implements HouseDetailContract.View {


    @BindView(R.id.bannerView)
    BannerView bannerView;
    @BindView(R.id.tvImageCount)
    TextView tvImageCount;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.tvHouseType)
    TextView tvHouseType;
    @BindView(R.id.tvHouseArea)
    TextView tvHouseArea;
    @BindView(R.id.tvCompanyName)
    TextView tvCompanyName;
    @BindView(R.id.tvPPName)
    TextView tvPPName;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvLanguage)
    TextView tvLanguage;
    @BindView(R.id.tvDesc)
    TextView tvDesc;
    @BindView(R.id.tvCommentNum)
    TextView tvCommentNum;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;
    @BindView(R.id.etComment)
    EditText etComment;
    @BindView(R.id.ivCollect)
    ImageView ivCollect;
    @BindView(R.id.layout1)
    LinearLayout layout1;
    @BindView(R.id.tvTitle2)
    TextView tvTitle2;
    @BindView(R.id.tvHouseType2)
    TextView tvHouseType2;
    @BindView(R.id.tvAddress2)
    TextView tvAddress2;
    @BindView(R.id.tvHouseArea2)
    TextView tvHouseArea2;
    @BindView(R.id.tvMoney2)
    TextView tvMoney2;
    @BindView(R.id.tvSend)
    TextView tvSend;
    @BindView(R.id.layout2)
    LinearLayout layout2;

    ImageView ivRightImage;

    @BindView(R.id.id_toolbar)
    View layoutToolBar;
    @BindView(R.id.nestScrollView)
    NestedScrollView nestScrollView;

    private String weixinCode, hId;
    private boolean isCollect;
    private CommentAdapter commentAdapter;
    private String contactPhone;
    private boolean isMyPublish;//是否我发布的
    private boolean isExpire;//是否过时
    private boolean isClosed;//是否关闭
    private String status;
    private int type;
    private String shareDesc;
    private String shareImage;
    private String shareUrl;
    private String shareTitle;
    private HouseDetailModel detailModel;

    public static Intent newInstance(Context context, String hId) {
        Intent intent = new Intent(context, HouseDetailActivity.class);
        intent.putExtra("cid", hId);
        return intent;
    }

    public static Intent newInstance(Context context, String hId, boolean isMyPublish) {
        Intent intent = new Intent(context, HouseDetailActivity.class);
        intent.putExtra("cid", hId);
        intent.putExtra("isMyPublish", isMyPublish);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mCommonStatusBarEnable = false;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_house_detail;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {
        isMyPublish = getIntent().getBooleanExtra("isMyPublish", false);
        Log.e("isMyPublish", "" + isMyPublish);
        hId = getIntent().getStringExtra("cid");
        GradientDrawableHelper.whit(tvSend).setColor(R.color.colorAccent).setCornerRadius(50);
        GradientDrawableHelper.whit(etComment).setCornerRadius(50).setColor(R.color.colorGrayBg);
        ivRightImage = ToolBarManager.with(this).setTitle("求租详情").setRightImage(R.drawable.more_black, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.showPublishDialog(ivRightImage, isExpire(), isClosed, true, isMyPublish);
//                if (isMyPublish) {
//                    mPresenter.showPublishDialog(ivRightImage, isExpire, isClosed,isMyPublish,isMyPublish);
//                } else {
//                    mPresenter.toggleCollect(isCollect, hId);//点击喜欢按钮
//                }
            }
        });


        refreshView.autoRefresh();
        commentAdapter = new CommentAdapter();
        commentAdapter.setHideFavNum(true);
        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.setAdapter(commentAdapter);

        commentAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                startActivityForResult(HouseCommentActivity.newInstance(getCurContext(),
                        JsonUtil.moderToString(commentAdapter.getData().get(position))), 11);
            }
        });
//        mPresenter.houseDetail(hId);
        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                if (refreshView.isRefresh()) {
                    mPresenter.houseDetail(hId);
                }
                mPresenter.getCommentList(page, hId);
            }
        });
        etComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
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

            }
        });
        //软键盘的搜索点击事件
        etComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    doComment();
                    return true;
                }
                return false;
            }
        });
    }

    private void doComment() {
        String content = etComment.getText().toString();
        mPresenter.addComment(content, hId);
        etComment.setText("");
    }


    @Override
    protected HouseDetailPresenter createPresenter() {
        return new HouseDetailPresenter(this);
    }

    @OnClick({R.id.ivBack, R.id.tvCopyWx, R.id.ivCollect, R.id.tvPhone, R.id.tvSend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.tvCopyWx:
                if (UserCenter.needLogin()) return;
                mPresenter.showContactDialog(weixinCode, false);
//                StringUtil.copyClip(weixinCode);
//                CommonToast.toast(getString(R.string.copy_success));
                break;
            case R.id.ivCollect:
                mPresenter.showPublishDialog(ivCollect, isExpire(), isClosed, true, isMyPublish);
//                if (isMyPublish) {
//                } else {
//                    mPresenter.toggleCollect(isCollect, hId);
//                }
                break;

            case R.id.tvPhone:
                mPresenter.showContactDialog(contactPhone, true);
                break;
            case R.id.tvSend:
                doComment();
                break;
        }
    }

    @Override
    public String getHid() {
        return hId;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getWeixinCode() {
        return weixinCode;
    }

    @Override
    public String getPhone() {
        return contactPhone;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void showHouseDetail(final HouseDetailModel detailModel) {
        this.detailModel = detailModel;

        shareDesc = detailModel.getDesc();
        shareImage = "";

        // http://47.97.159.184/invite/renting?hid=9

        StringBuilder sb = new StringBuilder();
        sb.append(Constant.HOST);
        sb.append("invite/");

        switch (detailModel.getType()) {
            case 1:
                sb.append("rentSeeking");
                break;
            case 2:
                sb.append("rentSeeking");
                break;
            case 3:
                sb.append("renting");
                break;
            case 4:
                sb.append("renting");
                break;
        }

        sb.append("?type=");
        int type = detailModel.getType();
        sb.append("" + type);

        sb.append("&id=");
        sb.append("" + detailModel.getHid());
        Log.e("分享链接:", sb.toString());


        shareUrl = sb.toString();

        status = detailModel.getStatus();
        this.type = detailModel.getType();
        isClosed = TextUtils.equals(detailModel.getStatus(), "1");
        isExpire = TextUtils.equals(detailModel.getStatus(), "3");
        tvPPName.setText(detailModel.getContactName());
        contactPhone = detailModel.getContactPhone();
        tvHouseType.setText(detailModel.getLayout());
        tvLanguage.setText(detailModel.getLanguage());
        tvDesc.setText(detailModel.getDesc());
        weixinCode = detailModel.getContactWeixin();
        isCollect = detailModel.isCollect();
        ivCollect.setImageResource(isCollect ? R.drawable.more_black : R.drawable.more_black);
        ivRightImage.setImageResource(isCollect ? R.drawable.more_black : R.drawable.more_black);


        if (detailModel.getImages() == null || detailModel.getImages().isEmpty()) {
            StatusBarManager.setColor(this, com.library.common.R.color.colorPrimary);
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);
            tvTitle2.setText(detailModel.getTitle());
            shareTitle = detailModel.getTitle();
            tvMoney2.setText(detailModel.getPrice().concat("/月"));
            tvAddress2.setText(detailModel.getCountryName().concat(detailModel.getCityName()));
            tvHouseArea2.setText(detailModel.getArea().concat("m²"));
            tvHouseType2.setText(detailModel.getLayout());
            layoutToolBar.setVisibility(View.VISIBLE);
        } else {
            StatusBarManager.transparentNavigationBar(this);
            tvTitle.setText(detailModel.getTitle());
            shareTitle = detailModel.getTitle();
            tvMoney.setText(detailModel.getPrice().concat("/月"));
            tvHouseArea.setText(detailModel.getArea().concat("m²"));
            tvHouseType.setText(detailModel.getLayout());
            tvAddress.setText(detailModel.getCountryName().concat(detailModel.getCityName()));
            layoutToolBar.setVisibility(View.GONE);

            layout2.setVisibility(View.GONE);
            layout1.setVisibility(View.VISIBLE);
            showImageCount(0, detailModel);
            bannerView.showImages(detailModel.getImages());
            bannerView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {
                    showImageCount(i, detailModel);
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
        }

    }

    @Override
    public void toggleCollectResult(boolean isCollect) {
        this.isCollect = isCollect;
        ivCollect.setImageResource(isCollect ? R.drawable.more_black : R.drawable.more_black);
        ivRightImage.setImageResource(isCollect ? R.drawable.more_black : R.drawable.more_black);

    }

    @Override
    public void showCommentList(NewsCommentModel commentModel) {
        String str = getString(R.string.comment);
        tvCommentNum.setText(str.concat("(").concat(commentModel.getCommentNum()).concat(")"));
        refreshView.noticeAdapterData(commentAdapter, commentModel.getCommentList(), commentModel.isHasNext());
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
    public void commentSuccess() {
        down = true;
        etComment.setText("");

        mPresenter.getCommentList(1, hId);
    }

    private Boolean down = false;

    @Override
    public void loadCommentError() {
        refreshView.finishLoading();
    }

    @Override
    public void refreshData() {
        refreshView.autoRefresh();
    }

    @Override
    public void toggleCollect() {
        mPresenter.toggleCollect(isCollect, hId);//点击喜欢按钮
    }

    @Override
    public void share() {
        showShareDialog();
    }

    private void showShareDialog() {
        ShareDialog shareDialog = new ShareDialog(getCurContext());
        shareDialog.setShareParams(shareTitle, shareDesc, shareImage, shareUrl);
        shareDialog.setShareType(ShareDialog.ShareType.web);
        new XPopup.Builder(getCurContext())
                .asCustom(shareDialog)
                .show();
    }

    private void showImageCount(int i, HouseDetailModel detailModel) {
        if (detailModel.getImages() == null || detailModel.getImages().isEmpty()) {
            return;
        }

        String curindex = String.valueOf(i + 1);
        String total = String.valueOf(detailModel.getImages().size());
        if (tvImageCount == null) return;
        tvImageCount.setText(curindex.concat("/").concat(total));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode) refreshView.doRefresh();
    }

    public boolean isExpire(){
        //判断是否自己发的帖子来控制menu的item显示内容
        String uid = detailModel.getUId();
        String myUid = UserCenter.getUserInfo().getUId();
        if (StringUtil.isNotEmpty(uid) && StringUtil.isNotEmpty(myUid)) {
            if (uid.equals(myUid)) {
                 isExpire = false;
            } else {
                isExpire = true;
            }
        }else {
            isExpire = true;
        }
        return isExpire;
    }
}
