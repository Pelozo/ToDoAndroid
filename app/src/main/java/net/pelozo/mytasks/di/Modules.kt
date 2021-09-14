package net.pelozo.mytasks.di

import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import net.pelozo.mytasks.services.TaskService
import net.pelozo.mytasks.ui.main.MainViewmodel
import net.pelozo.mytasks.ui.tasks.add.AddTaskViewmodel
import net.pelozo.mytasks.ui.tasks.list.TaskListViewmodel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    single { TaskService(get()) }
}

val viewModelModule = module {
    viewModel { MainViewmodel(get(), get()) }
    viewModel { TaskListViewmodel(get(), get()) }
    viewModel { AddTaskViewmodel(get(), get()) }
}


val firebaseModule = module{
    fun provideFirestore(): FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }
    fun provideAuth():  FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    fun provideAuthUI(): AuthUI {
        return AuthUI.getInstance()
    }

    single { provideFirestore() }
    single { provideAuth() }
    single { provideAuthUI() }
}
