package com.noonacademy.omdbmovies.di.module

import com.noonacademy.omdbmovies.ui.MainActivity
import com.noonacademy.omdbmovies.ui.MoviesDetailActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    internal abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun contributeDetailActivity(): MoviesDetailActivity
}