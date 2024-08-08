package com.metrostate.ics342application

data class TodoItem(
    val id: Int,
    val description: String,
    val completed: Any,
    val meta: Map<String, Any>? = emptyMap()
) {
    val isCompleted: Boolean
        get() = when (completed) {
            is Boolean -> completed
            is Number -> completed.toInt() != 0
            else -> false
        }
}


data class TodoRequest(
    val description: String,
    val completed: Boolean = false,
    val meta: Map<String, Any> = emptyMap()
)

data class TodoResponse(
    val todos: List<TodoItem>
)

