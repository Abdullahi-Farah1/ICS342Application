package com.metrostate.ics342application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Log

class CreateAccountViewModel : ViewModel() {
    var user: User? = null
        private set

    fun registerUser(name: String, email: String, password: String) {
        val apiKey = "63e93e2f-2888-492f-af0d-d744e4bb4025"
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.registerUser(
                    RegisterRequest(name, email, password),
                    apiKey
                )
                if (response.isSuccessful) {
                    response.body()?.let {
                        user = User(
                            id = it.id,
                            token = it.token,
                            email = it.email,
                            name = it.name,
                            enabled = it.enabled,
                            admin = it.admin
                        )
                        Log.d("RegisterUser", "User object assigned successfully: $user")
                    }
                } else {
                    Log.d("RegisterUser", "API Error: ${response.code()} - ${response.message()}")
                    // Handle API error
                }
            } catch (e: Exception) {
                Log.d("RegisterUser", "Network Exception: ${e.message}")
                // Handle network error
            }
        }
    }
}
