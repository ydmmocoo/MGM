package com.fjx.mg.main.search;

import android.widget.EditText;
import android.widget.TextView;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.library.common.base.BaseFragment;
import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.CompanyListModel;
import com.library.repository.models.HouseListModel;
import com.library.repository.models.JobListModel;
import com.library.repository.models.NewsItemModel;
import com.library.repository.models.NewsListModel;
import com.tencent.imsdk.friendship.TIMFriend;

import java.util.ArrayList;
import java.util.List;

public interface SearchContract {

    interface View extends BaseView {

        void selectType(int type, String text);

        void showClearImage(boolean enableShow);

        void showNewsList(NewsItemModel newsList);

        void showHouseList(HouseListModel houseList);

        void showJobList(JobListModel jobList);

        void showFriendList(List<TIMFriend> friendList);

        void showCompanyList(CompanyListModel companyList);

        void loadError();
    }

    abstract class Presenter extends BasePresenter<SearchContract.View> {

        Presenter(SearchContract.View view) {
            super(view);
        }


        abstract void showTypeDialog(TextView view);

        abstract void search(int type, String content, int page);

        abstract void searchNews(String content, int page);

        abstract void searchHouse(String content, int page);

        abstract void searchJob(String content, int page);

        abstract void searchFriend(String content, int page);

        abstract void searchCompany(String content, int page);

        abstract void bindTextWatcher(EditText editText);


    }

}
