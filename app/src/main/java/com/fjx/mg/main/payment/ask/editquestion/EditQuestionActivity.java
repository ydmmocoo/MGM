package com.fjx.mg.main.payment.ask.editquestion;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.dialog.PickImageDialog;
import com.fjx.mg.main.payment.ask.AskAdapter;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.SoftInputUtil;
import com.library.common.utils.StatusBarManager;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EditQuestionActivity extends BaseMvpActivity<EditPresenter> implements EditContract.View {
    @BindView(R.id.tv_title)
    EditText tv_title;

    @BindView(R.id.tv_title_ems_num)
    TextView tv_title_ems_num;

    @BindView(R.id.tv_question)
    EditText tv_question;

    @BindView(R.id.recycler)
    RecyclerView recycler;


    @BindView(R.id.tv_send)
    TextView tv_send;

    private AskAdapter imageAdapter;
    private List<String> imageUrls = new ArrayList<>();

    @Override
    protected EditPresenter createPresenter() {
        return new EditPresenter(this);
    }

    public static Intent newInstance(Context context, String qId, String question, String desc, String images) {
        Intent intent = new Intent(context, EditQuestionActivity.class);
        intent.putExtra("qId", qId);
        intent.putExtra("question", question);
        intent.putExtra("desc", desc);
        intent.putExtra("images", images);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_edit_question;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle("补充问题");
        StatusBarManager.setLightFontColor(this, R.color.colorAccent);
        mPresenter.searchWatcher(tv_title);

//        tv_title.setText(getIntent().getStringExtra("question"));
//        tv_question.setText(getIntent().getStringExtra("desc"));

        imageUrls.add("");
        imageAdapter = new AskAdapter();
        recycler.setLayoutManager(new GridLayoutManager(getCurContext(), 3));
        recycler.setAdapter(imageAdapter);
        imageAdapter.setList(imageUrls);

        imageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                String path = imageAdapter.getItem(position);
                if (TextUtils.isEmpty(path)) {
                    PickImageDialog dialog = new PickImageDialog(getCurContext());
                    dialog.setSelectSingle(false);
                    new XPopup.Builder(getCurContext())
                            .asCustom(dialog)
                            .show();
                    SoftInputUtil.hideSoftInput(getCurActivity());
                }
            }
        });
    }

    @OnClick(R.id.tv_send)
    public void onViewClicked() {
        SoftInputUtil.hideSoftInput(getCurActivity());

        mPresenter.updateImage(tv_title.getText().toString(), tv_question.getText().toString(), imageUrls, getIntent().getStringExtra("qId"));
        //输入框都正常，跳转获取支付订单号，获取完订单号先提交问题，提交问题成功再去支付。避免支付赏金后提交问题流程被打断。
    }

    @Override
    public void ShowNum(String num) {
        tv_title_ems_num.setText(num + "/49");
    }

    @Override
    public void voidEditSuccess() {
        setResult(18);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PictureConfig.CHOOSE_REQUEST:
                // 图片、视频、音频选择结果回调
                imageUrls.clear();
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                for (LocalMedia localMedia : selectList) {
                    imageUrls.add(localMedia.getCompressPath());
                }

                if (imageUrls.size() < 9) imageUrls.add(0, "");
                for (int i = 0; i < imageUrls.size(); i++) {
                    if (i != 0&&imageUrls.get(i).isEmpty()) {
                        imageUrls.remove(i);
                    }
                }
                imageAdapter.setList(imageUrls);
                Log.d("onActivityResult", JsonUtil.moderToString(selectList));
                break;
        }
    }
}
