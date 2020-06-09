package com.fjx.mg.friend.phone_contact;

import androidx.core.content.ContextCompat;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.utils.GradientDrawableHelper;
import com.library.repository.models.PhoneContactSection;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PhoneContactsAdapter extends BaseSectionQuickAdapter<PhoneContactSection, BaseViewHolder> {

    public PhoneContactsAdapter(List<PhoneContactSection> data) {
        super(R.layout.item_contact_section, R.layout.item_phone_contact, data);
    }

    @Override
    protected void convertHeader(@NotNull BaseViewHolder baseViewHolder, @NotNull PhoneContactSection item) {
        baseViewHolder.setText(R.id.tvSection, item.getHeader());
    }

    @Override
    protected void convert(BaseViewHolder helper, PhoneContactSection item) {
        TextView tvAddFriend = helper.getView(R.id.tvAddFriend);
        ImageView ivAcatar = helper.getView(R.id.ivAcatar);
        String nickName = item.getDisplay_name();
        String remark = item.getMobile();
        helper.setText(R.id.tvNickName, nickName);
        helper.setText(R.id.tvPhone, remark);


        //	0：不存在,1:存在，3：已经是好友
        if (item.getIsExits() == 2) {
            tvAddFriend.setText(getContext().getString(R.string.had_add));
            tvAddFriend.setTextColor(ContextCompat.getColor(helper.itemView.getContext(), R.color.textColorGray));
            GradientDrawableHelper.whit(tvAddFriend).setColor(R.color.white).setStroke(1, R.color.white);
        } else if (item.getIsExits() == 1) {
            tvAddFriend.setText(getContext().getString(R.string.add));
            tvAddFriend.setTextColor(ContextCompat.getColor(helper.itemView.getContext(), R.color.textColorAccent));
            GradientDrawableHelper.whit(tvAddFriend).setColor(R.color.white).setStroke(1, R.color.colorAccent);
        } else {
            tvAddFriend.setText(R.string.invite);
            tvAddFriend.setTextColor(ContextCompat.getColor(helper.itemView.getContext(), R.color.textColorAccent));
            GradientDrawableHelper.whit(tvAddFriend).setColor(R.color.white).setStroke(1, R.color.colorAccent);
        }


//        CommonImageLoader.load(item.t.getTimUserProfile().getFaceUrl())
//                .round()
//                .placeholder(R.drawable.default_user_image)
//                .into(ivAcatar);
    }
}
