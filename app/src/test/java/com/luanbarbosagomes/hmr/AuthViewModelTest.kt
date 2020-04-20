package com.luanbarbosagomes.hmr

import androidx.lifecycle.Observer
import com.google.firebase.auth.PhoneAuthProvider
import com.luanbarbosagomes.hmr.data.repository.AuthRepository
import com.luanbarbosagomes.hmr.feature.login.AuthViewModel
import com.luanbarbosagomes.hmr.feature.login.AuthViewModel.State
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify

class AuthViewModelTest : BaseViewModelTest() {

    @MockK
    lateinit var authRepository: AuthRepository

    @MockK
    lateinit var phoneAuthProvider: PhoneAuthProvider

    @Mock
    lateinit var observer: Observer<State>

    lateinit var sut: AuthViewModel

    @Before
    fun setup() {
        sut = AuthViewModel(authRepository, phoneAuthProvider)
    }

    @Test
    fun `Sign-in user as guest - success`() {
        every { runBlocking { authRepository.signInAsGuest() } } returns mockk()
        sut.state.observeForever(observer)

        sut.loginAsGuest()
        verify(observer).onChanged(State.Loading)
        verify(observer).onChanged(State.Success)
    }

    @Test
    fun `Sign-in user as guest - fail`() {
        every { runBlocking { authRepository.signInAsGuest() } } throws Boom("")
        sut.state.observeForever(observer)

        sut.loginAsGuest()
        verify(observer).onChanged(State.Loading)
        verify(observer).onChanged(State.Error(NullPointerException()))
    }

}
