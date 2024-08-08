package com.metrostate.ics342application

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(onLoginSuccess = { navController.navigate("todolist") }, navController = navController)
        }
        composable("createAccount") {
            CreateAccountScreen(onCreateSuccess = { navController.navigate("todolist") }, navController = navController)
        }
        composable("todolist") {
            ToDoListScreen() // Your TodoList content
        }
    }
}
