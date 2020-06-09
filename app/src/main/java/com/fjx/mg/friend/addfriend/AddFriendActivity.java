package com.fjx.mg.friend.addfriend;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.friend.imuser.ImageAdapter;
import com.fjx.mg.friend.imuser.ImageMomentAdapter;
import com.fjx.mg.image.ImageActivity;
import com.fjx.mg.me.userinfo.UserInfoActivity;
import com.fjx.mg.moments.all.AllCityCircleAndYellowPageActivity;
import com.fjx.mg.moments.all.AllMomentsActivity;
import com.fjx.mg.moments.all.MyAllMomentsActivity;
import com.fjx.mg.moments.city.CityMomentsTypeAdapter;
import com.fjx.mg.moments.tag.AddTagActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.CommonToast;
import com.library.common.utils.ContextManager;
import com.library.common.utils.DimensionUtil;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StatusBarManager;
import com.library.common.utils.StringUtil;
import com.library.repository.data.UserCenter;
import com.library.repository.models.FriendContactSectionModel;
import com.library.repository.models.ImUserRelaM;
import com.library.repository.models.InviteModel;
import com.library.repository.models.MomentsCityCircleImagesModel;
import com.library.repository.models.MomentsUserInfoModel;
import com.library.repository.repository.RepositoryFactory;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.qcloud.uikit.common.utils.TIMStringUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//import com.fjx.mg.moments.city.CityMomentsTypeAdapter;

/**
 * 用户详情页(用户本人和未添加好友的用户)
 * 入口1：点击用户头像
 * 入口2：个人中心头像
 */
public class AddFriendActivity extends BaseMvpActivity<AddFriendPresenter> implements AddFriendContract.View, View.OnLongClickListener {

    @BindView(R.id.recyclerType)
    RecyclerView recyclertype;

    @BindView(R.id.recyclerImage)
    RecyclerView recyclerImage;
    @BindView(R.id.rvCityCircle)
    RecyclerView mRvCityCircle;
    @BindView(R.id.rvYellowPage)
    RecyclerView mRvYellowPage;

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.tvNickName)
    TextView tvNickName;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvGenderAddress)
    TextView tvGenderAddress;
    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.tvRemark)
    TextView tvRemark;
    @BindView(R.id.tvSendRequest)
    TextView tvSendRequest;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tvTagTitle)
    TextView mTvTagTitle;
    @BindView(R.id.tvTagIn)
    TextView mTvTagIn;
    @BindView(R.id.llRemark)
    LinearLayout mLlRemark;
    @BindView(R.id.tvPersonInfo)
    TextView mTvPersonInfo;
    @BindView(R.id.tvIdNum)
    TextView mTvIdNum;
    @BindView(R.id.tvdy)
    TextView mTvFriendCircle;
    @BindView(R.id.tvCityCircle)
    TextView mTvCityCircle;
    @BindView(R.id.tvYellowPage)
    TextView mTvYellowPage;

    private TIMUserProfile profile;
    private Boolean black = false;
    private String addworld;
    private String remark;
    private ImageAdapter imageAdapter;
    private ImageMomentAdapter mCityCircleAdapter;
    private ImageMomentAdapter mYellowPageAdapter;
    private CityMomentsTypeAdapter typeAdapter;
    private MomentsUserInfoModel mModel;

    private View contentView;
    private PopupWindow popupWindow;
    private String mUId;

    public static Intent newInstance(Context context, String userInfo) {
        Intent intent = new Intent(context, AddFriendActivity.class);
        intent.putExtra("userInfo", userInfo);
        return intent;
    }

    public static Intent newInstance(Context context, String userInfo, Boolean black) {
        Intent intent = new Intent(context, AddFriendActivity.class);
        intent.putExtra("userInfo", userInfo);
        intent.putExtra("black", black);
        return intent;
    }

    @Override
    public void showRemark(String remark) {
        tvRemark.setText(remark);
    }

    @Override
    public void outblackFriendSuccess() {

    }

    @Override
    public void showInviteModel(InviteModel mode) {
    }

    @Override
    protected AddFriendPresenter createPresenter() {
        return new AddFriendPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_add_friend;
    }

    @Override
    protected void initView() {
        setTextViewLp();
        black = getIntent().getBooleanExtra("black", false);
//        tvSendRequest.setText(black ? "添加到通讯录" : "发送");
//        title.setText(black ? "" : "添加好友");
        tvSendRequest.setText(black ? getString(R.string.add_constact) : getString(R.string.add_constact));
        title.setText(black ? "" : "");
        StatusBarManager.setLightFontColor(this, R.color.colorAccent);
        String info = getIntent().getStringExtra("userInfo");
        profile = JsonUtil.strToModel(info, TIMUserProfile.class);
        setListData();
        initCityCircleList();
        initYellowPageList();
        if (profile == null) {
            mPresenter.findUser(info);
            if (StringUtil.isNotEmpty(info)) {
                mPresenter.MomentsGetUserInfo(info);
            }
            return;
        }
        mPresenter.MomentsGetUserInfo(profile.getIdentifier());
        showInfo();
        tvNickName.setOnLongClickListener(this);
        mTvIdNum.setOnLongClickListener(this);
        tvPhone.setOnLongClickListener(this);
        tvGenderAddress.setOnLongClickListener(this);
        contentView = LayoutInflater.from(this).inflate(R.layout.dialog_copy_tips, null);
        popupWindow = new PopupWindow(contentView,
                DimensionUtil.dip2px(60), DimensionUtil.dip2px(30), true);
        dismissPop();

//        mLlTypeTags.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(AddTagActivity.newInstance(AddFriendActivity.this));
//            }
//        });
    }

    private void setTextViewLp() {
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mTvYellowPage.measure(spec, spec);
        int measuredWidthTicketNum = mTvYellowPage.getMeasuredWidth();
        ViewGroup.LayoutParams friendCircleLp = mTvFriendCircle.getLayoutParams();
        ViewGroup.LayoutParams cityCircleLp = mTvCityCircle.getLayoutParams();
        friendCircleLp.width = measuredWidthTicketNum;
        cityCircleLp.width = measuredWidthTicketNum;
        mTvFriendCircle.setLayoutParams(friendCircleLp);
        mTvCityCircle.setLayoutParams(cityCircleLp);
    }


    @Override
    public boolean onLongClick(View view) {
        String result = "";
        if (view.getId() == R.id.tvNickName) {
            result = tvNickName.getText().toString();
            tvNickName.setBackgroundColor(getResources().getColor(R.color.c_666666));
        } else if (view.getId() == R.id.tvIdNum) {
            result = mTvIdNum.getText().toString();
            mTvIdNum.setBackgroundColor(getResources().getColor(R.color.c_666666));
        } else if (view.getId() == R.id.tvPhone) {
            result = tvPhone.getText().toString();
            tvPhone.setBackgroundColor(getResources().getColor(R.color.c_666666));
        } else if (view.getId() == R.id.tvGenderAddress) {
            result = tvGenderAddress.getText().toString();
            tvGenderAddress.setBackgroundColor(getResources().getColor(R.color.c_666666));
        }
        if (!TextUtils.isEmpty(result)) {
            final String finalResult = result;
            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    StringUtil.copyClip(finalResult);
                    CommonToast.toast(R.string.copy_success);
                    resetTextColor();
                }
            });
            int width = view.getWidth();
            int heght = view.getHeight();
            popupWindow.showAsDropDown(view, width / 2, -(int) (heght * 2.6));

        }
        return true;

    }

    private void dismissPop() {
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                resetTextColor();
            }
        });
    }

    private void resetTextColor() {
        tvNickName.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        mTvIdNum.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        tvPhone.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        tvGenderAddress.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }

    /**
     * 黄页列表
     */
    private void initYellowPageList() {
        mYellowPageAdapter = new ImageMomentAdapter();
        mRvYellowPage.setLayoutManager(new LinearLayoutManager(ContextManager.getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRvYellowPage.setAdapter(mYellowPageAdapter);
        mYellowPageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                startActivity(AllCityCircleAndYellowPageActivity.newIntent(getCurContext(), "2", mUId, mTvPersonInfo.isShown()));
            }
        });
    }

    /**
     * 马岛服务列表
     */
    private void initCityCircleList() {
        mCityCircleAdapter = new ImageMomentAdapter();
        mRvCityCircle.setLayoutManager(new LinearLayoutManager(ContextManager.getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRvCityCircle.setAdapter(mCityCircleAdapter);
        mCityCircleAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                startActivity(AllCityCircleAndYellowPageActivity.newIntent(getCurContext(), "1", mUId, mTvPersonInfo.isShown()));
            }
        });
    }

    private void setListData() {
        typeAdapter = new CityMomentsTypeAdapter();//标签列表
        recyclertype.setLayoutManager(new LinearLayoutManager(ContextManager.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclertype.setAdapter(typeAdapter);
        typeAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if (StringUtil.equals(mModel.getPhone(), UserCenter.getUserInfo().getPhone())) {
                    startActivity(AddTagActivity.newInstance(AddFriendActivity.this));
                }
            }
        });


        imageAdapter = new ImageAdapter();
        recyclerImage.setLayoutManager(new LinearLayoutManager(ContextManager.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerImage.setAdapter(imageAdapter);
        /*imageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                profile.getNickName();
                if (StringUtil.equals(mModel.getPhone(), UserCenter.getUserInfo().getPhone())) {
                    startActivity(MyAllMomentsActivity.newInstance(getCurContext(), profile.getIdentifier(), profile.getFaceUrl(), profile.getNickName()));
                } else {
                    startActivity(AllMomentsActivity.newInstance(getCurContext(), profile.getIdentifier(), profile.getFaceUrl(), profile.getNickName(), false));
                }
            }
        });*/
    }

    @Override
    public void showContactList(List<FriendContactSectionModel> datas) {
        if (datas.toString().contains(profile.getIdentifier())) {
            Log.e("containsgetIdentifier", "true");
            mPresenter.addFriend(profile.getIdentifier(), addworld, remark, true);
        } else {
            Log.e("containsgetIdentifier", "false");
            mPresenter.addFriend(profile.getIdentifier(), addworld, remark, false);
        }
        for (int i = 0; i < datas.size() - 1; i++) {
            FriendContactSectionModel model = datas.get(i);
            String identifier = model.getData().getIdentifier();
            if (identifier.equals(profile.getIdentifier())) {
                break;
            }
        }
    }

    @Override
    public void showMommetnsUserInfo(MomentsUserInfoModel model) {
        this.mUId = model.getUserId();
        List<String> momentList = model.getMomentList();
        if (momentList.size() > 4) {
            List<String> strings = momentList.subList(0, 4);
            imageAdapter.setList(strings);
        } else {
            imageAdapter.setList(model.getMomentList());
        }
        //设置马岛服务列表数据
        List<MomentsCityCircleImagesModel> cityCircleImages = model.getCityCircleImages();
        if (cityCircleImages != null && cityCircleImages.size() > 0) {
            if (cityCircleImages.size() > 4) {
                mCityCircleAdapter.setList(cityCircleImages.subList(0, 4));
            } else {

                mCityCircleAdapter.setList(cityCircleImages);
            }
        }

        //黄页
        List<MomentsCityCircleImagesModel> companyImages = model.getCompanyImages();
        if (companyImages != null && companyImages.size() > 0) {
            if (companyImages.size() > 4) {
                mYellowPageAdapter.setList(companyImages.subList(0, 4));
            } else {
                mYellowPageAdapter.setList(companyImages);
            }
        }


        typeAdapter.setList(model.getTags());//标签列表数据
        mModel = model;
        if (StringUtil.equals(model.getPhone(), UserCenter.getUserInfo().getPhone())) {
            tvSendRequest.setVisibility(View.GONE);
            mLlRemark.setVisibility(View.GONE);
            mTvPersonInfo.setVisibility(View.VISIBLE);
            mTvTagIn.setVisibility(View.VISIBLE);
        } else {
            tvSendRequest.setVisibility(View.VISIBLE);
            mLlRemark.setVisibility(View.VISIBLE);
            mTvPersonInfo.setVisibility(View.GONE);
            mTvTagIn.setVisibility(View.GONE);
        }
//        initTags(model);
    }

    @Override
    public void friendRelation(boolean isRelation) {
        if (!isRelation) {
            tvSendRequest.setVisibility(View.VISIBLE);
        } else {
            tvSendRequest.setVisibility(View.GONE);
        }
    }

    @Override
    public void chatact(String timUserId) {
        finish();
        RepositoryFactory.getChatRepository().getAllFriend(new TIMValueCallBack<List<TIMFriend>>() {
            @Override
            public void onError(int i, String s) {
            }

            @Override
            public void onSuccess(List<TIMFriend> list) {
                RepositoryFactory.getLocalRepository().saveAllFriend(list);
            }
        });
    }


    @Override
    public void showContactLists(Boolean noblack) {
        mPresenter.addFriend(profile.getIdentifier(), addworld, remark, false);
    }

    private void showInfo() {
        tvNickName.setText(profile.getNickName());
        mTvIdNum.setText(profile.getIdentifier());
        String phone = TIMStringUtil.getPhone(profile);
        if (!TextUtils.isEmpty(phone)) {
            tvPhone.setText(StringUtil.phoneText(phone));
        } else {
            tvPhone.setVisibility(View.GONE);
        }
        try {
            CommonImageLoader.load(URLDecoder.decode(profile.getFaceUrl(), "UTF-8")).round().placeholder(R.drawable.default_user_image).into(ivAvatar);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            CommonImageLoader.load(profile.getFaceUrl()).round().placeholder(R.drawable.default_user_image).into(ivAvatar);
        }

        GradientDrawableHelper.whit(tvSendRequest).setColor(R.color.colorAccent).setCornerRadius(50);
        tvGenderAddress.setText(GenderText(profile.getGender()).concat(" ").concat(profile.getLocation()));
        etMessage.setText(getString(R.string.iam).concat(UserCenter.getUserInfo().getUNick()));
    }

    private String GenderText(int gender) {
        switch (gender) {
            case 0:
                return getString(R.string.unknow);
            case 1:
                return getString(R.string.man);
            case 2:
                return getString(R.string.woman);
        }
        return "";
    }

    @OnClick({R.id.ivBack, R.id.tvRemark, R.id.tvSendRequest,
            R.id.ivAvatar, R.id.viewDetail, R.id.tvTagIn,
            R.id.tvTagTitle, R.id.rlTagRoot, R.id.tvPersonInfo,
            R.id.rlCityCircle, R.id.rlYellowPage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.tvRemark:
                mPresenter.showRemarkEditDialog();
                break;
            case R.id.tvSendRequest:
                if (profile == null) return;
                addworld = etMessage.getText().toString();
                remark = tvRemark.getText().toString();
//                mPresenter.getAllBlackFriend();
                mPresenter.addFriend(profile.getIdentifier(), addworld, remark, black);
                break;

            case R.id.ivAvatar:
                if (profile == null) return;
                String url = profile.getFaceUrl();
                if (TextUtils.isEmpty(url)) return;
                List<String> urlList = new ArrayList<>();
                urlList.add(url);
                String urls = JsonUtil.moderToString(urlList);
                startActivity(ImageActivity.newInstance(getCurContext(), urls, 0));
                break;
            case R.id.viewDetail:
                if (StringUtil.equals(mModel.getPhone(), UserCenter.getUserInfo().getPhone())) {
                    startActivity(MyAllMomentsActivity.newInstance(getCurContext(), profile.getIdentifier(), profile.getFaceUrl(), profile.getNickName()));
                } else {
                    startActivity(AllMomentsActivity.newInstance(getCurContext(), profile.getIdentifier(), profile.getFaceUrl(), profile.getNickName(), false));
                }
                break;
            case R.id.rlCityCircle://马岛服务
                startActivity(AllCityCircleAndYellowPageActivity.newIntent(getCurContext(), "1", mUId, mTvPersonInfo.isShown()));
                break;
            case R.id.rlYellowPage://黄页
                startActivity(AllCityCircleAndYellowPageActivity.newIntent(getCurContext(), "2", mUId, mTvPersonInfo.isShown()));
                break;
            case R.id.tvTagIn:
            case R.id.tvTagTitle:
            case R.id.rlTagRoot:
                if (StringUtil.equals(mModel.getPhone(), UserCenter.getUserInfo().getPhone())) {
                    startActivityForResult(AddTagActivity.newInstance(AddFriendActivity.this), 1);
                }
                break;
            case R.id.tvPersonInfo:
                startActivity(UserInfoActivity.newInstance(getCurContext()));
                break;
        }
    }


    @Override
    public void showUserInfo(ImUserRelaM userRelaM) {
        profile = userRelaM.getUserProfile();
        showInfo();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (profile != null) {
                mPresenter.MomentsGetUserInfo(profile.getIdentifier());
            }
        }
    }

    //    private void initTags(MomentsUserInfoModel model) {
//        if (model.getTags().isEmpty()) {
//            return;
//        }
//        List<String> tags = model.getTags();
//        for (int i = 0; i < tags.size(); i++) {
//            int num = new Random().nextInt(4);
//            switch (num) {
//                case 3:
//                    TextView tvTags4 = (TextView) LayoutInflater.from(AddFriendActivity.this).inflate(R.layout.item_tags_4, null);
//                    ViewUtil.setDrawableBackGround(tvTags4, R.drawable.friends_tv_green_bg);
//                    ViewUtil.setTextColor(tvTags4, R.color.friends_green);
//                    mFblTags.addView(tvTags4);
//                    break;
//                case 2:
//                    TextView tvTags2 = (TextView) LayoutInflater.from(AddFriendActivity.this).inflate(R.layout.item_tags_2, null);
//                    ViewUtil.setDrawableBackGround(tvTags2, R.drawable.friends_tv_red_bg);
//                    ViewUtil.setTextColor(tvTags2, R.color.friends_red);
//                    mFblTags.addView(tvTags2);
//                    break;
//                case 1:
//                    TextView tvTags1 = (TextView) LayoutInflater.from(AddFriendActivity.this).inflate(R.layout.item_tags_1, null);
//                    ViewUtil.setDrawableBackGround(tvTags1, R.drawable.friends_tv_blue_bg);
//                    ViewUtil.setTextColor(tvTags1, R.color.friends_blue);
//                    mFblTags.addView(tvTags1);
//                    break;
//                case 0:
//                    TextView tvTags = (TextView) LayoutInflater.from(AddFriendActivity.this).inflate(R.layout.item_friends_type_tags, null);
//                    ViewUtil.setDrawableBackGround(tvTags, R.drawable.friends_tv_blue_bg);
//                    ViewUtil.setTextColor(tvTags, R.color.friends_blue);
//                    mFblTags.addView(tvTags);
//                    break;
//                default:
//                    TextView tvTags0 = (TextView) LayoutInflater.from(AddFriendActivity.this).inflate(R.layout.item_friends_type_tags, null);
//                    ViewUtil.setDrawableBackGround(tvTags0, R.drawable.friends_tv_blue_bg);
//                    ViewUtil.setTextColor(tvTags0, R.color.friends_blue);
//                    mFblTags.addView(tvTags0);
//                    break;
//
//            }
//        }
//    }

}
