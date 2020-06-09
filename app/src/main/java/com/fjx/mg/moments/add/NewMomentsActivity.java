package com.fjx.mg.moments.add;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.dialog.PickAllTypeDialog;
import com.fjx.mg.moments.add.pois.AoisActivity;
import com.fjx.mg.utils.FileCache;
import com.library.common.base.BaseMvpActivity;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.SoftInputUtil;
import com.library.common.utils.StatusBarManager;
import com.library.common.utils.StringUtil;
import com.library.common.view.popupwindow.CommonPopupWindow;
import com.library.common.view.popupwindow.Utils;
import com.library.repository.models.TypeListModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.tencent.qcloud.uikit.business.chat.model.ElemExtModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 发布朋友圈同城编辑页面
 */
public class NewMomentsActivity extends BaseMvpActivity<NewMomentsPresenter> implements NewMomentsContract.View, CommonPopupWindow.ViewInterface {

    @BindView(R.id.recycler)
    RecyclerView recycler;

    @BindView(R.id.recyclertype)
    RecyclerView recyclertype;

    @BindView(R.id.tvShowType)
    TextView tvShowType;

    @BindView(R.id.tvShowAdr)
    TextView tvShowAdr;

    @BindView(R.id.tv_talk)
    EditText tv_talk;

    private CommonPopupWindow popWindow;
    private AddMomentsTypeAdapter typeAdapter;
    private List<LocalMedia> selectList = new ArrayList<>();

    private String type = "1";//1:图文,2:视频
    private String tIds = "";//分类，多个用“，”分割
    private String showType = "1";//	显示范围,1:公开,2:好友，3:私密'
    private String address = "";//定位的地址
    private String lng = "";//经度
    private String lat = "";//纬度
    private GridImageAdapter adapter;
    private BasePopupView popupView;
    /*----分享功能新增参数 TODO 2019年11月7日17:42:22 --*/
    private String cId;
    private String newsId;
    private String messageTyp;
    private String content;
    private String shareTitle;
    private ArrayList<String> imgs;
    private ArrayList<String> images;
    private String img;
    private String newsTitle;
    private String typeName;

    /*----分享功能新增控件 TODO 2019年11月7日17:42:22 --*/
    @BindView(R.id.llShareContainer)
    LinearLayout mLlShareContainer;
    @BindView(R.id.tvContent)
    TextView mTvContent;
    @BindView(R.id.ivPic)
    ImageView mIvPic;

    public static Intent newInstance(Context context) {
        return new Intent(context, NewMomentsActivity.class);
    }

    /**
     * 黄页
     *
     * @param context
     * @param cId
     * @param messageTyp
     * @param shareTitle
     * @param imgs
     * @return
     */
    public static Intent newIntent(Context context, String cId, String messageTyp, String shareTitle, ArrayList<String> imgs) {
        Intent intent = new Intent(context, NewMomentsActivity.class);
        intent.putExtra(IntentConstants.CID, cId);
        intent.putExtra(IntentConstants.MESSAGE_TYPE, messageTyp);
        intent.putExtra(IntentConstants.TITLE, shareTitle);
        intent.putStringArrayListExtra(IntentConstants.IMGS, imgs);
        return intent;
    }

    /**
     * @param context
     * @param newsId
     * @param messageTyp
     * @param img
     * @param newsTitle
     * @return
     */
    public static Intent newIntent(Context context, String newsId, String messageTyp, String newsTitle, String img) {
        Intent intent = new Intent(context, NewMomentsActivity.class);
        intent.putExtra(IntentConstants.NEWSID, newsId);
        intent.putExtra(IntentConstants.MESSAGE_TYPE, messageTyp);
        intent.putExtra(IntentConstants.NEWS_TITLE, newsTitle);
        intent.putExtra(IntentConstants.IMG, img);
        return intent;
    }

    /**
     * 同城分享发布到朋友圈
     *
     * @param context
     * @param cId
     * @param messageTyp
     * @param content
     * @param typeName
     * @param images
     */
    public static Intent newIntent(Context context, String cId, String messageTyp, ArrayList<String> images, String typeName, String content) {
        Intent intent = new Intent(context, NewMomentsActivity.class);
        intent.putExtra(IntentConstants.CID, cId);
        intent.putExtra(IntentConstants.MESSAGE_TYPE, messageTyp);
        intent.putExtra(IntentConstants.CONTENT, content);
        intent.putStringArrayListExtra(IntentConstants.IMAGES, images);
        intent.putExtra(IntentConstants.TYPE_NAME, typeName);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_new_moments;
    }

    @Override
    protected NewMomentsPresenter createPresenter() {
        return new NewMomentsPresenter(this);
    }

    @Override
    protected void initView() {
        cId = getIntent().getStringExtra(IntentConstants.CID);
        newsId = getIntent().getStringExtra(IntentConstants.NEWSID);
        messageTyp = getIntent().getStringExtra(IntentConstants.MESSAGE_TYPE);
        content = getIntent().getStringExtra(IntentConstants.CONTENT);
        shareTitle = getIntent().getStringExtra(IntentConstants.TITLE);
        imgs = getIntent().getStringArrayListExtra(IntentConstants.IMGS);
        images = getIntent().getStringArrayListExtra(IntentConstants.IMAGES);
        img = getIntent().getStringExtra(IntentConstants.IMG);
        newsTitle = getIntent().getStringExtra(IntentConstants.NEWS_TITLE);

        ToolBarManager.with(this).setRightText(getString(R.string.publisher), R.color.colorAccent, new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View v) {
                if (StringUtil.isNotEmpty(messageTyp)) {
                    sharePublishMoments();
                } else {
//                    for (int i = 0; i < selectList.size(); i++) {
//                        int pictureType = PictureMimeType.isPictureType(selectList.get(i).getPictureType());
//                        if (pictureType == PictureConfig.TYPE_IMAGE) {
//                            //今天头条下载的图片
//                            if (selectList.get(i).getCompressPath().contains("news_article"))
//                                getFile(selectList.get(i).getCompressPath(), i);
//                        }
//                    }
                    mPresenter.updateImage(tv_talk.getText().toString(), selectList);
                }
            }
        });
        StatusBarManager.setLightFontColor(this, R.color.colorAccent);
        mPresenter.getType();

        share();

        LocalMedia localMedia = new LocalMedia();
        localMedia.setCompressPath("");
        typeAdapter = new AddMomentsTypeAdapter();

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
                            PictureSelector.create(NewMomentsActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(NewMomentsActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(NewMomentsActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });


        recycler.setLayoutManager(new GridLayoutManager(getCurContext(), 4));
        recycler.setAdapter(adapter);
        recyclertype.setLayoutManager(new GridLayoutManager(getCurContext(), 5));
        recyclertype.setAdapter(typeAdapter);
        typeAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<TypeListModel.TypeListBean> data = typeAdapter.getData();
            data.get(position).setCliclk(!data.get(position).getCliclk());
            typeAdapter.setList(data);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getCliclk()) {
                    sb.append(data.get(i).gettId());
                    sb.append(",");
                }
            }
            tIds = sb.toString();
        });
        tIds = "长腿欧巴,土豪";
    }

    private void sharePublishMoments() {
        String id = "";
        String shareType = "";//1:黄页，2：新闻，3：同城
        if (StringUtil.equals(ElemExtModel.SHARE_CITY, messageTyp)) {
            id = cId;
            shareType = "3";
        } else if (StringUtil.equals(ElemExtModel.SHARE_NEWS, messageTyp)) {
            shareType = "2";
            id = newsId;

        } else {
            id = cId;
            shareType = "1";
        }
        mPresenter.shareAddMoments(tv_talk.getText().toString(), type, tIds, showType, address, lng, lat, "", shareType, id);
    }

    private void share() {
        if (StringUtil.isNotEmpty(messageTyp)) {
            recycler.setVisibility(View.GONE);
            mLlShareContainer.setVisibility(View.VISIBLE);
            if (StringUtil.equals(ElemExtModel.SHARE_CITY, messageTyp)) {
                //同城
                if (images != null && images.size() > 0) {
                    CommonImageLoader.load(images.get(0)).placeholder(R.drawable.food_default).into(mIvPic);
                }
                typeName = getIntent().getStringExtra(IntentConstants.TYPE_NAME);
                mTvContent.setText(typeName.concat(content));
            } else if (StringUtil.equals(ElemExtModel.SHARE_NEWS, messageTyp)) {
                //新闻
                if (StringUtil.isNotEmpty(img)) {
                    CommonImageLoader.load(img).placeholder(R.drawable.food_default).into(mIvPic);
                }
                mTvContent.setText(newsTitle);
            } else if (StringUtil.equals(ElemExtModel.SHARE_YELLOWPAGE, messageTyp)) {
                //黄页
                if (imgs != null && imgs.size() > 0) {
                    CommonImageLoader.load(imgs.get(0)).placeholder(R.drawable.food_default).into(mIvPic);
                }
                mTvContent.setText(shareTitle);
            }
        } else {
            recycler.setVisibility(View.VISIBLE);
            mLlShareContainer.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btn_wcsee)
    public void SetWhoCanWatch() {//全屏弹出增加照片
        if (popWindow != null && popWindow.isShowing()) return;
        View upView = LayoutInflater.from(this).inflate(R.layout.popup_up, null);
        //测量View的宽高
        Utils.measureWidthAndHeight(upView);
        popWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.popup_up)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, upView.getMeasuredHeight())
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setAnimationStyle(R.style.AnimUp)
                .setViewOnclickListener(this)
                .create();
        popWindow.showAtLocation(findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
    }

    @OnClick(R.id.btn_location)
    public void GetLocation() {
//        mPresenter.locationAddress();
        startActivityForResult(AoisActivity.newInstance(this), 9);
    }

    @Override
    public void getChildView(View view, int layoutResId) {

        if (layoutResId == R.layout.popup_up) {
            TextView tvShowPublic = view.findViewById(R.id.tvShowPublic);//公开
            TextView tvShowLimit = view.findViewById(R.id.tvShowLimit);//限制
            TextView tvShowPrivate = view.findViewById(R.id.tvShowPrivate);//私密
            tvShowPublic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popWindow != null) {
                        showType = "1";
                        tvShowType.setText(getString(R.string.open_all_people));
                        popWindow.dismiss();
                    }
                }
            });
            tvShowLimit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popWindow != null) {
                        showType = "2";
                        tvShowType.setText(getString(R.string.only_friends));
                        popWindow.dismiss();
                    }
                }
            });
            tvShowPrivate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popWindow != null) {
                        showType = "3";
                        tvShowType.setText(getString(R.string.privete_secret));
                        popWindow.dismiss();
                    }
                }
            });
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (popWindow != null) {
                        popWindow.dismiss();
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public void ShowTypeList(List<TypeListModel.TypeListBean> list) {
        typeAdapter.setList(list);
    }

    @Override
    public void updateImageSuccess(String url) {
        //1:图文,2:视频 type
        mPresenter.AddMoments(tv_talk.getText().toString(), type, tIds, showType, address, lng, lat, url);
    }


    @Override
    public void MomentsAddSuccess() {
        CommonToast.toast(R.string.spublish_success);
        finish();
        setResult(RESULT_OK, new Intent());
    }


    @SuppressLint("CheckResult")
    public void getFile(final String path, final int i) {
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                emitter.onNext(decodeSampledFromResuorce(path));
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Bitmap bitmap) throws Exception {
                        File file = FileCache.getInstance().saveF(bitmap, "imageInSampleSize" + i, NewMomentsActivity.this);
                        selectList.get(i).setCompressPath(file.getPath());
                    }
                });
    }

    public Bitmap decodeSampledFromResuorce(String compressPath) {
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(compressPath, options);
//        options.inSampleSize = 2;
//        options.inJustDecodeBounds = false;
//        Bitmap bitmap = BitmapFactory.decodeFile(compressPath, options);
        Bitmap bitmap = BitmapFactory.decodeFile(compressPath);

//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        //加载图片
//        BitmapFactory.decodeFile(compressPath, options);
//        //计算缩放比
//        options.inSampleSize = 2;
//        //重新加载图片
//        options.inJustDecodeBounds = false;
//        Bitmap bitmap = BitmapFactory.decodeFile(compressPath, options);
        return bitmap;
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            if (EasyPermissions.hasPermissions(NewMomentsActivity.this, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                PickAllTypeDialog dialog = new PickAllTypeDialog(getCurContext());
                dialog.setselectedList(selectList);
                dialog.setselectedType(true);
                new XPopup.Builder(getCurContext())
                        .asCustom(dialog)
                        .show();
            } else {
                EasyPermissions.requestPermissions(NewMomentsActivity.this, getString(R.string.permission_camata_sd),
                        1, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

//            PickImageDialog dialog = new PickImageDialog(getCurContext());
//            dialog.withAspectRatio(1, 1);
//
//            popupView = new XPopup.Builder(getCurContext())
//                    .asCustom(dialog)
//                    .show();
            SoftInputUtil.hideSoftInput(getCurActivity());

        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9 && resultCode == 9) {
            tvShowAdr.setText(data.getStringExtra("adr"));
            address = data.getStringExtra("adr");//定位的地址
            lng = data.getStringExtra("lng");//经度
            lat = data.getStringExtra("lat");//纬度
        }
        if (requestCode == 101 && resultCode == 111) {
            finish();
        }
        //TODO 华为p20pro失效
        if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片、视频、音频选择结果回调
//            selectList = PictureSelector.obtainMultipleResult(data);
//            if (selectList.size() != 0) {
//                int pictureType = PictureMimeType.isPictureType(selectList.get(0).getPictureType());
//                type = pictureType == PictureConfig.TYPE_VIDEO ? "2" : "1";
//            }
//            LocalMedia localMedia = new LocalMedia();
//            localMedia.setCompressPath("");
//            adapter.setList(selectList);
//            adapter.notifyDataSetChanged();
//            Log.d("onActivityResult", JsonUtil.moderToString(selectList));
            // 图片、视频、音频选择结果回调
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);

            if (selectList.size() > 0) {
                String path = selectList.get(0).getCompressPath();
                adapter.setList(selectList);
                adapter.notifyDataSetChanged();
                Log.d("onActivityResult", JsonUtil.moderToString(selectList));
                this.selectList = selectList;
//                    filePath = path;
//                    CommonImageLoader.load(path).circle().placeholder(R.drawable.user_default).into(ivAvatar);
//                    mPresenter.doUpdateImage(filePath);
            }
        }

    }
}
