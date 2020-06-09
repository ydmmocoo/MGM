package com.fjx.mg.job.detail;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.dialog.ShareDialog;
import com.fjx.mg.friend.addfriend.AddFriendActivity;
import com.fjx.mg.friend.chat.ChatActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.IntentUtil;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.Constant;
import com.library.repository.data.UserCenter;
import com.library.repository.models.JobModel;
import com.lxj.xpopup.XPopup;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.friendship.TIMFriend;

import butterknife.BindView;
import butterknife.OnClick;

public class JobDetailActivity extends BaseMvpActivity<JobDetailPresenter> implements JobDetailContract.View {

    @BindView(R.id.tvPay)
    TextView tvPay;
    @BindView(R.id.tvhint2)
    TextView tvhint2;
    @BindView(R.id.tvhint1)
    TextView tvhint1;
    @BindView(R.id.tvJobDesc)
    TextView tvJobDesc;
    @BindView(R.id.tvCompanyDesc)
    TextView tvCompanyDesc;
    @BindView(R.id.llCompanyDesc)
    LinearLayout llCompanyDesc;
    @BindView(R.id.tvAdress)
    TextView tvAdress;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvWechat)
    TextView tvWechat;
    @BindView(R.id.toolbar_iv_back)
    ImageView toolbarIvBack;
    @BindView(R.id.toolbar_tv_title)
    TextView toolbarTvTitle;
    @BindView(R.id.toolbar_iv_right)
    ImageView toolbarIvRight;
    @BindView(R.id.toolbar_tv_right)
    TextView toolbarTvRight;
    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.tvhint3)
    TextView tvhint3;
    @BindView(R.id.tvJobType)
    TextView tvJobType;
    @BindView(R.id.tvWorkYear)
    TextView tvWorkYear;
    @BindView(R.id.tvEducation)
    TextView tvEducation;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvCallPhone)
    TextView tvCallPhone;
    @BindView(R.id.tvAddFriend)
    TextView tvAddFriend;
    @BindView(R.id.tvJobTypeHint)
    TextView tvJobTypeHint;
    @BindView(R.id.tvWorkYearHint)
    TextView tvWorkYearHint;
    @BindView(R.id.tvEducationHint)
    TextView tvEducationHint;
    @BindView(R.id.tvAddressHint)
    TextView tvAddressHint;
    private String jobId;
    private boolean isRecruit, hasCollect;
    private boolean isMyPublish;

    private ImageView ivRightImage;
    private JobModel jobModel;
    private String shareDesc;
    private String shareImage;
    private String shareUrl;
    private String shareTitle;

    public static Intent newInstance(Context context, String id, boolean isRecruit) {
        Intent intent = new Intent(context, JobDetailActivity.class);
        intent.putExtra("cid", id);
        intent.putExtra("isRecruit", isRecruit);
        return intent;
    }

    public static Intent newInstance(Context context, String id, boolean isRecruit, boolean isMyPublish) {
        Intent intent = new Intent(context, JobDetailActivity.class);
        intent.putExtra("cid", id);
        intent.putExtra("isRecruit", isRecruit);
        intent.putExtra("isMyPublish", isMyPublish);
        return intent;
    }

    @Override
    protected JobDetailPresenter createPresenter() {
        return new JobDetailPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_job_detail;
    }


    @Override
    protected void initView() {
        isRecruit = getIntent().getBooleanExtra("isRecruit", false);
        isMyPublish = getIntent().getBooleanExtra("isMyPublish", false);
        jobId = getIntent().getStringExtra("cid");
        int drawable = isMyPublish ? R.drawable.more_black : R.drawable.more_black;

        ivRightImage = ToolBarManager.with(this).setTitle(isRecruit ? getString(R.string.recruit_message) : getString(R.string.job_wanted_message))
                .setRightImage(drawable, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.showPublishDialog(ivRightImage, isMyPublish, isMyPublish);
//                        if (isMyPublish) {
//                            if (jobModel == null) return;
//                            mPresenter.showPublishDialog(ivRightImage,isMyPublish,isMyPublish);
//                        } else {
//                            mPresenter.toggleCollect(jobId, hasCollect);
//                        }
                    }
                });
        mPresenter.getJobDetail(jobId);
        llCompanyDesc.setVisibility(isRecruit ? View.GONE : View.GONE);
        tvhint2.setText(isRecruit ? getString(R.string.job_describe) : getString(R.string.exp_describe));
    }

    @Override
    public void showJobDetaol(JobModel data) {
        shareTitle = data.getTitle();
        shareDesc = data.getDesc();
        shareImage = "";


        //2求职信息 jobInfo 1招聘信息 recruitInfo


        // http://47.97.159.184/invite/renting?hid=9

        StringBuilder sb = new StringBuilder();
        sb.append(Constant.HOST);
        sb.append("invite/");

        switch (data.getType()) {
            case 1:
                sb.append("recruitInfo");
                break;
            case 2:
                sb.append("jobInfo");
                break;
        }

        sb.append("?type=");
        int type = data.getType();
        sb.append("" + type);

        sb.append("&id=");
        sb.append("" + data.getJobId());
        Log.e("分享链接:", sb.toString());


        shareUrl = sb.toString();


        jobModel = data;
        hasCollect = data.isCollect();
        tvhint1.setText(data.getTitle());
        tvPay.setText(data.getPay());

        tvJobTypeHint.setText(getString(R.string.job) + "：");
        tvEducationHint.setText(getString(R.string.edu) + "：");
        tvWorkYearHint.setText(getString(R.string.exp) + "：");
        tvAddressHint.setText(getString(R.string.address1) + "：");

        tvJobType.setText(data.getJobType());
        tvWorkYear.setText(data.getWorkYear());
        tvEducation.setText(data.getEducation());
        tvAddress.setText(data.getCountryName().concat(data.getCityName()));


        tvAdress.setText(data.getCityName());
        tvPhone.setText(data.getContactPhone());
        tvWechat.setText(data.getContactWeixin());
        tvJobDesc.setText(data.getDesc());
        String cdesc = TextUtils.isEmpty(data.getCompanyDesc()) ? getString(R.string.no_describe) : data.getCompanyDesc();
        tvCompanyDesc.setText(cdesc);

        if (isMyPublish) return;
        ivRightImage.setImageResource(hasCollect ? R.drawable.more_black : R.drawable.more_black);


    }

    @Override
    public void toggleCollectResult(boolean isCollect) {
        hasCollect = isCollect;
        if (isMyPublish) return;
        ivRightImage.setImageResource(isCollect ? R.drawable.more_black : R.drawable.more_black);
    }


    @OnClick({R.id.tvPhone, R.id.tvWechat, R.id.tvCallPhone, R.id.tvAddFriend})
    public void onViewClicked(View view) {
        if (UserCenter.needLogin()) return;
        switch (view.getId()) {
            case R.id.tvPhone:
                break;
            case R.id.tvWechat:
                StringUtil.copyClip(tvWechat.getText().toString());
                CommonToast.toast(getString(R.string.wechat_had_copy));
                break;
            case R.id.tvCallPhone:
                String phone = tvPhone.getText().toString();
                IntentUtil.callPhone(phone);
                break;
            case R.id.tvAddFriend:
                String mgcode = tvWechat.getText().toString();
                mPresenter.findImUser(mgcode);
                break;
        }
    }

    @Override
    public void getImUserSuccess(TIMUserProfile profile) {
        startActivity(AddFriendActivity.newInstance(getCurContext(), JsonUtil.moderToString(profile)));
    }

    @Override
    public void getImUserSuccess(TIMFriend friend) {
//        startActivity(ImUserDetailActivity.newInstance(getCurContext(),
//                friend.getTimUserProfile().getIdentifier()));

        String string = friend.getTimUserProfile().getNickName();
        if (!TextUtils.isEmpty(friend.getRemark()))
            string = friend.getRemark();
        ChatActivity.startC2CChat(getCurContext(), friend.getIdentifier(), string);

    }

    @Override
    public JobModel getJobModel() {
        return jobModel;
    }

    @Override
    public void refreshData() {
        mPresenter.getJobDetail(jobId);
    }

    @Override
    public void toggleCollect() {
        mPresenter.toggleCollect(jobId, hasCollect);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode) refreshData();
    }
}
