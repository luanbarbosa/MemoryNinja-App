package com.luanbarbosagomes.hmr.feature

sealed class TransactionState {
    object Success: TransactionState()
    data class Fail(val error: Throwable): TransactionState()
}