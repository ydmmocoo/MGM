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
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.log.QLog;
import com.tencent.qcloud.uikit.R;
import com.tencent.qcloud.uikit.business.chat.group.model.GroupInfoUtils;
import com.tencent.qcloud.uikit.common.IUIKitCallBack;
import com.tencent.qcloud.uikit.common.utils.UIUtils;


/**
 * Author    by hanlz
 * Date      on 2019/12/5.
 * Description：修改群昵称/和群名字
 */
public class GroupNicknameActivity extends BaseActivity implements View.OnClickListener {


    private EditText mEtNotice;
    private ImageView mIvBack;
    private TextView mTvRight;

    private int mType;

    /**
     * @param context
     * @param groupId
     * @param hint
     * @param type    1--群昵称 2--群名字
     * @return
     */
    public static Intent newIntent(Context context, String groupId, String hint, int type, String content) {
        Intent intent = new Intent(context, GroupNicknameActivity.class);
        intent.putExtra(IntentConstants.GROUP_ID, groupId);
        intent.putExtra(IntentConstants.HINT, hint);
        intent.putExtra(IntentConstants.TYPE, type);
        intent.putExtra(IntentConstants.CONTENT, content);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.act_group_nickname;
    }

    @Override
    protected void initView() {
        super.initView();
        if (getIntent() == null) {
            return;
        }
        mType = getIntent().getIntExtra(IntentConstants.TYPE, 0);
        findViews();
        mEtNotice.setHint(getIntent().getStringExtra(IntentConstants.HINT));
        mEtNotice.setText(getIntent().getStringExtra(IntentConstants.CONTENT));
    }

    private void findViews() {
        mTvRight = findViewById(R.id.tvRight);
        mIvBack = findViewById(R.id.toolbar_iv_back);
        mEtNotice = findViewById(R.id.etNotice);
        mTvRight.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
    }

    /**
     * 修改群名
     *
     * @param name
     */
    public void modifyGroupName(final String name) {
        if (TextUtils.isEmpty(name)) {
            CommonToast.toast(getIntent().getStringExtra(IntentConstants.HINT));
            return;
        }
        TIMGroupManager.ModifyGroupInfoParam param = new TIMGroupManager.ModifyGroupInfoParam(getIntent().getStringExtra(IntentConstants.GROUP_ID));
        param.setGroupName(name);
        TIMGroupManager.getInstance().modifyGroupInfo(param, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {
                Intent intent = new Intent();
                intent.putExtra("message", mEtNotice.getText().toString());
                setResult(1, intent);
                finish();
            }
        });
    }

    private void modifyGroupNickname(String nickname) {
        if (TextUtils.isEmpty(nickname)) {
            CommonToast.toast(getIntent().getStringExtra(IntentConstants.HINT));
            return;
        }
        TIMGroupManager.ModifyMemberInfoParam param = new TIMGroupManager.ModifyMemberInfoParam(getIntent().getStringExtra(IntentConstants.GROUP_ID), TIMManager.getInstance().getLoginUser());
        param.setNameCard(nickname);
        TIMGroupManager.getInstance().modifyMemberInfo(param, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                UIUtils.toastLongMessage("modifyGroupNickname fail: " + code + "=" + desc);
            }

            @Override
            public void onSuccess() {
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
            switch (mType) {
                case 1:
                    modifyGroupNickname(mEtNotice.getText().toString());
                    break;
                case 2:
                    modifyGroupName(mEtNotice.getText().toString());
                    break;
                default:
                    break;
            }
        } else if (view.getId() == R.id.toolbar_iv_back) {
            finish();
        }
    }
}
