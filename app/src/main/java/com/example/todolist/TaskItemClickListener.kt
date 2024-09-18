package com.example.todolist

interface TaskItemClickListener {
    fun editTaskItem(taskItem: TaskItem)
    fun deleteTaskItem(taskItem: TaskItem)
}