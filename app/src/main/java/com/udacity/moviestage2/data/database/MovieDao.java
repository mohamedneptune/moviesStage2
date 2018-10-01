package com.udacity.moviestage2.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie")
    List<MovieEntry> loadAllMovie();

    @Query("SELECT * FROM movie WHERE id =:movieId")
    MovieEntry fetchMoviebyMovieId(int movieId);

    @Insert
    void insertTask(MovieEntry taskEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(MovieEntry taskEntry);

    @Delete
    void deleteMovie(MovieEntry taskEntry);
}
