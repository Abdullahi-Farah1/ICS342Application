package com.metrostate.ics342application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import com.metrostate.ics342application.ui.theme.ICS342ApplicationTheme
import com.metrostate.ics342application.ui.theme.LightBlue
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: TodoListViewModel = viewModel()

            // Fetch todos when the activity is created
            val userId = "userId"  // Replace with actual userId
            val apiKey = "63e93e2f-2888-492f-af0d-d744e4bb4025"  // Replace with actual apiKey
            viewModel.fetchTodos(userId, apiKey)

            MainScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(viewModel: TodoListViewModel = viewModel()) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var todoText by remember { mutableStateOf("") }
    val todoItems by viewModel.todoItems.observeAsState(emptyList())
    var showError by remember { mutableStateOf(false) }

    ICS342ApplicationTheme {
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopBar()
        }, floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.add_24dp_fill0_wght400_grad0_opsz24),
                    contentDescription = "Add Todo"
                )
            }
        }, content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                MainContent(todoItems, onToggle = { index, isChecked ->
                    val todoItem = todoItems[index]
                    viewModel.updateTodo(todoItem.id, isChecked)
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
                                        val userId = "userId"  // Replace with actual userId
                                        val apiKey = "apiKey"  // Replace with actual apiKey
                                        viewModel.createTodo(userId, apiKey, todoText)
                                        showBottomSheet = false
                                        todoText = ""
                                        showError = false
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
                            text = todoItem.title,
                            modifier = Modifier.weight(1f),
                        )

                        Checkbox(checked = todoItem.completed,
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

@Preview(showBackground = true)
@Composable
fun ContentPreview() {
    Content()
}
