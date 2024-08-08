package com.metrostate.ics342application

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val enabled: Any,
    val token: String,
    val admin: Any,
)

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
