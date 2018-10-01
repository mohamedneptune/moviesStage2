package com.udacity.moviestage2.ui.movieslist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.moviestage2.R;
import com.udacity.moviestage2.common.Constants;
import com.udacity.moviestage2.data.model.MovieModel;

import java.util.List;

public class MainViewAdapter extends RecyclerView.Adapter<MainViewAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private List<MovieModel> mMovieModels;
    private Context mContext;

    // data is passed into the constructor
    MainViewAdapter(Context context, List<MovieModel> movieModels) {
        this.mInflater = LayoutInflater.from(context);
        this.mMovieModels = movieModels;
        mContext = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {
            Picasso.with(mContext)
                    .load(Constants.IMAGE_BASE_URL_185 + mMovieModels.get(position).getPosterPath())
                    //.placeholder(R.mipmap.ic_launcher) // can also be a drawable
                    .error(R.mipmap.ic_launcher) // will be displayed if the image cannot be loaded
                    .into(holder.iconView);
        } catch (Exception e) {
            e.toString();
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mMovieModels.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        ImageView iconView;

        ViewHolder(View itemView) {
            super(itemView);
            iconView = (ImageView) itemView.findViewById(R.id.image_movie);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    MovieModel getItem(int id) {
        return mMovieModels.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


}
