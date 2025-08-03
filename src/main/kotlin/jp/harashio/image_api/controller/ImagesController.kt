package jp.harashio.image_api.controller

import jp.harashio.image_api.config.EnvironmentType
import jp.harashio.image_api.domain.request.ImageBase64UploadRequest
import jp.harashio.image_api.domain.ImageResource
import jp.harashio.image_api.domain.db.User
import jp.harashio.image_api.service.AwsStorageService
import jp.harashio.image_api.service.ImageAccessManageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PathVariable

@RestController
@RequestMapping("/api/v1/images")
class ImagesController {

    @Autowired
    private lateinit var environmentType: EnvironmentType

    @Autowired
    lateinit var awsStorageService: AwsStorageService

    @Autowired
    lateinit var imageAccessManageService: ImageAccessManageService

    /**
     * Base64で定義されたイメージのアップロード
     */
    @PostMapping("/uploadBase64")
    fun uploadImage(@RequestBody image: ImageBase64UploadRequest): Map<String, String> {

        val user = SecurityContextHolder.getContext().authentication.principal as User

        awsStorageService.uploadBase64Image(user, getUploadDirectory(user), ImageResource(image.image))
        return mapOf("status" to "ok")
    }

    /**
     *
     */
    @PostMapping("/{imageId}/{status}")
    fun updateImageStatus(@PathVariable imageId: String, @PathVariable status: String): Map<String, String> {

        val user = SecurityContextHolder.getContext().authentication.principal as User

        imageAccessManageService.updateImageAccessStatus(user.id.toString(), imageId, status)

        return mapOf("status" to "ok", "imageId" to imageId, "newStatus" to status)
    }



    private fun getUploadDirectory(user: User): String {
        if (environmentType == EnvironmentType.PRODUCTION) {
            // 本番環境のアップロードディレクトリの取得ロジック
            throw IllegalArgumentException("本番環境での画像アップロードは未実装です")
        }
        // 本番環境以外は develop ディレクトリにアップロード
        return "develop/${user.uid}"
    }
}