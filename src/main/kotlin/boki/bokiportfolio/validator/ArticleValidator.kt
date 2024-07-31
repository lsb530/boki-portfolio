package boki.bokiportfolio.validator

object ArticleValidator {

    const val INVALID_ARTICLE_TITLE_MSG = "제목은 200글자를 넘을 수 없습니다"
    const val INVALID_ARTICLE_CONTENT_MSG = "내용은 1000글자를 넘을 수 없습니다"

    /**
     * Checks if the given title is valid.
     *
     * @param title The title to be checked.
     * @return True if the title length is less than or equal to 200, false otherwise.
     *
     * ```
     *  isValidTitle("zzzzzzzzzzzzzz ... bigger than 200 length")) // false
     *
     *  isValidTitle("title")) // true
     * ```
     */
    fun isValidTitle(title: String): Boolean {
        return title.length <= 200
    }

    /**
     * Checks if the given content is valid.
     *
     * @param content The content to be checked.
     * @return True if the content length is less than or equal to 1000, false otherwise.
     *
     * ```
     *  isValidContent("zzzzzzzzzzzzzz ... bigger than 1000 length")) // false
     *
     *  isValidContent("content")) // true
     * ```
     */
    fun isValidContent(content: String): Boolean {
        return content.length <= 1000
    }
}
