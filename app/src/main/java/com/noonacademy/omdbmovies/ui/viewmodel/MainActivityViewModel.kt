package com.noonacademy.omdbmovies.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.noonacademy.omdbmovies.constant_.AppConstants.DEFAULT_SEARCH_KEY
import com.noonacademy.omdbmovies.data.local_.deo.BookMarkDao
import com.noonacademy.omdbmovies.data.local_.entity.BookMarkListModel
import com.noonacademy.omdbmovies.data.model.search.MoviesDataModel
import com.noonacademy.omdbmovies.data.model.search.SearchDataListModel
import com.noonacademy.omdbmovies.data.repo_.MoviesRepository
import com.noonacademy.omdbmovies.data.retrofit_.ApiService
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivityViewModel @Inject
constructor( bookMarkDao: BookMarkDao, apiService: ApiService):
    ViewModel() {

    private val repository: MoviesRepository = MoviesRepository(bookMarkDao, apiService)

    private val searchDataListModel = ArrayList<SearchDataListModel>()

    private var compositeDisposable = CompositeDisposable()

    private val loadingLiveData = MutableLiveData<Boolean>()
    private val showErrorRetry = MutableLiveData<Boolean>()

    private var moviesDataList = MutableLiveData<List<SearchDataListModel>>()
    private var moviesBookMarkList = MutableLiveData<List<BookMarkListModel>>()

    fun loading() = loadingLiveData
    fun errorRetry() = showErrorRetry
    fun getMoviesList() = moviesDataList
    fun getMovieBookMarkList() = moviesBookMarkList

    init {
        loadMoviesList(DEFAULT_SEARCH_KEY, 1)
    }

    fun loadMoviesList(search: String, page: Int){

        loadingLiveData.postValue(true)
        showErrorRetry.postValue(false)

        compositeDisposable.add(
            repository.callMoviesList(search, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    private fun handleResponse(response: MoviesDataModel) {
        if (response.response && response.searchDataListModels.isNotEmpty()) {
            searchDataListModel.addAll(response.searchDataListModels)
            checkBookMarkList()
        } else {
            checkBookMarkList()
        }
    }

    private fun handleError(error: Throwable) {
        loadingLiveData.postValue(true)
        showErrorRetry.postValue(true)
        error.printStackTrace()
    }

    private fun checkBookMarkList() {
        compositeDisposable.add(
            repository.getBookMark()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        moviesBookMarkList.postValue(it)
                        searchDataListModel.forEach { search->
                            search.bookmark = it.any { data -> data.imdbID == search.imdbID}
                        }
                        loadingLiveData.postValue(false)
                        showErrorRetry.postValue(false)
                        moviesDataList.postValue(searchDataListModel)
                    },
                    {
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