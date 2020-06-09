package com.fjx.mg.main.fragment.news;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.fjx.mg.R;
import com.fjx.mg.dialog.ScanQrDialogx;
import com.fjx.mg.main.MainActivity;
import com.fjx.mg.main.fragment.event.NewsEvent;
import com.fjx.mg.main.scan.QRCodeCollectionActivity;
import com.fjx.mg.main.scan.paymentcode.PaymentCodeActivity;
import com.fjx.mg.main.search.SearchActivity;
import com.fjx.mg.utils.DialogUtil;
import com.flyco.tablayout.SlidingTabLayout;
import com.library.common.base.BaseFragment;
import com.library.common.base.BaseMvpFragment;
import com.library.common.base.adapter.PagerAdapter;
import com.library.repository.data.UserCenter;
import com.library.repository.models.NewsTypeModel;
import com.lxj.xpopup.XPopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 新闻首页
 */
public class NewsFragment extends BaseMvpFragment<NewsPresenter> implements NewsContract.View {

    private List<NewsTypeModel> typeList;
    @BindView(R.id.s_tab)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.ivRightIcon)
    ImageView mIvRightIcon;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private ScanQrDialogx scanQrDialogx;

    public static NewsFragment newInstance() {
        Bundle args = new Bundle();
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_news;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (typeList == null || typeList.isEmpty())
            mPresenter.getNewsTypes();
//        ivRightIcon.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (typeList == null || typeList.isEmpty())
            mPresenter.getNewsTypes();
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        if (isVisibleToUser) {
//            if (typeList == null || typeList.isEmpty())
//                mPresenter.getNewsTypes();
//        }
//    }

    @Override
    public void showTabsAndFragment(List<NewsTypeModel> typeList, String[] titles, List<BaseFragment> fragments) {
        this.typeList = typeList;
        //viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setAdapter(new PagerAdapter(getChildFragmentManager(), fragments));
        slidingTabLayout.setTabSpaceEqual(titles.length < 5);
        slidingTabLayout.setViewPager(viewPager, titles);

    }

    @Override
    protected NewsPresenter createPresenter() {
        return new NewsPresenter(this);
    }


    @OnClick({R.id.tvSearch, R.id.ivRightIcon})
    public void onViewClicked(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tvSearch:
                startActivity(SearchActivity.newInstance(getCurContext()));
                break;
            case R.id.ivRightIcon:
                if (UserCenter.hasLogin()) {
                    showScanQrDialog();
                } else {
                    new DialogUtil().showLoginAlertDialog(getCurActivity());
                }
                break;
            default:
                break;
        }

    }

    private void showScanQrDialog() {
        scanQrDialogx = new ScanQrDialogx(getActivity(), new ScanQrDialogx.MenuDeleteDialogClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tvQrScan:
                        scanQrDialogx.dismiss();
                        Intent intent = new Intent(MainActivity.broadcast_tocpatureac);
                        getActivity().sendBroadcast(intent);
                        break;
                    case R.id.tvQrCode:
                        getContext().startActivity(QRCodeCollectionActivity.newInstance(getContext()));
                        scanQrDialogx.dismiss();
                        break;
                    case R.id.tvBarCode:
                        getContext().startActivity(PaymentCodeActivity.newIntent(getContext()));
                        scanQrDialogx.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
        new XPopup.Builder(getCurContext()).atView(mIvRightIcon).asCustom(scanQrDialogx).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewsEvent(NewsEvent event) {
        if (typeList == null || typeList.isEmpty())
            mPresenter.getNewsTypes();
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
