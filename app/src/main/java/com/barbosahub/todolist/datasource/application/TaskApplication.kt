package com.barbosahub.todolist.datasource.application

import android.app.Application
import com.barbosahub.todolist.datasource.database.AppDatabase
import com.barbosahub.todolist.datasource.repository.TaskRepository

class TaskApplication : Application() {

    val database by lazy { AppDatabase.getInstance(this) }
    val repository by lazy { TaskRepository(database.taskDao()) }

}