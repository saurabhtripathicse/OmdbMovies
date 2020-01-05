package com.noonacademy.omdbmovies.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.noonacademy.omdbmovies.R
import com.noonacademy.omdbmovies.constant_.AppConstants.IMDB_ID
import com.noonacademy.omdbmovies.data.local_.entity.BookMarkListModel
import com.noonacademy.omdbmovies.data.model.details.MovieDetailModel
import com.noonacademy.omdbmovies.factory.ViewModelFactory
import com.noonacademy.omdbmovies.ui.viewmodel.DetailViewModel
import dagger.android.AndroidInjection
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_movies_detail.*
import kotlinx.android.synthetic.main.error_state.*
import javax.inject.Inject

class MoviesDetailActivity : BaseActivity() {

    override fun inject() {
        AndroidInjection.inject(this)
    }

    var imdbID: String? = null

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory

    private lateinit var detailViewModel: DetailViewModel

    private var movieDetailModel: MovieDetailModel? = null

    private var isBookMark: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies_detail)

        initialiseView()

        initialiseViewModel()

        imdbID = intent.getStringExtra(IMDB_ID)
        if (imdbID != null)
            detailViewModel.getDetailMovies(imdbID!!)
    }

    private fun initialiseViewModel() {

        detailViewModel = ViewModelProviders
            .of(this, viewModelFactory)
            .get(DetailViewModel::class.java)


        detailViewModel.errorRetry().observe(this, Observer {

            if (it) {
                error_layout_detail.visibility = VISIBLE
                scrollView2.visibility = GONE
                fab_bookmark.visibility = GONE
            } else {
                error_layout_detail.visibility = GONE
                scrollView2.visibility = VISIBLE
                fab_bookmark.visibility = VISIBLE
            }

        })
        detailViewModel.loading().observe(this, Observer {
            if (it) {
                shimmer_loading.startShimmerAnimation()
            } else {
                shimmer_loading.stopShimmerAnimation()
            }
        })
        detailViewModel.getMovieDetails().observe(this, Observer {
            movieDetailModel = it
            isBookMark = it.bookmark

            Glide.with(this)
                .load(movieDetailModel!!.poster)
                .apply(bitmapTransform(BlurTransformation(25, 3)))
                .into(poster_blur)

            if (isBookMark) {
                Glide
                    .with(this)
                    .load(R.drawable.bookmark)
                    .circleCrop()
                    .into(fab_bookmark)
            } else {
                Glide
                    .with(this)
                    .load(R.drawable.bookmark_color)
                    .circleCrop()
                    .into(fab_bookmark)
            }

            Glide
                .with(this)
                .load(it.poster)
                .into(poster_image)

            title_txt.text = it.title

            supportActionBar!!.title = it.title

            genre.text = it.genre
            year.text = it.year
            if (it.ratings.size > 1) {
                rating.visibility = VISIBLE
                rating.text = it.ratings[1].value
            } else {
                rating.visibility = INVISIBLE
            }

            actor.text = it.actors.let { check -> if (check.isNullOrBlank()) "N/A" else check }
            director.text = it.director.let { check -> if (check.isNullOrBlank()) "N/A" else check }
            plot.text = it.plot.let { check -> if (check.isNullOrBlank()) "N/A" else check }
            production.text =
                it.production.let { check -> if (check.isNullOrBlank()) "N/A" else check }
            box_office.text =
                it.boxOffice.let { check -> if (check.isNullOrBlank()) "N/A" else check }

        })
    }

    private fun initialiseView() {

        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        retry.setOnClickListener {
            if (imdbID != null)
                detailViewModel.getDetailMovies(imdbID!!)
        }

        fab_bookmark.setOnClickListener {
            if (movieDetailModel != null) {
                val bookMarkListModel = BookMarkListModel(
                    movieDetailModel!!.imdbID,
                    movieDetailModel!!.title, movieDetailModel!!.year,
                    movieDetailModel!!.type, movieDetailModel!!.poster
                )

                if (isBookMark) {
                    detailViewModel.deleteBookMark(bookMarkListModel)
                } else {
                    detailViewModel.insertBookMark(bookMarkListModel)
                }
            }
        }
    }

    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
