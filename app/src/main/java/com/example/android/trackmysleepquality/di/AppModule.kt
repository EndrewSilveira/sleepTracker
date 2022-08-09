package com.example.android.trackmysleepquality.di

import android.content.Context
import androidx.room.Room
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSearchDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(context, SleepDatabase::class.java, "SleepDatabase")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun providesSleepDAO(sleepDatabase: SleepDatabase): SleepDatabaseDao {
        return sleepDatabase.sleepDatabaseDao
    }
}