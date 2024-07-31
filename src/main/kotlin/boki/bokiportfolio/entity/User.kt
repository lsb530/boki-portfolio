package boki.bokiportfolio.entity

import boki.bokiportfolio.common.Role
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class User(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val userId: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false, unique = true)
    val phoneNumber: String,

    @Column(nullable = false, unique = false)
    var name: String,

    @Column(nullable = false, unique = false)
    var password: String,

    @Column(nullable = false, unique = false)
    @Enumerated(EnumType.STRING)
    val role: Role,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    val articles: MutableList<Article> = mutableListOf(),
) : AuditEntity() {

    companion object {
        fun createUser(email: String, userId: String, phoneNumber: String, name: String, password: String): User {
            return User(
                email = email,
                userId = userId,
                phoneNumber = phoneNumber,
                name = name,
                password = password,
                role = Role.USER
            )
        }

        fun createAdminUser(email: String, userId: String, phoneNumber: String, name: String, password: String): User {
            return User(
                email = email,
                userId = userId,
                phoneNumber = phoneNumber,
                name = name,
                password = password,
                role = Role.ADMIN
            )
        }
    }
}
