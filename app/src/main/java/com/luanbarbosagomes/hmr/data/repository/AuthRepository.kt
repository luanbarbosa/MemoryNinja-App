package com.luanbarbosagomes.hmr.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) {

    fun logout() = firebaseAuth.signOut()

    suspend fun signInWithCredential(credential: PhoneAuthCredential): FirebaseUser =
        firebaseAuth
            .signInWithCredential(credential)
            .await()
            .user
            ?: throw FirebaseAuthException("","")

    suspend fun signInAsGuest(): FirebaseUser =
        firebaseAuth
            .signInAnonymously()
            .await()
            .user
            ?: throw FirebaseAuthException("","")
}