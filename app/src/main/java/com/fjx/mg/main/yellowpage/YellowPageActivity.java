package com.fjx.mg.main.yellowpage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.main.yellowpage.publish.YellowPagePublicActivity;
import com.fjx.mg.utils.DialogUtil;
import com.flyco.tablayout.SlidingTabLayout;
import com.library.common.base.BaseFragment;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.PagerAdapter;
import com.library.common.utils.SoftInputUtil;
import com.library.common.utils.StatusBarManager;
import com.library.repository.data.UserCenter;
import com.library.repository.db.model.CompanyTypeModel;
import com.library.repository.models.CompanyListModel;
import com.library.repository.models.CompanyTypeModelV1;
import com.library.repository.models.CountryListModel;

import java.util.List;

import butterknife.BindView;

public class YellowPageActivity extends BaseMvpActivity<YellowPagePresenter> implements YellowPageContract.View {


    @BindView(R.id.ivRightIcon)
    ImageView ivRightIcon;

    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.s_tab)
    SlidingTabLayout sTab;

    private YellowPageTabFragment curSelectFragment;
    private List<BaseFragment> fragments;


    public static Intent newInstance(Context context) {
        return new Intent(context, YellowPageActivity.class);
    }


    @Override
    protected int layoutId() {
        return R.layout.ac_yellow_page;
    }


    @Override
    protected YellowPagePresenter createPresenter() {
        return new YellowPagePresenter(this);
    }

    @Override
    protected void initView() {
        mPresenter.getCompanyTypes();
        StatusBarManager.setLightFontColor(this, R.color.colorRed);
        ToolBarManager.with(this).setNavigationIcon(R.drawable.iv_back)
                .setTitle(getString(R.string.yellow_page), R.color.white)
                .setBackgroundColor(R.color.colorRed)
                .setRightImage(R.drawable.ic_yp_right_white, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if (UserCenter.needLogin()) return;
//                        if (RankPermissionHelper.NoPrivileges(2)) return;
                        if (UserCenter.hasLogin()) {
                            startActivityForResult(YellowPagePublicActivity.newInstance(getCurContext()), 11);
                        } else {
                            new DialogUtil().showAlertDialog(getCurActivity(), R.string.tips, R.string.not_login_forward_login, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    UserCenter.goLoginActivity();
                                }
                            });
                        }
                    }
                })
        ;
        ivRightIcon.setVisibility(View.GONE);


        //软键盘的搜索点击事件
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    refreshView.doRefresh();
                    curSelectFragment.doRefresh();
                    SoftInputUtil.hideSoftInput(getCurActivity());
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void showCompanyList(CompanyListModel data) {
        // refreshView.noticeAdapterData(mAdapter, data.getCompanyList(), data.isHasNext());
    }

    @Override
    public void loadDataError() {
        // refreshView.finishLoading();
    }

    @Override
    public void showTabsAndFragment(List<CompanyTypeModel> data, String[] titles, final List<BaseFragment> fragments) {
        this.fragments = fragments;
        curSelectFragment = (YellowPageTabFragment) fragments.get(0);
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), fragments));
        sTab.setTabSpaceEqual(titles.length < 4);
        sTab.setViewPager(viewPager, titles);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                curSelectFragment = (YellowPageTabFragment) fragments.get(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void showSelectCountry(CountryListModel model) {

    }

    @Override
    public void showSelectCity(CountryListModel.CityListBean model) {

    }

    @Override
    public void showSelectService(CompanyTypeModelV1 modelV1) {

    }

    @Override
    public void showSelectSecondService(CompanyTypeModelV1.SecondListBean model) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode)
            for (BaseFragment fragment : fragments) {
                fragment.onActivityResult(requestCode, resultCode, data);

            }
    }

    public String getSearchTitle() {
        return etSearch.getText().toString();
    }
}
