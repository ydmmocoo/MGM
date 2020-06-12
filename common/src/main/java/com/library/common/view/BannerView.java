package com.library.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.library.common.R;
import com.library.common.utils.CommonImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.List;

public class BannerView extends Banner {

    public BannerView(Context context) {
        super(context);
        init();
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BannerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        setImageLoader(new GlideImageLoader());
    }

    class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            CommonImageLoader.load(path).resize(710,170).placeholder(R.drawable.banner_logo_ic).into(imageView);
        }
    }

    /**
     * 显示轮播图
     *
     * @param paths
     */
    public void showImages(List<String> paths) {
        setImages(paths);
        setDelayTime(3000);
        start();
    }

    public void showImagesRes(List<Integer> paths) {
        setImages(paths);
        setDelayTime(3000);
        start();
    }
}
