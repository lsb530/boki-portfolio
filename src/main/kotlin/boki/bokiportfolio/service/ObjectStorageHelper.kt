package boki.bokiportfolio.service

import org.springframework.web.multipart.MultipartFile

interface ObjectStorageHelper {
    fun upload(file: MultipartFile, infoMap: Map<String, String>? = emptyMap())
    fun download(objectName: String, path: String)
}
