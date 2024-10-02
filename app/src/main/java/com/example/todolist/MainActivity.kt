package com.example.todolist

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), TaskItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)

        binding.newTaskButton.setOnClickListener {
            NewTaskSheet(null).show(supportFragmentManager, "newTaskTag")
        }

        setRecyclerView()
    }

    private fun setRecyclerView() {
        taskViewModel.taskItems.observe(this) { tasks ->
            displayTasks(tasks)
        }
    }

    private fun displayTasks(tasks: List<TaskItem>) {
        binding.toDoListRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = TaskItemAdapter(tasks, this@MainActivity)
        }
    }

    override fun editTaskItem(taskItem: TaskItem) {
        NewTaskSheet(taskItem).show(supportFragmentManager, "newTaskTag")
    }

    override fun deleteTaskItem(taskItem: TaskItem) {
        taskViewModel.removeTaskItem(taskItem)
    }

    override fun updateTaskItem(taskItem: TaskItem) {
        taskViewModel.updateTaskItem(taskItem.id, taskItem.description, taskItem.isDone)
    }
}
