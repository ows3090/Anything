package com.ows.gemini.anything.data.repository

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.rxjava3.RxDataStore
import com.ows.gemini.anything.data.PreferenceKeys.IS_FIRST_LAUNCH
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class LocalRepositoryImpl
    @Inject
    constructor(
        private val dataStore: RxDataStore<Preferences>,
    ) : LocalRepository {
        @OptIn(ExperimentalCoroutinesApi::class)
        override fun isFirstLaunch(): Flowable<Boolean> =
            dataStore.data().map { preference ->
                preference[IS_FIRST_LAUNCH] ?: true
            }

        @OptIn(ExperimentalCoroutinesApi::class)
        override fun launch() {
            dataStore.updateDataAsync { preference ->
                val mutablePreference = preference.toMutablePreferences()
                mutablePreference[IS_FIRST_LAUNCH] = false
                Single.just(mutablePreference)
            }
        }
    }
