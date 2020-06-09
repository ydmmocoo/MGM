package com.fjx.mg.image;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.fjx.mg.R;
import com.fjx.mg.main.MainActivity;
import com.fjx.mg.main.qrcode.CaptureActivity;
import com.fjx.mg.utils.DialogUtil;
import com.library.common.base.BaseActivity;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.SavePictureUtil;
import com.library.common.utils.StatusBarManager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.EasyPermissions;

public class NewImageActivity extends BaseActivity {

    private final String TAG = "ImageActivity";

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tvPosition)
    TextView tvPosition;
    /*进入条*/
    @BindView(R.id.clPercentage)
    ConstraintLayout mClPercentage;
    @BindView(R.id.pb)
    ProgressBar mPb;
    @BindView(R.id.tvPercentage)
    TextView mTvPercentage;

    private List<String> imageUrls;
    private int position;

    private List<View> views = new ArrayList<>();

    public static Intent newInstance(Context context, String urls, int position) {
        Intent intent = new Intent(context, NewImageActivity.class);
        intent.putExtra("urls", urls);
        intent.putExtra("position", position);
        return intent;
    }

    public static Intent newInstance(Context context, String urls, int position, boolean def) {
        Intent intent = new Intent(context, NewImageActivity.class);
        intent.putExtra("urls", urls);
        intent.putExtra("position", position);
        intent.putExtra("def", def);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_new_image_pager;
    }

    @Override
    protected void initView() {
        StatusBarManager.setColor(this, com.library.common.R.color.black);
        String str = getIntent().getStringExtra("urls");
        imageUrls = JsonUtil.jsonToList(str, String.class);
        position = getIntent().getIntExtra("position", 0);
        boolean def = getIntent().getBooleanExtra("def", false);
        for (String url : imageUrls) {
            View view = LayoutInflater.from(this).inflate(R.layout.view_image, null);
            PhotoView imageView = view.findViewById(R.id.imageVIew);
            imageView.enable();
            String imgUrl = "";
            try {
                imgUrl = URLDecoder.decode(url, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                imgUrl = url;
            }
            mPb.setVisibility(View.VISIBLE);
            if (def) {
                CommonImageLoader.load(imgUrl).placeholder(R.drawable.group_chat_header_ic).into(imageView);
            } else {
//                CommonImageLoader.load(url).into(imageView);
                Glide.with(this).load(imgUrl).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        mPb.setVisibility(View.GONE);//关闭进度条
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        //TODO
                        mPb.setVisibility(View.GONE); //关闭进度条
                        return false;
                    }
                }).into(imageView);
            }
//            showImage(imageView, url, def);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new DialogUtil().showAlertDialog(getCurActivity(), R.string.tips, R.string.save_picture, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SavePictureUtil.getInstance().savePic(NewImageActivity.this, ((BitmapDrawable) imageView.getDrawable()).getBitmap()
                                    , SavePictureUtil.getInstance().getFilePath("/MG/pic"),
                                    SavePictureUtil.getInstance().getFileNameByTime());
                            dialog.dismiss();
                        }
                    });
                    return true;
                }
            });
            views.add(view);
        }

        viewpager.setAdapter(pagerAdapter);
        viewpager.setCurrentItem(position);
        tvPosition.setText((position + 1) + "/" + views.size());
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                position = i;
                tvPosition.setText((position + 1) + "/" + views.size());
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    private PagerAdapter pagerAdapter = new PagerAdapter() {

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(views.get(position));
        }
    };


    public void showImage(final ImageView img, final String url, boolean def) {
        ProgressInterceptor.addListener(url, new ProgressListener() {
            @Override
            public void onProgress(int progress) {
                mTvPercentage.setText(progress + "%");
                Log.d(TAG, progress + "");
            }
        });

        RequestOptions options = new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
//                .transforms(new CircleTransform(mContext,2, Color.DKGRAY))
//                .transforms(new BlackWhiteTransformation());
//                .transforms(new BlurTransformation(mContext, 25),new CircleTransform(mContext,2, Color.DKGRAY)) // (0 < r <= 25)
//                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        if (def)
            options.placeholder(R.drawable.group_chat_header_ic);
        Glide.with(this)
                .load(url)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        ProgressInterceptor.removeListener(url);
                        mClPercentage.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        ProgressInterceptor.removeListener(url);
                        mClPercentage.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(img);
    }
}
