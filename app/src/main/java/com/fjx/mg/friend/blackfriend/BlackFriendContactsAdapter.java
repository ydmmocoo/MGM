package com.fjx.mg.friend.blackfriend;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.utils.CommonImageLoader;
import com.library.repository.models.FriendContactSectionModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BlackFriendContactsAdapter extends BaseSectionQuickAdapter<FriendContactSectionModel, BaseViewHolder> {

    public BlackFriendContactsAdapter(List<FriendContactSectionModel> data) {
        super(R.layout.item_contact_section, R.layout.item_add_friend, data);
    }

    @Override
    protected void convertHeader(@NotNull BaseViewHolder helper, @NotNull FriendContactSectionModel item) {
        helper.setText(R.id.tvSection, item.getHeader());
    }

    @SuppressLint("CheckResult")
    @Override
    protected void convert(BaseViewHolder helper, final FriendContactSectionModel item) {
        helper.getView(R.id.tvAddFriend).setVisibility(View.GONE);
        helper.getView(R.id.tvPhone).setVisibility(View.GONE);
        final ImageView ivAcatar = helper.getView(R.id.ivAcatar);

        String nickName = item.getData().getTimUserProfile().getNickName();
        String remark = item.getData().getRemark();

        String content = "";
        if (!TextUtils.isEmpty(remark)) {
            content = remark;
        } else if (!TextUtils.isEmpty(nickName)) {
            content = nickName;
        } else {
            content = item.getData().getTimUserProfile().getIdentifier();
        }

        helper.setText(R.id.tvNickName, content);

        if (TextUtils.isEmpty(item.getData().getTimUserProfile().getFaceUrl())) {
            ivAcatar.setImageResource(R.drawable.default_user_image);
        } else {
            Observable.create(new ObservableOnSubscribe<Bitmap>() {
                @Override
                public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Bitmap> e) throws Exception {
                    Bitmap bitmap = CommonImageLoader.getBitmapByUrl(item.getData().getTimUserProfile().getFaceUrl());
                    if (bitmap != null)
                        e.onNext(bitmap);
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<Bitmap>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Bitmap bitmap) throws Exception {
                            if (bitmap == null) {
                                CommonImageLoader.load(item.getData().getTimUserProfile().getFaceUrl()).round().into(ivAcatar);
                                return;
                            }
                            ivAcatar.setImageBitmap(bitmap);
                        }
                    });
        }

    }
}
