package com.fjx.mg.main.scan.paymentcode;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.fjx.mg.R;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.DimensionUtil;
import com.library.common.utils.MulLanguageUtil;
import com.library.common.utils.StringUtil;
import com.library.common.utils.ZXingUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Author    by hanlz
 * Date      on 2020/4/2.
 * Description：
 */
public class BarCodeActivity extends AppCompatActivity {

    private Unbinder mUnbinder;
    @BindView(R.id.ivBarCode)
    ImageView mIvBarCode;
    @BindView(R.id.tvNum)
    TextView mTvNum;

    public static Intent newIntent(Context context, String contents) {
        Intent intent = new Intent(context, BarCodeActivity.class);
        intent.putExtra(IntentConstants.EXT, contents);
        return intent;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_bar_code);

        Log.d("hanlz", this.getClass().getSimpleName());
        mUnbinder = ButterKnife.bind(this);
        final String contents = getIntent().getStringExtra(IntentConstants.EXT);
        mTvNum.setText(contents);
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                int width = DimensionUtil.dip2px(600);
                int height = DimensionUtil.dip2px(130);
                Bitmap bitmap = ZXingUtils.creatBarcode(BarCodeActivity.this, contents, width, height, false);
                emitter.onNext(bitmap);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        mIvBarCode.setImageBitmap(bitmap);
                    }
                });

        final Dialog dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        //自定义布局
        View view = View.inflate(this, R.layout.dialog_bar_code, null);
        view.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //设置宽
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置高
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
        dialog.show();
        findViewById(R.id.clRoot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Context context = languageWork(newBase);
        super.attachBaseContext(context);
    }

    private Context languageWork(Context context) {
        // 8.0及以上使用createConfigurationContext设置configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return updateResources(context);
        } else {
            return context;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Context updateResources(Context context) {
        Resources resources = context.getResources();
        Locale locale = MulLanguageUtil.getLocalLanguage();
        if (locale == null || StringUtil.isEmpty(locale.toString())) {
            Locale loc = getResources().getConfiguration().locale;
            if (loc == null) {
                return context;
            }
            locale = loc;
            MulLanguageUtil.updateLocale(locale);
        }
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
    }
}
