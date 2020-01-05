package com.noonacademy.omdbmovies.data.model

import com.noonacademy.omdbmovies.data.local_.entity.BookMarkListModel
import com.noonacademy.omdbmovies.data.model.search.MoviesDataModel

data class JoinDataModel (val moviesDataModel: MoviesDataModel, val bookMarkListModel: BookMarkListModel)