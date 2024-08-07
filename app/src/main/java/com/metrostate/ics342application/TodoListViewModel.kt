package com.metrostate.ics342application

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TodoListViewModel : ViewModel() {
    private val _todoItems = MutableLiveData<List<TodoItem>>(emptyList())
    val todoItems: LiveData<List<TodoItem>> get() = _todoItems

    fun fetchTodos(userId: String, apiKey: String) {
        val apiKey = "63e93e2f-2888-492f-af0d-d744e4bb4025"
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getTodos(userId, apiKey)
                if (response.isSuccessful) {
                    _todoItems.value = response.body() ?: emptyList() // Replace the list
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                // Handle network error
            }
        }
    }

    fun createTodo(userId: String, apiKey: String, title: String) {
        val apiKey = "63e93e2f-2888-492f-af0d-d744e4bb4025"
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.createTodo(
                    userId, apiKey, TodoRequest(title)
                )
                if (response.isSuccessful) {
                    fetchTodos(userId, apiKey) // Refresh list after adding a new todo
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                // Handle network error
            }
        }
    }

    fun updateTodo(todoId: String, completed: Boolean) {
        val userId = "userId"  // Replace with actual userId
        val apiKey = "63e93e2f-2888-492f-af0d-d744e4bb4025"  // Replace with actual apiKey

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.updateTodo(
                    userId, todoId, apiKey, TodoRequest("", completed)
                )
                if (response.isSuccessful) {
                    fetchTodos(userId, apiKey) // Refresh list after updating a todo
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                // Handle network error
            }
        }
    }
}
