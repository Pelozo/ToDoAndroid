package net.pelozo.mytasks.ui.tasks.list.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import net.pelozo.mytasks.R
import net.pelozo.mytasks.model.Task
import net.pelozo.mytasks.util.glideload
import net.pelozo.mytasks.util.toDateString


class TaskAdapter(private var items: List<Task>, private val listener: TaskListener): RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    interface TaskListener{
        fun onTaskClicked(id: Task)
        fun onTaskLongClicked(id: Task)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

       val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
       return ViewHolder(view)
    }

    override fun getItemCount() = items.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(task: Task) {
            itemView.findViewById<CardView>(R.id.cv_root)
                .setOnClickListener { listener.onTaskClicked(task) }

            itemView.findViewById<CardView>(R.id.cv_root)
                .setOnLongClickListener {
                    listener.onTaskLongClicked(task)
                    return@setOnLongClickListener true
                }

            itemView.findViewById<TextView>(R.id.tv_title).text = task.title
            itemView.findViewById<TextView>(R.id.tv_description).text = task.description
            itemView.findViewById<TextView>(R.id.tv_date).text = task.createAt?.toDateString()


            with(itemView.findViewById<ImageView>(R.id.image)){
                if (task.image != null)
                    this.glideload(task.image)
                else
                    this.visibility = View.INVISIBLE
            }




            task.isDone.let{
                if(it){
                    itemView.findViewById<ImageView>(R.id.iv_done).setColorFilter(itemView.findViewById<ImageView>(R.id.iv_done).context.getColor(R.color.primary))
                } else {
                    itemView.findViewById<ImageView>(R.id.iv_done).setColorFilter(Color.GRAY)
                }

            }
        }
    }

    fun setData(items: List<Task>) {
        this.items = items
        notifyDataSetChanged()
    }




}