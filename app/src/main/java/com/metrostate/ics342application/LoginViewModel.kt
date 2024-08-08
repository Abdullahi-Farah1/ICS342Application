package com.metrostate.ics342application

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.metrostate.ics342application.DataStoreUtils.dataStore

class LoginViewModel : ViewModel() {
    var user by mutableStateOf<User?>(null)
        private set

    fun loginUser(email: String, password: String, context: Context) {
        val apiKey = "afb8eb8d-2a63-4b0d-9aa3-c63cad5b7412"
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.loginUser(LoginRequest(email, password), apiKey)
                if (response.isSuccessful) {
                    user = response.body()
                    Log.d("LoginUser", "User object assigned successfully: $user")


                    user?.let {
                        context.dataStore.edit { preferences ->
                            preferences[stringPreferencesKey("auth_token")] = it.token
                            preferences[stringPreferencesKey("user_id")] = it.id.toString()
                        }
                    }
                } else {
                    Log.d("LoginUser", "API Error: ${response.code()} - ${response.message()}")
                    response.errorBody()?.let {
                        Log.d("LoginUser", "Error body: ${it.string()}")
                    }
                }
            } catch (e: Exception) {
                Log.d("LoginUser", "Network Exception: ${e.message}")
            }
        }
    }

}