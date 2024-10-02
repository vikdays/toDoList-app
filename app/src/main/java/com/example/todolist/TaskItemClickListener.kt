package com.example.todolist

interface TaskItemClickListener {
    fun editTaskItem(taskItem: TaskItem)
    fun deleteTaskItem(taskItem: TaskItem)
    fun updateTaskItem(taskItem: TaskItem)
}