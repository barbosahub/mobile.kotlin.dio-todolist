package com.barbosahub.todolist.ui.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.barbosahub.todolist.datasource.model.Task
import com.barbosahub.todolist.datasource.repository.TaskRepository

class TaskViewModelFactory(
    private val repository: TaskRepository,
    private val task: Task? = null
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java))
            return TaskViewModel(repository, task) as T

        throw IllegalArgumentException("Unknown view model class")
    }

}