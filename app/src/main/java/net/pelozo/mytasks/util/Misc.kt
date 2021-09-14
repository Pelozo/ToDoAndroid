package net.pelozo.mytasks.util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.SignInButton
import kotlinx.coroutines.flow.StateFlow
import net.pelozo.mytasks.model.Task
import net.pelozo.mytasks.ui.tasks.list.adapters.TaskAdapter

//https://stackoverflow.com/questions/51842041/google-signinbuttons-onclick-doesnt-work-using-databinding
@BindingAdapter("android:onClick")
fun bindSignInClick(button: SignInButton, method: () -> Unit) {
    button.setOnClickListener { method.invoke() }
}

