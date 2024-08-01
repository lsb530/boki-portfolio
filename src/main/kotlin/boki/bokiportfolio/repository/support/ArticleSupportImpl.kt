package boki.bokiportfolio.repository.support

import boki.bokiportfolio.entity.Article
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ArticleSupportImpl(
    @PersistenceContext
    private val em: EntityManager,
) : ArticleSupport {
    override fun findArticlesContainsTitleAndCreatedAtSortDirection(
        title: String?,
        createdAtOrderBy: Sort.Direction,
    ): MutableList<Article> {
        val cb: CriteriaBuilder = em.criteriaBuilder
        val criteriaQuery: CriteriaQuery<Article> = cb.createQuery(Article::class.java)
        val root: Root<Article> = criteriaQuery.from(Article::class.java)

        val predicates: MutableList<Predicate> = mutableListOf()

        predicates.add(cb.isNull(root.get<LocalDateTime>("deletedAt")))

        if (!title.isNullOrEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("title")), "%${title.lowercase()}%"))
        }

        criteriaQuery.where(cb.and(*predicates.toTypedArray()))

        val order = if (createdAtOrderBy.isAscending) {
            cb.asc(root.get<LocalDateTime>("createdAt"))
        }
        else {
            cb.desc(root.get<LocalDateTime>("createdAt"))
        }

        criteriaQuery.orderBy(order)

        val query = em.createQuery(criteriaQuery)
        return query.resultList
    }
}
