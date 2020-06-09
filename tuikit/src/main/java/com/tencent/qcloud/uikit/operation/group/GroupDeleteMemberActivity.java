package com.tencent.qcloud.uikit.operation.group;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.library.common.base.BaseActivity;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.CommonToast;
import com.library.common.utils.LanguageConvent;
import com.library.common.utils.StatusBarManager;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupMemberResult;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.qcloud.uikit.R;
import com.tencent.qcloud.uikit.business.chat.group.model.GroupChatManager;
import com.tencent.qcloud.uikit.business.chat.group.model.GroupMemberInfo;
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
 * Date      on 2019/12/5.
 * Description：
 */
public class GroupDeleteMemberActivity extends BaseActivity {

    private Map<Integer, TimGroupInfoModel> mValuesMap = new HashMap<>();

    private ImageView mIvBack;
    private EditText mEtSearch;
    private RecyclerView mRecyclerView;
    private GroupSelectFriendsAdapter mAdapter;
    private String mGroupId;
    private TextView mTvRight;

    public static Intent newIntent(Context activity, String groupId) {
        Intent intent = new Intent(activity, GroupDeleteMemberActivity.class);
        intent.putExtra(IntentConstants.GROUP_ID, groupId);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.act_group_delete_member;
    }

    @Override
    protected void initView() {
        super.initView();
        mGroupId = getIntent().getStringExtra(IntentConstants.GROUP_ID);
        mRecyclerView = findViewById(R.id.rvGroupSelectFriends);
        mAdapter = new GroupSelectFriendsAdapter(new ArrayList<GroupSelectModel>());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnSelectItemClickListener(new GroupInvitelMemberActivity.OnSelectItemClickListener() {
            @Override
            public void onSelectItemClick(CheckBox checkBox, TimGroupInfoModel model, int position) {
                if (checkBox.isChecked()) {
                    mValuesMap.put(position, model);
                } else {
                    mValuesMap.remove(position);
                }
            }
        });
        init();
        mEtSearch = findViewById(R.id.etSearch);
        mTvRight = findViewById(R.id.tvRight);
        mIvBack=findViewById(R.id.toolbar_iv_back);
        //返回
        mIvBack.setOnClickListener(v -> finish());
        mTvRight.setOnClickListener(view -> deleteGroupMember());
    }


    private void init() {
        showLoading();
        TIMGroupManager.getInstance().getGroupMembers(mGroupId, new TIMValueCallBack<List<TIMGroupMemberInfo>>() {
            @Override
            public void onError(int i, String s) {
                hideLoading();
            }

            @Override
            public void onSuccess(List<TIMGroupMemberInfo> timGroupMemberInfos) {
                hideLoading();
                List<TIMUserProfile> timUserProfiles = new ArrayList<>();
                for (int i = 0; i < timGroupMemberInfos.size(); i++) {
                    TIMUserProfile timUserProfile = TIMFriendshipManager.getInstance().queryUserProfile(timGroupMemberInfos.get(i).getUser());
                    if (timUserProfile == null){
                        continue;
                    }
                    timUserProfiles.add(timUserProfile);
                }
                List<GroupSelectModel> datas = getContactList(timUserProfiles);
                mAdapter.setList(datas);
            }
        });
    }


    private List<GroupSelectModel> getContactList(List<TIMUserProfile> timUserProfiles) {
        List<GroupSelectModel> list = new ArrayList<>();
        Map<String, List<TIMUserProfile>> map = new HashMap<>();

        for (TIMUserProfile timUserProfile : timUserProfiles) {

            String identifier = timUserProfile.getIdentifier();
            String nickName = timUserProfile.getNickName();
            String name = "";
            if (!TextUtils.isEmpty(nickName)) {
                name = nickName;
            } else {
                name = identifier;
            }
            String firstChar = LanguageConvent.getFirstChar(name);
            if (map.containsKey(firstChar)) {
                List<TIMUserProfile> mapList = map.get(firstChar);
                mapList.add(timUserProfile);
            } else {
                List<TIMUserProfile> mapList = new ArrayList<>();
                mapList.add(timUserProfile);
                map.put(firstChar, mapList);
            }
        }
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String section = it.next();
            List<TIMUserProfile> friends = map.get(section);
            list.add(new GroupSelectModel(true, section));
            for (TIMUserProfile friend : friends) {
                TimGroupInfoModel memberModel = new TimGroupInfoModel();
                list.add(new GroupSelectModel(false,section,memberModel));
                memberModel.setFaceUrl(friend.getFaceUrl());
                memberModel.setNickName(friend.getNickName());
                memberModel.setRemark(friend.getNickName());
                memberModel.setUserName(friend.getNickName());
                memberModel.setIdentifier(friend.getIdentifier());
                memberModel.setSelected(false);
            }
        }

        return list;
    }

    public void deleteGroupMember() {
        ArrayList list = new ArrayList();
        for (int key :
                mValuesMap.keySet()) {
            TimGroupInfoModel timGroupInfoModel = mValuesMap.get(key);
            list.add(timGroupInfoModel.getIdentifier());
        }
        TIMGroupManager.DeleteMemberParam param = new TIMGroupManager.DeleteMemberParam(getIntent().getStringExtra(IntentConstants.GROUP_ID), list);
        param.setReason("some reason");

        TIMGroupManager.getInstance().deleteGroupMember(param, new TIMValueCallBack<List<TIMGroupMemberResult>>() {
            @Override
            public void onError(int code, String desc) {
                Log.e("hanlz", "deleteGroupMember onErr. code: " + code + " errmsg: " + desc);
            }

            @Override
            public void onSuccess(List<TIMGroupMemberResult> results) { //群组成员操作结果
                for (TIMGroupMemberResult r : results) {
                    Log.d("hanlz", "result: " + r.getResult()  //操作结果:  0：删除失败；1：删除成功；2：不是群组成员
                            + " user: " + r.getUser());    //用户帐号
                }
                finish();
                CommonToast.toast("移除成功");
            }
        });
    }
}
