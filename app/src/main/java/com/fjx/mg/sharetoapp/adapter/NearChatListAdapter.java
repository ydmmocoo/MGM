package com.fjx.mg.sharetoapp.adapter;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.tencent.qcloud.uikit.business.chat.model.CacheUtil;
import com.tencent.qcloud.uikit.business.session.model.SessionInfo;
import com.tencent.qcloud.uikit.business.session.view.SessionIconView;

/**
 * Author    by hanlz
 * Date      on 2019/11/6.
 * Description：最近聊天列表适配器
 */
public class NearChatListAdapter extends BaseAdapter<SessionInfo> {

    public NearChatListAdapter() {
        super(R.layout.item_nearby_chat_list_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, SessionInfo session) {
        String url = CacheUtil.getInstance().userAvatar(session.getPeer());
        SessionIconView sessionIconView = helper.getView(R.id.session_icon);
        sessionIconView.setIcon(url);
        helper.setText(R.id.session_title, session.getTitle());
    }
}
