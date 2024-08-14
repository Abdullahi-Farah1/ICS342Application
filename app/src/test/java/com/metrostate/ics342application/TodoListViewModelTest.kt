package com.metrostate.ics342application

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import retrofit2.Response

class TodoListViewModelTest {

    @Test
    fun `fetchTodos() should update on successful fetch`() = runTest {
        // Arrange
        val mockResponse = mockk<Response<List<TodoItem>>>()
        val todoItems = listOf(TodoItem(1, "Test Todo", false))
        coEvery { mockResponse.isSuccessful } returns true
        coEvery { mockResponse.body() } returns todoItems

        val mockApi = mockk<TodoApiService>()
        coEvery { mockApi.getTodos(any(), any(), any()) } returns mockResponse

        val viewModel = TodoListViewModel()


        viewModel.fetchTodos("userId", "apiKey", "token")


        assertEquals(todoItems, viewModel.todoItems)
        coVerify { mockApi.getTodos(any(), any(), any()) }
    }

    @Test
    fun `fetchTodos() should not update on failed fetch`() = runTest {

        val mockResponse = mockk<Response<List<TodoItem>>>()
        coEvery { mockResponse.isSuccessful } returns false

        val mockApi = mockk<TodoApiService>()
        coEvery { mockApi.getTodos(any(), any(), any()) } returns mockResponse

        val viewModel = TodoListViewModel()


        viewModel.fetchTodos("userId", "apiKey", "token")

        // Assert
        assertEquals(emptyList<TodoItem>(), viewModel.todoItems)
        coVerify { mockApi.getTodos(any(), any(), any()) }
    }

    @Test
    fun `createTodo() should fetchTodos after successful creation`() = runTest {
        val mockResponse = mockk<Response<TodoItem>>()
        val mockFetchResponse = mockk<Response<List<TodoItem>>>()
        val todoItems = listOf(TodoItem(1, "Test Todo", false))
        coEvery { mockResponse.isSuccessful } returns true
        coEvery { mockFetchResponse.isSuccessful } returns true
        coEvery { mockFetchResponse.body() } returns todoItems

        val mockApi = mockk<TodoApiService>()
        coEvery { mockApi.createTodo(any(), any(), any(), any()) } returns mockResponse
        coEvery { mockApi.getTodos(any(), any(), any()) } returns mockFetchResponse

        val viewModel = TodoListViewModel()

        viewModel.createTodo("userId", "apiKey", "New Todo", "token")

        assertEquals(todoItems, viewModel.todoItems)
        coVerify { mockApi.createTodo(any(), any(), any(), any()) }
        coVerify { mockApi.getTodos(any(), any(), any()) }
    }

    @Test
    fun `createTodo() should not fetchTodos on failed creation`() = runTest {
        val mockResponse = mockk<Response<TodoItem>>()
        coEvery { mockResponse.isSuccessful } returns false

        val mockApi = mockk<TodoApiService>()
        coEvery { mockApi.createTodo(any(), any(), any(), any()) } returns mockResponse

        val viewModel = TodoListViewModel()

        viewModel.createTodo("userId", "apiKey", "New Todo", "token")

        assertEquals(emptyList<TodoItem>(), viewModel.todoItems)
        coVerify { mockApi.createTodo(any(), any(), any(), any()) }
        coVerify(exactly = 0) { mockApi.getTodos(any(), any(), any()) }
    }

    @Test
    fun `updateTodo() should fetchTodos after successful update`() = runTest {
        val mockResponse = mockk<Response<TodoItem>>()
        val mockFetchResponse = mockk<Response<List<TodoItem>>>()
        val todoItems = listOf(TodoItem(1, "Updated Todo", true))
        coEvery { mockResponse.isSuccessful } returns true
        coEvery { mockFetchResponse.isSuccessful } returns true
        coEvery { mockFetchResponse.body() } returns todoItems

        val mockApi = mockk<TodoApiService>()
        coEvery { mockApi.updateTodo(any(), any(), any(), any(), any()) } returns mockResponse
        coEvery { mockApi.getTodos(any(), any(), any()) } returns mockFetchResponse

        val viewModel = TodoListViewModel()

        viewModel.updateTodo("userId", 1, "apiKey", "Updated Todo", true, "token")

        assertEquals(todoItems, viewModel.todoItems)
        coVerify { mockApi.updateTodo(any(), any(), any(), any(), any()) }
        coVerify { mockApi.getTodos(any(), any(), any()) }
    }

    @Test
    fun `updateTodo() should not fetchTodos on failed update`() = runTest {
        val mockResponse = mockk<Response<TodoItem>>()
        coEvery { mockResponse.isSuccessful } returns false

        val mockApi = mockk<TodoApiService>()
        coEvery { mockApi.updateTodo(any(), any(), any(), any(), any()) } returns mockResponse

        val viewModel = TodoListViewModel()

        viewModel.updateTodo("userId", 1, "apiKey", "Updated Todo", true, "token")

        assertEquals(emptyList<TodoItem>(), viewModel.todoItems)
        coVerify { mockApi.updateTodo(any(), any(), any(), any(), any()) }
        coVerify(exactly = 0) { mockApi.getTodos(any(), any(), any()) }
    }
}
