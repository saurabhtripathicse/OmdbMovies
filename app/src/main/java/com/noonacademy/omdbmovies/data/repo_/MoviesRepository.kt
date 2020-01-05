package com.noonacademy.omdbmovies.data.repo_

import com.noonacademy.omdbmovies.constant_.AppConstants.API_KEY
import com.noonacademy.omdbmovies.constant_.AppConstants.PLOT
import com.noonacademy.omdbmovies.data.local_.deo.BookMarkDao
import com.noonacademy.omdbmovies.data.local_.entity.BookMarkListModel
import com.noonacademy.omdbmovies.data.model.details.MovieDetailModel
import com.noonacademy.omdbmovies.data.model.search.MoviesDataModel
import com.noonacademy.omdbmovies.data.retrofit_.ApiService
import io.reactivex.Flowable
import io.reactivex.Observable
import javax.inject.Singleton

@Singleton
class MoviesRepository (private val bookMarkDao: BookMarkDao,
                        private val apiService: ApiService
){

    fun getBookMark() : Flowable<List<BookMarkListModel>>{
        return bookMarkDao.getBookMarkList().map { data -> data }
    }

    fun insertData(data : BookMarkListModel){
        bookMarkDao.insertBookMark(data)
    }

    fun checkData(id: String): Flowable<Int>{
        return bookMarkDao.checkDataCount(id)
    }

    fun deleteData(data : BookMarkListModel){
        bookMarkDao.delete(data)
    }

    fun callMoviesList(search: String, page: Int) : Observable<MoviesDataModel>{
        return apiService.fetchMovies(search, page, API_KEY)
    }

    fun callMoviesDetail(imdbID: String) : Observable<MovieDetailModel>{
        return apiService.fetchMoviesDetails(imdbID, PLOT, API_KEY)
    }
}


