package com.metrostate.ics342application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    var user: User? = null
        private set

    fun loginUser(email: String, password: String) {
        val apiKey = "63e93e2f-2888-492f-af0d-d744e4bb4025"
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.loginUser(LoginRequest(email, password), apiKey)
                if (response.isSuccessful) {
                    user = response.body()?.user
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                // Handle network error
            }
        }
    }
}
