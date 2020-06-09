package com.fjx.mg.moments.add;

import android.graphics.Bitmap;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.TypeListModel;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

public interface NewMomentsContract {

    interface View extends BaseView {
        void ShowTypeList(List<TypeListModel.TypeListBean> list);

        void updateImageSuccess(String url);


        void MomentsAddSuccess();

    }

    abstract class Presenter extends BasePresenter<NewMomentsContract.View> {
        public Presenter(NewMomentsContract.View view) {
            super(view);
        }

        abstract void getType();//获取用户标签

        abstract void updateImage(String content, List<LocalMedia> selectList);

        abstract void AddMoments(String content, String type, String tIds, String showType, String address, String lng, String lat, String urls);
    }
}
