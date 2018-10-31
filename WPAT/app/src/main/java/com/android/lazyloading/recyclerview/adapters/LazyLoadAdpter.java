package com.android.lazyloading.recyclerview.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.lazyloading.recyclerview.R;
import com.android.lazyloading.recyclerview.models.Row;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;


/**
 * Adapter class for list item
 */
public class LazyLoadAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private OnLoadMoreListener loadMoreListener;
    private boolean isLoading = false, isMoreDataAvailable = true;
    private Context mContext;
    private List<Row> mListRows;

    public LazyLoadAdpter(Context mContext, List<Row> rows) {
        this.mContext = mContext;
        this.mListRows = rows;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new ExerciseHolder(inflater.inflate(R.layout.activity_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((ExerciseHolder) holder).bindData(mListRows, position);

        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

    }

    @Override
    public int getItemViewType(int position) {

        return 0;
    }

    @Override
    public int getItemCount() {
        return mListRows.size();
    }

    /* VIEW HOLDERS class for caching*/

    /**
     * used to check if more data available
     *
     * @param moreDataAvailable
     */
    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    /* notifyDataSetChanged is final method so we can't override it
         call adapter.notifyDataChanged(); after update the list
         */
    public final void notifyDataChanged() {
        notifyDataSetChanged();
        isLoading = false;
    }

    /**
     * set load more  from activity
     *
     * @param loadMoreListener loadMoreListener
     */
    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    /**
     * interface for load more pagination
     */
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    private class ExerciseHolder extends RecyclerView.ViewHolder {
        TextView mTxtTitle;
        TextView mtxtdescription;
        ImageView mImgView;

        public ExerciseHolder(View itemView) {
            super(itemView);
            mTxtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            mtxtdescription = (TextView) itemView.findViewById(R.id.txtDescription);
            mImgView = (ImageView) itemView.findViewById(R.id.image_view);

        }

        void bindData(List<Row> rows, int position) {
            //If will get null from server will show NA
            if (rows.get(position).getTitle() == null) {
                mTxtTitle.setText(mContext.getResources().getString(R.string.NA));
            } else {
                mTxtTitle.setText(rows.get(position).getTitle());
            }
            if (rows.get(position).getDescription() == null) {
                mtxtdescription.setText(mContext.getResources().getString(R.string.NA));
            } else {
                mtxtdescription.setText(rows.get(position).getDescription());
            }

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.circleCrop();
            requestOptions.placeholder(R.drawable.no_image_icon);
            requestOptions.error(R.drawable.no_image_icon);

            String imgUrl = rows.get(position).getImageHref();
            //Glide lib for image loading
            Glide.with(mContext).load(imgUrl)
                    .apply(requestOptions)
                    .into(mImgView);
        }
    }
}
