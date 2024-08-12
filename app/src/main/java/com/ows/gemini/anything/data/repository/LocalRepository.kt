package com.ows.gemini.anything.data.repository

import com.ows.gemini.anything.data.model.RecentlyFoodModel
import io.reactivex.rxjava3.core.Flowable

interface LocalRepository {
    fun isFirstLaunch(): Flowable<Boolean>

    fun launch()

    fun getRecentlyFoods(): Flowable<List<RecentlyFoodModel>>

    fun insertFood(
        name: String,
        time: Long,
        meta: String,
    )
}
