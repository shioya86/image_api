package jp.harashio.image_api.domain

import java.util.Base64

class ImageResource {

    private val resource: String
    val data: String
    val contentType: String
    val extension: String
    val imageBytes: ByteArray

    constructor(resource: String) {
        this.resource = resource
        this.data = this.resource.substringAfter("base64,")
        this.contentType = this.resource.substringAfter("data:").substringBefore(";")
        this.extension = getExtensionFromContentType(this.contentType)

        // データが存在しない場合は例外を投げる
        if (this.data.isEmpty() || this.contentType.isEmpty() || this.extension.isEmpty()) {
            throw IllegalArgumentException("Invalid image resource.")
        }
        this.imageBytes = Base64.getDecoder().decode(this.data)
    }

    private fun getExtensionFromContentType(contentType: String): String {
        return when (contentType) {
            "image/jpeg" -> "jpg"
            "image/png" -> "png"
            "image/gif" -> "gif"
            "image/webp" -> "webp"
            "image/bmp" -> "bmp"
            "image/svg+xml" -> "svg"
            else -> ""
        }
    }
}