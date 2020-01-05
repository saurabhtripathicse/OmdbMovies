package com.noonacademy.omdbmovies.ui.adapter_

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.noonacademy.omdbmovies.R
import com.noonacademy.omdbmovies.data.local_.entity.BookMarkListModel
import com.noonacademy.omdbmovies.ui.communicator.AdapterCallbackListener
import kotlinx.android.synthetic.main.item_bookmark_layout.view.*
import kotlinx.android.synthetic.main.item_movies_layout.view.bookmark_image
import kotlinx.android.synthetic.main.item_movies_layout.view.poster_image
import kotlinx.android.synthetic.main.item_movies_layout.view.title
import kotlinx.android.synthetic.main.item_movies_layout.view.type
import kotlinx.android.synthetic.main.item_movies_layout.view.year


class BookMarkListAdapter(
    var data: List<BookMarkListModel>,
    var updateData: AdapterCallbackListener
) :
    RecyclerView.Adapter<BookMarkListAdapter.RepoHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RepoHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_bookmark_layout, parent, false)
        return RepoHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun getItem(position: Int): BookMarkListModel {
        return data[position]
    }

    override fun onBindViewHolder(holder: RepoHolder, position: Int) {
        holder.bindTo(getItem(holder.adapterPosition), holder.adapterPosition, updateData)
    }

    class RepoHolder(v: View) : RecyclerView.ViewHolder(v) {

        fun bindTo(bookMarkListModel: BookMarkListModel, position: Int, updateData: AdapterCallbackListener) {

            val circularProgressDrawable = CircularProgressDrawable(itemView.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            Glide
                .with(itemView.context)
                .load(bookMarkListModel.Poster)
                .placeholder(circularProgressDrawable)
                .error(R.drawable.not_found)
                .into(itemView.poster_image)

            itemView.title.text = bookMarkListModel.Title
            itemView.type.text = bookMarkListModel.Type
            itemView.year.text = bookMarkListModel.Year




            //Log.i("APPDATA", searchDataListModel.bookmark.toString())

                Glide
                    .with(itemView.context)
                    .load(R.drawable.bookmark)
                    .into(itemView.bookmark_image)


            itemView.bookmark_image.setOnClickListener {

               updateData.deleteBookmark(bookMarkListModel, position)

            }

            itemView.root_bookmark.setOnClickListener {
                updateData.onClick(bookMarkListModel.imdbID)
            }
        }
    }

}