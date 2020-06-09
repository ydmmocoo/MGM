package com.tencent.qcloud.uikit.operation.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.library.common.base.BaseActivity;
import com.library.common.constant.IntentConstants;
import com.tencent.qcloud.uikit.R;
import com.tencent.qcloud.uikit.common.UIKitConstants;

/**
 * Author    by hanlz
 * Date      on 2019/12/4.
 * Description：
 */
public class GroupMemberActivity extends BaseActivity {


    //    private MemberAdapter mAdapter;
//    private PageTitleBar mTitleBar;
//    private String groupId;
//
    public static Intent newIntent(Context context, String groupId) {
        Intent intent = new Intent(context, GroupMemberActivity.class);
        intent.putExtra(IntentConstants.GROUP_ID, groupId);
        return intent;
    }


    @Override
    protected int layoutId() {
        return R.layout.act_group_member;
    }

    @Override
    protected void initView() {
        super.initView();
        GroupMemberFragment fragment = new GroupMemberFragment();
        Bundle bundle = new Bundle();
        bundle.putString(UIKitConstants.GROUP_ID, getIntent().getStringExtra(IntentConstants.GROUP_ID));
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content,fragment).commit();
    }

    //
//    @Override
//    protected void initView() {
//        super.initView();
//        groupId = getIntent().getStringExtra(IntentConstants.GROUP_ID);
//        mTitleBar = findViewById(R.id.group_info_title_bar);
//        mTitleBar.getRightGroup().setVisibility(View.GONE);
//        mTitleBar.setTitle(getResources().getString(R.string.group_detail), PageTitleBar.POSITION.CENTER);
//        mTitleBar.setLeftClick(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//        mAdapter = new MemberAdapter(new ArrayList<MemberSectionModel>());
//        RecyclerView rvMember = findViewById(R.id.rvMember);
//        rvMember.addItemDecoration(new SpacesItemDecoration(1));
//        rvMember.setAdapter(mAdapter);
//        initData();
//    }
//
//    private void initData() {
//        //先获取用户唯一值identifier之后利用唯一值在Timsdk缓存中拿到用户信息
//        TIMGroupManager.getInstance().getGroupMembers(groupId, new TIMValueCallBack<List<TIMGroupMemberInfo>>() {
//            @Override
//            public void onError(int i, String s) {
//
//            }
//
//            @Override
//            public void onSuccess(List<TIMGroupMemberInfo> timGroupMemberInfos) {
//                Map<String, List<TIMUserProfile>> map = new HashMap<>();
//                List<MemberSectionModel> list = new ArrayList<>();
//                for (int i = 0; i < timGroupMemberInfos.size(); i++) {
//                    TIMUserProfile timUserProfile = TIMFriendshipManager.getInstance().queryUserProfile(timGroupMemberInfos.get(i).getUser());
//                    String nickName;
//                    if (timUserProfile == null) {
//                        nickName = timGroupMemberInfos.get(i).getUser();
//                    } else {
//                        String identifier = timGroupMemberInfos.get(i).getUser();
//                        nickName = TextUtils.isEmpty(timUserProfile.getNickName()) ? identifier : timUserProfile.getNickName();
//                        Log.d("hanlz",timUserProfile.getNickName()+"@@@");
//                    }
//
//                    String firstChar = LanguageConvent.getFirstChar(nickName);
//                    if (map.containsKey(firstChar)) {
//                        List<TIMUserProfile> mapList = map.get(firstChar);
//                        mapList.add(timUserProfile);
//                    } else {
//                        List<TIMUserProfile> mapList = new ArrayList<>();
//                        mapList.add(timUserProfile);
//                        map.put(firstChar, mapList);
//                    }
//                }
//                Iterator<String> it = map.keySet().iterator();
//                while (it.hasNext()) {
//                    String section = it.next();
//                    List<TIMUserProfile> friends = map.get(section);
//                    list.add(new MemberSectionModel(true, section));
//                    for (TIMUserProfile friend : friends) {
//                        MemberModel memberModel = new MemberModel();
//                        list.add(new MemberSectionModel(memberModel));
//                        memberModel.setFaceUrl(friend.getFaceUrl());
//                        memberModel.setNickName(friend.getNickName());
//                        memberModel.setRemark(friend.getNickName());
//                        memberModel.setIdentifier(friend.getIdentifier());
//                    }
//                }
//                mAdapter.setList(list);
//            }
//        });
//
//    }
}
