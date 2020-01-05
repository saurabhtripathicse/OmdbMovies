package com.noonacademy.omdbmovies.data.local_.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "bookmark_movies")
data class BookMarkListModel (
	@PrimaryKey
    var imdbID: String,
	var Title: String,
	var Year: String,
	var Type: String,
	var Poster: String
)