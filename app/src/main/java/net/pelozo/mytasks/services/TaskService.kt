package net.pelozo.mytasks.services

import com.google.firebase.firestore.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import net.pelozo.mytasks.model.Task

class TaskService(private val db: FirebaseFirestore) {

    private val collectionName = "tasks"

    fun getUserTasks(userEmail: String): Flow<List<Task>> = callbackFlow{
        val listener = db.collection(collectionName)
            .whereEqualTo("email", userEmail)
            .orderBy("createAt", Query.Direction.DESCENDING) //create firestore index --> https://github.com/invertase/react-native-firebase/issues/568
            .addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
                if (firebaseFirestoreException != null) {
                    cancel(message = "Error fetching tasks",
                        cause = firebaseFirestoreException)
                    return@addSnapshotListener
                }
                val map = querySnapshot?.documents?.mapNotNull{it.toObject(Task::class.java)?.withId(it.id);}!!
                trySend(map)
            }
        awaitClose {
            listener.remove()
        }
    }

    fun addTask(task: Task, onCompleteListener: (firebaseTask: com.google.android.gms.tasks.Task<DocumentReference>) -> Unit = {}){
        db.collection(collectionName)
            .add(task)
            .addOnCompleteListener{onCompleteListener.invoke(it)}
    }


    fun removeTask(taskId: String, onCompleteListener: (firebaseTask: com.google.android.gms.tasks.Task<Void>) -> Unit = {}){
        db.collection(collectionName).document(taskId)
            .delete()
            .addOnCompleteListener{onCompleteListener.invoke(it)}
    }

    fun updateTaskStatus(taskId: String, isDone: Boolean, onCompleteListener: (firebaseTask: com.google.android.gms.tasks.Task<Void>) -> Unit = {}){
        db.collection(collectionName).document(taskId)
            .update("isDone", isDone)
            .addOnCompleteListener{onCompleteListener.invoke(it)}
    }
}


