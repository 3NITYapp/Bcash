package com.ivos.bcash.di

import android.app.Application
import com.ivos.bcash.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun providesRoomDatabase(application: Application): AppDatabase {
        return AppDatabase.invoke(application.applicationContext)
    }
}