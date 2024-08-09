package boki.bokiportfolio.validator

object CommentValidator {

    const val INVALID_COMMENT_CONTENT_MSG = "댓글은 500글자를 넘을 수 없습니다"

    /**
     * Checks if the given content is valid.
     *
     * @param content The content to be checked.
     * @return True if the content length is less than or equal to 500, false otherwise.
     *
     * ```
     *  isValidContent("zzzzzzzzzzzzzz ... bigger than 500 length")) // false
     *
     *  isValidContent("content")) // true
     * ```
     */
    fun isValidContent(content: String): Boolean {
        return content.length <= 500
    }
}
