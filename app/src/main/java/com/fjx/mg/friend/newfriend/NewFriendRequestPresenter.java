package com.fjx.mg.friend.newfriend;

import android.util.Log;

import com.library.repository.data.UserCenter;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.db.model.DBFriendRequestModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriendPendencyItem;
import com.tencent.imsdk.friendship.TIMFriendPendencyResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewFriendRequestPresenter extends NewFriendRequestContact.Presenter {

    public NewFriendRequestPresenter(NewFriendRequestContact.View view) {
        super(view);
    }

    @Override
    void getPendencyList() {
        RepositoryFactory.getChatRepository().getPendencyList(new TIMValueCallBack<TIMFriendPendencyResponse>() {
            @Override
            public void onError(int i, String s) {
                Log.d("getPendencyList", s);

            }

            @Override
            public void onSuccess(TIMFriendPendencyResponse response) {
                List<TIMFriendPendencyItem> datas = response.getItems();
                DBFriendRequestModel requestModel;
                for (TIMFriendPendencyItem item : datas) {
                    String identifier = item.getIdentifier();
                    requestModel = DBDaoFactory.getFriendRequestDao().queryModel(identifier);
                    if (requestModel == null) {
                        requestModel = new DBFriendRequestModel();
                        requestModel.setIdentityId(identifier);
                        requestModel.setAddWording(item.getAddWording());
                        requestModel.setNickName(item.getNickname());
                        requestModel.setUid(UserCenter.getUserInfo().getUId());
                    } else {
                        requestModel.setStatus(0);
                        requestModel.setAddWording(item.getAddWording());
                        requestModel.setNickName(item.getNickname());
                        requestModel.setUid(UserCenter.getUserInfo().getUId());
                    }
                    DBDaoFactory.getFriendRequestDao().insertModel(requestModel);
                }

                List<DBFriendRequestModel> list = DBDaoFactory.getFriendRequestDao().queryList();
                if (mView != null && list != null) {
//                    Collections.reverse(list);
                    mView.showPendencyList(list);
                }
            }
        });
    }

    /**
     * 未同意排上面
     *
     * @param list
     */
    private List<DBFriendRequestModel> reverse(List<DBFriendRequestModel> list) {
        List<DBFriendRequestModel> agree = new ArrayList<>();//已接受的放下面
        List<DBFriendRequestModel> other = new ArrayList<>();//未接受排上面
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getStatus() == 0) {//未处理(可以显示同意按钮的数据)
                other.add(list.get(i));
            } else {
                agree.add(list.get(i));
            }
        }
        List<DBFriendRequestModel> result = new ArrayList<>();
        result.addAll(other);
        result.addAll(agree);
        return result;
    }


}
