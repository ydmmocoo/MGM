package com.fjx.mg.scan;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

/**
 * Author    by hanlz
 * Date      on 2019/11/8.
 * Description：
 */
public interface ScanShopContract {
    interface View extends BaseView {
        void checkSuccess();
    }

     abstract class Presenter  extends BasePresenter<View> {

        public Presenter(ScanShopContract.View view) {
            super(view);
        }


    }
}
