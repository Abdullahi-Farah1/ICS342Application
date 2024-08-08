package com.metrostate.ics342application


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.metrostate.ics342application.ui.theme.ICS342ApplicationTheme
import com.metrostate.ics342application.ui.theme.LightBlue
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.stringPreferencesKey
import com.metrostate.ics342application.DataStoreUtils.dataStore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoListScreen(viewModel: TodoListViewModel = viewModel()) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var todoText by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val userId = context.dataStore.data.map { preferences ->
            preferences[stringPreferencesKey("user_id")] ?: ""
        }.first()
        val token = context.dataStore.data.map { preferences ->
            preferences[stringPreferencesKey("auth_token")] ?: ""
        }.first()
        val apiKey = "afb8eb8d-2a63-4b0d-9aa3-c63cad5b7412"
        viewModel.fetchTodos(userId, apiKey, token)
    }

    ICS342ApplicationTheme {
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopBar()
        }, floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.add_24dp_fill0_wght400_grad0_opsz24),
                    contentDescription = stringResource(id = R.string.add_todo)
                )
            }
        }, content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                MainContent(viewModel.todoItems, onToggle = { index, isChecked ->
                    val todoItem = viewModel.todoItems[index]
                    scope.launch {
                        val userId = context.dataStore.data.map { preferences ->
                            preferences[stringPreferencesKey("user_id")] ?: ""
                        }.first()
                        val token = context.dataStore.data.map { preferences ->
                            preferences[stringPreferencesKey("auth_token")] ?: ""
                        }.first()
                        val apiKey = "afb8eb8d-2a63-4b0d-9aa3-c63cad5b7412"
                        viewModel.updateTodo(userId, todoItem.id, apiKey, todoItem.description, isChecked, token)
                    }
                })
                if (showBottomSheet) {
                    ModalBottomSheet(
                        modifier = Modifier.fillMaxHeight(),
                        containerColor = LightBlue,
                        onDismissRequest = { showBottomSheet = false },
                        sheetState = sheetState
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                                value = todoText,
                                onValueChange = { todoText = it },
                                label = { Text(stringResource(id = R.string.new_todo)) },
                                trailingIcon = {
                                    if (todoText.isNotEmpty()) {
                                        IconButton(onClick = { todoText = "" }) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.cancel_24dp_fill0_wght400_grad0_opsz24),
                                                contentDescription = stringResource(id = R.string.clear_text)
                                            )
                                        }
                                    }
                                })
                            if (showError) {
                                Text(
                                    stringResource(id = R.string.please_enter_todo),
                                    color = Color.Red
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Button(onClick = {
                                    if (todoText.isBlank()) {
                                        showError = true
                                    } else {

                                        scope.launch {
                                            val userId = context.dataStore.data.map { preferences ->
                                                preferences[stringPreferencesKey("user_id")] ?: ""
                                            }.first()
                                            val token = context.dataStore.data.map { preferences ->
                                                preferences[stringPreferencesKey("auth_token")] ?: ""
                                            }.first()
                                            val apiKey = "afb8eb8d-2a63-4b0d-9aa3-c63cad5b7412"
                                            viewModel.createTodo(userId, apiKey, todoText, token)
                                            showBottomSheet = false
                                            todoText = ""
                                            showError = false
                                        }
                                    }
                                }, modifier = Modifier.fillMaxWidth()) {
                                    Text(stringResource(id = R.string.save))
                                }
                                OutlinedButton(
                                    onClick = {
                                        showBottomSheet = false
                                        todoText = ""
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Text(stringResource(id = R.string.cancel))
                                }
                            }
                        }
                    }
                }
            }
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.todo),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = LightBlue,
            titleContentColor = Color.Black,
        )
    )
}

@Composable
fun MainContent(
    todoItems: List<TodoItem>,
    onToggle: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TodoCard {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                todoItems.forEachIndexed { index, todoItem ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = todoItem.description,
                            modifier = Modifier.weight(1f),
                        )

                        Checkbox(checked = todoItem.isCompleted,
                            onCheckedChange = { isChecked -> onToggle(index, isChecked) })

                    }
                }
            }
        }
    }
}

@Composable
fun TodoCard(content: @Composable () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = LightBlue,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 12.dp)
            .height(248.dp)
            .shadow(elevation = 12.dp)
    ) {
        content()
    }
}

