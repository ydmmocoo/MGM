package com.fjx.mg.friend.blackfriend;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.friend.addfriend.AddFriendActivity;
import com.fjx.mg.friend.contacts.FriendContactsAdapter;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.repository.models.FriendContactSectionModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class BlackFriendActivity extends BaseMvpActivity<BlackFriendContactsPresenter> implements BlackFriendContactsContract.View {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    private FriendContactsAdapter contactsAdapter;
    private boolean isSelect;
    private MaterialDialog deleteDialog;
    @BindView(R.id.etSearch)
    EditText etSearch;

    @Override
    protected BlackFriendContactsPresenter createPresenter() {
        return new BlackFriendContactsPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_friend_contacts;
    }

    public static Intent newInstance(Context context, boolean isSelect) {
        Intent intent = new Intent(context, BlackFriendActivity.class);
        intent.putExtra("isSelect", isSelect);
        return intent;
    }

    @Override
    protected void initView() {
//        test();
        isSelect = getIntent().getBooleanExtra("isSelect", false);
        GradientDrawableHelper.whit(etSearch).setColor(R.color.white);
        mPresenter.searchWatcher(etSearch);
        ToolBarManager.with(this).setTitle(getString(R.string.blacklist));
        contactsAdapter = new FriendContactsAdapter(new ArrayList<FriendContactSectionModel>());
        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.addItemDecoration(new SpacesItemDecoration(1));
        recycler.setAdapter(contactsAdapter);
        contactsAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                final FriendContactSectionModel model = contactsAdapter.getItem(position);
                if (model.isHeader()) return;
                if (isSelect) {
                    String phone = "";
                    byte[] bytes = model.getData().getTimUserProfile().getCustomInfo().get("phone");
                    if (bytes != null)
                        phone = new String(bytes);
                    Intent intent = new Intent();
                    intent.putExtra("mobile", phone);
                    setResult(1, intent);
                    finish();
                } else {
//                    mPresenter.OutBlackFriend( model.t.getIdentifier());
                    startActivity(AddFriendActivity.newInstance(getCurContext(), model.getData().getIdentifier(), true));
                }
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getAllFriend();
    }


    @Override
    public void showContactList(List<FriendContactSectionModel> datas) {
        contactsAdapter.setList(datas);
    }

    @Override
    public void outblackFriendSuccess() {
        CommonToast.toast(R.string.remove_blacklist);
        mPresenter.getAllFriend();
    }


}
