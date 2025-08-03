package jp.harashio.image_api.controller

import jp.harashio.image_api.config.EnvironmentType
import jp.harashio.image_api.domain.ImageBase64UploadRequest
import jp.harashio.image_api.domain.ImageResource
import jp.harashio.image_api.service.AwsStorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/images")
class ImagesController {

    @Autowired
    private lateinit var environmentType: EnvironmentType

    @Autowired
    lateinit var awsStorageService: AwsStorageService

    /**
     * Base64で定義されたイメージのアップロード
     */
    @PostMapping("/uploadBase64")
    fun uploadImage(@RequestBody image: ImageBase64UploadRequest): Map<String, String> {

        awsStorageService.uploadBase64Image(getUploadDirectory(), ImageResource(image.image))
        return mapOf("status" to "ok")
    }

    /**
     * イメージサンプルの取得
     */
    @GetMapping("/sample")
    fun getSampleImage(): HttpEntity<ByteArray> {
        val image = awsStorageService.downloadFile("IMG_1226.JPEG")

        val headers = HttpHeaders()
        headers.contentType = MediaType.IMAGE_JPEG
        headers.contentLength = image.size.toLong()

        return HttpEntity<ByteArray>(image, headers)
    }

    private fun getUploadDirectory(): String {
        if (environmentType == EnvironmentType.PRODUCTION) {
            // 本番環境のアップロードディレクトリの取得ロジック
            throw IllegalArgumentException("本番環境での画像アップロードは未実装です")
        }
        // 本番環境以外は develop ディレクトリにアップロード
        return "develop"
    }
}