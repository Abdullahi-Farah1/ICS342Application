package com.metrostate.ics342application

data class TodoItem(
    val id: Int,
    val description: String,
    val completed: Any,  // As handled previously
    val meta: Map<String, Any>? = emptyMap()  // Allow meta to be nullable and provide a default value
) {
    val isCompleted: Boolean
        get() = when (completed) {
            is Boolean -> completed
            is Number -> completed.toInt() != 0
            else -> false
        }
}


data class TodoRequest(
    val description: String,  // Change from title to description
    val completed: Boolean = false,
    val meta: Map<String, Any> = emptyMap()  // Include meta in the request as well
)

data class TodoResponse(
    val todos: List<TodoItem>
)

