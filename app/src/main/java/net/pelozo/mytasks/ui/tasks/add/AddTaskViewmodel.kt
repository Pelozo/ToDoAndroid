package net.pelozo.mytasks.ui.tasks.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.pelozo.mytasks.model.Task
import net.pelozo.mytasks.services.TaskService

class AddTaskViewmodel(private val auth: FirebaseAuth, private val taskService: TaskService): ViewModel() {

    sealed class Event {
        object CloseDialog: Event()
    }

    private val _events = Channel<Event>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onAddButtonPressed(title: String, description: String){

        auth.currentUser?.email?.let { email ->
            taskService.addTask(Task(title = title, description = description, email = email)) {
                if (it.exception == null) {
                    viewModelScope.launch {
                        _events.send(Event.CloseDialog)
                    }
                }
            }
        }

    }
}