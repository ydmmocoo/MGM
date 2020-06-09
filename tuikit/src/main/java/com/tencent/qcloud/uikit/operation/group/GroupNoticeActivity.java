package com.tencent.qcloud.uikit.operation.group;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.common.base.BaseActivity;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.CommonToast;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.qcloud.uikit.R;


/**
 * Author    by hanlz
 * Date      on 2019/12/5.
 * Description：修改群公告
 */
public class GroupNoticeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = GroupNoticeActivity.class.getSimpleName();

    private EditText mEtNotice;
    private ImageView mIvBack;
    private TextView mTvTitle;
    private TextView mTvRight;
    private TextView mTvTips;

    public static Intent newIntent(Context context, String groupId, String content) {
        Intent intent = new Intent(context, GroupNoticeActivity.class);
        intent.putExtra(IntentConstants.GROUP_ID, groupId);
        intent.putExtra(IntentConstants.CONTENT, content);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.act_group_notice;
    }

    @Override
    protected void initView() {
        super.initView();
        findViews();
        boolean extra = getIntent().getBooleanExtra(IntentConstants.IS_OWNER, false);
        if (extra) {
            mTvTips.setVisibility(View.GONE);
            mTvRight.setVisibility(View.VISIBLE);
        } else {
            mTvTips.setVisibility(View.VISIBLE);
            mEtNotice.setFocusable(false);
            mTvRight.setVisibility(View.GONE);
        }
    }

    private void findViews() {
        mTvRight = findViewById(R.id.tvRight);
        mTvTitle = findViewById(R.id.toolbar_tv_title);
        mIvBack = findViewById(R.id.toolbar_iv_back);
        mEtNotice = findViewById(R.id.etNotice);
        mTvTips = findViewById(R.id.tvtips);
        mTvRight.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mEtNotice.setText(getIntent().getStringExtra(IntentConstants.CONTENT));
    }

    private void modifyGroupInfo() {
        if (TextUtils.isEmpty(mEtNotice.getText().toString())) {
            CommonToast.toast(getString(R.string.modification_group_notice_hint));
            return;
        }
        TIMGroupManager.ModifyGroupInfoParam param = new TIMGroupManager.ModifyGroupInfoParam(getIntent().getStringExtra(IntentConstants.GROUP_ID));
        param.setNotification(mEtNotice.getText().toString());
        TIMGroupManager.getInstance().modifyGroupInfo(param, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                Log.e(TAG, "modify group info failed, code:" + code + "|desc:" + desc);
            }

            @Override
            public void onSuccess() {
                Log.e(TAG, "modify group info succ");
                Intent intent = new Intent();
                intent.putExtra("message", mEtNotice.getText().toString());
                setResult(1, intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvRight) {
            modifyGroupInfo();
        } else if (view.getId() == R.id.toolbar_iv_back) {
            finish();
        }
    }
}
