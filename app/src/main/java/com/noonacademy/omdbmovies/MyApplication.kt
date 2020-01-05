package com.noonacademy.omdbmovies

import com.facebook.stetho.Stetho
import com.noonacademy.omdbmovies.di.component.AppComponent
import com.noonacademy.omdbmovies.di.component.DaggerAppComponent
import dagger.android.DaggerApplication

class MyApplication : DaggerApplication()  {

    override fun applicationInjector(): AppComponent =
        DaggerAppComponent.builder().application(this).build()


    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}