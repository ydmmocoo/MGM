package com.fjx.mg.news.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.fjx.mg.image.NewImageActivity;
import com.library.common.utils.JsonUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/18.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class MJavascriptInterface {

    private Activity context;
    private static final String TAG="SIMON";
    private String[] images;

    public MJavascriptInterface(Activity context,String[] images) {
        this.context = context;
        this.images=images;
    }

    @android.webkit.JavascriptInterface
    public void openImage(String img) {
        Log.e(TAG,"img:"+img);
        int pos=0;
        List<String> list=new ArrayList<>();
        for (int i=0;i<images.length;i++){
            Log.e(TAG,"images:"+images[i]);
            list.add(images[i]);
            if (img.equals(images[i].replaceAll("&amp;","&"))){
                pos=i;
            }
        }
        Intent intent = NewImageActivity.newInstance(context, JsonUtil.moderToString(list), pos);
        context.startActivity(intent);
    }
}
