package com.noonacademy.omdbmovies.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.noonacademy.omdbmovies.data.local_.deo.BookMarkDao
import com.noonacademy.omdbmovies.data.local_.entity.BookMarkListModel
import com.noonacademy.omdbmovies.data.model.details.MovieDetailModel
import com.noonacademy.omdbmovies.data.repo_.MoviesRepository
import com.noonacademy.omdbmovies.data.retrofit_.ApiService
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailViewModel @Inject
constructor(bookMarkDao: BookMarkDao, apiService: ApiService):
    ViewModel() {

    private val repository: MoviesRepository = MoviesRepository(bookMarkDao, apiService)

    private var compositeDisposable = CompositeDisposable()

    private val loadingLiveData = MutableLiveData<Boolean>()
    private val showErrorRetry = MutableLiveData<Boolean>()

    private val movieDetailModel = MutableLiveData<MovieDetailModel>()

    fun loading() = loadingLiveData
    fun errorRetry() = showErrorRetry
    fun getMovieDetails() = movieDetailModel

    fun getDetailMovies(imdbID: String){

        loadingLiveData.postValue(true)
        showErrorRetry.postValue(false)

        compositeDisposable.add(
            repository.callMoviesDetail(imdbID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    private fun handleResponse(response: MovieDetailModel) {
        loadingLiveData.postValue(false)
        if (response.response) {
            checkBookMark(response.imdbID, response)
        } else {
            showErrorRetry.postValue(true)
        }
    }
    private fun handleError(error: Throwable) {
        loadingLiveData.postValue(false)
        showErrorRetry.postValue(true)
        error.printStackTrace()
    }

    private fun checkBookMark(id: String, response: MovieDetailModel){
        compositeDisposable.add(
            repository.checkData(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        showErrorRetry.postValue(false)
                        response.bookmark = it > 0
                        movieDetailModel.postValue(response)
                    },
                    {
                        loadingLiveData.postValue(false)
                        showErrorRetry.postValue(true)
                    })
        )
    }

    fun insertBookMark(data: BookMarkListModel) {
        compositeDisposable.add(
            Completable.fromAction {
                repository.insertData(data)
            }.subscribeOn(Schedulers.io()).doOnComplete {

            }.subscribe()
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun deleteBookMark(data: BookMarkListModel) {
        compositeDisposable.add(
            Completable.fromAction {
                repository.deleteData(data)
            }.subscribeOn(Schedulers.io()).doOnComplete {

            }.subscribe()
        )
    }

}