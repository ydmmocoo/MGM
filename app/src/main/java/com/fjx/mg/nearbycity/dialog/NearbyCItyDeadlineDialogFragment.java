package com.fjx.mg.nearbycity.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.nearbycity.adapter.DeadlinePopAdapter;
import com.library.common.constant.IntentConstants;
import com.library.repository.models.NearbyCityConfigModel;
import com.library.repository.models.NearbyCityExpListModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author    by hanlz
 * Date      on 2019/10/17.
 * Description：
 */
public class NearbyCItyDeadlineDialogFragment extends DialogFragment {

    private NearbyCityConfigModel mModel;
    @BindView(R.id.rvPublisherPopLayout)
    RecyclerView mRvPublisherPopLayout;
    private DeadlinePopAdapter mAdapter;

    public static NearbyCItyDeadlineDialogFragment getInstance(NearbyCityConfigModel model) {
        NearbyCItyDeadlineDialogFragment fragment = new NearbyCItyDeadlineDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(IntentConstants.CONFIG, model);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments == null) {
            return;
        }
        mModel = (NearbyCityConfigModel) arguments.getParcelable(IntentConstants.CONFIG);
    }

    @Override
    public void onStart() {
        super.onStart();
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window win = getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.BOTTOM;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pop_nearby_city_publisher_type, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mAdapter = new DeadlinePopAdapter();
        mRvPublisherPopLayout.setAdapter(mAdapter);
        mAdapter.setList(mModel.getExpList());
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if (expListener != null) {
                    expListener.ExpData((NearbyCityExpListModel) adapter.getItem(position));
                }
                dismiss();
            }
        });

    }


    public interface OnExpListener {
        void ExpData(NearbyCityExpListModel model);
    }

    private OnExpListener expListener;

    public void setOnExpListener(OnExpListener onExpListener) {
        expListener = onExpListener;
    }

}
