package com.noonacademy.omdbmovies.ui.adapter_

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeTransition
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.request.transition.TransitionFactory
import com.noonacademy.omdbmovies.R
import com.noonacademy.omdbmovies.data.local_.entity.BookMarkListModel
import com.noonacademy.omdbmovies.data.model.search.SearchDataListModel
import com.noonacademy.omdbmovies.ui.communicator.AdapterCallbackListener
import kotlinx.android.synthetic.main.item_header_.view.*
import kotlinx.android.synthetic.main.item_movies_layout.view.*


class MoviesListAdapter(
    private var movieList: ArrayList<SearchDataListModel>,
    private var bookmarkList: ArrayList<BookMarkListModel>,
    private var updateData: AdapterCallbackListener
) : RecyclerView.Adapter<MoviesListAdapter.BaseViewHolder<*>>() {


    var showLoader = false
    var pool = RecyclerView.RecycledViewPool()

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            TYPE_HEADING -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_header_, parent, false)
                HeadingViewHolder(view)
            }
            TYPE_LOADING -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_loader_, parent, false)
                LoadingViewHolder(view)
            }
            TYPE_MAIN -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_movies_layout, parent, false)
                MainViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }

    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is HeadingViewHolder -> holder.bind(bookmarkList, holder.adapterPosition, updateData)
            is LoadingViewHolder -> holder.bind(bookmarkList, holder.adapterPosition, updateData)
            is MainViewHolder -> {
                var adapterpos = position
                when (showHeader()) {
                    true -> --adapterpos
                }
                holder.bind(
                    movieList[adapterpos],
                    holder.adapterPosition,
                    updateData
                )
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (showFooter(position))
            TYPE_LOADING
        else if (position == 0 && showHeader())
            TYPE_HEADING
        else
            TYPE_MAIN
    }

    fun showHeader(): Boolean {
        return bookmarkList.size > 0
    }

    private fun showFooter(position: Int): Boolean {
        return position == itemCount - 1
    }

    inner class HeadingViewHolder(itemView: View) :
        BaseViewHolder<List<BookMarkListModel>>(itemView) {

        override fun bind(item: List<BookMarkListModel>, position: Int, updateData: AdapterCallbackListener) {
            itemView.bookmark_list.setRecycledViewPool(pool)

            itemView.bookmark_list.layoutManager =
                LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
            val adapter = BookMarkListAdapter(item.reversed(), updateData)
            itemView.bookmark_list.adapter = adapter
            adapter.notifyDataSetChanged()

        }
    }

    inner class LoadingViewHolder(itemView: View) :
        BaseViewHolder<List<BookMarkListModel>>(itemView) {

        override fun bind(item: List<BookMarkListModel>, position: Int, updateData: AdapterCallbackListener) {
            when (showLoader) {
                true -> itemView.visibility = VISIBLE
                false -> itemView.visibility = GONE

            }
        }
    }

    inner class MainViewHolder(itemView: View) : BaseViewHolder<SearchDataListModel>(itemView) {

        override fun bind(item: SearchDataListModel, position: Int, updateData: AdapterCallbackListener) {
            //Do your view assignment here from the data model

            val circularProgressDrawable = CircularProgressDrawable(itemView.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()


            Glide
                .with(itemView.context)
                .load(item.poster)
                .error(R.drawable.not_found)
                .placeholder(circularProgressDrawable)
                .into(itemView.poster_image)

            itemView.title.text = item.title
            itemView.type.text = item.type
            itemView.year.text = item.year


            //Log.i("APPDATA", searchDataListModel.bookmark.toString())


            if (item.bookmark) {
                Glide
                    .with(itemView.context)
                    .load(R.drawable.bookmark)
                    .transition(DrawableTransitionOptions.with(DrawableAlwaysCrossFadeFactory()))
                    .into(itemView.bookmark_image)
            } else {
                Glide
                    .with(itemView.context)
                    .load(R.drawable.bookmark_color)
                    .transition(DrawableTransitionOptions.with(DrawableAlwaysCrossFadeFactory()))
                    .into(itemView.bookmark_image)
            }

            itemView.root_main.setOnClickListener {
                updateData.onClick(item.imdbID)
            }

            itemView.bookmark_image.setOnClickListener {

                val bookMarkListModel = BookMarkListModel(
                    item.imdbID,
                    item.title, item.year,
                    item.type, item.poster
                )

                if (item.bookmark) {

                    updateData.deleteBookmark(bookMarkListModel, position)

                } else {

                    updateData.updateBookmark(bookMarkListModel, position)
                }
            }
        }
    }

    companion object {
        private const val TYPE_HEADING = 0
        private const val TYPE_MAIN = 1
        private const val TYPE_LOADING = 2
    }

    override fun getItemCount(): Int {
        return when (showHeader()) {
            true -> movieList.size + 2
            false -> movieList.size + 1
        }
    }

    fun updateLoader(loading: Boolean) {
        showLoader = loading
        notifyItemChanged(itemCount - 1)
    }

    fun showingLoader(position: Int): Boolean {
        return position == itemCount - 1
    }

    fun clear() {
        bookmarkList.clear()
        movieList.clear()
        notifyDataSetChanged()
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T, position: Int, updateData: AdapterCallbackListener)
    }

    class DrawableAlwaysCrossFadeFactory : TransitionFactory<Drawable> {
        private val resourceTransition: DrawableCrossFadeTransition = DrawableCrossFadeTransition(600,
            true) //customize to your own needs or apply a builder pattern
        override fun build(dataSource: DataSource?, isFirstResource: Boolean): Transition<Drawable> {
            return resourceTransition
        }
    }
}