package com.example.todolist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.todolist.service.RetrofitClient
import com.example.todolist.service.TaskService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    var taskItems = MutableLiveData<MutableList<TaskItem>>()
    private val taskService: TaskService = RetrofitClient.create()

    init {
        taskItems.value = mutableListOf()
        fetchTasksFromBackend()
    }

    private fun fetchTasksFromBackend() {
        taskService.getAllTasks().enqueue(object : Callback<List<TaskItem>> {
            override fun onResponse(call: Call<List<TaskItem>>, response: Response<List<TaskItem>>) {
                if (response.isSuccessful) {
                    taskItems.postValue(response.body()?.toMutableList() ?: mutableListOf())
                } else {
                    Log.e("TaskViewModel", "Error fetching tasks: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<TaskItem>>, t: Throwable) {
                Log.e("TaskViewModel", "Failed to fetch tasks: ${t.message}")
            }
        })
    }

    fun addTaskItem(newTask: CreateTask?) {
        newTask?.let {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = taskService.createTask(it).awaitResponse()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            fetchTasksFromBackend()
                        } else {
                            Log.e("TaskViewModel", "Error adding task: ${response.code()}" +
                                    " - ${response.message()}")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("TaskViewModel", "Failed to add task: ${e.message}")
                    withContext(Dispatchers.Main) {
                    }
                }
            }
        }
    }

    fun removeTaskItem(taskItem: TaskItem) {
        taskService.deleteTask(taskItem.id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    fetchTasksFromBackend()
                } else {
                    Log.e("TaskViewModel", "Error deleting task: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("TaskViewModel", "Failed to delete task: ${t.message}")
            }
        })
    }

    fun updateTaskItem(id: String, description: String, isDone: Boolean) {
        val task = TaskItem(description, isDone, id)
        taskService.updateTask(id, task).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    fetchTasksFromBackend()
                } else {
                    Log.e("TaskViewModel", "Error updating task: ${response.code()} - ${response.message()}")
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("TaskViewModel", "Failed to update task: ${t.message}")
            }
        })
    }
}
