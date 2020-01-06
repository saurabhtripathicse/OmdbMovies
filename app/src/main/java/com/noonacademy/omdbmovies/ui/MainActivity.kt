package com.noonacademy.omdbmovies.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.noonacademy.omdbmovies.R
import com.noonacademy.omdbmovies.constant_.AppConstants.DEFAULT_SEARCH_KEY
import com.noonacademy.omdbmovies.constant_.AppConstants.IMDB_ID
import com.noonacademy.omdbmovies.data.local_.entity.BookMarkListModel
import com.noonacademy.omdbmovies.data.model.search.SearchDataListModel
import com.noonacademy.omdbmovies.factory.ViewModelFactory
import com.noonacademy.omdbmovies.ui.adapter_.MoviesListAdapter
import com.noonacademy.omdbmovies.ui.communicator.AdapterCallbackListener
import com.noonacademy.omdbmovies.ui.viewmodel.MainActivityViewModel
import com.noonacademy.omdbmovies.utility.Utility.Companion.hideKeyboard
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.error_state.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject


class MainActivity : BaseActivity(), AdapterCallbackListener {

    override fun inject() {
        AndroidInjection.inject(this)
    }

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory

    private lateinit var mainActivityViewModel: MainActivityViewModel

    private lateinit var adapter: MoviesListAdapter

    private var repoListData: ArrayList<SearchDataListModel> = ArrayList()
    private var bookMarkListModel: ArrayList<BookMarkListModel> = ArrayList()

    private var loading: Boolean = false

    private var loadedItem = 1

    private var searchItem = DEFAULT_SEARCH_KEY

    private val layoutManager =
        GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialiseRecyclerView()

        initialiseViewModel()

        initialiseView()

    }


    private fun initialiseView() {

        search_.setOnClickListener {
            searchMovies(it)
        }

        retry.setOnClickListener {
            retrySearch(it)
        }

        search_text.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchMovies(v)
                return@OnEditorActionListener true
            }
            return@OnEditorActionListener false
        })
    }

    private fun searchMovies(v: View) {
        hideKeyboard(v)
        searchItem = search_text.text.toString().trim()
        if (searchItem.isNotEmpty()) {
            repoListData.clear()
            bookMarkListModel.clear()
            adapter.clear()
            loadedItem = 1
            mainActivityViewModel.loadMoviesList(searchItem, loadedItem)
        }

    }

    private fun retrySearch(v: View) {
        hideKeyboard(v)
        searchItem = search_text.text.toString().trim()
        if (searchItem.isNotEmpty()) {
            repoListData.clear()
            bookMarkListModel.clear()
            adapter.clear()
            loadedItem = 1
            mainActivityViewModel.loadMoviesList(searchItem, loadedItem)
        }else{
            searchItem = DEFAULT_SEARCH_KEY
            repoListData.clear()
            bookMarkListModel.clear()
            adapter.clear()
            loadedItem = 1
            mainActivityViewModel.loadMoviesList(searchItem, loadedItem)
        }

    }

    private fun initialiseRecyclerView() {
        adapter = MoviesListAdapter(repoListData, bookMarkListModel, this)
        movie_list.adapter = adapter

        movie_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!loading) {
                    if (layoutManager.findLastCompletelyVisibleItemPosition() >= layoutManager.itemCount - 1) {
                        mainActivityViewModel.loadMoviesList(searchItem, ++loadedItem)
                    }
                }
            }
        })
    }

    private fun initialiseViewModel() {
        mainActivityViewModel = ViewModelProviders
            .of(this, viewModelFactory)
            .get(MainActivityViewModel::class.java)

        mainActivityViewModel.getMoviesList().observe(this, Observer {
            layoutManager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (adapter.showHeader() && position == 0 || adapter.showingLoader(
                            position
                        )
                    ) 2 else 1
                }
            }
            movie_list.layoutManager = layoutManager

            val hash = LinkedHashSet<SearchDataListModel>()
            hash.addAll(repoListData)
            hash.addAll(it)
            repoListData.clear()
            repoListData.addAll(hash)
            adapter.notifyDataSetChanged()

        })

        mainActivityViewModel.getMovieBookMarkList().observe(this, Observer {
            bookMarkListModel.clear()
            bookMarkListModel.addAll(it)
        })

        mainActivityViewModel.loading().observe(this, Observer {
            loading = it
            adapter.updateLoader(it)
        })

        mainActivityViewModel.errorRetry().observe(this , Observer {

            if (it) {
                error_layout.visibility = VISIBLE
                movie_list.visibility = GONE
            }else{
                error_layout.visibility = GONE
                movie_list.visibility = VISIBLE
            }


        })
    }

    override fun updateBookmark(bookMarkListModel: BookMarkListModel, position: Int) {
        mainActivityViewModel.insertBookMark(bookMarkListModel)
    }

    override fun deleteBookmark(bookMarkListModel: BookMarkListModel, position: Int) {
        mainActivityViewModel.deleteBookMark(bookMarkListModel)
    }

    override fun onClick(imdbIDN: String) {
        startActivity(
            Intent(this@MainActivity, MoviesDetailActivity::class.java)
                .putExtra(IMDB_ID, imdbIDN)
        )
    }
}

