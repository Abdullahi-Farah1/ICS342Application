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
import com.metrostate.ics342application.ui.theme.ICS342ApplicationTheme
import com.metrostate.ics342application.ui.theme.LightBlue
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content() {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var todoText by remember { mutableStateOf("") }
    var todoItems = remember { mutableStateListOf<Pair<String, Boolean>>() }
    var showError by remember { mutableStateOf(false) }

    ICS342ApplicationTheme {
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopBar()
        }, floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.add_24dp_fill0_wght400_grad0_opsz24),
                    contentDescription = " "
                )
            }
        }, content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                MainContent(todoItems, onToggle = { index, isChecked ->
                    todoItems[index] = todoItems[index].copy(second = isChecked)
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
                                        todoItems.add(todoText to false)
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
    todoItems: List<Pair<String, Boolean>>,
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
                            text = todoItem.first,
                            modifier = Modifier.weight(1f),
                        )

                        Checkbox(checked = todoItem.second,
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
