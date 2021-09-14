package net.pelozo.mytasks.util

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


// snackbar
fun Fragment.showSnackbar(message: String, length: Int = Snackbar.LENGTH_SHORT) = view?.run { Snackbar.make(this, message, length).show()}
fun View.showSnackbar(message: String, length: Int = Snackbar.LENGTH_SHORT) = Snackbar.make(this, message, length).show()


// str -> date
fun Long.toDateString(dateFormat: Int =  DateFormat.MEDIUM): String {
    val df = DateFormat.getDateInstance(dateFormat, Locale.getDefault())
    return df.format(this)
}

fun Timestamp.toDateString(dateFormat: Int =  DateFormat.MEDIUM): String{
    val df = DateFormat.getDateInstance(dateFormat, Locale.getDefault())
    return df.format(this.toDate())
}


// dialog

fun DialogFragment.setSizePercent(width: Int, height: Int) {
    val dm = Resources.getSystem().displayMetrics
    val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
    val percentWidth = rect.width() * (width.toFloat() / 100)
    val percentHeight = rect.height() * (height.toFloat() / 100)
    dialog?.window?.setLayout(percentWidth.toInt(),percentHeight.toInt())
}
fun DialogFragment.setFullScreen() {
    dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
}