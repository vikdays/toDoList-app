package com.example.todolist

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.databinding.FragmentNewTaskSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewTaskSheet(var taskItem: TaskItem?) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNewTaskSheetBinding
    private lateinit var taskViewModel: TaskViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskViewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)

        if (taskItem != null) {
            binding.TaskTitle.text = "Edit Task"
            binding.name.text = Editable.Factory.getInstance().newEditable(taskItem!!.description)
        } else {
            binding.TaskTitle.text = "New Task"
        }

        binding.saveButton.setOnClickListener {
            saveAction()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewTaskSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun saveAction() {
        val name = binding.name.text.toString().trim()
        if (name.isEmpty()) {
            binding.name.error = "Task name cannot be empty"
            return
        }

        val newTask = CreateTask(description = name)

        CoroutineScope(Dispatchers.Main).launch {
            if (taskItem == null) {
                taskViewModel.addTaskItem(newTask)
            } else {
                taskViewModel.updateTaskItem(taskItem!!.id, name, taskItem!!.isDone)
            }
            binding.name.text?.clear()
            dismiss()
        }
    }
}
