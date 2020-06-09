package com.fjx.mg.friend.phone_contact;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.friend.addfriend.AddFriendActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.IntentUtil;
import com.library.common.utils.JsonUtil;
import com.library.repository.models.PhoneContactModel;
import com.library.repository.models.PhoneContactSection;
import com.tencent.imsdk.TIMUserProfile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PhoneContactsActivity extends BaseMvpActivity<PhoneContactsPresenter> implements PhoneContactsContract.View {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    private PhoneContactsAdapter contactsAdapter;
    private String inviteContent;
    @BindView(R.id.etSearch)
    EditText etSearch;

    @Override
    protected PhoneContactsPresenter createPresenter() {
        return new PhoneContactsPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_friend_contacts;
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, PhoneContactsActivity.class);
        return intent;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.phone_contact));
        GradientDrawableHelper.whit(etSearch).setColor(R.color.white);
        mPresenter.searchWatcher(etSearch);
        mPresenter.getPhoneContactList();
        contactsAdapter = new PhoneContactsAdapter(new ArrayList<PhoneContactSection>());
        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.addItemDecoration(new SpacesItemDecoration(1));
        recycler.setAdapter(contactsAdapter);
        contactsAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                PhoneContactSection model = contactsAdapter.getItem(position);
                if (model.isHeader()) return;

                //已添加好友
                if (model.getIsExits() == 2) return;
                if (model.getIsExits() == 1) {
                    //平台存在用户
                    mPresenter.findImUser(model.getMobile());
                } else {
                    //用户未注册平台
                    IntentUtil.sendSmsMessage(model.getMobile(), inviteContent);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void showContactList(List<PhoneContactModel> contactModels) {
        List<PhoneContactSection> sections = mPresenter.getContactList(contactModels);
        contactsAdapter.setList(sections);
    }


    @Override
    public void getImUserSuccess(TIMUserProfile profile) {
        startActivity(AddFriendActivity.newInstance(getCurContext(), JsonUtil.moderToString(profile)));
    }

    @Override
    public void inviteContent(String text) {
        inviteContent = text;
    }
}
