package jp.harashio.image_api.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
class AwsS3Config {
    @Value("\${spring.cloud.aws.credentials.access-key:unknown}")
    private val accessKey: String? = null

    @Value("\${spring.cloud.aws.credentials.secret-key:unknown}")
    private val secretKey: String? = null

    @Value("\${spring.cloud.aws.region.static:unknown}")
    private val region: String? = null

    /**
     * ローカル環境からS3にアクセスするためのクライアント
     */
    @Bean("s3Client")
    @Profile("local")
    fun s3Client(): S3Client {
        val provider = StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey))
        return S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(provider)
            .build()
    }
}