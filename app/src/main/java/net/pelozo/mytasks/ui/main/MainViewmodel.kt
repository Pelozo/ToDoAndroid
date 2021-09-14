package net.pelozo.mytasks.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewmodel(private val auth: FirebaseAuth, private val authUi: AuthUI) : ViewModel() {


    private val _loading = MutableStateFlow<Boolean>(true)
    val loading: StateFlow<Boolean> = _loading

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    sealed class Event {
        object MoveToList: Event()
        object ErrorSigningIn: Event()
        data class OpenLogIn(val intent: Intent): Event()
    }

    fun checkIfLogged() {
        viewModelScope.launch {
            _loading.value = true
            if (auth.currentUser != null ) {
                //authenticated, move to next activity
                eventChannel.send(Event.MoveToList)
            } else {
                _loading.value = false
            }
        }
    }

    fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        viewModelScope.launch {
            _loading.value = false
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                eventChannel.send((Event.MoveToList))
            } else {
                eventChannel.send(Event.ErrorSigningIn)
            }
        }
    }

    fun onSignInButtonClicked(){
        viewModelScope.launch {
            val intent = authUi.createSignInIntentBuilder()
                .setAvailableProviders(
                    listOf(
                        AuthUI.IdpConfig.GoogleBuilder().build()
                    )
                )
                .build()
            eventChannel.send(Event.OpenLogIn(intent))
        }
    }


}