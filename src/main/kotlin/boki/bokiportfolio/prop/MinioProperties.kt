package boki.bokiportfolio.prop

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "boki.minio")
data class MinioProperties(
    val endpoint: String?,
    val bucketName: String?,
    val accessKey: String? = System.getenv("MINIO_ROOT_USER"),
    val secretKey: String? = System.getenv("MINIO_ROOT_PASSWORD"),
) {
    init {
        require(!endpoint.isNullOrBlank()) { "minIO의 endpoint는 비어있거나 빈 문자열일 수 없습니다" }
        require(!bucketName.isNullOrBlank()) { "minIO의 bucketName은 비어있거나 빈 문자열일 수 없습니다" }
        require(!accessKey.isNullOrBlank()) { "minIO의 accessKey는 비어있거나 빈 문자열일 수 없습니다" }
        require(!secretKey.isNullOrBlank()) { "minIO의 secretKey는 비어있거나 빈 문자열일 수 없습니다" }
    }
}
