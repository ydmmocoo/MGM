package com.library.common.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.concurrent.ExecutionException;

public class CommonImageLoader {

    private RequestBuilder<Drawable> requestBuilder;
    private RequestOptions options;

    private CommonImageLoader(Object any) {
        requestBuilder = Glide.with(ContextManager.getContext()).load(any);
        options = new RequestOptions();
    }

    public static CommonImageLoader load(Object any) {
        return new CommonImageLoader(any);
    }

    public CommonImageLoader placeholder(@DrawableRes int resId) {
        options.placeholder(resId);
        return this;
    }
    public CommonImageLoader error(@DrawableRes int resId) {
        options.error(resId);
        return this;
    }

    public CommonImageLoader circle() {
        options = options.circleCrop();
        return this;
    }

    public CommonImageLoader round() {
        round(6);
        return this;
    }

    public CommonImageLoader round(int radius) {
        options = options.transform(new GlideRoundTransform(radius));
        return this;
    }

    public CommonImageLoader withoutCache() {
        options = options.diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true);
        return this;
    }

    public CommonImageLoader resize(int width, int hight) {
        options = options.override(width, hight);
        return this;
    }

    public void into(ImageView imageView) {
        if (imageView == null) return;
        requestBuilder.apply(options)//.transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);

    }

    public CommonImageLoader noAnim() {
        options = options.dontAnimate();
        return this;
    }


    public static Bitmap getBitmapByUrl(String imageUrl) {
        try {
            return Glide.with(ContextManager.getContext()).asBitmap()
                    .load(imageUrl)
                    .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getBitmapByUrl(String imageUrl, RequestListener<Bitmap> listener) {
        try {
            return Glide.with(ContextManager.getContext()).asBitmap()
                    .load(imageUrl)
                    .addListener(listener)
                    .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getThumbBitmapByUrl(String imageUrl) {
        try {
            Bitmap bitmap = Glide.with(ContextManager.getContext()).asBitmap()
                    .load(imageUrl)
                    .submit(100, 100)
                    .get();
            CacheManager.getInstance(ContextManager.getContext()).putBitmap(imageUrl, bitmap);
            return bitmap;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
