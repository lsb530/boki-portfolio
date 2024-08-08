package boki.bokiportfolio.aop

import boki.bokiportfolio.validator.SecurityManager
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.stereotype.Component

@Aspect
@Component
class AuthAspect {

    companion object {
        val log = LoggerFactory.getLogger(this::class.java)
    }

    @Around("@annotation(adminOrMine)")
    fun checkAuth(jointPoint: ProceedingJoinPoint, adminOrMine: AdminOrMine): Any? {
        log.info("<<<<<<<<<< 권한 체크 요청")

        val signature = jointPoint.signature as MethodSignature
        val args = jointPoint.args
        val paramNames = signature.parameterNames

        // SpEL 평가 context
        val context = StandardEvaluationContext()
        for (i in paramNames.indices) {
            context.setVariable(paramNames[i], args[i])
        }

        // SpEL 표현식 평가
        val parser: ExpressionParser = SpelExpressionParser()
        val targetId = parser.parseExpression(adminOrMine.targetId).getValue(context, String::class.java)!!

        log.info("AdminOrMine Annotation targetId: $targetId")
        SecurityManager.verifyAdminOrMine(targetId)

        log.info(">>>>>>>>>> 권한 체크 성공")
        return jointPoint.proceed()
    }

}
