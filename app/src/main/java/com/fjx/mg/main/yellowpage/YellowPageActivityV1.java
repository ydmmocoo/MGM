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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fingerprints.service.IFingerprintClient;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.main.yellowpage.detail.YellowPageDetailActivity;
import com.fjx.mg.main.yellowpage.publish.YellowPagePublicActivity;
import com.fjx.mg.utils.DialogUtil;
import com.fjx.mg.utils.RankPermissionHelper;
import com.library.common.base.BaseFragment;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.SoftInputUtil;
import com.library.common.utils.StatusBarManager;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.data.UserCenter;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.db.model.CompanyTypeModel;
import com.library.repository.models.CmpanydetaisModel;
import com.library.repository.models.CompanyDetailModel;
import com.library.repository.models.CompanyListModel;
import com.library.repository.models.CompanyTypeModelV1;
import com.library.repository.models.CountryListModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * description:企业黄页首页
 */
public class YellowPageActivityV1 extends BaseMvpActivity<YellowPagePresenter> implements YellowPageContract.View {

    @BindView(R.id.ivRightIcon)
    ImageView ivRightIcon;

    @BindView(R.id.etSearch)
    EditText etSearch;

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;
    @BindView(R.id.tvProvince)
    TextView tvProvince;
    @BindView(R.id.tvCity)
    TextView tvCity;
    @BindView(R.id.tvFirstSort)
    TextView tvFirstSort;
    @BindView(R.id.tvSecondSort)
    TextView tvSecondSort;
    @BindView(R.id.viewTrans)
    View viewTrans;
    @BindView(R.id.dropMenu)
    View dropMenu;
    private YellowPageAdapter mAdapter;

    private String serviceId, secondServiceId, countryId, cityId;
    private CompanyListModel data;

    public static Intent newInstance(Context context) {
        return new Intent(context, YellowPageActivityV1.class);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_yellow_page_v1;
    }

    @Override
    protected YellowPagePresenter createPresenter() {
        return new YellowPagePresenter(this);
    }

    @Override
    protected void initView() {
        mPresenter.getCompanyTypesV1();
        StatusBarManager.setLightFontColor(this, R.color.colorRed);
        ToolBarManager.with(this).setNavigationIcon(R.drawable.iv_back)
                .setTitle(getString(R.string.yellow_page), R.color.white)
                .setBackgroundColor(R.color.colorRed)
                .setRightImage(R.drawable.ic_yp_right_white, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if (UserCenter.needLogin()) return;
//                        if (RankPermissionHelper.NoPrivileges(2)) return;
                        if (UserCenter.hasLogin()){
                            startActivityForResult(YellowPagePublicActivity.newInstance(getCurContext()), 11);
                        }else {
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
        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                String title = etSearch.getText().toString();
                mPresenter.getCompanyListV1(page, title, serviceId, secondServiceId, countryId, cityId);
            }
        });

        mAdapter = new YellowPageAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.addItemDecoration(new SpacesItemDecoration(1));
        recycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                startActivity(YellowPageDetailActivity.newInstance(getCurContext(), mAdapter.getItem(position).getCId(), "", true,mAdapter.getItem(position).getUId()));
            }
        });

        List<CmpanydetaisModel> dataList = DBDaoFactory.getCompanyListDaos().queryList();
        mAdapter.setList(dataList);

        //软键盘的搜索点击事件
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    refreshView.doRefresh();
                    SoftInputUtil.hideSoftInput(getCurActivity());
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView.autoRefresh();
    }

    @Override
    public void showCompanyList(CompanyListModel data) {
        this.data = data;
        refreshView.noticeAdapterData(mAdapter, data.getCompanyList(), data.isHasNext());
    }

    @Override
    public void loadDataError() {
        refreshView.finishLoading();
    }

    @Override
    public void showTabsAndFragment(List<CompanyTypeModel> data, String[] titles, final List<BaseFragment> fragments) {
    }

    @Override
    public void showSelectCountry(CountryListModel model) {
        countryId = model.getCId();
        tvProvince.setText(model.getCountryName());
    }

    @Override
    public void showSelectCity(CountryListModel.CityListBean model) {
        cityId = model.getCityId();
        tvCity.setText(model.getCityName());
    }

    @Override
    public void showSelectService(CompanyTypeModelV1 modelV1) {
        serviceId = modelV1.getCId();
        tvFirstSort.setText(modelV1.getName());
    }

    @Override
    public void showSelectSecondService(CompanyTypeModelV1.SecondListBean model) {
        secondServiceId = model.getSecondId();
        tvSecondSort.setText(model.getSecondName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode) refreshView.doRefresh();
    }

    @OnClick({R.id.tvProvince, R.id.tvCity, R.id.tvFirstSort, R.id.tvSecondSort, R.id.tvReset, R.id.tvConfirm, R.id.viewTrans,
            R.id.tvSelect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvSelect:
                if (dropMenu.getVisibility() == View.VISIBLE) {
                    dropMenu.setVisibility(View.GONE);
                } else {
                    dropMenu.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tvProvince:
                mPresenter.showProvinceDialog();
                break;
            case R.id.tvCity:
                mPresenter.showCityDialog(countryId);
                break;
            case R.id.tvFirstSort:
                mPresenter.showFirstSortDialog();
                break;
            case R.id.tvSecondSort:
                mPresenter.showSecondSortDialog(serviceId);
                break;
            case R.id.tvReset:
                serviceId = "";
                secondServiceId = "";
                countryId = "";
                cityId = "";
                tvProvince.setText("");
                tvCity.setText("");
                tvFirstSort.setText("");
                tvSecondSort.setText("");
                break;
            case R.id.tvConfirm:
                dropMenu.setVisibility(View.GONE);
                refreshView.doRefresh();
                break;
            case R.id.viewTrans:
                dropMenu.setVisibility(View.GONE);
                break;
        }
    }
}
