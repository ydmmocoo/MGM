package com.fjx.mg.friend.chat.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.fjx.mg.friend.chat.StartGroupChatActivity;
import com.library.common.utils.CommonImageLoader;
import com.library.repository.models.GroupSelectModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Author    by hanlz
 * Date      on 2019/12/2.
 * Description：
 */
public class GroupSelectFriendsAdapter extends BaseSectionQuickAdapter<GroupSelectModel, BaseViewHolder> {

    private StartGroupChatActivity.OnSelectChangedListener mOnSelectChangedListener;
    private StartGroupChatActivity.OnSelectItemClickListener mOnSelectItemClickListener;

    public GroupSelectFriendsAdapter(List<GroupSelectModel> data) {
        super(R.layout.item_contact_section, R.layout.item_select_friend, data);
    }

    @Override
    protected void convertHeader(@NotNull BaseViewHolder helper, @NotNull GroupSelectModel item) {
        helper.setText(R.id.tvSection, item.getHeader());
    }

    @SuppressLint("CheckResult")
    @Override
    protected void convert(BaseViewHolder helper, final GroupSelectModel item) {

        final ImageView ivAcatar = helper.getView(R.id.ivAcatar);

        String nickName = item.getData().getNickName();
        String remark = item.getData().getRemark();

        String content = "";
        if (!TextUtils.isEmpty(remark)) {
            content = remark;
        } else if (!TextUtils.isEmpty(nickName)) {
            content = nickName;
        } else {
            content = item.getData().getIdentifier();
        }
        helper.setText(R.id.tvUserName, content);
        final int position = helper.getAdapterPosition();
        final CheckBox cbAddFriend = helper.getView(R.id.cbAddFriend);
        cbAddFriend.setChecked(item.getData().isSelected());
        cbAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnSelectItemClickListener != null) {
                    mOnSelectItemClickListener.onSelectItemClick(cbAddFriend, item.getData(), position);
                }
            }
        });

        if (TextUtils.isEmpty(item.getData().getFaceUrl())) {
            ivAcatar.setImageResource(R.drawable.food_default);
        } else {
            Observable.create(new ObservableOnSubscribe<Bitmap>() {
                @Override
                public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Bitmap> e) throws Exception {
                    Bitmap bitmap = CommonImageLoader.getBitmapByUrl(item.getData().getFaceUrl());
                    if (bitmap != null)
                        e.onNext(bitmap);
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<Bitmap>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Bitmap bitmap) throws Exception {
                            if (bitmap == null) {
                                CommonImageLoader.load(item.getData().getFaceUrl()).round().into(ivAcatar);
                                return;
                            }
                            ivAcatar.setImageBitmap(bitmap);
                        }
                    });
        }

    }


    public void setOnSelectItemClickListener(StartGroupChatActivity.OnSelectItemClickListener listener) {
        mOnSelectItemClickListener = listener;
    }
}