package boki.bokiportfolio.repository

import boki.bokiportfolio.entity.Article
import boki.bokiportfolio.repository.support.ArticleSupport
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository : JpaRepository<Article, Long>, ArticleSupport
