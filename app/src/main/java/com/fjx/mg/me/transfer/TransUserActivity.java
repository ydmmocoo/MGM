package com.fjx.mg.me.transfer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.friend.contacts.FriendContactsActivity;
import com.fjx.mg.me.record.BillRecord2Activity;
import com.fjx.mg.me.transfer.adapter.RecentlyTransferAdapter;
import com.fjx.mg.recharge.center.PhoneListAdapter;
import com.fjx.mg.utils.FileCache;
import com.fjx.mg.widget.recyclerview.LinearManagerItemDecaration;
import com.library.common.base.BaseActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.CommonToast;
import com.library.common.utils.ContextManager;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.SoftInputUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.db.base.DBHelper;
import com.library.repository.db.model.DaoSession;
import com.library.repository.models.OtherUserModel;
import com.library.repository.models.PhoneHistoryModel;
import com.library.repository.models.RechargePhoneModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.mob.tools.utils.Strings.getString;

public class TransUserActivity extends BaseActivity {
    @BindView(R.id.toolbar_tv_title)
    TextView mTvTitle;
    @BindView(R.id.etMobile)
    EditText etMobile;
    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.tvNext)
    TextView tvNext;
    @BindView(R.id.flPhoneList)
    FrameLayout flPhoneList;
    @BindView(R.id.phoneRecycler)
    RecyclerView phoneRecycler;
    private PhoneListAdapter phoneListAdapter;
    private List<RechargePhoneModel> rechargePhoneModels;
    @BindView(R.id.llRecently)
    LinearLayout mLlRecently;
    @BindView(R.id.rvRecently)
    RecyclerView mRvRecently;
    private RecentlyTransferAdapter mAdapter;

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, TransUserActivity.class);
        return intent;
    }


    @Override
    protected int layoutId() {
        return R.layout.ac_transfer_user;
    }

    @Override
    protected void initView() {
        mTvTitle.setText(getString(R.string.transfer));
        GradientDrawableHelper.whit(tvNext).setColor(R.color.colorAccent).setCornerRadius(50);

        phoneRecycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        phoneRecycler.addItemDecoration(new SpacesItemDecoration(1));
        phoneListAdapter = new PhoneListAdapter();
        phoneRecycler.setAdapter(phoneListAdapter);

        phoneListAdapter.addFooterView(getView());

        etMobile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (phoneListAdapter.getData().isEmpty()) return false;
                    flPhoneList.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        DaoSession daoSession = DBHelper.getInstance().getDaoSession();
        List<PhoneHistoryModel> phoneHistoryModels = daoSession.loadAll(PhoneHistoryModel.class);
        List<PhoneHistoryModel> newPhoneHistoryModels = new ArrayList<>();

        Iterator it = phoneHistoryModels.iterator();
        while (it.hasNext()) {
            PhoneHistoryModel s = (PhoneHistoryModel) it.next();
            if (!newPhoneHistoryModels.contains(s)) {
                newPhoneHistoryModels.add(s);
            }
        }
        Collections.reverse(newPhoneHistoryModels);
        rechargePhoneModels = new ArrayList<>();
        if (newPhoneHistoryModels != null || newPhoneHistoryModels.size() > 0) {
            mLlRecently.setVisibility(View.VISIBLE);
            for (int i = 0; i < newPhoneHistoryModels.size(); i++) {
                RechargePhoneModel model = new RechargePhoneModel();
                model.setPhone(newPhoneHistoryModels.get(i).getPhone());
                rechargePhoneModels.add(model);
            }
            if (rechargePhoneModels.size() > 0) {
                phoneListAdapter.setList(rechargePhoneModels);
            }
        } else {
            mLlRecently.setVisibility(View.GONE);
            return;
        }

        final List<RechargePhoneModel> searchPhoneList = new ArrayList<>();
        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchPhoneList.clear();
                if (0 == s.length()) {
                    phoneListAdapter.setList(rechargePhoneModels);
                    flPhoneList.setVisibility(View.VISIBLE);
                } else {
                    for (int i = 0; i < rechargePhoneModels.size(); i++) {
                        if (rechargePhoneModels.get(i).getPhone().contains(s)) {
                            searchPhoneList.add(rechargePhoneModels.get(i));
                        }
                    }
                    phoneListAdapter.setList(searchPhoneList);
                    if (searchPhoneList.size() <= 0) {
                        flPhoneList.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < searchPhoneList.size(); i++) {
                        if (TextUtils.equals(s, searchPhoneList.get(i).getPhone())) {
                            flPhoneList.setVisibility(View.GONE);
                        } else {
                            flPhoneList.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mAdapter = new RecentlyTransferAdapter();
        mRvRecently.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                PhoneHistoryModel item = (PhoneHistoryModel) adapter.getItem(position);
                etMobile.setText(item.getPhone());
                etMobile.setSelection(item.getPhone().length());
                getUserInfo();
                flPhoneList.setVisibility(View.GONE);
            }
        });
        mAdapter.setList(newPhoneHistoryModels);
        mRvRecently.addItemDecoration(new LinearManagerItemDecaration(1, LinearManagerItemDecaration.VERTICAL_LIST));

        phoneListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if (!TextUtils.isEmpty(rechargePhoneModels.get(position).getPhone())) {
                    etMobile.setText(rechargePhoneModels.get(position).getPhone());
                    etMobile.setSelection(rechargePhoneModels.get(position).getPhone().length());
                }
                flPhoneList.setVisibility(View.GONE);
                SoftInputUtil.hideSoftInput(getCurActivity());
            }
        });
    }

    private View getView() {
        View view = LayoutInflater.from(getCurContext()).inflate(R.layout.layout_center_text, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper.getInstance().getDaoSession().deleteAll(PhoneHistoryModel.class);
                flPhoneList.setVisibility(View.GONE);
                phoneListAdapter.setList(null);

            }
        });
        return view;
    }

    @OnClick(R.id.toolbar_iv_back)
    public void finishAct() {
        finish();
    }

    @OnClick(R.id.toolbar_iv_right)
    public void billRecord() {
        startActivity(BillRecord2Activity.newInstance(getCurContext(), "7"));
    }

    @OnClick(R.id.ivImage)
    public void cickImage() {

        startActivityForResult(FriendContactsActivity.newInstance(getCurContext(), true, true), 1);

    }


    @OnClick(R.id.tvNext)
    public void clickNext() {
        getUserInfo();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(null);
        super.onDestroy();
    }

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 5, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10));
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final String data = (String) msg.obj;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    FileCache.getInstance().saveText("MGM转账信息", data, ContextManager.getContext());
                }
            });
        }
    };

    private void getUserInfo() {
        final String phone = etMobile.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            CommonToast.toast(R.string.please_input_other_phono_number);
            return;
        }
        createAndShowDialog();
        String p = "";
        String id = "";
        if (phone.contains("mgm") || phone.contains("fjx")) {
            id = phone;
        } else {
            p = phone;
        }
        RepositoryFactory.getRemoteAccountRepository().getUserInfo(p, id, "")
                .compose(RxScheduler.<ResponseModel<OtherUserModel>>toMain())
                .as(this.<ResponseModel<OtherUserModel>>bindAutoDispose())
                .subscribe(new CommonObserver<OtherUserModel>() {
                    @Override
                    public void onSuccess(OtherUserModel data) {
                        //存储数据到数据库 便于后面模糊搜索和其他历史记录展示
                        final DaoSession daoSession = DBHelper.getInstance().getDaoSession();
                        PhoneHistoryModel phoneHistoryModel = new PhoneHistoryModel();
                        phoneHistoryModel.setType("1");
                        phoneHistoryModel.setPhone(phone);
                        phoneHistoryModel.setFaceIcon(data.getAvatar());
                        phoneHistoryModel.setImUserId(data.getIdentifier());
                        phoneHistoryModel.setNickname(TextUtils.isEmpty(data.getUserNick()) ? data.getUserName() : data.getUserNick());
                        daoSession.insertOrReplace(phoneHistoryModel);

                        destoryAndDismissDialog();
                        data.setPhone(phone);
                        startActivityForResult(MeTransferActivityx.newInstance(getCurContext(), JsonUtil.moderToString(data)), 1);
                    }

                    @Override
                    public void onUserFailed(ResponseModel data) {
                        super.onUserFailed(data);
                        destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                        Message msg = Message.obtain();
                        msg.obj = JsonUtil.moderToString(data);
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        hideLoading();
                        CommonToast.toast(data.getMsg());
                        Message msg = Message.obtain();
                        msg.obj = JsonUtil.moderToString(data);
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode && data != null) {
            String phone = data.getStringExtra("mobile");
            etMobile.setText(phone);
            etMobile.setSelection(etMobile.getText().length());
        } else if (resultCode == 111) {
            setResult(resultCode);
            finish();
        } else if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                String phone = data.getStringExtra("mobile");
                etMobile.setText(phone);
                etMobile.setSelection(etMobile.getText().length());
            }
        }
    }
}
