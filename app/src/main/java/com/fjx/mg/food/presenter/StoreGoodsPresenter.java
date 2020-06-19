package com.fjx.mg.food.presenter;

import android.widget.ImageView;

import com.fjx.mg.food.contract.StoreGoodsContract;
import com.fjx.mg.food.model.bean.StoreGoodsGroupBean;
import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.StoreGoodsBean;
import com.library.repository.repository.RepositoryFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class StoreGoodsPresenter extends StoreGoodsContract.Presenter {

    public StoreGoodsPresenter(StoreGoodsContract.View view) {
        super(view);
    }

    @Override
    public void getGoodsData(String sId) {
        RepositoryFactory.getRemoteFoodApi().getAllGoodsList(sId)
                .compose(RxScheduler.<ResponseModel<StoreGoodsBean>>toMain())
                .as(mView.<ResponseModel<StoreGoodsBean>>bindAutoDispose())
                .subscribe(new CommonObserver<StoreGoodsBean>() {
                    @Override
                    public void onSuccess(StoreGoodsBean data) {
                        if (mView != null) {
                            mView.getGoodsDataSuccess(data.getCateList());
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null)
                            mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    public void addShopCart(String sId, String gId, String gName, String seId, String seName, String aIds, String aNames, String price, String num, String img, ImageView ivAdd) {
        RepositoryFactory.getRemoteFoodApi().addShopCart(sId, gId, gName, seId, seName, aIds, aNames, price, num, img)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        if (mView != null) {
                            mView.addShopCartSuccess(ivAdd);
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    public List<StoreGoodsGroupBean> getGroupList(List<StoreGoodsBean.CateListBean> data) {
        List<StoreGoodsGroupBean> list = new ArrayList<>();
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                StoreGoodsGroupBean bean = new StoreGoodsGroupBean();
                bean.setCount(0);
                bean.setName(data.get(i).getCateName());
                bean.setGroupId(i);
                list.add(bean);
            }
        }
        return list;
    }

    public List<StoreGoodsBean.CateListBean.GoodsListBean> getGoodsList(List<StoreGoodsBean.CateListBean> data) {
        List<StoreGoodsBean.CateListBean.GoodsListBean> list = new ArrayList<>();
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                for (int j = 0; j < data.get(i).getGoodsList().size(); j++) {
                    StoreGoodsBean.CateListBean.GoodsListBean bean = data.get(i).getGoodsList().get(j);
                    bean.setGroupId(i);
                    bean.setGroupName(data.get(i).getCateName());
                    list.add(bean);
                }
            }
        }
        return list;
    }
}
