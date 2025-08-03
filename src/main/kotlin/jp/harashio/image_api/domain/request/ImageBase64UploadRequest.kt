package jp.harashio.image_api.domain.request

import com.fasterxml.jackson.annotation.JsonProperty

data class ImageBase64UploadRequest(
    @JsonProperty("path")
    val path: String,
    @JsonProperty("description")
    val description: String?,
    @JsonProperty("image")
    val image: String // Base64 encoded image data
)