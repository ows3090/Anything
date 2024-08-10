package com.ows.gemini.anything.data.service.request

data class ImageRequest(
    val prompt: String,
    val size: String = "1024x1024",
    val model: String = "dall-e-3",
    val n: Int = 1,
)
