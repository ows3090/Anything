package com.ows.gemini.anything.data.di

import android.content.Context
import androidx.room.Room
import com.ows.gemini.anything.data.db.AnythingDatabase
import com.ows.gemini.anything.data.db.dao.RecentlyFoodDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    private const val DATABASE_NAME = "Anything.db"

    @Provides
    @Singleton
    fun provideAnythingDatabase(
        @ApplicationContext context: Context,
    ): AnythingDatabase =
        Room
            .databaseBuilder(
                context,
                AnythingDatabase::class.java,
                DATABASE_NAME,
            ).build()

    @Provides
    @Singleton
    fun provideRecentlyFoodDao(anythingDatabase: AnythingDatabase): RecentlyFoodDao = anythingDatabase.recentlyFoodDao()
}
