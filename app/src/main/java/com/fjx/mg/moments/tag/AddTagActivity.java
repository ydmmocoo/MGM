package com.fjx.mg.moments.tag;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.ContextManager;
import com.library.common.utils.SoftInputUtil;
import com.library.common.utils.StatusBarManager;
import com.library.common.utils.StringUtil;
import com.library.repository.data.UserCenter;
import com.library.repository.models.TagModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddTagActivity extends BaseMvpActivity<AddTagPresenter> implements AddTagContract.View {

    @BindView(R.id.etComment)
    EditText etComment;

    @BindView(R.id.recyclerType2)
    RecyclerView recyclertype1;

    @BindView(R.id.viewComment)
    View viewComment;

    private TagAdapter typeAdapter1;
    private List<TagModel.TagsBean> tags = new ArrayList<>();

    public static Intent newInstance(Context context) {
        return new Intent(context, AddTagActivity.class);
    }


    @Override
    protected int layoutId() {
        return R.layout.activity_add_tag;
    }

    @Override
    protected void initView() {

        StatusBarManager.setLightFontColor(this, R.color.colorRed);

        ToolBarManager.with(this).setRightText(getString(R.string.confirm), R.color.colorAccent, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer tags = new StringBuffer();
                StringBuffer tagIds = new StringBuffer();
                List<TagModel.TagsBean> data = typeAdapter1.getData();
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getSelected()) {
                        tags.append(data.get(i).getName() + ",");
                        tagIds.append(data.get(i).gettId() + ",");
                    }
                }
                mPresenter.addTag(tags.toString(), tagIds.toString());
            }
        });
        if (StringUtil.isNotEmpty(UserCenter.getUserInfo().getUSex())) {
            mPresenter.getTags(UserCenter.getUserInfo().getUSex().equals("男") ? "1" : "2");
        } else {
            mPresenter.getTags("1");
        }
        typeAdapter1 = new TagAdapter();//分类列表
        FlexboxLayoutManager manager = new FlexboxLayoutManager(this);
        recyclertype1.setLayoutManager(manager);
        recyclertype1.setAdapter(typeAdapter1);
        manager.setFlexWrap(FlexWrap.WRAP);
        manager.setFlexDirection(FlexDirection.ROW);
        typeAdapter1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                List<TagModel.TagsBean> data = typeAdapter1.getData();
                data.get(position).setSelected(!data.get(position).getSelected());
                typeAdapter1.setNewInstance(data);
            }
        });
    }

    @OnClick(R.id.tvAdd)
    public void onViewClicked() {//自定义标签
        updateEditTextBodyVisible(View.VISIBLE);
    }

    @OnClick(R.id.tvSend)
    public void onViewSend() {
        TagModel.TagsBean tagsBean = new TagModel.TagsBean();
        String name = etComment.getText().toString();
        if (name.equals("")) {
            return;
        } else {
            updateEditTextBodyVisible(View.GONE);
        }

        if (name.getBytes().length > 15) {
            Toast.makeText(this, getString(R.string.string_length_is_too_long), Toast.LENGTH_SHORT).show();
            return;
        }
        tagsBean.setName(name);
        tagsBean.settId("0");
        tags.add(tagsBean);
        typeAdapter1.setList(tags);
    }


    @Override
    protected AddTagPresenter createPresenter() {
        return new AddTagPresenter(this);
    }

    @Override
    public void ShowTag(TagModel data) {
        tags = data.getTags();
        typeAdapter1.setList(tags);
    }

    @Override
    public void AddTagSuccess() {
        setResult(101);
        finish();
    }

    public void updateEditTextBodyVisible(int visibility) {
        viewComment.setVisibility(visibility);
        if (View.VISIBLE == visibility) {
            etComment.requestFocus();//弹出键盘
            SoftInputUtil.showSoftInput(getCurActivity(), etComment);

        } else if (View.GONE == visibility) {//隐藏键盘
            etComment.setText("");
            SoftInputUtil.hideSoftInput(getCurActivity(), etComment);
        }
    }
}
