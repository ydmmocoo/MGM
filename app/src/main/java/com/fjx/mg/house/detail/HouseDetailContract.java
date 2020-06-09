package com.fjx.mg.house.detail;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.HouseDetailModel;
import com.library.repository.models.NewsCommentModel;

public interface HouseDetailContract {

    interface View extends BaseView {
        String getHid();

        String getStatus();

        String getWeixinCode();

        String getPhone();

        int getType();

        void showHouseDetail(HouseDetailModel detailModel);

        void toggleCollectResult(boolean isCollect);

        void showCommentList(NewsCommentModel commentModel);

        void commentSuccess();

        void loadCommentError();

        void refreshData();

        void toggleCollect();

        void share();

    }

    abstract class Presenter extends BasePresenter<HouseDetailContract.View> {

        Presenter(HouseDetailContract.View view) {
            super(view);
        }

        abstract void houseDetail(String hId);


        abstract void toggleCollect(boolean hasCollect, String hid);

        abstract void getCommentList(int page, String hid);

        abstract void addComment(String conent, String hid);


        abstract void showContactDialog(String code, boolean isPhone);

        abstract void settingOptions(int position);

        abstract void showPublishDialog(android.view.View view, boolean isExpire, boolean isClosed, boolean isLove, boolean isShare);

        abstract void deleteHousePublish(String hid);

        abstract void closeOrOpenHousePublish(String hid);


    }

}
