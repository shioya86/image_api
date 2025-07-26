package jp.harashio.image_api.service

import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest

@Service
@Slf4j
class AwsStorageService {

    @Value("\${spring.cloud.aws.s3.bucket}")
    private val bucket: String? = null

    @Autowired
    var s3Client: S3Client? = null

    fun downloadFile(s3Path: String): ByteArray {
        println("Downloading file from S3: bucket=$bucket, path=$s3Path")
        val request = GetObjectRequest.builder().bucket(bucket).key(s3Path).build()

        s3Client?.getObject(request).use { result ->
            if (result != null) {
                return result.readAllBytes()
            }
        }

        return ByteArray(0)
    }
}