package com.ows.gemini.anything.data.repository

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.rxjava3.RxDataStore
import com.ows.gemini.anything.data.PreferenceKeys.IS_FIRST_LAUNCH
import com.ows.gemini.anything.data.db.dao.RecentlyFoodDao
import com.ows.gemini.anything.data.db.entity.RecentlyFoodEntity
import com.ows.gemini.anything.data.model.RecentlyFoodModel
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class LocalRepositoryImpl
    @Inject
    constructor(
        private val dataStore: RxDataStore<Preferences>,
        private val foodDao: RecentlyFoodDao,
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

        override fun getRecentlyFoods(): Flowable<List<RecentlyFoodModel>> =
            foodDao.getAll().map { list ->
                list.map { it.toModel() }
            }

        override fun insertFood(
            name: String,
            time: Long,
            meta: String,
        ) {
            foodDao.insertEntity(
                RecentlyFoodEntity(
                    name = name,
                    time = time,
                    meta = meta,
                ),
            )
        }
    }
