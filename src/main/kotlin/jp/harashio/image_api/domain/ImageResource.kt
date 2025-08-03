package jp.harashio.image_api.domain

import java.util.Base64

class ImageResource {

    val contentType: String
    val extension: String
    val imageBytes: ByteArray

    constructor(resource: String) {
        val data = resource.substringAfter("base64,")
        this.contentType = resource.substringAfter("data:").substringBefore(";")
        this.extension = getExtensionFromContentType(this.contentType)

        // データが存在しない場合は例外を投げる
        if (data.isEmpty() || this.contentType.isEmpty() || this.extension.isEmpty()) {
            throw IllegalArgumentException("Invalid image resource.")
        }
        this.imageBytes = Base64.getDecoder().decode(data)
    }

    constructor(contentType: String, extension: String, imageBytes: ByteArray) {
        this.contentType = contentType
        this.extension = extension
        this.imageBytes = imageBytes

        // データが存在しない場合は例外を投げる
        if (this.contentType.isEmpty() || this.extension.isEmpty() || this.imageBytes.isEmpty()) {
            throw IllegalArgumentException("Invalid image resource.")
        }
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