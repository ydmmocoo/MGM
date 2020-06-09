package com.fjx.mg.main.payment.search;

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
import com.fjx.mg.main.payment.QuestionListAdapter;
import com.fjx.mg.main.payment.detail.AskDetailActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StatusBarManager;
import com.library.repository.models.QuestionListModel;

import butterknife.BindView;
import butterknife.OnClick;

public class PaymentSearchActivity extends BaseMvpActivity<PayMentSearchPresenter> implements PayMentSearchContract.View {

    @BindView(R.id.etSearch)
    EditText etSearch;


    @BindView(R.id.recycler)
    RecyclerView recycler;
    private QuestionListAdapter mAdapter;

    public static Intent newInstance(Context context) {
        return new Intent(context, PaymentSearchActivity.class);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_payment_search;
    }

    @Override
    protected PayMentSearchPresenter createPresenter() {
        return new PayMentSearchPresenter(this);
    }

    @Override
    protected void initView() {

        SetToolBar();


        mAdapter = new QuestionListAdapter();
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                startActivity(AskDetailActivity.newInstance(getCurContext(), mAdapter.getItem(position).getQId()));
            }
        });
        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.addItemDecoration(new SpacesItemDecoration(1));
        recycler.setAdapter(mAdapter);
    }


    @Override
    public void showQuestionListModel(QuestionListModel data) {
        mAdapter.setList(data.getQuestionList());

    }

    private void SetToolBar() {
        StatusBarManager.setLightFontColor(this, R.color.colorAccent);
    }

    @Override
    public void loadError() {
        CommonToast.toast(R.string.no_search_result);

    }


    @OnClick({R.id.ivBack, R.id.ivClear, R.id.tvSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.ivClear:
                etSearch.setText("");
                break;
            case R.id.tvSearch:
                mPresenter.getQuestionList(etSearch.getText().toString(), "", "", "1", 1);
                break;
        }
    }
}
