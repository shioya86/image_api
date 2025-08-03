package jp.harashio.image_api.domain.response

import com.fasterxml.jackson.annotation.JsonProperty
import jp.harashio.image_api.domain.db.Image

class ImageListResponse {
    @JsonProperty("images")
    var images: List<ImageResponse> = emptyList()

    constructor(imageList: List<Image>) {
        val images = imageList.map { image ->
            ImageResponse(image)
        }

        this.images = images
    }

    class ImageResponse(
        @JsonProperty("url")
        var url: String,

        @JsonProperty("description")
        val description: String? = null,
    ) {
        constructor(image: Image) : this(
            url = "http://localhost:8080/api/v1/public/images/" + image.path.substringAfterLast("/"),
            description = image.description
        )
    }
}
