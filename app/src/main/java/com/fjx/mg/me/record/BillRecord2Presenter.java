package com.fjx.mg.me.record;

import android.view.View;

import androidx.appcompat.view.menu.ActionMenuItemView;

import com.fjx.mg.dialog.RecordSelectDialog;
import com.fjx.mg.dialog.anim.TranslateAnimator;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.BillRecordModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.interfaces.XPopupCallback;

import java.util.List;

public class BillRecord2Presenter extends BillRecord2Contract.Presenter {


    BillRecord2Presenter(BillRecord2Contract.View view) {
        super(view);
    }

    @Override
    void getBillRecord(final int page, String billType, String accountType) {
        RepositoryFactory.getRemoteAccountRepository().getBillList(page, billType, accountType)
                .compose(RxScheduler.<ResponseModel<BillRecordModel>>toMain())
                .as(mView.<ResponseModel<BillRecordModel>>bindAutoDispose())
                .subscribe(new CommonObserver<BillRecordModel>() {
                    @Override
                    public void onSuccess(BillRecordModel data) {
                        if (mView != null && data != null) {
                            mView.showBillRecore(data, page);
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                        mView.loadDataerror();

                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                        mView.loadDataerror();
                    }
                });
    }

    @Override
    void showSelectDialog(final View view, List<BillRecordModel.AccountTypeBean> accountTypeList, List<BillRecordModel.BillTypeBean> billTypeList) {
        if (accountTypeList == null && billTypeList == null) return;

        RecordSelectDialog recordSelectDialog = new RecordSelectDialog(mView.getCurContext(), billTypeList, accountTypeList,
                new RecordSelectDialog.OnSelectListener() {
                    @Override
                    public void onSelect(String typeId, String typeName, boolean isOther) {
                        mView.onSelectType(typeId, typeName, isOther);
                    }
                });

        new XPopup.Builder(mView.getCurContext())
                .atView(view)
                .hasShadowBg(true)
                .popupAnimation(PopupAnimation.ScrollAlphaFromLeftTop)
                .setPopupCallback(new XPopupCallback() {
                    @Override
                    public void onCreated() {
                    }

                    @Override
                    public void beforeShow() {
                    }

                    @Override
                    public void onShow() {
                    }

                    @Override
                    public void onDismiss() {
                        mView.onDialogDismiss();
                    }

                    @Override
                    public boolean onBackPressed() {
                        return false;
                    }
                })
                .asCustom(recordSelectDialog)
                .show();
    }


}
