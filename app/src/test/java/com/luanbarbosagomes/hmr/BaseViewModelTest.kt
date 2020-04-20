package com.luanbarbosagomes.hmr

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import org.junit.Before
import org.junit.Rule
import org.mockito.MockitoAnnotations

open class BaseViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun basicSetup() {
        MockKAnnotations.init(this)
        MockitoAnnotations.initMocks(this)
    }
}

class Boom(message: String) : Throwable(message)