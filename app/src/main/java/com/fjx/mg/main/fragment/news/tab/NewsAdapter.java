package com.fjx.mg.main.fragment.news.tab;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.fjx.mg.news.detail.NewsDetailActivity;
import com.library.common.base.adapter.BaseMulAdapter;
import com.library.common.utils.CommonImageLoader;
import com.library.repository.models.NewsListModel;

public class NewsAdapter extends BaseMulAdapter<NewsListModel> {

    public NewsAdapter() {
        super();
        addItemType(NewsListModel.BIG_SINGLE_IMAGE, R.layout.item_news_bigimage);
        addItemType(NewsListModel.SMALL_SINGLE_IMAGE, R.layout.item_news_smallimage);
        addItemType(NewsListModel.MUL_IMAGE, R.layout.item_news_mulimage);
    }

    @Override
    protected void convert(BaseViewHolder helper, final NewsListModel item) {

        helper.setText(R.id.tvNewsTitle, item.getNewsTitle());
        helper.setText(R.id.tvUserName, item.getNewsAuth());
        helper.setText(R.id.tvCommentNum, item.getReadNum() + getContext().getString(R.string.Read));
        helper.setText(R.id.tvCommentTime, item.getPublishTime());
        ImageView ivImage = helper.getView(R.id.ivImage);
        if (!item.getImgs().isEmpty())
            CommonImageLoader.load(item.getImgs().get(0)).placeholder(R.drawable.food_default).into(ivImage);
        switch (helper.getItemViewType()) {
            case NewsListModel.MUL_IMAGE:
                RecyclerView imageRecycler = helper.getView(R.id.imageRecycler);
                imageRecycler.setLayoutManager(new GridLayoutManager(helper.itemView.getContext(), 3));
                ImageAdapter adapter = new ImageAdapter();
                imageRecycler.setAdapter(adapter);
                adapter.setNewInstance(item.getImgs());
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                        view.getContext().startActivity(NewsDetailActivity.newInstance(view.getContext(), item.getNewsId()));
                    }
                });
                break;
            default:
                //Log.d(TAG, "convert: ");
                break;
        }

    }
}
