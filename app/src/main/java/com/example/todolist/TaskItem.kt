package com.example.todolist

import android.content.Context
import androidx.core.content.ContextCompat
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID
import java.util.*


class TaskItem (
    var name: String,
    var isDone: Boolean = false,
    var id: UUID = UUID.randomUUID()
)


