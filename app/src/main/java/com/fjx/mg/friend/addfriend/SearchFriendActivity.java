package com.fjx.mg.friend.addfriend;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.common.sharesdk.PlatformType;
import com.common.sharesdk.repostory.ShareCallback;
import com.fjx.mg.R;
import com.fjx.mg.friend.imuser.NewImUserDetailActivity;
import com.fjx.mg.friend.nearby.NearbyActivity;
import com.fjx.mg.friend.phone_contact.PhoneContactsActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.DimensionUtil;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.SoftInputUtil;
import com.library.repository.Constant;
import com.library.repository.models.FriendContactSectionModel;
import com.library.repository.models.ImUserRelaM;
import com.library.repository.models.InviteModel;
import com.library.repository.models.MomentsUserInfoModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class SearchFriendActivity extends BaseMvpActivity<AddFriendPresenter> implements AddFriendContract.View {
    private final int READ_CONTACTS_REQUESTCODE = 1;
    private final int LOCATION_REQUESTCODE = 2;

    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private AddFeiendAdapter friendAdapter;
    private String inviteCode;
    private String shareTitle;
    private String shareDesc;
    private boolean isStranger;

    @Override
    protected AddFriendPresenter createPresenter() {
        return new AddFriendPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_searchfriend;
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, SearchFriendActivity.class);
        return intent;
    }

    public static Intent newInstance(Context context,boolean isStranger) {
        Intent intent = new Intent(context, SearchFriendActivity.class);
        intent.putExtra("isStranger", isStranger);
        return intent;
    }

    @Override
    protected void initView() {
        isStranger=getIntent().getBooleanExtra("isStranger",false);
        if (isStranger){
            etContent.setHint(getResources().getString(R.string.friends_search_stranger_tips));
        }
        mPresenter.getInviteCode();
        friendAdapter = new AddFeiendAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.addItemDecoration(new SpacesItemDecoration(1));
        friendAdapter.addFooterView(getContactFootView());
        friendAdapter.addFooterView(getNearbyFootView());
        friendAdapter.addFooterView(getWechatFootView());
        recycler.setAdapter(friendAdapter);

        friendAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                ImUserRelaM userRelaM = friendAdapter.getItem(position);
                if (userRelaM.isFriend()) {
                    startActivity(NewImUserDetailActivity.newInstance(getCurContext(), userRelaM.getUserProfile().getIdentifier()));
                } else {
                    startActivity(AddFriendActivity.newInstance(getCurContext(), JsonUtil.moderToString(userRelaM.getUserProfile())));
                    final TextView tvAddFriend = view.findViewById(R.id.tvAddFriend);
                    new Handler().postDelayed(() -> {
                        tvAddFriend.setText(getString(R.string.sent));
                        tvAddFriend.setEnabled(false);
                    }, 1000);
                }
            }
        });


        etContent.setOnEditorActionListener((arg0, arg1, arg2) -> {
            if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                findUser();
            }
            return true;
        });
        new Handler().postDelayed(() -> SoftInputUtil.showSoftInputView(getCurActivity(), etContent), 100);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO 不需要每次刷新搜索列表数据 如果是后续发现和之前代码逻辑冲突再做修改
//        findUser();
    }

    private void findUser() {
        String imUid = etContent.getText().toString();
        if (TextUtils.isEmpty(imUid)) return;
        mPresenter.getUser(imUid);
    }

    private View getContactFootView() {
        View view = LayoutInflater.from(getCurContext()).inflate(R.layout.item_add_friend_footer, null);
        ConstraintLayout contentLayout = view.findViewById(R.id.clContent);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) contentLayout.getLayoutParams();
        params.topMargin = DimensionUtil.dip2px(10);
        contentLayout.setLayoutParams(params);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCintactPermission();
            }
        });
        return view;
    }

    private View getNearbyFootView() {
        View view = LayoutInflater.from(getCurContext()).inflate(R.layout.item_add_friend_footer, null);
        ImageView image_view = view.findViewById(R.id.image_view);
        TextView textView = view.findViewById(R.id.textView);
        TextView textView1 = view.findViewById(R.id.textView1);
        image_view.setImageResource(R.drawable.ic_nearby);
        textView.setText(getString(R.string.nearby_user));
        textView1.setText(getString(R.string.add_nearby_user));
        view.setOnClickListener(v -> requestLocationPermission());
        return view;
    }


    private View getWechatFootView() {
        View view = LayoutInflater.from(getCurContext()).inflate(R.layout.item_add_friend_footer, null);
        ImageView image_view = view.findViewById(R.id.image_view);
        TextView textView = view.findViewById(R.id.textView);
        TextView textView1 = view.findViewById(R.id.textView1);
        image_view.setImageResource(R.drawable.add_wechat);
        textView.setText(getString(R.string.add_wechat_friend));
        textView1.setText(getString(R.string.hint_add_wechat_friend));
        view.setOnClickListener(v -> showShareDialog());
        return view;
    }

    @OnClick({R.id.ivBack, R.id.tvSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvSearch:
                findUser();
                break;
            case R.id.ivBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void showUserInfo(ImUserRelaM userRelaM) {
        friendAdapter.getData().clear();
        friendAdapter.addData(userRelaM);
    }

    @Override
    public void showRemark(String remark) {
    }

    @Override
    public void outblackFriendSuccess() {
    }

    @Override
    public void showInviteModel(InviteModel model) {
        this.inviteCode = model.getInviteCode();
        shareTitle = model.getInviteTitle();
        shareDesc = model.getInviteDesc();
    }

    @Override
    public void showContactLists(Boolean noblack) {
    }

    @Override
    public void showContactList(List<FriendContactSectionModel> datas) {
    }

    @Override
    public void showMommetnsUserInfo(MomentsUserInfoModel model) {
    }

    @Override
    public void friendRelation(boolean isRelation) {
    }

    @Override
    public void chatact(String timUserId) {
    }

    private void requestCintactPermission() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_CONTACTS)) {
            startActivity(PhoneContactsActivity.newInstance(getCurContext()));
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_contacts_message),
                    READ_CONTACTS_REQUESTCODE, Manifest.permission.READ_CONTACTS);
        }
    }

    private void requestLocationPermission() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            startActivity(NearbyActivity.newInstance(getCurContext()));
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_location_message),
                    LOCATION_REQUESTCODE, Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == READ_CONTACTS_REQUESTCODE) {
            startActivity(PhoneContactsActivity.newInstance(getCurContext()));
        } else if (requestCode == LOCATION_REQUESTCODE) {
            startActivity(NearbyActivity.newInstance(getCurContext()));
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //弹窗拒绝是调用
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms))
            new AppSettingsDialog.Builder(this).build().show();
    }

    private void showShareDialog() {
        String imageUrl = "";
        String webview = Constant.INVITE_URL.concat(inviteCode).concat("&l=").concat(RepositoryFactory.getLocalRepository().getLangugeType());;
        RepositoryFactory.getShareApi().wechareShareWeb(PlatformType.WECHAT, shareTitle, shareDesc, imageUrl, webview,
                new ShareCallback() {
                    @Override
                    public void onSucces() {
                    }
                });
    }
}
