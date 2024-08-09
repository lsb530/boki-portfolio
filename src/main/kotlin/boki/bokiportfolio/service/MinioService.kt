package boki.bokiportfolio.service

import io.minio.DownloadObjectArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.StatObjectArgs
import io.minio.StatObjectResponse
import io.minio.errors.MinioException
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile


@Service
class MinioService(
    private val minioClient: MinioClient,
) : ObjectStorageHelper {

    override fun upload(file: MultipartFile, infoMap: Map<String, String>?) {
        try {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket("test")
                    .`object`(file.originalFilename)
                    .stream(file.inputStream, file.size, -1)
                    .userMetadata(infoMap)
                    .contentType(file.contentType)
                    .build(),
            )
        } catch (ex: MinioException) {
            throw ex
        }
    }

    override fun download(objectName: String, path: String) {
        try {
            minioClient.downloadObject(
                DownloadObjectArgs.builder()
                    .bucket("test")
                    .`object`(objectName)
                    .filename(path)
                    .build(),
            )
        } catch (ex: MinioException) {
            throw ex
        }
    }

    fun get(objName: String): StatObjectResponse {
        return try {
            minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket("test")
                    .`object`(objName)
                    .build(),
            )
        } catch (ex: MinioException) {
            throw ex
        }
    }
}
