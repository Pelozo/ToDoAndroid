package net.pelozo.mytasks.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.NonCancellable.cancel
import kotlinx.coroutines.flow.collect
import net.pelozo.mytasks.R
import net.pelozo.mytasks.databinding.ActivityMainBinding
import net.pelozo.mytasks.model.Task
import net.pelozo.mytasks.ui.main.MainViewmodel.*
import net.pelozo.mytasks.ui.tasks.TasksActivity
import net.pelozo.mytasks.util.showSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.google.android.gms.ads.initialization.InitializationStatus

import com.google.android.gms.ads.initialization.OnInitializationCompleteListener

import com.google.android.gms.ads.MobileAds





class MainActivity : AppCompatActivity() {

    private val viewmodel: MainViewmodel by viewModel()
    lateinit var binding: ActivityMainBinding

    private val signInLauncher = registerForActivityResult(FirebaseAuthUIActivityResultContract()) {
            result: FirebaseAuthUIAuthenticationResult -> onSignInResult(result)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewmodel = viewmodel

        handleEvents()
        subscribeToGlobalTopic()
        viewmodel.checkIfLogged()

    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        viewmodel.onSignInResult(result)
    }

    private fun handleEvents(){
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.eventsFlow.collect { event->
                    when(event) {
                        is Event.OpenLogIn -> signInLauncher.launch(event.intent)
                        is Event.MoveToList -> startActivity(Intent(this@MainActivity, TasksActivity::class.java))
                        is Event.ErrorSigningIn -> binding.root.showSnackbar(getString(R.string.error_signing_up), Snackbar.LENGTH_LONG)
                    }
                }
            }
        }
    }

    private fun subscribeToGlobalTopic(){
        Firebase.messaging.subscribeToTopic("global")
            .addOnCompleteListener { task ->
                println("subscribed! + $task")
            }
    }

}

