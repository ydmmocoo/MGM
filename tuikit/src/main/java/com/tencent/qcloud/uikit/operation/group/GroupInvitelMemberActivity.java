package com.tencent.qcloud.uikit.operation.group;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.library.common.base.BaseActivity;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.CommonToast;
import com.library.common.utils.LanguageConvent;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupMemberResult;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.qcloud.uikit.R;
import com.tencent.qcloud.uikit.business.chat.model.GroupSelectModel;
import com.tencent.qcloud.uikit.business.chat.model.TimGroupInfoModel;
import com.tencent.qcloud.uikit.operation.group.adapter.GroupSelectFriendsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Author    by hanlz
 * Date      on 2019/12/4.
 * Description：
 */
public class GroupInvitelMemberActivity extends BaseActivity {


    private Map<Integer, TimGroupInfoModel> mValuesMap = new HashMap<>();

    EditText mEtSearch;
    RecyclerView mRecyclerView;
    private GroupSelectFriendsAdapter mAdapter;
    private String mGroupId;
    private TextView mTvRight;

    public static Intent newIntent(Context context, String groupId) {
        Intent intent = new Intent(context, GroupInvitelMemberActivity.class);
        intent.putExtra(IntentConstants.GROUP_ID, groupId);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.act_container_layout;
    }

    @Override
    protected void initView() {
        super.initView();
        mGroupId = getIntent().getStringExtra(IntentConstants.GROUP_ID);
        mRecyclerView = findViewById(R.id.rvGroupSelectFriends);
        mAdapter = new GroupSelectFriendsAdapter(new ArrayList<GroupSelectModel>());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnSelectItemClickListener(new OnSelectItemClickListener() {
            @Override
            public void onSelectItemClick(CheckBox checkBox, TimGroupInfoModel model, int position) {
                if (checkBox.isChecked()) {
                    mValuesMap.put(position, model);
                } else {
                    mValuesMap.remove(position);
                }
            }
        });
        mEtSearch = findViewById(R.id.etSearch);
        init();
        mTvRight = findViewById(R.id.tvRight);
        mTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInviteGroupMember();
            }
        });
        findViewById(R.id.toolbar_iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public interface OnSelectItemClickListener {
        void onSelectItemClick(CheckBox checkBox, TimGroupInfoModel model, int position);
    }


    private void init() {
        getAllFriend();
    }

    void getAllFriend() {
        showLoading();
        TIMFriendshipManager.getInstance().getFriendList(new TIMValueCallBack<List<TIMFriend>>() {

            @Override
            public void onError(int i, String s) {
                hideLoading();
            }

            @Override
            public void onSuccess(List<TIMFriend> timFriends) {
                hideLoading();
                List<GroupSelectModel> datas = getContactList(timFriends);
                mAdapter.setList(datas);
            }
        });
    }

    private List<GroupSelectModel> getContactList(List<TIMFriend> timFriends) {
        List<GroupSelectModel> list = new ArrayList<>();
        Map<String, List<TIMFriend>> map = new HashMap<>();

        for (TIMFriend timFriend : timFriends) {

            String nickName = timFriend.getTimUserProfile().getNickName();
            String remark = timFriend.getRemark();
            String content = "";
            if (!TextUtils.isEmpty(remark)) {
                content = remark;
            } else if (!TextUtils.isEmpty(nickName)) {
                content = nickName;
            } else {
                content = timFriend.getTimUserProfile().getIdentifier();
            }

            String firstChar = LanguageConvent.getFirstChar(content);
            if (map.containsKey(firstChar)) {
                List<TIMFriend> mapList = map.get(firstChar);
                mapList.add(timFriend);
            } else {
                List<TIMFriend> mapList = new ArrayList<>();
                mapList.add(timFriend);
                map.put(firstChar, mapList);
            }
        }
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String section = it.next();
            List<TIMFriend> friends = map.get(section);
            list.add(new GroupSelectModel(true, section));
            for (TIMFriend friend : friends) {
                TimGroupInfoModel model = new TimGroupInfoModel();
                model.setNickName(friend.getTimUserProfile().getNickName());
                model.setFaceUrl(friend.getTimUserProfile().getFaceUrl());
                model.setSelected(false);
                model.setRemark(friend.getRemark());
                model.setIdentifier(friend.getIdentifier());
                list.add(new GroupSelectModel(false,section,model));
            }
        }

        return list;
    }

    private void getInviteGroupMember() {
        List<String> memList = new ArrayList<>();
        for (int key :
                mValuesMap.keySet()) {
            String identifier = mValuesMap.get(key).getIdentifier();
            memList.add(identifier);
        }
        TIMGroupManager.getInstance().inviteGroupMember(mGroupId, memList, new TIMValueCallBack<List<TIMGroupMemberResult>>() {
            @Override
            public void onError(int i, String s) {
                finish();
                CommonToast.toast("code：" + i + "/desc：" + s);
            }

            @Override
            public void onSuccess(List<TIMGroupMemberResult> timGroupMemberResults) {
                finish();
                setResult(1212);
            }
        });
    }

}
