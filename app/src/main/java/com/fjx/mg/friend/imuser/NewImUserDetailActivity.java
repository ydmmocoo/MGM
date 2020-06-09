package com.fjx.mg.friend.imuser;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.friend.chat.ChatActivity;
import com.fjx.mg.friend.transfer.TransferMoneyActivity;
import com.fjx.mg.image.ImageActivity;
import com.fjx.mg.main.MainActivity;
import com.fjx.mg.me.transfer.MeTransferActivityx;
import com.fjx.mg.moments.all.AllCityCircleAndYellowPageActivity;
import com.fjx.mg.moments.all.AllMomentsActivity;
import com.fjx.mg.moments.city.CityMomentsTypeAdapter;
import com.fjx.mg.moments.tag.AddTagActivity;
import com.fjx.mg.setting.feedback.FeedBackActivity;
import com.fjx.mg.utils.RankPermissionHelper;
import com.library.common.base.BaseMvpActivity;
import com.library.common.callback.CActivityManager;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.CommonToast;
import com.library.common.utils.ContextManager;
import com.library.common.utils.DimensionUtil;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StatusBarManager;
import com.library.common.utils.StringUtil;
import com.library.common.utils.ViewUtil;
import com.library.repository.data.UserCenter;
import com.library.repository.models.MomentsCityCircleImagesModel;
import com.library.repository.models.MomentsUserInfoModel;
import com.library.repository.models.OtherUserModel;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.ext.message.TIMManagerExt;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.qcloud.uikit.TimConfig;
import com.tencent.qcloud.uikit.event.ModifyEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;

public class NewImUserDetailActivity extends BaseMvpActivity<NewImUserDetailPresenter> implements NewImUserDetailContract.View, View.OnLongClickListener {

    @BindView(R.id.recyclerType)
    RecyclerView recyclertype;

    @BindView(R.id.recyclerImage)
    RecyclerView recyclerImage;
    @BindView(R.id.rvCityCircle)
    RecyclerView mRvCityCircle;
    @BindView(R.id.rvYellowPage)
    RecyclerView mRvYellowPage;
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.tvNickName)
    TextView tvNickName;
    @BindView(R.id.tvTag)
    TextView tvTag;
    @BindView(R.id.tvNickName2)
    TextView tvNickName2;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvGenderAddress)
    TextView tvGenderAddress;
    @BindView(R.id.tvTranMoney)
    TextView tvTranMoney;
    @BindView(R.id.tvChat)
    TextView tvChat;
    @BindView(R.id.tvRemark)
    TextView tvRemark;
    @BindView(R.id.tvIdNum)
    TextView mTvIdNum;
    @BindView(R.id.tvdy)
    TextView mTvFriendCircle;
    @BindView(R.id.tvYellowPage)
    TextView mTvYellowPage;
    @BindView(R.id.tvCityCircle)
    TextView mTvCityCircle;
    @BindView(R.id.viewDetail)
    RelativeLayout mRlCircle;

    private TIMUserProfile mProfile;
    private String imUserId;
    private String remark;
    private ImageAdapter imageAdapter;
    private ImageMomentAdapter mCityCircleAdapter;
    private ImageMomentAdapter mYellowPageAdapter;
    private CityMomentsTypeAdapter typeAdapter;
    private View contentView;
    private PopupWindow popupWindow;
    private String mUid;

    public static Intent newInstance(Context context, String imUserId) {
        Intent intent = new Intent(context, NewImUserDetailActivity.class);
        intent.putExtra("imUserId", imUserId);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_new_im_user_detail;
    }

    @Override
    protected void initView() {
        //TODO 正式环境屏蔽红包转账相关 2020年1月15日09:29:51
        setTextViewLp();
        //if (TimConfig.isRelease)
        //tvTranMoney.setVisibility(View.GONE);
        typeAdapter = new CityMomentsTypeAdapter();//标签列表
        recyclertype.setLayoutManager(new LinearLayoutManager(ContextManager.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclertype.setAdapter(typeAdapter);
        typeAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
            }
        });

        imageAdapter = new ImageAdapter();
        recyclerImage.setLayoutManager(new LinearLayoutManager(ContextManager.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerImage.setAdapter(imageAdapter);
        /*imageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if (!TextUtils.isEmpty(mProfile.getFaceUrl())) {
                    startActivity(AllMomentsActivity.newInstance(getCurContext(), imUserId, mProfile.getFaceUrl(), mProfile.getNickName()));
                } else {
                    startActivity(AllMomentsActivity.newInstance(getCurContext(), imUserId, "", mProfile.getNickName()));
                }
            }
        });*/

        initCityCircleList();
        initYellowPageList();
        StatusBarManager.setLightFontColor(this, R.color.colorAccent);

        GradientDrawableHelper.whit(tvTranMoney).setColor(R.color.colorAccent).setCornerRadius(50);
        GradientDrawableHelper.whit(tvChat).setColor(R.color.parentBgColor).setStroke(1, R.color.colorAccent).setCornerRadius(50);

        imUserId = getIntent().getStringExtra("imUserId");
        mPresenter.refreshUserSig(imUserId);
        Log.e("我的imUserId", "" + UserCenter.getUserInfo().getIdentifier());
        Log.e("imUserId", "" + imUserId);
        if (UserCenter.getUserInfo().getIdentifier().equals(imUserId)) {
            ViewUtil.drawableRight(tvTag, R.drawable.ic_right_arrow);
            tvTranMoney.setVisibility(View.GONE);
            tvRemark.setVisibility(View.GONE);
            tvChat.setVisibility(View.GONE);

            ToolBarManager.with(this).setTitle("").setBackgroundColor(R.color.colorAccent)
                    .setNavigationIcon(R.drawable.iv_back)
                    .setRightTextBack(getString(R.string.editing_materials), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPresenter.showSettingDialog(imUserId, findViewById(R.id.toolbar_tv_right1));
                        }
                    });
        } else {
            ToolBarManager.with(this).setTitle("").setBackgroundColor(R.color.colorAccent)
                    .setNavigationIcon(R.drawable.iv_back)
                    .setRightImage(R.drawable.ic_dot_more, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TIMFriend friend = UserCenter.getFriend(imUserId);
                            if (friend == null) {//非好友
                                mPresenter.showSettingDialog(imUserId, findViewById(R.id.toolbar_iv_right));
                            } else {
                                mPresenter.showSettingDialog2(imUserId, findViewById(R.id.toolbar_iv_right));
                            }
                        }
                    });
        }
//        TIMFriend friend = UserCenter.getFriend(imUserId);
//        if (friend == null) {
//            startActivity(AddFriendActivity.newInstance(getCurContext(), imUserId));
//            finish();
//            return;
//        }
        mPresenter.getImUserInfo(imUserId);
        mPresenter.MomentsGetUserInfo(imUserId);

        tvNickName.setOnLongClickListener(this);
        mTvIdNum.setOnLongClickListener(this);
        tvPhone.setOnLongClickListener(this);
        tvGenderAddress.setOnLongClickListener(this);
        contentView = LayoutInflater.from(this).inflate(R.layout.dialog_copy_tips, null);
        popupWindow = new PopupWindow(contentView,
                DimensionUtil.dip2px(60), DimensionUtil.dip2px(30), true);
        dismissPop();
    }

    private void setTextViewLp() {
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mTvYellowPage.measure(spec, spec);
        int measuredWidthTicketNum = mTvYellowPage.getMeasuredWidth();
        RelativeLayout.LayoutParams friendCircleLp = (RelativeLayout.LayoutParams) mTvFriendCircle.getLayoutParams();
        RelativeLayout.LayoutParams cityCircleLp = (RelativeLayout.LayoutParams) mTvCityCircle.getLayoutParams();
        friendCircleLp.width = measuredWidthTicketNum;
        cityCircleLp.width = measuredWidthTicketNum;
        mTvFriendCircle.setLayoutParams(friendCircleLp);
        mTvCityCircle.setLayoutParams(cityCircleLp);
        Log.d("InstallReceiver", measuredWidthTicketNum + "\n");
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
                startActivity(AllCityCircleAndYellowPageActivity.newIntent(getCurContext(), "2", mUid, false));
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
                startActivity(AllCityCircleAndYellowPageActivity.newIntent(getCurContext(), "1", mUid, false));
            }
        });
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

    @OnClick({R.id.tvTranMoney, R.id.tvChat, R.id.tvRemark, R.id.ivAvatar, R.id.viewDetail, R.id.tvTag, R.id.rlCityCircle, R.id.rlYellowPage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvTranMoney:
                if (RankPermissionHelper.NoPrivileges()) return;
                startActivity(TransferMoneyActivity.newInstance(getCurContext(), "", imUserId));
                break;
            case R.id.tvChat:
                if (mProfile == null) return;
                ChatActivity.startC2CChat(getCurContext(), mProfile.getIdentifier(), mProfile.getNickName());
                break;
            case R.id.tvRemark:
//                String remark=mProfile.
                mPresenter.showRemarkEditDialog(imUserId, remark);
                break;
            case R.id.ivAvatar:
                if (mProfile == null) return;
                String url = mProfile.getFaceUrl();
                if (TextUtils.isEmpty(url)) return;
                List<String> urlList = new ArrayList<>();
                urlList.add(url);
                String urls = JsonUtil.moderToString(urlList);
                startActivity(ImageActivity.newInstance(getCurContext(), urls, 0));
                break;
            case R.id.viewDetail:
                startActivity(AllMomentsActivity.newInstance(getCurContext(), imUserId, mProfile.getFaceUrl(), mProfile.getNickName(), true));
                break;
            case R.id.tvTag:
                if (UserCenter.getUserInfo().getIdentifier().equals(imUserId)) {
                    startActivityForResult(AddTagActivity.newInstance(getCurContext()), 101);
                }
                break;
            case R.id.rlCityCircle://马岛服务
                startActivity(AllCityCircleAndYellowPageActivity.newIntent(getCurContext(), "1", mUid, false));
                break;
            case R.id.rlYellowPage://黄页
                startActivity(AllCityCircleAndYellowPageActivity.newIntent(getCurContext(), "2", mUid, false));
                break;
        }
    }

    @Override
    public void showImUserInfo(TIMUserProfile profile) {
        mProfile = profile;
        mTvIdNum.setText(profile.getIdentifier());
        String phone = "";
        byte[] bytes = profile.getCustomInfo().get("phone");
        if (bytes != null)
            phone = new String(bytes);
        try {
            CommonImageLoader.load(URLDecoder.decode(profile.getFaceUrl(), "UTF-8")).placeholder(R.drawable.default_user_image)
                    .round().into(ivAvatar);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            CommonImageLoader.load(profile.getFaceUrl()).placeholder(R.drawable.default_user_image)
                    .round().into(ivAvatar);
        }


//        if (TextUtils.isEmpty(profile.getNickName()) && !TextUtils.isEmpty(profile.getIdentifier())) {
//            List<String> ids = new ArrayList<>();
//            ids.add(profile.getIdentifier());
//            TIMFriendshipManager.getInstance().getUsersProfile(ids, false, new TIMValueCallBack<List<TIMUserProfile>>() {
//                @Override
//                public void onError(int i, String s) {
//                    tvNickName.setText(mProfile.getNickName());
//                }
//
//                @Override
//                public void onSuccess(List<TIMUserProfile> timUserProfiles) {
//                    tvNickName.setText(timUserProfiles.get(0).getNickName());
//                }
//            });
//        } else {
        tvNickName.setText(profile.getNickName());
//        }
        if (StringUtil.isNotEmpty(phone)) {
            tvPhone.setText(phone);
        }

        tvNickName2.setText(profile.getNickName());
        tvNickName2.setVisibility(View.GONE);
        tvGenderAddress.setText(getGender(profile.getGender()).concat(" ").concat(profile.getLocation()));
        tvRemark.setVisibility(View.GONE);
        mPresenter.getFriendLis(imUserId, false);
    }

    /**
     * 进入该页面时 为用户好友显示的数据
     *
     * @param user
     */
    @Override
    public void showImUserInfo(TIMFriend user) {
        TIMUserProfile profile = user.getTimUserProfile();
        mTvIdNum.setText(profile.getIdentifier());
        remark = user.getRemark();
        mProfile = profile;
        try {
            CommonImageLoader.load(URLDecoder.decode(profile.getFaceUrl(), "UTF-8"))
                    .placeholder(R.drawable.default_user_image)
                    .into(ivAvatar);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            CommonImageLoader.load(profile.getFaceUrl())
                    .placeholder(R.drawable.default_user_image)
                    .into(ivAvatar);
        }

        String phone = "";
        byte[] bytes = profile.getCustomInfo().get("phone");
        if (bytes != null)
            phone = new String(bytes);

        //有备注优先显示备注，否则显示用户昵称
        if (TextUtils.isEmpty(user.getRemark())) {
            tvNickName.setText(user.getTimUserProfile().getNickName());
            tvNickName2.setVisibility(View.GONE);
        } else {
            tvNickName.setText(user.getRemark());
            tvNickName2.setVisibility(View.VISIBLE);
            tvNickName2.setText(getString(R.string.nickname).concat("：").concat(profile.getNickName()));
        }
        if (StringUtil.isNotEmpty(phone)) {
            tvPhone.setText(StringUtil.phoneText(phone));
        }
        tvGenderAddress.setText(getGender(profile.getGender()).concat(" ").concat(profile.getLocation()));
    }

    @Override
    public void deleteFriendSuccess() {
        TIMManagerExt.getInstance().deleteConversation(TIMConversationType.C2C, imUserId);
        CommonToast.toast(getString(R.string.friend_delete_success));
        UserCenter.getAllFriend();
        CActivityManager.getAppManager().finishOthersActivity(MainActivity.class);
//        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.MomentsGetUserInfo(imUserId);
    }

    @Override
    public void blackFriendSuccess() {
        TIMManagerExt.getInstance().deleteConversation(TIMConversationType.C2C, imUserId);//删除对话
        CommonToast.toast(R.string.add_blacklist);
        UserCenter.getAllFriend();
        CActivityManager.getAppManager().finishOthersActivity(MainActivity.class);
//        finish();
    }

    @Override
    public void showRemarkDialog() {
        mPresenter.showRemarkEditDialog(imUserId, remark);
    }

    @Override
    public void complaintsUser(String imUid) {
        startActivity(FeedBackActivity.newInstance(getCurContext(), imUid, 1));
    }

    @Override
    public void showMommetnsUserInfo(MomentsUserInfoModel model) {
        this.mUid = model.getUserId();
        if (StringUtil.isNotEmpty(model.getPhone())) {
            tvPhone.setText(model.getPhone());
        }
        List<String> momentList = model.getMomentList();
        if (momentList.size() > 4) {
            List<String> strings = momentList.subList(0, 4);
            imageAdapter.setList(strings);
        } else {
            imageAdapter.setList(model.getMomentList());
        }
        //设置马岛服务列表数据
        List<MomentsCityCircleImagesModel> cityCircleImages = model.getCityCircleImages();
        if (cityCircleImages.size() > 4) {
            mCityCircleAdapter.setList(cityCircleImages.subList(0, 4));
        } else {
            mCityCircleAdapter.setList(cityCircleImages);
        }
        //黄页
        List<MomentsCityCircleImagesModel> companyImages = model.getCompanyImages();
        if (cityCircleImages.size() > 4) {
            mYellowPageAdapter.setList(companyImages.subList(0, 4));
        } else {
            mYellowPageAdapter.setList(companyImages);
        }
        typeAdapter.setList(model.getTags());//标签列表数据
        recyclerImage.setVisibility(model.getMomentList() == null || model.getMomentList().size() == 0 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void setNickname(String nickname, boolean isChangeNickname) {
        this.remark = nickname;
        tvNickName.setText(nickname);
        if (isChangeNickname)
            EventBus.getDefault().post(new ModifyEvent(tvNickName.getText().toString().trim()));
    }


    //    @Override
//    public void editUserRemarkSuccess(TIMUserProfile profile) {
//        showImUserInfo(profile);
//    }

    private String getGender(int g) {
        if (g == 1) {
            return getString(R.string.man);

        } else if (g == 2) {
            return getString(R.string.woman);
        } else {
            return getString(R.string.unknow);
        }
    }

    @Override
    protected NewImUserDetailPresenter createPresenter() {
        return new NewImUserDetailPresenter(this);
    }

}
