package com.ows.gemini.anything.data.di

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder
import androidx.datastore.rxjava3.RxDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    private const val LAUNCH_DATASTORE = "LAUNCH_DATASTORE"

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context,
    ): RxDataStore<Preferences> = RxPreferenceDataStoreBuilder(context, LAUNCH_DATASTORE).build()
}
