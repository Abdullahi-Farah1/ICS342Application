package com.metrostate.ics342application

import retrofit2.Response
import retrofit2.http.*

interface TodoApiService {
    @POST("/api/users/register")
    suspend fun registerUser(
        @Body registerRequest: RegisterRequest,
        @Query("apikey") apiKey: String
    ): Response<RegisterResponse>

    @POST("/api/users/login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest,
        @Query("apikey") apiKey: String
    ): Response<User>

    @GET("/api/users/{user_id}/todos")
    suspend fun getTodos(
        @Path("user_id") userId: String,
        @Query("apikey") apiKey: String,
        @Header("Authorization") token: String
    ): Response<List<TodoItem>>

    @POST("/api/users/{user_id}/todos")
    suspend fun createTodo(
        @Path("user_id") userId: String,
        @Query("apikey") apiKey: String,
        @Header("Authorization") token: String,
        @Body todoRequest: TodoRequest
    ): Response<TodoItem>

    @PUT("/api/users/{user_id}/todos/{id}")
    suspend fun updateTodo(
        @Path("user_id") userId: String,
        @Path("id") todoId: Int,
        @Query("apikey") apiKey: String,
        @Header("Authorization") token: String,
        @Body todoRequest: TodoRequest
    ): Response<TodoItem>
}