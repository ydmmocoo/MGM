package com.fjx.mg.moments.tag;

import com.fjx.mg.R;
import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.TagModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

class AddTagPresenter extends AddTagContract.Presenter {

    AddTagPresenter(AddTagContract.View view) {
        super(view);
    }

    @Override
    void getTags(String type) {
        mView.showLoading();
        RepositoryFactory.getRemoteRepository()
                .getTags(type)
                .compose(RxScheduler.<ResponseModel<TagModel>>toMain())
                .as(mView.<ResponseModel<TagModel>>bindAutoDispose())
                .subscribe(new CommonObserver<TagModel>() {
                    @Override
                    public void onSuccess(TagModel data) {
                        mView.hideLoading();
                        if (mView != null && data != null) {
                            mView.ShowTag(data);
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                    }
                });
    }

    @Override
    void addTag(String tags, String tagIds) {
        if (tags.equals("")) {
            CommonToast.toast(R.string.select_tag);
            return;
        }
        String s = tags.endsWith(",") ? tags.substring(0, tags.length() - 1) : tags;
        String s1 = tagIds.endsWith(",") ? tagIds.substring(0, tagIds.length() - 1) : tagIds;
        mView.showLoading();
        RepositoryFactory.getRemoteRepository()
                .addTag(s, s1)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        if (mView != null) {
                            mView.AddTagSuccess();
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        mView.hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        mView.hideLoading();
                    }
                });
    }

}
