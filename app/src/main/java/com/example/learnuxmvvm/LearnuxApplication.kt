package com.example.learnuxmvvm

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class LearnuxApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }

}