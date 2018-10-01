package com.udacity.moviestage2.ui.reviewslist;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.udacity.moviestage2.R;
import com.udacity.moviestage2.data.model.ReviewModel;
import com.udacity.moviestage2.databinding.ItemReviewBinding;

import java.util.List;

public class ReviewsViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ReviewModel> mReviewModelList;
    private Context mContext;

    public ReviewsViewAdapter(Context context, List<ReviewModel> reviewModelList) {
        mReviewModelList = reviewModelList;
        mContext = context;
        replaceData(mReviewModelList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        ItemReviewBinding binding =
                ItemReviewBinding.inflate(layoutInflater, parent, false);
        return new ReviewViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final ReviewModel reviewModel = mReviewModelList.get(position);
        ReviewViewHolder reviewViewHolder = (ReviewViewHolder) holder;

        reviewViewHolder.binding.reviewAuthor.setText(mContext.getString(R.string.author_label)+ " "+ reviewModel.getAuthor());
        reviewViewHolder.binding.reviewContent.setText(reviewModel.getContent());

        reviewViewHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mReviewModelList != null ? mReviewModelList.size() : 0;
    }

    public void replaceData(List<ReviewModel> reviewModelList) {
        mReviewModelList = reviewModelList;
        notifyDataSetChanged();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        private final ItemReviewBinding binding;

        public ReviewViewHolder(final ItemReviewBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}