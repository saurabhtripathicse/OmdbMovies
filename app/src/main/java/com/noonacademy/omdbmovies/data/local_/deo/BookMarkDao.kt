package com.noonacademy.omdbmovies.data.local_.deo

import androidx.room.*
import com.noonacademy.omdbmovies.data.local_.entity.BookMarkListModel
import io.reactivex.Flowable

@Dao
interface BookMarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBookMark(bookMarkListModel: BookMarkListModel)

    @Query("SELECT * FROM bookmark_movies")
    fun getBookMarkList(): Flowable<List<BookMarkListModel>>

    @Query("SELECT * FROM bookmark_movies")
    fun getBookMarkListN(): List<BookMarkListModel>

    @Query("SELECT COUNT(imdbID) FROM bookmark_movies WHERE imdbID =:id")
    fun checkDataCount(id: String) : Flowable<Int>

    @Delete
    fun delete(model: BookMarkListModel)

}