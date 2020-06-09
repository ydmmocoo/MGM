package com.fjx.mg.nearbycity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.common.paylibrary.model.PayExtModel;
import com.common.paylibrary.model.UsagePayMode;
import com.fjx.mg.R;
import com.fjx.mg.dialog.PickAllTypeDialog;
import com.fjx.mg.moments.add.GridImageAdapter;
import com.fjx.mg.nearbycity.dialog.NearbyCItyDeadlineDialogFragment;
import com.fjx.mg.nearbycity.dialog.NearbyCItyDialogFragment;
import com.fjx.mg.nearbycity.mvp.PublisherNearbyCityContract;
import com.fjx.mg.nearbycity.mvp.PublisherNearbyCityPresenter;
import com.fjx.mg.nearbycity.pay.NearbyCityPayActivity;
import com.fjx.mg.utils.EPSoftKeyBoardListener;
import com.fjx.mg.widget.eidttext.TextEditTextView;
import com.library.common.base.BaseMvpActivity;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.SoftInputUtil;
import com.library.common.utils.StringUtil;
import com.library.common.utils.ViewUtil;
import com.library.repository.models.NearbyCityConfigModel;
import com.library.repository.models.NearbyCityExpListModel;
import com.library.repository.models.NearbyCityTypeListModel;
import com.library.repository.models.ResponseModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Author    by Administrator
 * Date      on 2019/10/16.
 * Description：同城发布页
 */
public class PublisherNearbyCityActivity extends BaseMvpActivity<PublisherNearbyCityPresenter> implements PublisherNearbyCityContract.View, CompoundButton.OnCheckedChangeListener, TextWatcher {

    private NearbyCityConfigModel data;
    private String mCId, mContent, mEId, mTopDays, currentPrice;
    private int mPerPrice;

    @BindView(R.id.cbIsTop)
    CheckBox mCbIsTop;
    @BindView(R.id.etPrice)
    TextEditTextView mEtPrice;
    @BindView(R.id.etDay)
    EditText mEtDay;
    @BindView(R.id.tvSubtractPrice)
    TextView mTvSubtractPrice;
    @BindView(R.id.tvSubtractDays)
    TextView mTvSubtractDays;

    @BindView(R.id.rlPriceRoot)
    RelativeLayout mRlPriceRoot;
    @BindView(R.id.rlDayRoot)
    RelativeLayout mRlDayRoot;
    @BindView(R.id.line)
    LinearLayout line;
    @BindView(R.id.tvTotal)
    TextView mTvTotal;

    @BindView(R.id.tvType)
    TextView mTvType;
    @BindView(R.id.tvDeadline)
    TextView mTvDeadline;
    @BindView(R.id.etTalk)
    EditText mEtTalk;
    @BindView(R.id.rvTakePhoto)
    RecyclerView mRvTakePhoto;
    private GridImageAdapter adapter;
    private List<LocalMedia> selectList = new ArrayList<>();

    @BindView(R.id.llContainer)
    LinearLayout mLlContainer;
    private boolean isShow;

    public static Intent newIntent(Context context, NearbyCityConfigModel data) {
        Intent intent = new Intent(context, PublisherNearbyCityActivity.class);
        intent.putExtra(IntentConstants.CONFIG, data);
        return intent;
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, PublisherNearbyCityActivity.class);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.act_publisher_nearby_city;
    }

    @Override
    protected PublisherNearbyCityPresenter createPresenter() {
        return new PublisherNearbyCityPresenter(this);
    }

    @Override
    protected void initView() {
        super.initView();
        if (getIntent() != null) {
            data = getIntent().getParcelableExtra(IntentConstants.CONFIG);
            List<NearbyCityTypeListModel> typeList = data.getTypeList();
            List<NearbyCityExpListModel> expList = data.getExpList();
            try {
                if (typeList.isEmpty() || expList.isEmpty()) {
                    createAndShowDialog();
                    mPresenter.requestConfig();
                }
            } catch (NullPointerException e) {
                createAndShowDialog();
                mPresenter.requestConfig();
            }

        } else {
            createAndShowDialog();
            mPresenter.requestConfig();
        }

        adapter = new GridImageAdapter(this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(9);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            PictureSelector.create(PublisherNearbyCityActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(PublisherNearbyCityActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(PublisherNearbyCityActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });


        mRvTakePhoto.setLayoutManager(new GridLayoutManager(getCurContext(), 4));
        mRvTakePhoto.setAdapter(adapter);

        mCbIsTop.setOnCheckedChangeListener(this);

        mEtPrice.addTextChangedListener(this);
        mEtDay.addTextChangedListener(this);

        EPSoftKeyBoardListener.setListener(this, new EPSoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                if (isShow) {
                    mLlContainer.setVisibility(View.GONE);
                }
            }

            @Override
            public void keyBoardHide(int height) {
                if (isShow) {
                    mLlContainer.setVisibility(View.VISIBLE);
                }
            }
        });

        mEtDay.setOnTouchListener(new EditText.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isShow = true;
                }
                return false;
            }
        });
        mEtPrice.setOnTouchListener(new EditText.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isShow = true;
                }
                return false;
            }
        });
    }

    /**
     * checkedBox状态监听
     * 设置置顶相关控件状态
     *
     * @param compoundButton
     * @param isChecked      是否选择
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
            mRlPriceRoot.setVisibility(View.VISIBLE);
            mRlDayRoot.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            mTvTotal.setText(StringUtil.add(currentPrice, StringUtil.multiply(mEtPrice.getText().toString(), mEtDay.getText().toString())));
        } else {
            mRlPriceRoot.setVisibility(View.GONE);
            mRlDayRoot.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            mTvTotal.setText(currentPrice);
        }
    }


    @OnClick({R.id.llSelectPublisherType, R.id.rlNearbyCityDeadline, R.id.btnPublisherNearbyCity, R.id.tvAddPrice,
            R.id.tvSubtractPrice, R.id.tvAddDays, R.id.tvSubtractDays, R.id.ivBack, R.id.etPrice, R.id.etDay})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.ivBack://返回
                finish();
                break;
            case R.id.llSelectPublisherType://选择发布类型
                initPublisherTypePop();
                break;
            case R.id.rlNearbyCityDeadline://有效期
                initDeadline();
                break;
            case R.id.btnPublisherNearbyCity://发布
                mPresenter.updateImage(mEtTalk.getText().toString().trim(), selectList);
                break;
            case R.id.tvAddPrice://增加价格
                mEtPrice.setText(StringUtil.add(mEtPrice.getText().toString(), "1"));
                mTvTotal.setText(StringUtil.add(StringUtil.multiply(mEtPrice.getText().toString()
                        , mEtDay.getText().toString()), currentPrice));
                if (!"0".equals(mEtPrice.getText().toString())) {
                    ViewUtil.setDrawableLeft(mTvSubtractPrice, R.drawable.nearby_city_subtract_black);
                }
                break;
            case R.id.tvSubtractPrice://减少价格
                mEtPrice.setText(StringUtil.subtract(mEtPrice.getText().toString(), "1", 0l));
                mTvTotal.setText(StringUtil.add(StringUtil.multiply(mEtPrice.getText().toString(), mEtDay.getText().toString()), currentPrice));
                if ("0".equals(mEtPrice.getText().toString())) {
                    ViewUtil.setDrawableLeft(mTvSubtractPrice, R.drawable.nearby_city_subtract_gray);
                }
                break;
            case R.id.tvAddDays://增加天数
                mEtDay.setText(StringUtil.add(mEtDay.getText().toString(), "1"));
                mTvTotal.setText(StringUtil.add(StringUtil.multiply(mEtPrice.getText().toString(), mEtDay.getText().toString()), currentPrice));
                if (!"0".equals(mEtDay.getText().toString())) {
                    ViewUtil.setDrawableLeft(mTvSubtractDays, R.drawable.nearby_city_subtract_black);
                }
                break;
            case R.id.tvSubtractDays://减少天数
                mEtDay.setText(StringUtil.subtract(mEtDay.getText().toString(), "1", 0l));
                mTvTotal.setText(StringUtil.add(StringUtil.multiply(mEtPrice.getText().toString(), mEtDay.getText().toString()), currentPrice));
                if ("0".equals(mEtDay.getText().toString())) {
                    ViewUtil.setDrawableLeft(mTvSubtractDays, R.drawable.nearby_city_subtract_gray);
                }
                break;
            case R.id.etPrice:
            case R.id.etDay:
                isShow = true;
                break;
            default:
                break;
        }
    }

    /**
     * 选择发布类型pop弹窗
     */
    private void initPublisherTypePop() {
        NearbyCItyDialogFragment fragment = NearbyCItyDialogFragment.getInstance(data);
        fragment.show(getSupportFragmentManager(), "initPublisherTypePop");
        fragment.setOnTypeListener(new NearbyCItyDialogFragment.OnTypeListener() {
            @Override
            public void typeData(NearbyCityTypeListModel model) {
                mCId = model.getcId();
                mTvType.setText(model.getTypeName());
                mEtTalk.setText(model.getDesc());
                if (TextUtils.isEmpty(model.getDesc())){
                    mEtTalk.setSelection(model.getDesc().length());
                }
            }
        });
    }

    private void initDeadline() {
        NearbyCItyDeadlineDialogFragment fragment = NearbyCItyDeadlineDialogFragment.getInstance(data);
        fragment.show(getSupportFragmentManager(), "initDeadline");
        fragment.setOnExpListener(new NearbyCItyDeadlineDialogFragment.OnExpListener() {
            @Override
            public void ExpData(NearbyCityExpListModel model) {
                mEId = model.geteId();
                StringBuilder sb = new StringBuilder();
                sb.append(model.getTime());
                sb.append("(");
                sb.append(model.getPrice());
                sb.append(model.getUnit());
                sb.append(")");
                mTvDeadline.setText(sb.toString());
                currentPrice = model.getPrice();
                if (mCbIsTop.isChecked()) {
                    mTvTotal.setText(StringUtil.add(StringUtil.multiply(mEtPrice.getText().toString(), mEtDay.getText().toString()), currentPrice));
                } else {
                    mTvTotal.setText(model.getPrice());
                }
            }
        });
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            if (EasyPermissions.hasPermissions(PublisherNearbyCityActivity.this, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                PickAllTypeDialog dialog = new PickAllTypeDialog(getCurContext());
                dialog.setselectedList(selectList);
                dialog.setselectedType(true);
                new XPopup.Builder(getCurContext())
                        .asCustom(dialog)
                        .show();
            } else {
                EasyPermissions.requestPermissions(PublisherNearbyCityActivity.this, getString(R.string.permission_camata_sd),
                        1, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            SoftInputUtil.hideSoftInput(getCurActivity());
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 233) {
            if (resultCode == 111) {
                //支付成功
                finish();
            }
        }
        if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片、视频、音频选择结果回调
            // 图片、视频、音频选择结果回调
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);

            if (selectList.size() > 0) {
                adapter.setList(selectList);
                adapter.notifyDataSetChanged();
                this.selectList = selectList;
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mTvTotal.setText(StringUtil.add(currentPrice, StringUtil.multiply(mEtPrice.getText().toString(), mEtDay.getText().toString())));
        if (!"0".equals(mEtDay.getText().toString()) && mEtDay.getText().toString().length() != 0) {
            ViewUtil.setDrawableLeft(mTvSubtractDays, R.drawable.nearby_city_subtract_black);
        } else {
            ViewUtil.setDrawableLeft(mTvSubtractDays, R.drawable.nearby_city_subtract_gray);
        }

        if (!"0".equals(mEtPrice.getText().toString()) && mEtPrice.getText().toString().length() != 0) {
            ViewUtil.setDrawableLeft(mTvSubtractPrice, R.drawable.nearby_city_subtract_black);
        } else {
            ViewUtil.setDrawableLeft(mTvSubtractPrice, R.drawable.nearby_city_subtract_gray);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    @Override
    public void responseFailed(ResponseModel data) {
        hideLoading();
    }

    @Override
    public void responseConfigDatas(NearbyCityConfigModel model) {
        data = model;
        destoryAndDismissDialog();
    }

    @Override
    public void updateImageSuccess(String url) {
        mContent = mEtTalk.getText().toString().trim();
        if (StringUtil.isEmpty(mCId)) {
            CommonToast.toast(R.string.please_select_publish_type);
            return;
        }
        if (StringUtil.isEmpty(mContent)) {
            CommonToast.toast(R.string.please_input_content);
            return;
        }
        if (StringUtil.isEmpty(mEId)) {
            CommonToast.toast(R.string.please_select_publisher_time);
            return;
        }
        if (mCbIsTop.isChecked()) {
            mTopDays = mEtDay.getText().toString();
            mPerPrice = Integer.valueOf(mEtPrice.getText().toString());
        }
        if (StringUtil.equals("0", mTvTotal.getText().toString())) {
            CommonToast.toast(R.string.less_0ar);
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put(IntentConstants.NERBY_CITY_CID, mCId);
        map.put(IntentConstants.NERBY_CITY_CONTENT, mContent);
        map.put(IntentConstants.NERBY_CITY_IMAGES, url);
        map.put(IntentConstants.NERBY_CITY_EID, mEId);
        map.put(IntentConstants.NERBY_CITY_TOPDAYS, mTopDays);
        map.put(IntentConstants.NERBY_CITY_PERPRICE, mPerPrice + "");
        map.put(IntentConstants.NERBY_CITY_TOLPRICE, mTvTotal.getText().toString());
        String ext = JsonUtil.moderToString(new PayExtModel(UsagePayMode.nearby_city_pay, map));
        startActivityForResult(NearbyCityPayActivity.newInstance(getCurContext(), ext), 233);
    }

    /**
     * 点击软键盘外面的区域关闭软键盘
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                isShow = false;
                mLlContainer.setVisibility(View.VISIBLE);
                SoftInputUtil.hideSoftInput(getCurActivity(), v);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }
}
