package com.noonacademy.omdbmovies.data.local_

import androidx.room.Database
import androidx.room.RoomDatabase
import com.noonacademy.omdbmovies.data.local_.deo.BookMarkDao
import com.noonacademy.omdbmovies.data.local_.entity.BookMarkListModel

@Database(entities = [BookMarkListModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bookMarkDao(): BookMarkDao
}