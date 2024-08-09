package boki.bokiportfolio.service

import org.springframework.cache.annotation.Cacheable
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisService(
    private val redisTemplate: RedisTemplate<String, String>,
) {

    @Cacheable(key = "#key", value = ["test"])
    fun save(key: String, value: String): String {
        println("save")
        return value
    }

    fun saveWithTemplate(key: String, value: String, ttl: Long) {
        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(ttl))
    }

    fun getWithTemplate(key: String): String? {
        val valueOperations = redisTemplate.opsForValue()
        return valueOperations[key]
    }

    fun hasKey(key: String) = redisTemplate.hasKey(key)

}
