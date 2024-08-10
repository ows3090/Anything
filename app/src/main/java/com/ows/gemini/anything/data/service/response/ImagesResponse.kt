package com.ows.gemini.anything.data.service.response

import com.ows.gemini.anything.data.model.ImageModel

data class ImagesResponse(
    val created: Long = 0L,
    val data: List<ImageResponse> = listOf(),
)

data class ImageResponse(
    val url: String,
) {
    fun toModel() =
        ImageModel(
            url = url,
        )
}
