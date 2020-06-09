package com.tencent.qcloud.uikit.operation.group.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.library.common.utils.CommonImageLoader;
import com.tencent.qcloud.uikit.R;
import com.tencent.qcloud.uikit.business.chat.model.GroupSelectModel;
import com.tencent.qcloud.uikit.operation.group.GroupInvitelMemberActivity;

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

    private GroupInvitelMemberActivity.OnSelectItemClickListener mOnSelectItemClickListener;

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

        String nickName = item.getItem().getNickName();
        String remark = item.getItem().getRemark();

        String content = "";
        if (!TextUtils.isEmpty(remark)) {
            content = remark;
        } else if (!TextUtils.isEmpty(nickName)) {
            content = nickName;
        } else {
            content = item.getItem().getIdentifier();
        }
        helper.setText(R.id.tvUserName, content);
        final int position = helper.getAdapterPosition();
        final CheckBox cbAddFriend = helper.getView(R.id.cbAddFriend);
        cbAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnSelectItemClickListener!=null){
                    mOnSelectItemClickListener.onSelectItemClick(cbAddFriend,item.getItem(),position);
                }
            }
        });

        if (TextUtils.isEmpty(item.getItem().getFaceUrl())) {
            ivAcatar.setImageResource(R.drawable.food_default);
        } else {
            Observable.create(new ObservableOnSubscribe<Bitmap>() {
                @Override
                public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Bitmap> e) throws Exception {
                    Bitmap bitmap = CommonImageLoader.getBitmapByUrl(item.getItem().getFaceUrl());
                    if (bitmap != null)
                        e.onNext(bitmap);
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<Bitmap>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Bitmap bitmap) throws Exception {
                            if (bitmap == null) {
                                CommonImageLoader.load(item.getItem().getFaceUrl()).round().into(ivAcatar);
                                return;
                            }
                            ivAcatar.setImageBitmap(bitmap);
                        }
                    });
        }

    }




    public void setOnSelectItemClickListener(GroupInvitelMemberActivity.OnSelectItemClickListener listener) {
        mOnSelectItemClickListener = listener;
    }
}