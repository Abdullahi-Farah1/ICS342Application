package com.metrostate.ics342application

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.metrostate.ics342application.DataStoreUtils.dataStore
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import retrofit2.Response

class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `loginUser() should assign User object and save token on successful login`() = runTest {

        val mockResponse = mockk<Response<User>>()
        val mockUser = User(1, "John Doe", "john@example.com", true, "token123", false)
        coEvery { mockResponse.isSuccessful } returns true
        coEvery { mockResponse.body() } returns mockUser

        val mockApi = mockk<TodoApiService>()
        coEvery { mockApi.loginUser(any(), any()) } returns mockResponse

        val mockContext = mockk<Context>(relaxed = true)
        val mockDataStore = mockk<Preferences>(relaxed = true)
        val preferencesFlow = MutableStateFlow(mockDataStore)
        coEvery { mockContext.dataStore.data } returns preferencesFlow

        val viewModel = LoginViewModel()


        viewModel.loginUser("abdullahi@gmail.com", "password123", mockContext)


        assertEquals(mockUser, viewModel.user)
        coVerify { mockApi.loginUser(any(), any()) }
        coVerify { mockContext.dataStore.edit(any()) }
    }

    @Test
    fun `loginUser() should not assign User object on failed login`() = runTest {

        val mockResponse = mockk<Response<User>>()
        coEvery { mockResponse.isSuccessful } returns false
        val mockApi = mockk<TodoApiService>()
        coEvery { mockApi.loginUser(any(), any()) } returns mockResponse

        val mockContext = mockk<Context>(relaxed = true)
        val viewModel = LoginViewModel()


        viewModel.loginUser("abdullahi@gmail.com", "password123", mockContext)


        assertNull(viewModel.user)
        coVerify { mockApi.loginUser(any(), any()) }
    }
}
