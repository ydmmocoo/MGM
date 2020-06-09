package com.fjx.mg.me.transfer.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.StringUtil;
import com.library.repository.models.PhoneHistoryModel;
import com.tencent.qcloud.uikit.business.session.view.SessionIconView;


/**
 * Author    by hanlz
 * Date      on 2020/1/15.
 * Description：
 */
public class RecentlyTransferAdapter extends BaseAdapter<PhoneHistoryModel> {

    public RecentlyTransferAdapter() {
        super(R.layout.session_adapter);
    }

    @Override
    protected void convert(BaseViewHolder helper, PhoneHistoryModel item) {
        helper.setVisible(R.id.session_time, false)
                .setText(R.id.session_title, TextUtils.isEmpty(item.getNickname()) ? item.getPhone() : item.getNickname())
                .setText(R.id.session_last_msg, item.getPhone());
        SessionIconView sessionIconView = helper.getView(R.id.session_icon);
        if (StringUtil.isNotEmpty(item.getFaceIcon())) {
            sessionIconView.setIcon(item.getFaceIcon());
        } else {
            sessionIconView.setDefIcon();
        }
    }
}
