package boki.bokiportfolio.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Article(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id: Long? = null,

    @Column(nullable = false, unique = false)
    var title: String,

    @Column(nullable = false, unique = false)
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "user_id", foreignKey = ForeignKey(name = "article_user_key"))
    val user: User,
) : AuditEntity() {

    companion object {
        fun createArticle(title: String, content: String, user: User): Article {
            return Article(
                title = title,
                content = content,
                user = user
            )
        }
    }
}
