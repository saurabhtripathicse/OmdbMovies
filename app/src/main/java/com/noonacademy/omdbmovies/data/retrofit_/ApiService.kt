package com.noonacademy.omdbmovies.data.retrofit_

import com.noonacademy.omdbmovies.data.model.details.MovieDetailModel
import com.noonacademy.omdbmovies.data.model.search.MoviesDataModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/")
    fun fetchMovies(@Query("s") Search: String,
                    @Query("page") page: Int,
                    @Query("apikey") key: String): Observable<MoviesDataModel>

    @GET("/")
    fun fetchMoviesDetails(@Query("i") imdbID: String,
                    @Query("plot") plot: String,
                    @Query("apikey") key: String): Observable<MovieDetailModel>

}