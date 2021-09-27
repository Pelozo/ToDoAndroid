package net.pelozo.mytasks.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp

data class Task(
    @get:Exclude var id: String = "",
    @ServerTimestamp val createAt: Timestamp? = null,
    val title: String = "",
    val description: String = "",
    val email: String = "",
    val image: String? = null,
    @field:JvmField val isDone: Boolean = false
){
    fun withId(id: String): Task {
        this.id = id
        return this
    }
}