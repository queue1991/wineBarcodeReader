package com.example.winebarcodereader

import android.app.Application
import com.example.winebarcodereader.di.myDiModule
import com.example.winebarcodereader.util.SharedPreferenceUtil
import org.koin.android.ext.android.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferenceUtil.init(this)
        startKoin(applicationContext, myDiModule)
    }
}