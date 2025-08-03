package jp.harashio.image_api.service

import jp.harashio.image_api.domain.db.Image
import jp.harashio.image_api.domain.ImageResource
import jp.harashio.image_api.domain.db.User
import jp.harashio.image_api.repository.ImageRepository
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectResponse
import java.io.ByteArrayInputStream

@Service
@Slf4j
class AwsStorageService {

    @Value("\${spring.cloud.aws.s3.bucket}")
    private val bucket: String? = null

    @Autowired
    var s3Client: S3Client? = null

    @Autowired
    lateinit var imageRepository: ImageRepository

    val log = org.slf4j.LoggerFactory.getLogger(AwsStorageService::class.java)

    fun downloadFile(s3Path: String): ByteArray {
        val request = GetObjectRequest.builder().bucket(bucket).key(s3Path).build()

        s3Client?.getObject(request).use { result ->
            if (result != null) {
                return result.readAllBytes()
            }
        }

        return ByteArray(0)
    }

    fun uploadBase64Image(user: User, s3Path: String, resource: ImageResource) {
        // Base64デコード
        val imageBytes = resource.imageBytes

        val filePath = s3Path + "/" + generateFileName(resource.extension)

        val request = PutObjectRequest.builder().bucket(bucket).key(filePath)
            .storageClass("STANDARD")
            .contentType(resource.contentType)
            .contentLength(resource.imageBytes.size.toLong())
            .build()

        var response: PutObjectResponse?
        ByteArrayInputStream(imageBytes).use { inputStream ->
            response = s3Client?.putObject(request, RequestBody.fromInputStream(inputStream, imageBytes.size.toLong()))
        }

        if (response == null) {
            log.error("Failed to upload image to S3: $filePath")
            throw IllegalStateException("Failed to upload image to S3")
        } else {
            log.info("Image uploaded successfully: $filePath")
            // アップロードに成功時、DBに登録する
            imageRepository.save(Image(null, filePath, null, user.id))
        }
    }

    private fun generateFileName(ext: String): String {
        // ファイル名生成ロジック「image_{タイムスタンプ}{10桁のランダム文字列}.{拡張子}」
        return "image_${System.currentTimeMillis()}${getRandomString(10)}." + ext;
    }

    fun getRandomString(length: Int) : String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }
}