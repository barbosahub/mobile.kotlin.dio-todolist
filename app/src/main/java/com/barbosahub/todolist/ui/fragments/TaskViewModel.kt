package com.barbosahub.todolist.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.barbosahub.todolist.datasource.model.Task
import com.barbosahub.todolist.datasource.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(private val taskRepository: TaskRepository, var task: Task?) : ViewModel() {

    val taskList: LiveData<List<Task>> = taskRepository.list.asLiveData()

    internal fun insertTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.insertTask(task)
    }

    internal fun findById() = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.findById(task?.id ?: -1)
    }

    internal fun deleteTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.deleteTask(task)
    }

    internal fun deleteCompletedTasks() = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.deleteCompletedTasks()
    }

    internal fun updateTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.updateTask(task)
    }

}
