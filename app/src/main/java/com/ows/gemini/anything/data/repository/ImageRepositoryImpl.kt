package com.ows.gemini.anything.data.repository

import com.ows.gemini.anything.data.model.ImageModel
import com.ows.gemini.anything.data.service.DallyService
import com.ows.gemini.anything.data.service.request.ImageRequest
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ImageRepositoryImpl
    @Inject
    constructor(
        private val dallyService: DallyService,
    ) : ImageRepository {
        override fun getImage(prompt: String): Single<ImageModel> =
            dallyService
                .getImage(
                    imageRequest = ImageRequest(prompt),
                ).map { response ->
                    response.data
                        .first {
                            it.url.isNotEmpty()
                        }.toModel()
                }
    }
