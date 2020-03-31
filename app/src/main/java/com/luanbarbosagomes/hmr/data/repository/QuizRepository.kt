package com.luanbarbosagomes.hmr.data.repository

import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.Level
import javax.inject.Inject
import kotlin.random.Random

class QuizRepository @Inject constructor(
    private val expressionRepository: BaseExpressionRepository
) {
    suspend fun nextQuiz(): Expression? {
        val allExpressions = expressionRepository.getAll()
        val lotteryExpressions = mutableListOf<Expression>()

        allExpressions.forEach { expression ->
            val expressionLevel = expression.currentLevel.toLevel()
            if (Random.nextInt(0, 101) <= expressionLevel.priorityPercentage) {
                lotteryExpressions.add(expression)
            }
        }

        return when {
            allExpressions.isEmpty() -> null
            lotteryExpressions.isEmpty() -> allExpressions.random()
            else -> lotteryExpressions.random()
        }
    }


}

private fun Int.toLevel(): Level =
    when (this) {
        in 1..10 -> Level.BASIC
        in 11..20 -> Level.BASIC
        in 21..30 -> Level.INTERMEDIATE
        in 31..40 -> Level.ADVANCED
        else -> Level.KNOWN
    }
