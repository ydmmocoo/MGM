package com.fjx.mg.friend.chat.redpacket;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.GroupRedPacketDetailModel;
import com.library.repository.models.ReciveRedRrecordModel;

public interface GroupRedPacketDetailContact {

    interface View extends BaseView {

        void responseRedPacketDetail(GroupRedPacketDetailModel data);

        void responseReciveRedRrecord(ReciveRedRrecordModel data);

    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void requestRedPacketDetail(String rId);

        public abstract void requestReciveRedRrecord(String rId, String page);


    }
}
