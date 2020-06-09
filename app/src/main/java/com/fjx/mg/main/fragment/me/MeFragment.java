package com.fjx.mg.main.fragment.me;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fjx.mg.food.OrderActivity;
import com.fjx.mg.R;
import com.fjx.mg.food.ChooseCouponActivity;
import com.fjx.mg.food.RedEnvelopeAndCouponActivity;
import com.fjx.mg.friend.addfriend.AddFriendActivity;
import com.fjx.mg.main.payment.questionceter.QuestionCenterActivity;
import com.fjx.mg.me.collect.MyCollectActivity;
import com.fjx.mg.me.comment.MyCommentActivity;
import com.fjx.mg.me.invite.InviteActivity;
import com.fjx.mg.me.level.LevelHomeActivity;
import com.fjx.mg.me.myhelp.MyHelpActivity;
import com.fjx.mg.me.partner.PartnerActivity;
import com.fjx.mg.me.publish.MyPublishActivity;
import com.fjx.mg.me.qr.QrCodeActivity;
import com.fjx.mg.me.record.BillRecord2Activity;
import com.fjx.mg.me.setting.SettingActivity;
import com.fjx.mg.me.wallet.WalletActivity;
import com.fjx.mg.recharge.record.BillRecordActivity;
import com.fjx.mg.setting.feedback.FeedBackActivity;
import com.fjx.mg.utils.DialogUtil;
import com.fjx.mg.web.CommonWebActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.library.common.base.BaseMvpFragment;
import com.library.common.callback.CActivityManager;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.Constant;
import com.library.repository.data.UserCenter;
import com.library.repository.models.CityCircleListModel;
import com.library.repository.models.ImUserRelaM;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MeFragment extends BaseMvpFragment<MePresenter> implements MeContract.View {

    @BindView(R.id.ivUserAvatar)
    CircleImageView ivUserAvatar;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvBalance)
    TextView tvBalance;
    @BindView(R.id.tvLevel)
    TextView tvLevel;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.tvQusetion)
    TextView tvQusetion;
    @BindView(R.id.tvPoint)
    TextView tvPoint;

    private MaterialDialog contactDialog;
    private UserInfoModel mUserInfoModel;

    public static MeFragment newInstance() {
        Bundle args = new Bundle();
        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_me;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        if (UserCenter.hasLogin()) {
            UserInfoModel userInfo = UserCenter.getUserInfo();
            showUserInfo(userInfo);
        } else {
            tvUserName.setText(getString(R.string.plese_loign));
            tvBalance.setText("0.00AR");
            ivUserAvatar.setImageResource(R.drawable.user_default);
            tvLevel.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.ivSetting)
    public void clickISetting() {
        startActivity(SettingActivity.newInstance(getCurContext()));
    }

    @Override
    public void doLoadVisible() {
//        if (UserCenter.isTimFirstLogin())
        mPresenter.getUserProfile();
    }

    @Override
    protected MePresenter createPresenter() {
        return new MePresenter(this);
    }


    @OnClick({R.id.tvMyCollect, R.id.tvMyComment, R.id.tvRedPacket, R.id.tvInviteFriend, R.id.tvOrderHint,R.id.tvWaitPay, R.id.tvWaitReceive,
            R.id.tvWaitComment, R.id.tvAfterSale, R.id.tvBillRecord, R.id.tvPaymentRecord, R.id.ivUserAvatar, R.id.llMyWallet,
            R.id.tvFeedBack, R.id.tvPublish, R.id.tvContactUs, R.id.tvLevel, R.id.tvQusetion, R.id.tvPartner, R.id.ivQrCode
            , R.id.tvBusinessCooperation, R.id.tvMyHelp,R.id.tv_order})
    public void onViewClicked(View view) {
        if (UserCenter.needLogin()) return;
        switch (view.getId()) {
            case R.id.ivUserAvatar:
                if (UserCenter.hasLogin()) {
                    String identifier = UserCenter.getUserInfo().getIdentifier();
                    mPresenter.findUser(identifier);
                } else {
                    new DialogUtil().showAlertDialog(getActivity(), R.string.tips, R.string.not_login_forward_login, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            UserCenter.goLoginActivity();
                        }
                    });
                }
                break;
            case R.id.tvMyCollect://我的收藏
                if (UserCenter.hasLogin()) {
                    startActivity(MyCollectActivity.newInstance(getCurContext()));
                } else {
                    new DialogUtil().showAlertDialog(getActivity(), R.string.tips, R.string.not_login_forward_login, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            UserCenter.goLoginActivity();
                        }
                    });
                }
                break;
            case R.id.tvMyComment://我的评论
                if (UserCenter.hasLogin()) {
                    startActivity(MyCommentActivity.newInstance(getCurContext()));
                } else {
                    new DialogUtil().showAlertDialog(getActivity(), R.string.tips, R.string.not_login_forward_login, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            UserCenter.goLoginActivity();
                        }
                    });
                }
                break;
            case R.id.tvRedPacket://红包
                startActivity(RedEnvelopeAndCouponActivity.newInstance(getCurContext()));
                break;
            case R.id.tvInviteFriend://邀请好友
                startActivity(InviteActivity.newInstance(getCurContext()));
                break;
            case R.id.tvOrderHint://订单全部
                startActivity(OrderActivity.newInstance(getCurContext(),0));
                break;
            case R.id.tvWaitPay://待付款
                startActivity(OrderActivity.newInstance(getCurContext(),1));
                break;
            case R.id.tvWaitReceive://待收货
                startActivity(OrderActivity.newInstance(getCurContext(),0));
                break;
            case R.id.tvWaitComment://待评价
                startActivity(OrderActivity.newInstance(getCurContext(),2));
                break;
            case R.id.tvAfterSale://售后
                startActivity(OrderActivity.newInstance(getCurContext(),3));
                break;
            case R.id.tvBillRecord://账单
                if (UserCenter.hasLogin()) {
                    startActivity(BillRecord2Activity.newInstance(getCurContext()));
                } else {
                    new DialogUtil().showAlertDialog(getActivity(), R.string.tips, R.string.not_login_forward_login, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            UserCenter.goLoginActivity();
                        }
                    });
                }
                break;
            case R.id.tvPaymentRecord://缴费
                if (UserCenter.hasLogin()) {
                    startActivity(BillRecordActivity.newInstance(getCurContext()));
                } else {
                    new DialogUtil().showAlertDialog(getActivity(), R.string.tips, R.string.not_login_forward_login, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            UserCenter.goLoginActivity();
                        }
                    });
                }
                break;
            case R.id.llMyWallet://钱包
                if (UserCenter.hasLogin()) {
                    startActivity(WalletActivity.newInstance(getCurContext()));
                } else {
                    new DialogUtil().showAlertDialog(getActivity(), R.string.tips, R.string.not_login_forward_login, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            UserCenter.goLoginActivity();
                        }
                    });
                }
                break;
            case R.id.tvPublish://发布
                if (UserCenter.hasLogin()) {
                    startActivity(MyPublishActivity.newInstance(getCurContext()));
                } else {
                    new DialogUtil().showAlertDialog(getActivity(), R.string.tips, R.string.not_login_forward_login, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            UserCenter.goLoginActivity();
                        }
                    });
                }
                break;
            case R.id.tvFeedBack://反馈
                startActivity(FeedBackActivity.newInstance(getCurContext()));
                break;
            case R.id.tvContactUs://联系我们
                showContactUsDialog();
                break;
            case R.id.tvLevel:
                startActivity(LevelHomeActivity.newInstance(getCurContext()));
                break;
            case R.id.tvQusetion://我的问答
                startActivity(QuestionCenterActivity.newInstance(getCurContext()));
                break;
            case R.id.tvPartner://成为股东
                startActivity(PartnerActivity.newIntent(getActivity()));
                break;
            case R.id.ivQrCode://个人二维码,用于添加MGM好友
                String uSex = UserCenter.getUserInfo().getUSex();
                Intent intent = new Intent(getActivity(), QrCodeActivity.class);
                intent.putExtra("tvGender", uSex);
                startActivity(intent);
                break;
            case R.id.tvBusinessCooperation:
                StringBuilder sb = new StringBuilder();
                sb.append(Constant.HOST);
                sb.append("invite/merchants?gms=");
                boolean isOpenGms = false;
                GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
                int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(getActivity());
                if (resultCode != ConnectionResult.SUCCESS) {
                    //未安装谷歌服务
                    isOpenGms = false;
                } else {
                    isOpenGms = true;
                }
                sb.append(isOpenGms + "");
                sb.append("&l=");
                sb.append(RepositoryFactory.getLocalRepository().getLangugeType());
                Activity activity = CActivityManager.getAppManager().currentActivity();
                CommonWebActivity.Options options = new CommonWebActivity.Options();
                options.setTitle(getString(R.string.business_cooperation));
                options.setLoadUrl(sb.toString());
                activity.startActivity(CommonWebActivity.newInstance(activity, JsonUtil.moderToString(options)));
                break;
            case R.id.tvMyHelp://我的帮助
                startActivity(MyHelpActivity.newIntent(getActivity()));
                break;
            case R.id.tv_order://我的订单
                break;
            default:
                break;
        }
    }

    @Override
    public void showUserInfo(UserInfoModel userInfoModel) {
        mUserInfoModel = userInfoModel;
        CommonImageLoader.load(userInfoModel.getUImg()).placeholder(R.drawable.user_default).into(ivUserAvatar);
        tvUserName.setText(userInfoModel.getUNick());
        tvBalance.setText(String.format("%s%s", userInfoModel.getUMoney(), "AR"));
        tvPoint.setText(userInfoModel.getScore());
        tvLevel.setText(getString(R.string.lv).concat(userInfoModel.getRank()));

        // TODO: 2019/7/26 第一版先不用 
//        tvLevel.setVisibility(View.VISIBLE);
        tvStatus.setVisibility(userInfoModel.isForbidden() ? View.VISIBLE : View.GONE);
    }


    @Override
    public void showContactUsDialog() {
        contactDialog = new MaterialDialog.Builder(getCurActivity())
                .customView(R.layout.dialog_contact_us, true)
                .backgroundColor(ContextCompat.getColor(getCurContext(), R.color.trans))
                .build();
        contactDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = contactDialog.getCustomView();
        if (view == null) return;
        TextView textEmail = view.findViewById(R.id.textEmail);
        final TextView tvTel = view.findViewById(R.id.tvTel);
        final TextView tvWechat = view.findViewById(R.id.tvWechat);
        final TextView tvMGM = view.findViewById(R.id.tvMGM);
        textEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactDialog.dismiss();
                StringUtil.copyClip(getString(R.string.mg_mail));
                CommonToast.toast(getString(R.string.copy_success));
            }
        });
        tvTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactDialog.dismiss();
                StringUtil.copyClip(tvTel.getText().toString());
                CommonToast.toast(getString(R.string.copy_success));
            }
        });
        tvWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactDialog.dismiss();
                StringUtil.copyClip(tvWechat.getText().toString());
                CommonToast.toast(getString(R.string.copy_success));
            }
        });
        tvMGM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactDialog.dismiss();
                StringUtil.copyClip(tvMGM.getText().toString());
                CommonToast.toast(getString(R.string.copy_success));
            }
        });
//        GradientDrawableHelper.whit(tvCopy).setColor(R.color.colorAccent).setCornerRadius(50);

        contactDialog.show();
    }

    @Override
    public void isMe(ImUserRelaM userRelaM) {
        if (!userRelaM.isFriend()) {
            startActivity(AddFriendActivity.newInstance(getCurContext(), JsonUtil.moderToString(userRelaM.getUserProfile())));
        }
    }

    @Override
    public void requestIde(CityCircleListModel model) {
    }
}
