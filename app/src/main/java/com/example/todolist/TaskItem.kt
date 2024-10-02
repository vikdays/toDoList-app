package com.example.todolist

import java.util.UUID

data class TaskItem (
    var description: String,
    var isDone: Boolean = false,
    var id: String = UUID.randomUUID().toString()
)
