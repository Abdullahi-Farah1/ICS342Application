package com.metrostate.ics342application

data class User(
    val id: String,
    val token: String,
    val email: String,
    val name: String,
    val enabled: Boolean,
    val admin: Boolean
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)


data class RegisterResponse(
    val name: String,
    val email: String,
    val enabled: Boolean,
    val token: String,
    val admin: Boolean,
    val id: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val user: User
)
