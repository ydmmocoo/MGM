package com.fjx.mg.me.safe_center.lock;

import com.fjx.mg.view.lock.GestureLockLayout;
import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

import java.util.List;

public interface GestureLockContract {


    interface View extends BaseView {


        /**
         * 设置手势第一次图案
         *
         * @param answerList
         */
        void onFirstPasswordFinished(List<Integer> answerList);

        /**
         * 设置手势完成
         *
         * @param isMatched
         * @param answerList
         */
        void onSetPasswordFinished(boolean isMatched, String answerList);

        /**
         * //绘制手势解锁完成时调用
         *
         * @param isMatched
         */
        void onGestureFinished(boolean isMatched);

        /**
         * 超出限次
         */
        void onGestureTryTimesBoundary();

        void setGestureCodeSuccess();

        void refreshTokenSuccess();
    }

    abstract class Presenter extends BasePresenter<GestureLockContract.View> {

        Presenter(GestureLockContract.View view) {
            super(view);
        }

        abstract void setOnLockResetListener(GestureLockLayout lockLayout);

        abstract void setOnLockVerifyListener(GestureLockLayout lockLayout);

        abstract void setGestureCode(String gCode);

        abstract void refreshToken(String gCode);


    }
}
