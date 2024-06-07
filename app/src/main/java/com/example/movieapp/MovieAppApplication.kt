package com.example.movieapp

import android.app.Application
import com.example.movieapp.data.AppContainer
import com.example.movieapp.data.DefaultAppContainer

class MovieAppApplication : Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}