package net.pelozo.mytasks.ui.tasks.list

import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.pelozo.mytasks.model.Task
import net.pelozo.mytasks.services.TaskService

class TaskListViewmodel(private val auth: FirebaseAuth, private val taskService: TaskService): ViewModel(){

    sealed class Event {
        object MoveToLogin: Event()
        object TaskRemoved: Event()
        object ErrorRemovingTask: Event()
        data class AskForDeleteConfirmation(val task: Task): Event()
    }


    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _events = Channel<Event>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init{
        viewModelScope.launch {
            auth.currentUser?.email?.let { email->
                taskService.getUserTasks(email).collect {
                    _tasks.value = it
                }
            }
        }
    }

    fun onTaskClicked(task: Task) {
        viewModelScope.launch {
            taskService.updateTaskStatus(task.id, !task.isDone)
        }
    }

    fun onTaskLongClicked(task: Task) {
        viewModelScope.launch {
            _events.send(Event.AskForDeleteConfirmation(task))
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskService.removeTask(task.id){
                if(it.isSuccessful) {
                    viewModelScope.launch {
                        _events.send(Event.TaskRemoved)
                    }
                }
            }
        }
    }

    fun onLogoutClicked() {
        viewModelScope.launch {
            auth.signOut()
            _events.send(Event.MoveToLogin)
        }
    }
}