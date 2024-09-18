package com.example.todolist

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import java.lang.reflect.Type

class MainActivity : AppCompatActivity(), TaskItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var taskViewModel: TaskViewModel
    private val fileName = "todo_list.json"

    // Register an ActivityResultLauncher for opening the file picker
    private val filePickerLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    loadJSONFromUri(uri)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)
        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        binding.newTaskButton.setOnClickListener {
            NewTaskSheet(null).show(supportFragmentManager, "newTaskTag")
        }
        setRecucleView()
    }

    private fun setRecucleView() {
        taskViewModel.taskItems.observe(this) {
            binding.toDoListRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = TaskItemAdapter(it, this@MainActivity)
            }
        }
    }

    override fun editTaskItem(taskItem: TaskItem) {
        NewTaskSheet(taskItem).show(supportFragmentManager, "newTaskTag")
    }

    override fun deleteTaskItem(taskItem: TaskItem) {
        taskViewModel.removeTaskItem(taskItem)
    }

    override fun updateTaskItem(taskItem: TaskItem) {
        taskViewModel.updateTaskItem(taskItem.id, taskItem.name, taskItem.isDone)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                taskViewModel.saveTaskItemsToJsonFile(fileName)
                Toast.makeText(this, "Data saved to $fileName", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_load -> {
                openFilePicker()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "application/json"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        filePickerLauncher.launch(intent)
    }

    private fun loadJSONFromUri(uri: Uri) {
        try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val reader = InputStreamReader(inputStream)
                val gson = Gson()
                val taskListType: Type = object : TypeToken<List<TaskItem>>() {}.type
                val tasks: List<TaskItem> = gson.fromJson(reader, taskListType)
                taskViewModel.taskItems.postValue(tasks.toMutableList())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to load JSON file", Toast.LENGTH_SHORT).show()
        }
    }
}
