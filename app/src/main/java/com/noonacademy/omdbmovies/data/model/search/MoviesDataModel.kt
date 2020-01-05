package com.noonacademy.omdbmovies.data.model.search

import com.google.gson.annotations.SerializedName


data class MoviesDataModel (
	@SerializedName("Search") var searchDataListModels : List<SearchDataListModel>,
	@SerializedName("totalResults") var totalResults : Int,
	@SerializedName("Response") var response : Boolean
)