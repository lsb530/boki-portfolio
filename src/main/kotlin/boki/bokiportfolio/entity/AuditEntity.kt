package boki.bokiportfolio.entity

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PreUpdate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@MappedSuperclass
abstract class AuditEntity {

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        protected set

    @Column(name = "updated_at", nullable = true)
    var updatedAt: LocalDateTime? = null
        protected set

    @Column(name = "deleted_at", nullable = true)
    var deletedAt: LocalDateTime? = null
        protected set

    @PreUpdate
    protected fun onUpdate() {
        updatedAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    }

    fun softDelete() {
        deletedAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
    }
}
