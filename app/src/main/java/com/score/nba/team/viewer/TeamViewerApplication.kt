package com.score.nba.team.viewer

import android.app.Application
import di.coreModule
import di.databaseModule
import di.frameworkModule
import di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TeamViewerApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@TeamViewerApplication)
            modules(coreModule, frameworkModule, presentationModule, databaseModule)
        }
    }
}