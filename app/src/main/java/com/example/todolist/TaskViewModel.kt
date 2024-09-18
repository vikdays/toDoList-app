package com.example.todolist
import android.app.Application
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileWriter
import java.io.FileReader
import java.io.IOException
import java.lang.reflect.Type
import java.util.UUID

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    var taskItems = MutableLiveData<MutableList<TaskItem>>()

    init {
        taskItems.value = mutableListOf()
    }

    fun addTaskItem(newTask: TaskItem) {
        val list = taskItems.value
        list!!.add(newTask)
        taskItems.postValue(list)
    }

    fun updateTaskItem(id: UUID, name: String, isDone: Boolean) {
        val list = taskItems.value
        val task = list!!.find { it.id == id }
        task?.let {
            it.name = name
            it.isDone = isDone
            taskItems.postValue(list)
        }
    }

    fun removeTaskItem(taskItem: TaskItem) {
        val list = taskItems.value
        list!!.remove(taskItem)
        taskItems.postValue(list)
    }

    fun saveTaskItemsToJsonFile(fileName: String) {
        val list = taskItems.value
        if (list.isNullOrEmpty()) {
            println("No tasks to save.")
            return
        }

        val gson = Gson()
        val jsonString = gson.toJson(list)

        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val externalStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            val file = File(externalStorageDir, fileName)

            try {
                val writer = FileWriter(file)
                writer.write(jsonString)
                writer.close()
                println("File saved successfully: ${file.absolutePath}")
            } catch (e: IOException) {
                e.printStackTrace()
                println("Error saving file: ${e.message}")
            }
        } else {
            println("External storage is not mounted or not available.")
        }
    }

    fun loadTaskItemsFromJsonFile(fileName: String) {
        val gson = Gson()
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val externalStorageDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            val file = File(externalStorageDir, fileName)

            if (file.exists()) {
                try {
                    val reader = FileReader(file)
                    val type: Type = object : TypeToken<MutableList<TaskItem>>() {}.type
                    val list: MutableList<TaskItem> = gson.fromJson(reader, type)
                    reader.close()
                    taskItems.postValue(list)
                    println("File loaded successfully: ${file.absolutePath}")
                } catch (e: IOException) {
                    e.printStackTrace()
                    println("Error loading file: ${e.message}")
                }
            } else {
                println("File does not exist: ${file.absolutePath}")
            }
        } else {
            println("External storage is not mounted or not available.")
        }
    }
}
