package com.fjx.mg.job.publish;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.SoftInputUtil;
import com.library.repository.data.UserCenter;
import com.library.repository.models.HouseConfigModel;
import com.library.repository.models.JobConfigModel;
import com.library.repository.models.JobModel;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class JobPublicActivity extends BaseMvpActivity<JobPublicPresenter> implements JobPublicContract.View {
    public static final int TYPE_QZ = 2;
    public static final int TYPE_ZP = 1;


    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.etTitle)
    EditText etTitle;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvJobType)
    TextView tvJobType;
    @BindView(R.id.tvGender)
    TextView tvGender;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.tvMgCode)
    TextView tvMgCode;
    @BindView(R.id.etDesc)
    EditText etDesc;
    @BindView(R.id.tvEducation)
    TextView tvEducation;
    @BindView(R.id.tvWorkYear)
    TextView tvWorkYear;
    @BindView(R.id.tvCommit)
    TextView tvCommit;
    @BindView(R.id.tvTitleHint)
    TextView tvTitleHint;
    @BindView(R.id.tvMoneyHint)
    TextView tvMoneyHint;
    @BindView(R.id.tvExpire)
    TextView tvExpire;

    @BindView(R.id.llGender)
    LinearLayout llGender;
    @BindView(R.id.llAddress)
    LinearLayout llAddress;

    private int mType;
    private String cuntryName, cityName, sex = "3", pay, jobTypeIds, edution, expireId, workYear;
    private String jobId;

    private Map<String, Object> map = new HashMap<>();


    public static Intent newInstance(Context context, int type) {
        Intent intent = new Intent(context, JobPublicActivity.class);
        intent.putExtra("type", type);
        return intent;
    }

    public static Intent newInstance(Context context, int type, String jobId) {
        Intent intent = new Intent(context, JobPublicActivity.class);
        intent.putExtra("jobId", jobId);
        intent.putExtra("type", type);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_job_public;
    }


    @Override
    protected JobPublicPresenter createPresenter() {
        return new JobPublicPresenter(this);
    }

    @Override
    protected void initView() {
        mType = getIntent().getIntExtra("type", TYPE_QZ);
        jobId = getIntent().getStringExtra("jobId");
        GradientDrawableHelper.whit(tvCommit).setColor(R.color.colorAccent).setCornerRadius(50);
        ToolBarManager.with(this).setTitle(getString(R.string.spublish));
        initShow();
        mPresenter.getConfig();
        mPresenter.getJobDetail(jobId);
        tvMgCode.setText(UserCenter.getUserInfo().getIdentifier());

        setDefaultData();
    }

    private void setDefaultData() {
        if (!TextUtils.isEmpty(jobId)) return;
        JobConfigModel.CountryListBean countryBean = (JobConfigModel.CountryListBean) mPresenter.getDefaultData("cuntryName");
        if (countryBean != null) {
            cuntryName = countryBean.getCountryName();
            cityName = countryBean.getCityList().get(0).getCityName();
            tvAddress.setText(cuntryName.concat(cityName));
        }

        JobConfigModel.JobTypesBean jobTypesBean = (JobConfigModel.JobTypesBean) mPresenter.getDefaultData("jobTypeIds");
        if (jobTypesBean != null) {
            tvJobType.setText(jobTypesBean.getName());
            jobTypeIds = jobTypesBean.getTypeId();
        }
        JobConfigModel.EducationConfBean edutionBean = (JobConfigModel.EducationConfBean) mPresenter.getDefaultData("edution");
        if (edutionBean != null) {
            tvEducation.setText(edutionBean.getName());
            this.edution = edutionBean.getEId();
        }
        JobConfigModel.ExpireTypeConfBean expireBean = (JobConfigModel.ExpireTypeConfBean) mPresenter.getDefaultData("expireId");
        if (expireBean != null) {
            tvExpire.setText(expireBean.getExpireType());
            expireId = expireBean.getEId();
        }
        JobConfigModel.PayTypeBean payTypeBean = (JobConfigModel.PayTypeBean) mPresenter.getDefaultData("pay");
        if (payTypeBean != null) {
            tvMoney.setText(payTypeBean.getName());
            pay = payTypeBean.getPId();
        }

        JobConfigModel.WorkYearTypeBean workYearBean = (JobConfigModel.WorkYearTypeBean) mPresenter.getDefaultData("workYear");
        if (workYearBean != null) {
            tvWorkYear.setText(workYearBean.getName());
            workYear = workYearBean.getWId();
        }

        etPhone.setText(UserCenter.getUserInfo().getPhone());
    }


    private void initShow() {
        if (mType == TYPE_QZ) {
            tvType.setText(getString(R.string.job_wanted));
            tvTitleHint.setText(getString(R.string.job_wanted_title));
            llGender.setVisibility(View.VISIBLE);
            tvMoneyHint.setText(getString(R.string.expected_salary));
            etDesc.setHint(getString(R.string.job_wanted_describe));
        } else {
            tvType.setText(getString(R.string.recruit));
            tvTitleHint.setText(getString(R.string.recruit_title));
            llGender.setVisibility(View.GONE);
            tvMoneyHint.setText(getString(R.string.budget_salary));
            etDesc.setHint(getString(R.string.post_describe));
        }

        map.put("type", mType);
    }


    @Override
    public void commitSuccess() {
        CommonToast.toast(getString(R.string.spublish_success));
        setResult(11);
        finish();

    }

    @Override
    public void selectGender(HouseConfigModel.SexConfBean sexConfBean) {
        sex = sexConfBean.getSexId();
        tvGender.setText(sexConfBean.getName());
    }

    @Override
    public void selectType(String name, int type) {
        mType = type;
//        tvType.setText(name);
        initShow();
    }

    @Override
    public void selecrtAddress(String countryName, String cityName) {
        this.cuntryName = countryName;
        this.cityName = cityName;
        tvAddress.setText(countryName.concat(cityName));

        map.put("countryName", countryName);
        map.put("cityName", cityName);
    }

    @Override
    public void selectJobType(String typeName, String typeId) {
        this.jobTypeIds = typeId;
        tvJobType.setText(typeName);
        map.put("typeIds", jobTypeIds);
    }

    @Override
    public void selectEducation(String name, String id) {
        edution = id;
        tvEducation.setText(name);
        map.put("education", edution);
    }

    @Override
    public void showJobModel(JobModel jobModel) {
        if (jobModel == null) return;
        cuntryName = jobModel.getCountryName();
        cityName = jobModel.getCityName();
        sex = jobModel.getSexId();
        jobTypeIds = jobModel.getJobTypeId();// mPresenter.getTypeIdByName("jobTypeIds", jobModel.getJobType());
        edution = jobModel.getEducationId();// mPresenter.getTypeIdByName("edution", jobModel.getEducation());
        expireId = mPresenter.getTypeIdByName("expireId", jobModel.getEducation());

        etTitle.setText(jobModel.getTitle());
        etDesc.setText(jobModel.getDesc());
        tvMoney.setText(jobModel.getPay());
        tvWorkYear.setText(jobModel.getWorkYear());
        etPhone.setText(jobModel.getContactPhone());
        tvMgCode.setText(jobModel.getContactWeixin());
        tvGender.setText(jobModel.getSex());
        tvAddress.setText(cuntryName.concat(cityName));
        tvJobType.setText(jobModel.getJobType());
        tvEducation.setText(jobModel.getEducation());
    }

    @Override
    public void selectExpire(String name, String id) {
        tvExpire.setText(name);
        expireId = id;
    }

    @Override
    public void selectPayType(String name, String pId) {
        tvMoney.setText(name);
        pay = pId;
    }

    @Override
    public void selectWorkYear(String name, String wId) {
        tvWorkYear.setText(name);
        workYear = wId;
    }


    @OnClick({R.id.llType, R.id.llGender, R.id.llJobType, R.id.tvCommit, R.id.llAddress, R.id.llEducation,
            R.id.llExpire, R.id.llMoney, R.id.llWorkYear})
    public void onViewClicked(View view) {
        SoftInputUtil.hideSoftInput(this);
        switch (view.getId()) {
            case R.id.llType:
                if (!TextUtils.isEmpty(jobId)) return;
                mPresenter.showTypeDialog();
                break;
            case R.id.llGender:
                mPresenter.showGenderDialog();
                break;
            case R.id.llMoney:
                mPresenter.showMoneyDialog();
                break;
            case R.id.llWorkYear:
                mPresenter.showWorkYearDialog();
                break;

            case R.id.tvCommit:
                String title = etTitle.getText().toString();
                String phone = etPhone.getText().toString();
                String weixin = tvMgCode.getText().toString();
                String desx = etDesc.getText().toString();

                map.put("title", title);
                map.put("phone", phone);
                map.put("jobTypeId", jobTypeIds);
                map.put("weixin", weixin);
                map.put("desc", desx);
                map.put("pay", pay);
                map.put("workYear", workYear);
                map.put("sex", sex);
                map.put("expireType", expireId);


                if (TextUtils.isEmpty(title)) {
                    CommonToast.toast(getString(R.string.hint_input_title));
                    return;
                }
                if (TextUtils.isEmpty(cuntryName) && TextUtils.isEmpty(cityName)) {
                    CommonToast.toast(getString(R.string.hint_input_workarea));
                    return;
                }
                if (TextUtils.isEmpty(jobTypeIds)) {
                    CommonToast.toast(getString(R.string.hint_input_work_type));
                    return;
                }
                if (TextUtils.isEmpty(pay)) {
                    CommonToast.toast(getString(R.string.hint_input_work_pay));
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    CommonToast.toast(getString(R.string.hint_input_contact_phone));
                    return;
                }
                if (TextUtils.isEmpty(weixin)) {
                    CommonToast.toast(getString(R.string.hint_input_mg_code));
                    return;
                }
                if (TextUtils.isEmpty(edution)) {
                    CommonToast.toast(getString(R.string.hint_input_education));
                    return;
                }
                if (TextUtils.isEmpty(workYear)) {
                    CommonToast.toast(getString(R.string.hint_input_work_year));
                    return;
                }
                if (TextUtils.isEmpty(desx)) {
                    CommonToast.toast(getString(R.string.hint_input_work_desc));
                    return;
                }
                if (TextUtils.isEmpty(expireId)) {
                    CommonToast.toast(getString(R.string.hint_input_expire_date));
                    return;
                }


                if (TextUtils.isEmpty(jobId))
                    mPresenter.addJob(title, cuntryName, cityName, jobTypeIds, sex, pay, phone, weixin, desx, "1", workYear, edution, expireId, mType);
                else
                    mPresenter.editJob(title, cuntryName, cityName, jobTypeIds, sex, pay, phone, weixin, desx, "1", workYear, edution, jobId, expireId, mType);
                break;

            case R.id.llJobType:
                mPresenter.showJobTypeDoalog();
                break;

            case R.id.llAddress:
                mPresenter.showAddressDialog();
                break;

            case R.id.llEducation:
                mPresenter.showEducationDialog();
                break;
            case R.id.llExpire:
                mPresenter.showExpireDialog();
                break;
        }
    }
}
