package com.metrostate.ics342application

data class TodoItem(
    val id: String,
    val title: String,
    val completed: Boolean
)

data class TodoRequest(
    val title: String,
    val completed: Boolean = false
)

data class TodoResponse(
    val todos: List<TodoItem>
)
