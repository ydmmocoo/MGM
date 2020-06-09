package com.fjx.mg.friend.chat;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.friend.chat.adapter.GroupSelectFriendsAdapter;
import com.library.common.base.BaseActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.LanguageConvent;
import com.library.common.utils.StringUtil;
import com.library.repository.data.UserCenter;
import com.library.repository.models.FriendContactSectionModel;
import com.library.repository.models.GroupSelectModel;
import com.library.repository.models.TimGroupInfoModel;
import com.library.repository.repository.RepositoryFactory;
import com.library.repository.util.LogUtil;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfo;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfoUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * author：      hanlz
 * create by：  2019-12-2 15:00:25
 * description：发起群聊选择好友列表
 */
public class StartGroupChatActivity extends BaseActivity {


    private Map<Integer, TimGroupInfoModel> mValuesMap = new HashMap<>();

    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.rvGroupSelectFriends)
    RecyclerView mRecyclerView;
    private GroupSelectFriendsAdapter mAdapter;
    private List<TIMFriend> myTimFriends;

    public static Intent newIntent(Context context, String members) {
        Intent intent = new Intent(context, StartGroupChatActivity.class);
        intent.putExtra(IntentConstants.EXT, members);
        return intent;
    }


    @Override
    protected int layoutId() {
        return R.layout.act_start_group_chat;
    }

    @Override
    protected void initView() {
        super.initView();
        ToolBarManager toolBarManager = ToolBarManager.with(this).setTitle(getString(R.string.create_group_chat2));
        toolBarManager.setRightText(getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGroupModel();
            }
        });
        mAdapter = new GroupSelectFriendsAdapter(new ArrayList<GroupSelectModel>());
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(1));
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
        getAllFriend();
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (myTimFriends == null) return;
                String content = editable.toString();
                if (TextUtils.isEmpty(content)) {
                    List<GroupSelectModel> datas = getContactList(myTimFriends);
                    mAdapter.setList(datas);
                } else {
                    List<TIMFriend> searchList = new ArrayList<>();
                    for (TIMFriend friend : myTimFriends) {
                        String phone = "";
                        byte[] bytes = friend.getTimUserProfile().getCustomInfo().get("phone");
                        if (bytes != null)
                            phone = new String(bytes);
                        boolean b = !TextUtils.isEmpty(phone) && phone.contains(content);
                        if (JsonUtil.moderToString(friend).contains(content) || b) {
                            searchList.add(friend);
                        }
                    }
                    List<GroupSelectModel> datas = getContactList(searchList);
                    mAdapter.setList(datas);
                }
            }
        });
    }

    public interface OnSelectChangedListener {
        void onSelectChanged(TimGroupInfoModel model, boolean selected, int position);
    }

    public interface OnSelectItemClickListener {
        void onSelectItemClick(CheckBox checkBox, TimGroupInfoModel model, int position);
    }

    void getAllFriend() {
        showLoading();
        RepositoryFactory.getChatRepository().getAllFriend(new TIMValueCallBack<List<TIMFriend>>() {

            @Override
            public void onError(int i, String s) {
                hideLoading();
            }

            @Override
            public void onSuccess(List<TIMFriend> timFriends) {
                myTimFriends = timFriends;
                hideLoading();
                RepositoryFactory.getLocalRepository().saveAllFriend(timFriends);
                Log.d("getAllFriend", JsonUtil.moderToString(timFriends));
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
                if (getIntent() != null) {
                    String extra = getIntent().getStringExtra(IntentConstants.EXT);
                    if (TextUtils.isEmpty(extra)) {
                        model.setSelected(false);
                    } else {
                        TimGroupInfoModel infoModel = JsonUtil.strToModel(extra, TimGroupInfoModel.class);
                        if (TextUtils.equals(friend.getIdentifier(), infoModel.getIdentifier())) {
                            model.setSelected(true);
                        } else {
                            model.setSelected(false);
                        }
                    }

                } else {
                    model.setSelected(false);
                }
                model.setRemark(friend.getRemark());
                model.setIdentifier(friend.getIdentifier());
                list.add(new GroupSelectModel(false,section,model));
            }
        }

        return list;
    }

    String message;

    private void createGroupModel() {
        StringBuilder groupName = new StringBuilder();
        TIMUserProfile timUserProfile = TIMFriendshipManager.getInstance().queryUserProfile(UserCenter.getTimUser().getIdentifier());
        if (!TextUtils.isEmpty(timUserProfile.getNickName())) {
            groupName.append(timUserProfile.getNickName());
        } else {
            groupName.append(UserCenter.getTimUser().getIdentifier());
        }
        groupName.append("、");
        List<TIMGroupMemberInfo> infos = new ArrayList<>();
        //添加自己到默认成员中
        TIMGroupMemberInfo myInfo = new TIMGroupMemberInfo(UserCenter.getTimUser().getIdentifier());
        infos.add(myInfo);
        if (getIntent() != null) {
            String extra = getIntent().getStringExtra(IntentConstants.EXT);
            if (!TextUtils.isEmpty(extra)) {
                TimGroupInfoModel infoModel = JsonUtil.strToModel(extra, TimGroupInfoModel.class);
                mValuesMap.put(mValuesMap.size() + 1, infoModel);
            }
        }
        for (int key : mValuesMap.keySet()) {
            String nickName = mValuesMap.get(key).getNickName();
            String remark = mValuesMap.get(key).getRemark();
            String content = "";
            if (!TextUtils.isEmpty(remark)) {
                content = remark;
            } else if (!TextUtils.isEmpty(nickName)) {
                content = nickName;
            } else {
                content = mValuesMap.get(key).getIdentifier();
            }
            groupName.append(content);
            groupName.append("、");
            TIMGroupMemberInfo member = new TIMGroupMemberInfo(mValuesMap.get(key).getIdentifier());
            member.setNameCard(content);
            infos.add(member);
        }
        String name = groupName.toString();
        if (name.length() > 10) {
            name = name.substring(0, 10);
        }
        TIMGroupManager timGroupManager = TIMGroupManager.getInstance();
        TIMGroupManager.CreateGroupParam param = new TIMGroupManager.CreateGroupParam("Private", name);
        //添加群成员
        param.setMembers(infos);
        //指定群简介
        param.setIntroduction("hello world");
        //指定群公告
        param.setNotification(getString(R.string.group_emptry_tips));
        timGroupManager.createGroup(param, new TIMValueCallBack<String>() {
            @Override
            public void onError(int i, String s) {
                Log.e("StartGroupChatActivity", s + "/code：" + i);
                CommonToast.toast(s);
            }

            @Override
            public void onSuccess(final String groupId) {
                ChatActivity.startGroupChat(StartGroupChatActivity.this, groupId);
                //获取群聊会话
                TIMFriendshipManager instance = TIMFriendshipManager.getInstance();
                TIMUserProfile profile = instance.queryUserProfile(TIMManager.getInstance().getLoginUser());
                if (profile == null) {
                    List<String> ids = new ArrayList<>();
                    ids.add(TIMManager.getInstance().getLoginUser());
                    instance.getUsersProfile(ids, false, new TIMValueCallBack<List<TIMUserProfile>>() {
                        @Override
                        public void onError(int i, String s) {

                        }

                        @Override
                        public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                            message = timUserProfiles.get(0).getNickName() + getString(R.string.create_group_chat);
                        }
                    });
                } else {
                    message = profile.getNickName() + getString(R.string.create_group_chat);
                }
                final MessageInfo createTips = MessageInfoUtil.buildGroupCustomMessage(MessageInfoUtil.GROUP_CREATE, message);
                createTips.setPeer(groupId);
                TIMConversation conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, groupId);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                createTips.setSelf(true);
                createTips.setRead(true);
                conversation.sendMessage(createTips.getTIMMessage(), new TIMValueCallBack<TIMMessage>() {
                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onSuccess(TIMMessage timMessage) {
                        finish();//创建群聊后发送一条创建群聊的消息
                        if (getIntent() != null) {
                            if (StringUtil.isNotEmpty(getIntent().getStringExtra(IntentConstants.EXT))) {
                                SettingsEvent settingsEvent = new SettingsEvent();
                                settingsEvent.setType("1");
                                EventBus.getDefault().post(settingsEvent);
                            }
                        }
                    }
                });

            }
        });
    }
}
