package net.pelozo.mytasks.ui.tasks.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collect
import net.pelozo.mytasks.databinding.DialogAddTaskBinding
import net.pelozo.mytasks.util.setFullScreen
import org.koin.androidx.viewmodel.ext.android.viewModel
import net.pelozo.mytasks.ui.tasks.add.AddTaskViewmodel.Event.*

class AddTaskDialog : DialogFragment() {

    private lateinit var binding: DialogAddTaskBinding
    private val viewmodel: AddTaskViewmodel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddTaskBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewmodel = viewmodel
        handleEvents()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setFullScreen()
    }

    private fun handleEvents(){
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.events.collect { event->
                    when(event) {
                        is CloseDialog ->{dismiss()}
                    }
                }
            }
        }
    }
}