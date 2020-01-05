package com.noonacademy.omdbmovies.ui.communicator

import com.noonacademy.omdbmovies.data.local_.entity.BookMarkListModel

interface AdapterCallbackListener {
    fun updateBookmark(bookMarkListModel: BookMarkListModel, position: Int)
    fun deleteBookmark(bookMarkListModel: BookMarkListModel, position: Int)
    fun onClick(imdbIDN: String)
}