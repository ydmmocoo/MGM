package com.fjx.mg.friend.chat.redpacket;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fjx.mg.R;
import com.library.common.base.BaseMvpActivity;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.CommonImageLoader;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.models.GroupRedPacketDetailModel;
import com.library.repository.models.ReciveRedRrecordModel;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author    by hanlz
 * Date      on 2019/12/8.
 * Description：
 */
public class GroupRedPacketDetailActivity extends BaseMvpActivity<GroupRedPacketDetailPresenter> implements GroupRedPacketDetailContact.View {

    private GroupRedPacketDetailAdapter mAdapter;
    @BindView(R.id.rvDetail)
    RecyclerView mRvDetail;
    @BindView(R.id.ivHeaderIcon)
    CircleImageView mIvHeaderIcon;
    @BindView(R.id.tvUserName)
    TextView mTvUserName;
    @BindView(R.id.tvRemark)
    TextView mTvRemark;
    @BindView(R.id.tvRedPacketMoney)
    TextView mTvRedPacketMoney;
    @BindView(R.id.tvCollectionsNum)
    TextView mTvCollectionsNum;
    @BindView(R.id.refreshView)
    CustomRefreshView mRefreshView;


    public static Intent newIntent(Context context, String groupId, String rId) {
        Intent intent = new Intent(context, GroupRedPacketDetailActivity.class);
        intent.putExtra(IntentConstants.GROUP_ID, groupId);
        intent.putExtra(IntentConstants.RID, rId);
        return intent;
    }


    @Override
    protected GroupRedPacketDetailPresenter createPresenter() {
        return new GroupRedPacketDetailPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.act_group_red_packet_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        if (getIntent() == null) {
            return;
        }
        mAdapter = new GroupRedPacketDetailAdapter();
        mRvDetail.setAdapter(mAdapter);
        mRefreshView.setEnableRefresh(false);

        String rId = getIntent().getStringExtra(IntentConstants.RID);
        mPresenter.requestRedPacketDetail(rId);
        mPresenter.requestReciveRedRrecord(rId, "1");
    }

    @OnClick(R.id.ivBack)
    public void back() {
        finish();
    }

    @Override
    public void responseRedPacketDetail(GroupRedPacketDetailModel data) {
        mTvRedPacketMoney.setText(data.getPrice());
        CommonImageLoader.load(data.getAvatar()).placeholder(R.drawable.food_default).into(mIvHeaderIcon);
        mTvUserName.setText(data.getNickName());
        mTvRemark.setText(data.getRemark());
        String v1 = data.getReciveNum().concat("/").concat(data.getTotalNum());
        String v2 = data.getRecivePrice().concat("/").concat(data.getTotalPrice());
        mTvCollectionsNum.setText(String.format(getString(R.string.collections_num_tips), v1, v2));
    }

    @Override
    public void responseReciveRedRrecord(ReciveRedRrecordModel data) {
        mRefreshView.noticeAdapterData(mAdapter, data.getReciveList(), data.isHasNext());//考虑实际，不显示无更多数据，hasNext=true
        mRvDetail.invalidateItemDecorations();
    }
}
