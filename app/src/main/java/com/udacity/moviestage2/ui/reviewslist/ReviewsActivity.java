package com.udacity.moviestage2.ui.reviewslist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.udacity.moviestage2.R;
import com.udacity.moviestage2.data.database.AppDatabase;
import com.udacity.moviestage2.data.database.MovieEntry;
import com.udacity.moviestage2.data.database.ReviewEntry;
import com.udacity.moviestage2.data.model.MovieModel;
import com.udacity.moviestage2.data.model.ReviewModel;
import com.udacity.moviestage2.data.provider.api.movie.dto.ReviewListResponse;
import com.udacity.moviestage2.data.provider.api.movie.mapper.MovieMapper;
import com.udacity.moviestage2.databinding.ActivityReviewsBinding;

import java.util.List;

import timber.log.Timber;

public class ReviewsActivity extends AppCompatActivity {

    private ActivityReviewsBinding mBinding;
    private ReviewsViewModel mReviewsViewModel;
    private MovieModel mMovieModel;
    private List<ReviewModel> mReviewListModel;
    public static final String MOVIE_ID = "movie_id";
    private static final int DEFAULT_SELECTED_MOVIE = -1;
    private int mSelectedMovieId = 0;
    // Member variable for the Database
    private AppDatabase mDb;
    private MovieMapper mMovieMapper;
    private Boolean mIsFromDB = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_reviews);

        mDb = AppDatabase.getInstance(getApplicationContext());

        //get selected id from sharedPreference
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        mSelectedMovieId = sharedPreferences.getInt("selected_movie_id", DEFAULT_SELECTED_MOVIE);
        mIsFromDB = sharedPreferences.getBoolean("is_movie_from_db", false);

        //Mapper
        mMovieMapper = new MovieMapper();

        setTitle(getString(R.string.reviews));

        mReviewsViewModel = ViewModelProviders.of(this).get(ReviewsViewModel.class);


        if (savedInstanceState == null) {
            if (mIsFromDB) {
                //fetsh movie reviwers from db
                fetchMovieInDB();
                List<ReviewModel> reviewModelList = mMovieModel.getReviewModelList();
                if (reviewModelList != null) {
                    if (!reviewModelList.isEmpty()) {
                        Timber.i("reviewModelList size: " + reviewModelList.size());
                        showDetails(reviewModelList);
                        mReviewsViewModel.setReviewModelList(reviewModelList);
                    }
                } else {
                    Timber.i("0 reviewers in db");
                    //fetch movie reviews from server
                    mReviewsViewModel.getReviewsMovieById(mSelectedMovieId);
                }
            } else {
                //fetch movie reviews from server
                mReviewsViewModel.getReviewsMovieById(mSelectedMovieId);
            }
        } else {
            Timber.i("welcom back");
        }

        setupViewModel();

    }

    private void setupViewModel() {
        // Observe the LiveData Reviewers in the ViewModel
        mReviewsViewModel.getReviewList().observe(this, new Observer<ReviewListResponse>() {
            @Override
            public void onChanged(@Nullable ReviewListResponse reviewListResponse) {
                //ModelMapper
                mReviewListModel = mMovieMapper.convertListReviewResponseToModels(reviewListResponse);
                Timber.i("mReviewListModel size: " + mReviewListModel.size());
                showDetails(mReviewListModel);

            }
        });

        if (mIsFromDB) {
            //retrive reviews from bd
            fetchMovieInDB();
            List<ReviewModel> reviewModelList = mMovieModel.getReviewModelList();
            if (reviewModelList != null) {
                if (!reviewModelList.isEmpty()) {
                    Timber.i("reviewModelList size: " + reviewModelList.size());
                    showDetails(reviewModelList);
                }
            }
        }
    }


    private void showDetails(List<ReviewModel> reviewModelList) {
        ReviewsViewAdapter reviewsViewAdapter;

        RecyclerView recyclerView = mBinding.recyclerReviewsList;
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        reviewsViewAdapter = new ReviewsViewAdapter(this, reviewModelList);
        recyclerView.setAdapter(reviewsViewAdapter);
    }


    public void fetchMovieInDB() {
        MovieEntry movieEntry = mDb.movieDao().fetchMoviebyMovieId(mSelectedMovieId);

        mMovieModel = mMovieMapper.convertEntryToModel(movieEntry);
    }

    private void updateMovie(List<ReviewEntry> reviewEntries) {
        MovieEntry taskEntry = new MovieEntry(
                mMovieModel.getId(),
                mMovieModel.getPosterPath(),
                mMovieModel.getOverview(),
                mMovieModel.getReleaseDate(),
                mMovieModel.getOriginalTitle(),
                mMovieModel.getBackdropPath(),
                mMovieModel.getVoteCount(),
                mMovieModel.getVideo(),
                mMovieModel.getVoteAverage(),
                reviewEntries);
        mDb.movieDao().updateMovie(taskEntry);
    }
}

