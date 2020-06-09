package com.fjx.mg.setting.feedback;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.dialog.FeeBackDialog;
import com.fjx.mg.dialog.PickImageDialog;
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
import butterknife.OnClick;

public class FeedBackActivity extends BaseMvpActivity<FeedBackPresenter> implements FeedBackContract.View {

    @BindView(R.id.etContent)
    EditText etContent;

    @BindView(R.id.btn_feedback)
    LinearLayout btn_feedback;

    @BindView(R.id.tv_feedbacktype)
    TextView tv_feedbacktype;

    @BindView(R.id.tv_feedback)
    TextView tv_feedback;


    @BindView(R.id.recycler)
    RecyclerView recycler;
    private FeedbackImageAdapter imageAdapter;
    private List<String> imageUrls = new ArrayList<>();
    private FeeBackDialog scanQrDialogx;
    private int type = -1;
    private String identifier;

    @Override
    protected int layoutId() {
        return R.layout.ac_feedback;
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, FeedBackActivity.class);
    }

    public static Intent newInstance(Context context, String identifier, int type) {
        Intent intent = new Intent(context, FeedBackActivity.class);
        intent.putExtra("identifier", identifier);
        intent.putExtra("type", type);
        return intent;
    }

    @OnClick(R.id.btn_feedback)
    public void onViewClicked() {
        scanQrDialogx = new FeeBackDialog(this, new FeeBackDialog.MenuDeleteDialogClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tvQrScan:
                        tv_feedback.setText(getString(R.string.complaint_reporting));
                        etContent.setHint(getString(R.string.complaint_tips));
                        type = 2;
                        scanQrDialogx.dismiss();
                        break;
                    case R.id.tvQrCode:
                        tv_feedback.setText(getString(R.string.suggestion_feedback));
                        type = -1;
                        etContent.setHint(getString(R.string.complaint_tips));
                        scanQrDialogx.dismiss();
                        break;
                }
            }
        });
        new XPopup.Builder(getCurContext()).atView(btn_feedback).asCustom(scanQrDialogx).show();
    }

    @Override
    protected void initView() {
        identifier = getIntent().getStringExtra("identifier");
        type = getIntent().getIntExtra("type", -1);
        btn_feedback.setVisibility(identifier == null ? View.VISIBLE : View.GONE);
        etContent.setHint(identifier == null ? getString(R.string.input_feedback_opinion) : getString(R.string.input_complaint_opinion));
        Log.e("identifier:", "" + identifier);
        TextView tvRightText = ToolBarManager.with(this).setTitle(identifier == null ? getString(R.string.feedback) : getString(R.string.friend_comlaint))
                .setRightText(getString(R.string.commit),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SoftInputUtil.hideSoftInput(getCurActivity());

                                String content = etContent.getText().toString();
                                mPresenter.updateImage(content, imageUrls, FeedBackActivity.this.type, identifier == null ? "" : identifier);
                            }
                        });
        tvRightText.setTextColor(ContextCompat.getColor(getCurContext(), R.color.textColorAccent));
        imageUrls.add("");
        imageAdapter = new FeedbackImageAdapter();
        recycler.setLayoutManager(new GridLayoutManager(getCurContext(), 3));
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
    protected FeedBackPresenter createPresenter() {
        return new FeedBackPresenter(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PictureConfig.CHOOSE_REQUEST:
                // 图片、视频、音频选择结果回调
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
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
    public void uploadImageSucces(List<String> remoteUrl) {


    }

    @Override
    public void feedbackSuccess() {
        finish();
    }
}
