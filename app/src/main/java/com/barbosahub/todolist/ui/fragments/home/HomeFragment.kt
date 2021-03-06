package com.barbosahub.todolist.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.barbosahub.todolist.R
import com.barbosahub.todolist.databinding.FragmentHomeBinding
import com.barbosahub.todolist.datasource.application.TaskApplication
import com.barbosahub.todolist.ui.fragments.TaskViewModel
import com.barbosahub.todolist.ui.fragments.TaskViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val adapter: TaskListAdapter by lazy {
        TaskListAdapter()
    }
    private lateinit var viewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val viewModelFactory =
            TaskViewModelFactory((requireActivity().application as TaskApplication).repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TaskViewModel::class.java)

        setObservers()

        // Recycler View
        binding.homeFragmentRecyclerTasks.layoutManager =
            GridLayoutManager(context, resources.getInteger(R.integer.grid_column_count))
        binding.homeFragmentRecyclerTasks.adapter = adapter

        setListeners()
        setHasOptionsMenu(true)

        return binding.root
    }

    private fun setObservers() {
        viewModel.taskList.observe(viewLifecycleOwner, { tasks ->
            adapter.notifyDataSetChanged()
            adapter.submitList(tasks)
            if (tasks.isNotEmpty()) {
                binding.emptyState.emptyStateConstraint.visibility = View.GONE
                binding.homeFragmentRecyclerTasks.visibility = View.VISIBLE
            } else if (tasks.isNullOrEmpty()) {
                binding.emptyState.emptyStateConstraint.visibility = View.VISIBLE
            }
        })
    }


    private fun setListeners() {
        // Fab
        binding.fabAddTask.setOnClickListener {
            it.findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToAddTaskFragment()
            )
        }

        // item_task listeners
        adapter.listenerEdit = { task ->
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToAddTaskFragment().setTask(task)
            )
        }

        adapter.listenerDelete = { task ->
            viewModel.deleteTask(task)
        }
    }

}