package com.luanbarbosagomes.hmr.data.repository

import com.luanbarbosagomes.hmr.data.Expression
import javax.inject.Inject

class QuizRepository @Inject constructor(
    private val expressionRepository: BaseExpressionRepository
) {
    fun nextQuiz(): Expression? {
        TODO("Not yet implemented")
    }


}