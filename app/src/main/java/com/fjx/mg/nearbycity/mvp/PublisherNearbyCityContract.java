package com.fjx.mg.nearbycity.mvp;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.NearbyCityConfigModel;
import com.library.repository.models.ResponseModel;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

public interface PublisherNearbyCityContract {

    interface View extends BaseView {
        void responseFailed(ResponseModel data);

        void responseConfigDatas(NearbyCityConfigModel model);
        void updateImageSuccess(String url);
    }

    abstract class Presenter extends BasePresenter<PublisherNearbyCityContract.View> {

        Presenter(PublisherNearbyCityContract.View view) {
            super(view);
        }

        public abstract void requestConfig();
        public abstract void updateImage(String content, List<LocalMedia> selectList);
    }
}
