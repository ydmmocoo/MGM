package com.fjx.mg.moments.tag;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.TagModel;

public interface AddTagContract {
    interface View extends BaseView {
        void ShowTag(TagModel data);

        void AddTagSuccess();
    }

    abstract class Presenter extends BasePresenter<AddTagContract.View> {
        Presenter(AddTagContract.View view) {
            super(view);
        }

        abstract void getTags(String type);

        abstract void addTag(String tags, String tagIds);
    }
}
