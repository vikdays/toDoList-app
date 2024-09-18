package com.example.todolist

import java.util.UUID

interface TaskItemClickListener {
    fun editTaskItem(taskItem: TaskItem)
    fun deleteTaskItem(taskItem: TaskItem)
    fun updateTaskItem(taskItem: TaskItem)
}