package com.luanbarbosagomes.hmr.data.repository

import com.luanbarbosagomes.hmr.App.Companion.anonymousUser
import com.luanbarbosagomes.hmr.data.Expression
import javax.inject.Inject

class ExpressionRepository @Inject constructor() : BaseExpressionRepository() {

    @Inject
    lateinit var localRepository: LocalExpressionRepository

    @Inject
    lateinit var remoteRepository: RemoteExpressionRepository

    private val repository: BaseExpressionRepository
        get() = if (anonymousUser) localRepository else remoteRepository

    override suspend fun save(expression: Expression) = repository.save(expression)

    override suspend fun getAll(): List<Expression> = repository.getAll()

    override suspend fun deleteAll() = repository.deleteAll()

    override suspend fun getRandom(): Expression? = repository.getRandom()

    override suspend fun get(uid: Long): Expression? = repository.get(uid)

    override suspend fun get(stringUid: String): Expression? = repository.get(stringUid)
}