package com.metrostate.ics342application

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
import androidx.navigation.NavController
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.launch
import com.metrostate.ics342application.DataStoreUtils.dataStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: () -> Unit,
    navController: NavController  // Add NavController as a parameter
) {
    val context = LocalContext.current
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
                if (email.isNotBlank() && password.isNotBlank()) {
                    viewModel.loginUser(email, password)
                    if (viewModel.user != null) {
                        scope.launch {
                            // Store token and user id in DataStore
                            context.dataStore.edit { preferences ->
                                preferences[stringPreferencesKey("user_id")] = viewModel.user!!.id
                                preferences[stringPreferencesKey("api_key")] = viewModel.user!!.token
                            }
                            onLoginSuccess()
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
            Text("Log In")
        }
        if (showError) {
            Text(
                text = "Error logging in. Please check your email and password.",
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(
            onClick = {
                navController.navigate("createAccount")  // Navigate to CreateAccountScreen
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Account")
        }
    }
}
