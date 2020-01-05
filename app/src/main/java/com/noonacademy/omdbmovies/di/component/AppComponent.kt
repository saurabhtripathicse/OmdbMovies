package com.noonacademy.omdbmovies.di.component


import com.noonacademy.omdbmovies.MyApplication
import com.noonacademy.omdbmovies.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AndroidInjectionModule::class,
        DbModule::class,
        ContextModule::class,
        ApiModule::class,
        ViewModelModule::class,
        ActivityModule::class
    ]
)
interface AppComponent : AndroidInjector<MyApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: MyApplication): Builder

        fun build(): AppComponent
    }
    override fun inject(application: MyApplication)
}