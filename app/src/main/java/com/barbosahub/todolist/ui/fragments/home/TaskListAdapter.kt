package com.barbosahub.todolist.ui.fragments.home

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.barbosahub.todolist.R
import com.barbosahub.todolist.databinding.ItemTaskBinding
import com.barbosahub.todolist.datasource.model.Task

class TaskListAdapter : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(DiffCallback()) {

    var listenerEdit: (Task) -> Unit = {}
    var listenerDelete: (Task) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context))
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.itemTaskTitle.text = task.title
            binding.itemTaskDescription.text = task.description
            binding.itemTaskHour.text = task.hour
            binding.itemTaskDate.text = task.date
            binding.itemImageMore.setOnClickListener { showPopUp(task) }

            // Card view Configuration
            // disable onClicklistener in landScape mode, at least for now.
            if (binding.root.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                binding.itemTaskDescription.visibility = View.VISIBLE
                binding.itemTaskCardView.isClickable = false
                binding.itemTaskCardView.isFocusable = false
            } else {
                binding.itemTaskDescription.visibility = View.GONE
                binding.itemTaskCardView.isClickable = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    binding.itemTaskCardView.isFocusable = true
                }
                binding.itemTaskCardView.setOnClickListener {
                    with(binding.itemTaskDescription) {
                        visibility = if (visibility == View.GONE) View.VISIBLE else View.GONE
                    }
                }
            }
        }

        // menu popUp
        @SuppressLint("RestrictedApi")
        private fun showPopUp(task: Task) {
            val imgMore = binding.itemImageMore
            val popupMenu = PopupMenu(imgMore.context, imgMore)
            popupMenu.menuInflater.inflate(R.menu.menu_popup_more, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menu ->
                when (menu.itemId) {
                    R.id.action_edit -> listenerEdit(task)
                    R.id.action_delete -> listenerDelete(task)
                }
                return@setOnMenuItemClickListener true
            }

            // Show Menu Icons
            if (popupMenu.menu is MenuBuilder) {
                val menuBuilder = popupMenu.menu as MenuBuilder
                menuBuilder.setOptionalIconsVisible(true)
                for (item in menuBuilder.visibleItems) {
                    val iconMarginPx = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        4.0f,
                        binding.root.resources.displayMetrics
                    ).toInt()
                    if (item.icon != null) {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                            item.icon =
                                InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0)
                        } else {
                            item.icon = object :
                                InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0) {
                                override fun getIntrinsicWidth(): Int =
                                    intrinsicHeight + iconMarginPx + iconMarginPx
                            }
                        }
                    }
                }
            }

            popupMenu.show()
        }

    }

}

class DiffCallback : DiffUtil.ItemCallback<Task>() {

    override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem

    override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id

}


