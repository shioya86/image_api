package jp.harashio.image_api.controller.public

import jp.harashio.image_api.domain.response.ImageListResponse
import jp.harashio.image_api.repository.ImageRepository
import jp.harashio.image_api.service.AwsStorageService
import jp.harashio.image_api.service.ImageAccessManageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/public/images")
class PublicImageController {

    @Autowired
    lateinit var awsStorageService: AwsStorageService

    @Autowired
    lateinit var imageAccessManageService: ImageAccessManageService

    /**
     * イメージサンプルの取得
     */
    @GetMapping("/sample.jpeg")
    fun getSampleImage(): HttpEntity<ByteArray> {
        val image = awsStorageService.downloadFile("IMG_1226.JPEG")

        val headers = HttpHeaders()
        headers.contentType = MediaType.IMAGE_JPEG
        headers.contentLength = image.size.toLong()

        return HttpEntity<ByteArray>(image, headers)
    }

    /**
     * 指定したイメージの取得
     */
    @GetMapping("/{imageId}")
    fun getImage(@PathVariable("imageId") imageId: String): HttpEntity<ByteArray> {
        val image = awsStorageService.downloadFileFrom(imageId)

        val headers = HttpHeaders()
        headers.contentType = MediaType.valueOf(image.extension)
        headers.contentLength = image.imageBytes.size.toLong()

        return HttpEntity<ByteArray>(image.imageBytes, headers)
    }


    /**
     * ユーザのイメージリストの取得
     */
    @GetMapping("/latest")
    fun getImages(@RequestParam("user_id") userId: Long): ImageListResponse {
        // ユーザの最新50件の画像リストを取得
        val images = imageAccessManageService.getUserPublicImagesLatest(userId, 50)

        return ImageListResponse(images)
    }
}