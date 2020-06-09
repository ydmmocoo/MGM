package com.fjx.mg.main.payment.answer;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.dialog.PickImageDialog;
import com.fjx.mg.main.payment.ask.AskAdapter;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.SoftInputUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AnswerActivity extends BaseMvpActivity<AnswerPresenter> implements AnswerContract.View {
    @BindView(R.id.etContent)
    EditText etContent;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    private AskAdapter imageAdapter;
    private List<String> imageUrls = new ArrayList<>();
    List<LocalMedia> selectList = new ArrayList<>();

    public static Intent newInstance(Context context, String qId) {
        Intent intent = new Intent(context, AnswerActivity.class);
        intent.putExtra("qId", qId);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_answer;
    }

    @Override
    protected AnswerPresenter createPresenter() {
        return new AnswerPresenter(this);
    }


    @Override
    protected void initView() {
        TextView tvRightText = ToolBarManager.with(this).setTitle("我要回答")
                .setRightText(getString(R.string.commit),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SoftInputUtil.hideSoftInput(getCurActivity());

                                String content = etContent.getText().toString();
                                mPresenter.updateImage(getIntent().getStringExtra("qId"), content, imageUrls);
                            }
                        });
        tvRightText.setTextColor(ContextCompat.getColor(getCurContext(), R.color.textColorAccent));
        imageUrls.add("");
        imageAdapter = new AskAdapter();
        recycler.setLayoutManager(new GridLayoutManager(getCurContext(), 4));
        recycler.addItemDecoration(new SpacesItemDecoration(10, 10));
        recycler.setAdapter(imageAdapter);
        imageAdapter.setList(imageUrls);

        imageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                String path = imageAdapter.getItem(position);
                if (TextUtils.isEmpty(path)) {
                    PickImageDialog dialog = new PickImageDialog(getCurContext());
                    dialog.setSelectSingle(false);
                    dialog.setselectedList(selectList);
                    new XPopup.Builder(getCurContext())
                            .asCustom(dialog)
                            .show();
                    SoftInputUtil.hideSoftInput(getCurActivity());
                }
            }
        });
        imageAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, final int position) {
                new XPopup.Builder(getCurContext()).asConfirm(getCurContext().getString(R.string.Tips),
                        getCurContext().getString(R.string.delete_confirm_message), new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                imageAdapter.remove(position);
                            }
                        }).show();
                return true;
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PictureConfig.CHOOSE_REQUEST:
                // 图片、视频、音频选择结果回调
                selectList = PictureSelector.obtainMultipleResult(data);
                imageUrls.clear();
                for (LocalMedia localMedia : selectList) {
                    imageUrls.add(localMedia.getCompressPath());
                }

                if (imageUrls.size() < 9) imageUrls.add(0, "");
                for (int i = 0; i < imageUrls.size(); i++) {
                    if (i != 0 && imageUrls.get(i).isEmpty()) {
                        imageUrls.remove(i);
                    }
                }
                imageAdapter.setList(imageUrls);
                Log.d("onActivityResult", JsonUtil.moderToString(selectList));
                break;
        }
    }


    @Override
    public void feedbackSuccess() {
        setResult(18);
        finish();
    }
}
