package boki.bokiportfolio.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
class Comment(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id: Long? = null,

    @Column(nullable = false, unique = false)
    var content: String,

    @Column(nullable = false, unique = false, updatable = false)
    val userId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "article_id")
    val article: Article,
) : AuditEntity() {

    fun addComment2Article() {
        article.comments.add(this)
    }

    companion object {
        fun createComment(content: String, article: Article, userId: Long): Comment {
            val comment = Comment(
                content = content,
                article = article,
                userId = userId,
            )
            comment.addComment2Article()
            return comment
        }
    }
}
