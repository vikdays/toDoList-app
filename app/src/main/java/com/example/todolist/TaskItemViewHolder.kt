package com.example.todolist

import android.content.Context
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.TaskItemCellBinding
import java.time.format.DateTimeFormatter
import java.util.PrimitiveIterator

class TaskItemViewHolder(
    private val binding: TaskItemCellBinding,
    private val clickListener: TaskItemClickListener
): RecyclerView.ViewHolder(binding.root)
{

    fun bindTaskItem(taskItem: TaskItem) {
        binding.name.text = taskItem.name
        binding.checkBox.isChecked = taskItem.isDone
        binding.taskCellContainer.setOnClickListener{
            clickListener.editTaskItem(taskItem)
        }
        binding.deleteButton.setOnClickListener {
            clickListener.deleteTaskItem(taskItem)
        }
        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            taskItem.isDone = isChecked
            clickListener.updateTaskItem(taskItem)  // Обновление в модели
        }

    }

}