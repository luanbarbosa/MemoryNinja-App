package com.luanbarbosagomes.hmr

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.luanbarbosagomes.hmr.feature.init.InitViewModel
import com.luanbarbosagomes.hmr.feature.init.InitViewModel.State
import com.luanbarbosagomes.hmr.feature.preference.PreferenceViewModel
import com.luanbarbosagomes.hmr.utils.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InitViewModelTest {

    @MockK
    lateinit var preferenceViewModel: PreferenceViewModel

    lateinit var sut: InitViewModel

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sut = InitViewModel(preferenceViewModel)
    }

    @Test
    fun `Not logged in`() {
        every { preferenceViewModel.isLoggedIn() } returns false
        assertEquals(sut.state.getOrAwaitValue(), State.LoginNeeded)
    }

    @Test
    fun `No storage option choice`() {
        every { preferenceViewModel.isLoggedIn() } returns true
        every { preferenceViewModel.storageOptionSet() } returns false
        assertEquals(sut.state.getOrAwaitValue(), State.StorageOptionNeeded)
    }

    @Test
    fun `All clear to initialize`() {
        every { preferenceViewModel.isLoggedIn() } returns true
        every { preferenceViewModel.storageOptionSet() } returns true
        assertEquals(sut.state.getOrAwaitValue(), State.Initiated)
    }

}
