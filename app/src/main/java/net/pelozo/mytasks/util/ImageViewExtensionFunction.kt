package net.pelozo.mytasks.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import net.pelozo.mytasks.R

fun ImageView.glideload(url: String) {
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.progress_anim)
        .into(this)
}