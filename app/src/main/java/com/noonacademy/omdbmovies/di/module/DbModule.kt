package com.noonacademy.omdbmovies.di.module

import androidx.room.Room
import com.noonacademy.omdbmovies.MyApplication
import com.noonacademy.omdbmovies.data.local_.AppDatabase
import com.noonacademy.omdbmovies.data.local_.deo.BookMarkDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {

    @Provides
    @Singleton
    internal fun provideDatabase(application: MyApplication): AppDatabase {
        return Room.databaseBuilder(application,
            AppDatabase::class.java, "MoviesDB.db")
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    internal fun provideGithubDao(appDatabase: AppDatabase): BookMarkDao {
        return appDatabase.bookMarkDao()
    }

}