package com.noonacademy.omdbmovies.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ContextModule {

    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
}