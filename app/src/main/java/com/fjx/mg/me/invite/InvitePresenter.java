package com.fjx.mg.me.invite;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.ContactsContract;

import com.fjx.mg.R;
import com.fjx.mg.utils.SharedPreferencesHelper;
import com.google.gson.Gson;
import com.library.common.utils.CommonToast;
import com.library.common.utils.DimensionUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.InviteModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class InvitePresenter extends InviteContract.Presenter {

    InvitePresenter(InviteContract.View view) {
        super(view);
    }

    @SuppressLint("CheckResult")
    @Override
    void getBitmap(final String url) {
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Bitmap> e) throws Exception {
                Bitmap logo = BitmapFactory.decodeResource(mView.getCurActivity().getResources(), R.drawable.invite_logo);
                int width = DimensionUtil.dip2px(274);
                Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(url, width, Color.BLACK, logo);
                e.onNext(bitmap);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Bitmap bitmap) throws Exception {
                        mView.showQrBitmap(bitmap);
                    }
                });
    }

    @Override
    void getInviteCode(final InviteActivity context) {
        mView.showLoading();
        RepositoryFactory.getRemoteAccountRepository().getInviteCode()
                .compose(RxScheduler.<ResponseModel<InviteModel>>toMain())
                .as(mView.<ResponseModel<InviteModel>>bindAutoDispose())
                .subscribe(new CommonObserver<InviteModel>() {
                    @Override
                    public void onSuccess(InviteModel data) {
                        SharedPreferencesHelper sp = new SharedPreferencesHelper(context);
                        Gson gson = new Gson();
                        sp.putString("InviteModel", gson.toJson(data));
                        sp.close();
                        mView.hideLoading();
                        mView.showInviteMessage(data);
                        getBitmap(data.getRegisterUrl());
                        UserInfoModel userInfo = UserCenter.getUserInfo();
                        userInfo.setInviteCode(data.getInviteCode());
                        UserCenter.saveUserInfo(userInfo);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }
                });
    }


    @Override
    void getPhoneNum(Intent data) {
        try {
            if (data.getData() != null) {
                Cursor cursor = mView.getCurContext().getContentResolver()
                        .query(data.getData(),
                                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                                null, null, null);
                while (cursor.moveToNext()) {
                    //取出该条数据的联系人姓名
                    String name = cursor.getString(1).replaceAll(" ", "");
                    //取出该条数据的联系人的手机号
                    String number = cursor.getString(0).replaceAll(" ", "").replaceAll("-", "");
                    mView.showSelectPhoneNUm(number);
                }
                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
