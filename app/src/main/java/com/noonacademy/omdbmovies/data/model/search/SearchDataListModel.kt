package com.noonacademy.omdbmovies.data.model.search

import com.google.gson.annotations.SerializedName


data class SearchDataListModel (

	@SerializedName("Title") var title : String,
	@SerializedName("Year") var year : String,
	@SerializedName("imdbID") var imdbID : String,
	@SerializedName("Type") var type : String,
	@SerializedName("Poster") var poster : String,
	var bookmark: Boolean = false
)