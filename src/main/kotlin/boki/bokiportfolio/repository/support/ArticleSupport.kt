package boki.bokiportfolio.repository.support

import boki.bokiportfolio.entity.Article
import org.springframework.data.domain.Sort

fun interface ArticleSupport {
    fun findArticlesContainsTitleAndCreatedAtSortDirection(title: String?, createdAtOrderBy: Sort.Direction): MutableList<Article>
}
