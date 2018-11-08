package com.android.lazyloading.recyclerview.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.lazyloading.recyclerview.R;
import com.android.lazyloading.recyclerview.models.Row;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;


/**
 * Adapter class for list item
 */
public class LazyLoadAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
    }

    @Override
    public int getItemViewType(int position) {

        return 0;
    }

    @Override
    public int getItemCount() {
        return mListRows.size();
    }


    private class ExerciseHolder extends RecyclerView.ViewHolder {
        TextView mTxtTitle;
        TextView mtxtdescription;
        ImageView mImgView;

        public ExerciseHolder(View itemView) {
            super(itemView);
            mTxtTitle = itemView.findViewById(R.id.txtTitle);
            mtxtdescription = itemView.findViewById(R.id.txtDescription);
            mImgView = itemView.findViewById(R.id.image_view);

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
            String imgUrl = rows.get(position).getImageHref();
            //Glide lib for image loading
            Glide.with(mContext).load(imgUrl)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            mImgView.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            mImgView.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .apply(requestOptions)
                    .into(mImgView);
        }
    }
}
