package boki.bokiportfolio.aop

@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
/**
 * usage:
 * ```
 * @AdminOrMine(targetId = "#articleId")
 * ```
 */
annotation class AdminOrMine(val targetId: String)
