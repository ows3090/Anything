package com.ows.gemini.anything.data.repository

import io.reactivex.rxjava3.core.Flowable

interface LocalRepository {
    fun isFirstLaunch(): Flowable<Boolean>

    fun launch()
}
