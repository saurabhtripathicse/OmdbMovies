package com.noonacademy.omdbmovies.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.noonacademy.omdbmovies.di.ViewModelKey
import com.noonacademy.omdbmovies.factory.ViewModelFactory
import com.noonacademy.omdbmovies.ui.viewmodel.DetailViewModel
import com.noonacademy.omdbmovies.ui.viewmodel.MainActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    protected abstract fun mainViewModel(githubListViewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    protected abstract fun detailViewModel(githubListViewModel: DetailViewModel): ViewModel
}