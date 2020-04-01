package com.luanbarbosagomes.hmr.data.repository

import com.luanbarbosagomes.hmr.data.Expression
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt

class QuizRepository @Inject constructor(
    private val expressionRepository: BaseExpressionRepository
) {
    suspend fun nextQuiz(): Expression? {
        val allExpressions = expressionRepository.getAll()

        if (allExpressions.isEmpty()) return null

        while (true) {
            val target = Random.nextInt(IntRange(0, 100))
            with (allExpressions.random()) {
                if (currentLevel >= target) return this
            }
        }
    }

}

