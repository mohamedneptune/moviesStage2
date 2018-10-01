package com.udacity.moviestage2.ui.moviedetails;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.moviestage2.data.model.TraillerModel;
import com.udacity.moviestage2.databinding.ItemTraillerBinding;

import java.util.List;

public class TraillerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private List<TraillerModel> mTraillerModelList;
    private DetailsViewModel mDetailsViewModel;

    public TraillerViewAdapter(Context context, List<TraillerModel> traillerModelList, DetailsViewModel detailsViewModel) {
        mContext = context;
        mDetailsViewModel = detailsViewModel;
        replaceData(traillerModelList);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        ItemTraillerBinding binding =
                ItemTraillerBinding.inflate(layoutInflater, parent, false);
        return new TraillerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final TraillerModel traillerModel = mTraillerModelList.get(position);
        TraillerViewHolder traillerViewHolder = (TraillerViewHolder) holder;

        traillerViewHolder.binding.traillerName.setText(traillerModel.getName());

        traillerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                mDetailsViewModel.getOpenMovieEvent().setValue(position);
            }
        });

        traillerViewHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mTraillerModelList != null ? mTraillerModelList.size() : 0;
    }

    public void replaceData(List<TraillerModel> traillerModelList) {
        mTraillerModelList = traillerModelList;
        notifyDataSetChanged();
    }

    public class TraillerViewHolder extends RecyclerView.ViewHolder {

        private final ItemTraillerBinding binding;

        public TraillerViewHolder(final ItemTraillerBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
