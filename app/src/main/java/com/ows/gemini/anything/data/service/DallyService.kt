package com.ows.gemini.anything.data.service

import com.ows.gemini.anything.BuildConfig
import com.ows.gemini.anything.data.service.request.ImageRequest
import com.ows.gemini.anything.data.service.response.ImagesResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface DallyService {
    @Headers("Authorization: Bearer ${BuildConfig.DALLE_KEY}")
    @POST("images/generations")
    fun getImage(
        @Body() imageRequest: ImageRequest,
    ): Single<ImagesResponse>
}
