package com.example.todolist

import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.TaskItemCellBinding

class TaskItemViewHolder(
    private val binding: TaskItemCellBinding,
    private val clickListener: TaskItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bindTaskItem(taskItem: TaskItem) {
        binding.name.text = taskItem.description
        binding.checkBox.isChecked = taskItem.isDone
        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            taskItem.isDone = isChecked
            clickListener.updateTaskItem(taskItem)
        }
        binding.taskCellContainer.setOnClickListener {
            clickListener.editTaskItem(taskItem)
        }
        binding.deleteButton.setOnClickListener {
            clickListener.deleteTaskItem(taskItem)
        }

        if (taskItem.isDone) {
            binding.name.paintFlags = binding.name.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            binding.name.paintFlags = binding.name.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
}
