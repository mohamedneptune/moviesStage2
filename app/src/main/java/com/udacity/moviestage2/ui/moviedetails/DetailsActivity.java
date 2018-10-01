package com.udacity.moviestage2.ui.moviedetails;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.udacity.moviestage2.R;
import com.udacity.moviestage2.common.Constants;
import com.udacity.moviestage2.data.database.AppDatabase;
import com.udacity.moviestage2.data.database.MovieEntry;
import com.udacity.moviestage2.data.model.MovieModel;
import com.udacity.moviestage2.data.model.TraillerModel;
import com.udacity.moviestage2.data.provider.api.movie.dto.MovieResponse;
import com.udacity.moviestage2.data.provider.api.movie.dto.TraillerListResponse;
import com.udacity.moviestage2.data.provider.api.movie.mapper.MovieMapper;
import com.udacity.moviestage2.databinding.ActivityDetailsBinding;
import com.udacity.moviestage2.ui.reviewslist.ReviewsActivity;

import java.util.List;

import timber.log.Timber;


public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityDetailsBinding mBinding;
    private DetailsViewModel mDetailsViewModel;
    private MovieModel mMovieModel;
    private List<TraillerModel> mTraillerListModel;
    public static final String SELECTED_MOVIE_ID = "selected_movie_id";
    private static final int DEFAULT_SELECTED_MOVIE = -1;
    private int mSelectedMovieId = 0;
    // Member variable for the Database
    private AppDatabase mDb;
    private boolean mMovieInDB;
    private MovieMapper mMovieMapper;
    private SharedPreferences.Editor mEditorPreference;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        mDb = AppDatabase.getInstance(getApplicationContext());
        mMovieInDB = false;
        //Mapper
        mMovieMapper = new MovieMapper();

        setTitle(getString(R.string.movie_details));

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        mEditorPreference = sharedPreferences.edit();

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        //get selected id from sharedPreference
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        mSelectedMovieId = sharedPreferences.getInt("selected_movie_id", DEFAULT_SELECTED_MOVIE);
        mMovieInDB = sharedPreferences.getBoolean("is_movie_from_db", false);

        mDetailsViewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);

        if (mSelectedMovieId == DEFAULT_SELECTED_MOVIE) {
            closeOnError();
            return;
        } else {

            if (savedInstanceState == null) {
                if (mMovieInDB) {
                    //Get value from DB
                    Timber.i("Fetch value from DB");
                    fetchMovieInDB(mSelectedMovieId);
                } else {
                    //fetch movie details from server
                    Timber.i("Fetch value from Server");
                    mDetailsViewModel.getMovieById(mSelectedMovieId);
                    //fetch Traillers from server
                    mDetailsViewModel.getTraillersMovieById(mSelectedMovieId);
                }

            } else {
                Timber.i("welcom back");
                if(mMovieInDB){
                    fetchMovieInDB(mSelectedMovieId);
                }
            }


            setupViewModel();
        }

        // Subscribe to "click trailler" event
        mDetailsViewModel.getOpenMovieEvent().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer position) {
                if (position != null) {
                    playTraillerVideo(mTraillerListModel.get(position).getKey());
                }
            }
        });

        mBinding.reviewsBtn.setOnClickListener(this);
    }

    private void setupViewModel() {
        // Observe the LiveData Movies in the ViewModel
        mDetailsViewModel.getMovie().observe(this, new Observer<MovieResponse>() {
            @Override
            public void onChanged(@Nullable MovieResponse movieResponse) {
                //ModelMapper
                mMovieModel = mMovieMapper.convertResponseToModel(movieResponse);
                showDetails(mMovieModel);
            }
        });

        // Observe the LiveData Traillers in the ViewModel
        mDetailsViewModel.getTraillerList().observe(this, new Observer<TraillerListResponse>() {
            @Override
            public void onChanged(@Nullable TraillerListResponse traillerListResponse) {
                //ModelMapper
                mTraillerListModel = mMovieMapper.convertListTraillerResponseToModels(traillerListResponse);
                showTraillersDetails(mTraillerListModel);
            }
        });

    }

    private void setupTraillerListAdapter() {
        RecyclerView recyclerView = mBinding.recyclerTraillerList;
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        TraillerViewAdapter traillerViewAdapter = new TraillerViewAdapter(this, mTraillerListModel, mDetailsViewModel);
        recyclerView.setAdapter(traillerViewAdapter);
    }


    public void fetchMovieInDB(int movieId) {
        MovieEntry movieEntry = mDb.movieDao().fetchMoviebyMovieId(movieId);
        if (null != movieEntry) {
            Timber.i("movie exist in db");
            mMovieInDB = true;
            //Convert movieEntry to model
            mMovieModel = mMovieMapper.convertEntryToModel(movieEntry);
            showDetails(mMovieModel);
        } else {
            Timber.i("movie does not exist");
            mMovieInDB = false;
        }

    }

    private void setMenuIcon() {
        if (mMovieInDB) {
            mMenu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.star_full));
        } else {
            mMenu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.star_empty));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        mMenu = menu;
        setMenuIcon();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_favorite:
                if (mMovieInDB) {
                    deleteMovieFromDB();
                    mMenu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.star_empty));
                    mMovieInDB = false;
                    mEditorPreference.putBoolean("is_movie_from_db", mMovieInDB);
                    mEditorPreference.apply();
                } else {
                    addMovieToDB();
                    mMenu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.star_full));
                    mMovieInDB = true;
                    mEditorPreference.putBoolean("is_movie_from_db", mMovieInDB);
                    mEditorPreference.apply();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteMovieFromDB() {
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
                null);
        mDb.movieDao().deleteMovie(taskEntry);
    }

    private void addMovieToDB() {
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
                null);
        mDb.movieDao().insertTask(taskEntry);
    }

    private void showTraillersDetails(List<TraillerModel> traillerModelList) {
        Timber.i("nbr trailler: " + traillerModelList.size());
        setupTraillerListAdapter();
    }

    private void showDetails(MovieModel movieModel) {

        try {
            Picasso.with(DetailsActivity.this)
                    .load(Constants.IMAGE_BASE_URL_780 + movieModel.getBackdropPath())
                    .placeholder(R.mipmap.ic_launcher) // can also be a drawable
                    .error(R.mipmap.ic_launcher) // will be displayed if the image cannot be loaded
                    .into(mBinding.movieBackdropImage);
        } catch (Exception e) {
            e.toString();
        }

        mBinding.movieTitle.setText(movieModel.getOriginalTitle());
        mBinding.movieReleaseDate.setText(movieModel.getReleaseDate());
        mBinding.movieOversView.setText(movieModel.getOverview());
        Double voteAverage = movieModel.getVoteAverage();
        mBinding.movieVoteAverage.setRating(voteAverage.floatValue() / 2);
    }

    private void playTraillerVideo(String movieKey) {
        Uri uri = Uri.parse("https://www.youtube.com/watch?v=" + movieKey);
        uri = Uri.parse("vnd.youtube:" + uri.getQueryParameter("v"));
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    private void closeOnError() {
        finish();
    }

    @Override
    public void onClick(View v) {

        if (v == mBinding.reviewsBtn) {

            mEditorPreference.putInt("selected_movie_id", mSelectedMovieId);
            mEditorPreference.apply();

            Intent intent = new Intent(DetailsActivity.this, ReviewsActivity.class);
            intent.putExtra(ReviewsActivity.MOVIE_ID, mSelectedMovieId);
            startActivity(intent);
        }
    }
}
