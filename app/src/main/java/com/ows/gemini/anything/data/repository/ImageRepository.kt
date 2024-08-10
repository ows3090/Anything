package com.ows.gemini.anything.data.repository

import com.ows.gemini.anything.data.model.ImageModel
import io.reactivex.rxjava3.core.Single

interface ImageRepository {
    fun getImage(prompt: String): Single<ImageModel>
}
