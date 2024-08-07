package com.metrostate.ics342application

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.metrostate.ics342application.DataStoreUtils.dataStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountScreen(
    viewModel: CreateAccountViewModel = viewModel(),
    onCreateSuccess: () -> Unit,
    navController: NavController  // Add NavController parameter
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }  // Add name state
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        painter = painterResource(id = if (showPassword) R.drawable.visibility_24dp_e8eaed_fill0_wght400_grad0_opsz24 else R.drawable.visibility_off_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                        contentDescription = if (showPassword) "Hide password" else "Show password"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                    viewModel.registerUser(name, email, password)
                    if (viewModel.user != null) {
                        scope.launch {
                            context.dataStore.edit { preferences ->
                                preferences[stringPreferencesKey("user_id")] = viewModel.user!!.id
                                preferences[stringPreferencesKey("api_key")] = viewModel.user!!.token
                            }
                            onCreateSuccess()
                            navController.navigate("todolist")  // Navigate to TodoList
                        }
                    } else {
                        showError = true
                    }
                } else {
                    showError = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Account")
        }

        if (showError) {
            Text(
                text = "Error creating account. Please try again.",
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(
            onClick = {
                navController.navigate("login")  // Navigate to LoginScreen
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log In")
        }
    }
}
