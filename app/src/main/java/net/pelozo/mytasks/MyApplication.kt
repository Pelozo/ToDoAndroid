package net.pelozo.mytasks

import android.app.Application
import com.google.firebase.FirebaseApp
import net.pelozo.mytasks.di.appModule
import net.pelozo.mytasks.di.firebaseModule
import net.pelozo.mytasks.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext

class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        // Start firebase
        FirebaseApp.initializeApp(this)

        // Start Koin
        GlobalContext.startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(appModule, viewModelModule,firebaseModule)
        }
    }
}