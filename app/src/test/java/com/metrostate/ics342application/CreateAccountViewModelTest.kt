package com.metrostate.ics342application

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import retrofit2.Response

class CreateAccountViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `registerUser() should assign User object on successful registration`() = runTest {

        val mockResponse = mockk<Response<RegisterResponse>>()
        val mockRegisterResponse = RegisterResponse(1, "Abdullahi Farah", "abdulllahi@gmail.com", true, "token123", false)
        coEvery { mockResponse.isSuccessful } returns true
        coEvery { mockResponse.body() } returns mockRegisterResponse
        val mockApi = mockk<TodoApiService>()
        coEvery { mockApi.registerUser(any(), any()) } returns mockResponse

        val viewModel = CreateAccountViewModel()


        viewModel.registerUser("Abdullahi Farah", "abdullahi@gmail.com", "password123")


        assertEquals(mockRegisterResponse, viewModel.user)
        coVerify { mockApi.registerUser(any(), any()) }
    }

    @Test
    fun `registerUser() should not assign User object on failed registration`() = runTest {

        val mockResponse = mockk<Response<RegisterResponse>>()
        coEvery { mockResponse.isSuccessful } returns false
        val mockApi = mockk<TodoApiService>()
        coEvery { mockApi.registerUser(any(), any()) } returns mockResponse

        val viewModel = CreateAccountViewModel()


        viewModel.registerUser("Abdullahi Farah", "abdullahi@gmail.com", "password123")


        assertNull(viewModel.user)
        coVerify { mockApi.registerUser(any(), any()) }
    }
}
