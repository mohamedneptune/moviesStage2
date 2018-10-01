package com.udacity.moviestage2.ui.movieslist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.udacity.moviestage2.R;
import com.udacity.moviestage2.data.database.AppDatabase;
import com.udacity.moviestage2.data.database.MovieEntry;
import com.udacity.moviestage2.data.model.MovieModel;
import com.udacity.moviestage2.data.provider.api.movie.dto.Example;
import com.udacity.moviestage2.data.provider.api.movie.dto.MovieResponse;
import com.udacity.moviestage2.data.provider.api.movie.mapper.MovieMapper;
import com.udacity.moviestage2.databinding.ActivityMainBinding;
import com.udacity.moviestage2.ui.moviedetails.DetailsActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import timber.log.Timber;


public class MainActivity extends AppCompatActivity
        implements MainViewAdapter.ItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private MainViewModel mViewModel;
    private List<MovieModel> mMovieModelList = new LinkedList<>();
    private String mCurrentCategorieMovie;
    private SharedPreferences.Editor mEditorPreference;
    private ActivityMainBinding mBinding;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mBinding.progressBar.setVisibility(View.VISIBLE);

        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mDb = AppDatabase.getInstance(getApplicationContext());

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        mEditorPreference = sharedPreferences.edit();

        mCurrentCategorieMovie = sharedPreferences.getString("current_categorie_movie", "");

        if (savedInstanceState == null) {
            if (!mCurrentCategorieMovie.equals("")) {
                if (mCurrentCategorieMovie.equals("FAVORITE")) {
                    fetchMoviesFromDB();
                } else if (mCurrentCategorieMovie.equals("TOPRATED")) {
                    fetchTopRatedMovies();
                } else {
                    fetchPopularMovies();
                }
            } else {
                fetchPopularMovies();
            }

            setupViewModel();
        } else {
            Timber.i("welcom back");
            if (mCurrentCategorieMovie.equals("FAVORITE")) {
                fetchMoviesFromDB();
            }else{
                setupViewModel();
            }
        }
    }


    private void setupViewModel() {
        // Observe the LiveData object in the ViewModel

        mViewModel.getMovies().observe(this, new Observer<Example>() {
            @Override
            public void onChanged(@Nullable Example moviesModel) {
                Log.d(TAG, "Updating list of movies from LiveData in ViewModel");
                List<MovieResponse> moviesResponses = moviesModel.getMovieResponse();

                //ModelMapper
                MovieMapper movieMapper = new MovieMapper();
                mMovieModelList = new ArrayList<>();
                mMovieModelList = movieMapper.convertListResponseToListModel(moviesResponses);

                setRecyclerAdapter(mMovieModelList);
            }
        });
    }

    private void setRecyclerAdapter(List<MovieModel> movieModelList) {
        int numberOfColumns = 2;
        mBinding.recyclerMovie.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        MainViewAdapter mainViewAdapter = new MainViewAdapter(this, movieModelList);
        mainViewAdapter.setClickListener(this);
        mBinding.recyclerMovie.setAdapter(mainViewAdapter);
        mBinding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_popular:
                mBinding.progressBar.setVisibility(View.VISIBLE);
                fetchPopularMovies();
                setupViewModel();
                return true;

            case R.id.menu_top_rated:
                mBinding.progressBar.setVisibility(View.VISIBLE);
                fetchTopRatedMovies();
                setupViewModel();
                return true;

            case R.id.menu_favorite:
                mBinding.progressBar.setVisibility(View.VISIBLE);
                fetchMoviesFromDB();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fetchPopularMovies() {
        mViewModel.setmIsMovieFromDB(false);
        mViewModel.getPopularMovies();
        setTitle(getResources().getString(R.string.popular));
        mCurrentCategorieMovie = "POPULAR";
        setSharedPreferenceCategorieValue();
    }

    private void fetchTopRatedMovies() {
        mViewModel.setmIsMovieFromDB(false);
        mViewModel.getTopRatedMovies();
        setTitle(getResources().getString(R.string.top_rated));
        mCurrentCategorieMovie = "TOPRATED";
        setSharedPreferenceCategorieValue();
    }

    private void fetchMoviesFromDB() {
        List<MovieEntry> movieEntries = mDb.movieDao().loadAllMovie();
        //ModelMapper
        MovieMapper movieMapper = new MovieMapper();
        mMovieModelList = new ArrayList<>();
        mMovieModelList = movieMapper.convertEntryListToModels(movieEntries);
        setRecyclerAdapter(mMovieModelList);
        mViewModel.setmIsMovieFromDB(true);
        setTitle(getResources().getString(R.string.favorite));
        mCurrentCategorieMovie = "FAVORITE";
        setSharedPreferenceCategorieValue();
    }

    private void setSharedPreferenceCategorieValue() {
        mEditorPreference.putString("current_categorie_movie", mCurrentCategorieMovie);
        mEditorPreference.apply();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        mEditorPreference.putBoolean("is_movie_from_db", mViewModel.ismIsMovieFromDB());
        mEditorPreference.putInt("selected_movie_id", mMovieModelList.get(position).getId());
        mEditorPreference.apply();
        startActivity(intent);
    }
}
