package boki.bokiportfolio.config

import boki.bokiportfolio.prop.MinioProperties
import io.minio.BucketExistsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MinioConfig(private val minioProperties: MinioProperties) {

    @Bean
    fun minioClient(): MinioClient {
        return MinioClient.builder()
            .endpoint(minioProperties.endpoint)
            .credentials(minioProperties.accessKey, minioProperties.secretKey)
            .build()
    }

    @Bean
    fun initializeBucket(): Boolean {
        val found = minioClient().bucketExists(BucketExistsArgs.builder().bucket(minioProperties.bucketName).build())
        if (!found) {
            minioClient().makeBucket(MakeBucketArgs.builder().bucket(minioProperties.bucketName).build())
        }
        return found
    }
}
