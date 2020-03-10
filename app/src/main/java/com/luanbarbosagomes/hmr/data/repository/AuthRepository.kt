package com.luanbarbosagomes.hmr.data.repository

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class AuthRepository @Inject constructor(private val auth : FirebaseAuth) {

    fun logout() = auth.signOut()

}