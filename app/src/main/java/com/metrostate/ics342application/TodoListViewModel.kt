package com.metrostate.ics342application

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class TodoListViewModel : ViewModel() {
    var todoItems by mutableStateOf<List<TodoItem>>(emptyList())
        private set

    fun fetchTodos(userId: String, apiKey: String, token: String) {
        Log.d("TodoListViewModel", "Fetching todos for userId: $userId")
        viewModelScope.launch {
            try {
                val authHeader = "Bearer $token"
                val response = RetrofitInstance.api.getTodos(userId, apiKey, authHeader)
                if (response.isSuccessful) {
                    response.body()?.let {
                        todoItems = it  // This should now only contain the todos for the specific user
                        Log.d("TodoListViewModel", "Fetched todos successfully: ${it.size} items")
                    }
                } else {
                    Log.e("TodoListViewModel", "Failed to fetch todos: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("TodoListViewModel", "Network error fetching todos: ${e.message}")
            }
        }
    }


    fun createTodo(userId: String, apiKey: String, description: String, token: String) {
        Log.d("TodoListViewModel", "Creating todo for userId: $userId with description: $description")
        viewModelScope.launch {
            try {
                val authHeader = "Bearer $token"
                val response = RetrofitInstance.api.createTodo(
                    userId, apiKey, authHeader, TodoRequest(description)
                )
                if (response.isSuccessful) {
                    fetchTodos(userId, apiKey, token) // Refresh list after adding a new todo
                    Log.d("TodoListViewModel", "Todo created successfully")
                } else {
                    Log.e("TodoListViewModel", "Failed to create todo: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("TodoListViewModel", "Network error creating todo: ${e.message}")
            }
        }
    }


    fun updateTodo(userId: String, todoId: Int, apiKey: String, description: String, completed: Boolean, token: String) {
        Log.d("TodoListViewModel", "Updating todo with id: $todoId for userId: $userId")
        viewModelScope.launch {
            try {
                val authHeader = "Bearer $token"
                val todoRequest = TodoRequest(description, completed)  // Include both description and completed in the request
                Log.d("TodoListViewModel", "Request body: $todoRequest")
                val response = RetrofitInstance.api.updateTodo(
                    userId, todoId, apiKey, authHeader, todoRequest
                )
                if (response.isSuccessful) {
                    Log.d("TodoListViewModel", "Todo updated successfully")
                    fetchTodos(userId, apiKey, token) // Refresh list after updating a todo
                } else {
                    Log.e("TodoListViewModel", "Failed to update todo: ${response.code()} - ${response.message()}")
                    response.errorBody()?.let {
                        Log.e("TodoListViewModel", "Error body: ${it.string()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("TodoListViewModel", "Network error updating todo: ${e.message}")
            }
        }
    }

}

