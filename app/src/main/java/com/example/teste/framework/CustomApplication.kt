package com.example.teste.framework

import android.app.Application
import com.example.teste.main.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module

class CustomApplication(): Application() {

    override fun onCreate() {
        super.onCreate()

        val koin = KoinApplication.init()
            .printLogger(Level.ERROR)
            .modules(modules())
            .androidContext(this)
        startKoin(koin)
    }

    private fun modules() = arrayListOf(mainModule)

}