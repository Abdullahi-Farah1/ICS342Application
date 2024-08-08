package com.metrostate.ics342application

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Log

class CreateAccountViewModel : ViewModel() {
    var user by mutableStateOf<User?>(null)
        private set

    fun registerUser(name: String, email: String, password: String) {
        val apiKey = "4f10a22c-565b-40cc-8885-78a9d5fc34bb"
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.registerUser(
                    RegisterRequest(email,name, password),
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
                            admin = it.admin,
                        )
                        Log.d("RegisterUser", "User object assigned successfully: $user")
                    }
                } else {
                    Log.d("RegisterUser", "API Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.d("RegisterUser", "Network Exception: ${e.message}")
            }
        }
    }

}