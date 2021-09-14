package net.pelozo.mytasks.ui.tasks.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import net.pelozo.mytasks.R
import net.pelozo.mytasks.databinding.FragmentTaskListBinding
import net.pelozo.mytasks.model.Task
import net.pelozo.mytasks.ui.main.MainActivity
import net.pelozo.mytasks.ui.tasks.TasksActivity
import net.pelozo.mytasks.ui.tasks.add.AddTaskDialog
import net.pelozo.mytasks.ui.tasks.list.adapters.TaskAdapter
import net.pelozo.mytasks.util.showSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import net.pelozo.mytasks.ui.tasks.list.TaskListViewmodel.Event.*


class TaskListFragment : Fragment(), TaskAdapter.TaskListener {

    private lateinit var adapter: TaskAdapter
    private lateinit var binding: FragmentTaskListBinding
    private val viewmodel: TaskListViewmodel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTaskListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        adapter = TaskAdapter(emptyList(), this)
        binding.rvTasks.adapter = adapter
        binding.viewmodel = viewmodel

        binding.fab.setOnClickListener { openAddDialog() }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleEvents()

        lifecycleScope.launch {
            viewmodel.tasks.collect {
                adapter.setData(it)
            }
        }
    }

    private fun handleEvents(){
        //events from viewmodel
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.events.collect { event ->
                    when(event) {
                        is AskForDeleteConfirmation -> openDeleteDialog(event.task)
                        is ErrorRemovingTask -> binding.root.showSnackbar(getString(R.string.error_removing_task))
                        is TaskRemoved -> binding.root.showSnackbar(getString(R.string.task_removed))
                        is MoveToLogin -> startActivity(Intent(activity, MainActivity::class.java))
                    }
                }
            }
        }

        //clicks on toolbar
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.logout -> {
                    viewmodel.onLogoutClicked()
                    true
                }
                else -> false
            }
        }
    }


    override fun onTaskClicked(id: Task) {
        viewmodel.onTaskClicked(id)
    }

    override fun onTaskLongClicked(id: Task) {
        viewmodel.onTaskLongClicked(id)
    }

    private fun openDeleteDialog(task: Task){
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(getString(R.string.delete_task_confirmation, task.title))
            .setPositiveButton(getString(R.string.yes)){ _, _ -> viewmodel.deleteTask(task) }
            .setNegativeButton(getString(R.string.no)){ dialog, _ -> dialog.dismiss()}
            .show()
    }

    private fun openAddDialog(){
        AddTaskDialog().show(childFragmentManager, null)
    }


}