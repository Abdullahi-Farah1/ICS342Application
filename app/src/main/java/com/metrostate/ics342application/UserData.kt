package com.metrostate.ics342application

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val enabled: Any,
    val token: String,
    val admin: Any,
) {
    val isEnabled: Boolean
        get() = when (enabled) {
            is Boolean -> enabled
            is Number -> enabled.toInt() != 0
            else -> false
        }

    val isAdmin: Boolean
        get() = when (admin) {
            is Boolean -> admin
            is Number -> admin.toInt() != 0
            else -> false
        }

    val enabledAsInt: Int
        get() = if (isEnabled) 1 else 0

    val adminAsInt: Int
        get() = if (isAdmin) 1 else 0
}

data class RegisterRequest(
    val email: String,
    val name: String,
    val password: String
)

data class RegisterResponse(
    val id: Int,
    val name: String,
    val email: String,
    val enabled: Boolean,
    val token: String,
    val admin: Boolean,
)

data class LoginRequest(
    val email: String,
    val password: String
)
