package boki.bokiportfolio.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany

@Entity
class Article(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id: Long? = null,

    @Column(nullable = false, unique = false)
    var title: String,

    @Column(nullable = false, unique = false)
    var content: String,

    @Column(nullable = true, unique = false)
    var imgFileName: String? = null,

    @Column(nullable = false, unique = false)
    var viewCnt: Int = 0,

    @ElementCollection
    @CollectionTable(name = "article_likes", joinColumns = [JoinColumn(name = "article_id")])
    @Column(name = "user_id")
    val likeUsers: MutableSet<Long> = HashSet(),

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "user_id", foreignKey = ForeignKey(name = "article_user_key"))
    val user: User,

    @OneToMany(mappedBy = "article")
    val comments: MutableList<Comment> = mutableListOf(),
) : AuditEntity() {

    fun addViewCnt() {
        viewCnt += 1
    }

    fun likeArticle(userId: Long) {
        likeUsers.add(userId)
    }

    fun cancelLikeArticle(userId: Long) {
        if (likeUsers.contains(userId))
            likeUsers.remove(userId)
    }

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
