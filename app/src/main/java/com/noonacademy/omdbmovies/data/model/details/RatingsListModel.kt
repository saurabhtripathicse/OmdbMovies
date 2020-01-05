package com.noonacademy.omdbmovies.data.model.details

import com.google.gson.annotations.SerializedName


data class RatingsListModel (
	@SerializedName("Source") val source : String,
	@SerializedName("Value") val value : String
)