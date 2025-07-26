package jp.harashio.image_api.controller

import jp.harashio.image_api.service.AwsStorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/images")
class ImagesController {

    @Autowired
    var awsStorageService: AwsStorageService? = null

    /**
     * イメージサンプルの取得
     */
    @GetMapping("/sample")
    fun getSampleImge(): HttpEntity<ByteArray> {
        val image = awsStorageService?.downloadFile("IMG_1226.JPEG")

        val headers = HttpHeaders()
        headers.contentType = MediaType.IMAGE_JPEG
        headers.contentLength = image!!.size.toLong()

        return HttpEntity<ByteArray>(image, headers)
    }
}