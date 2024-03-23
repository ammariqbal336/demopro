package com.base.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import javax.inject.Inject


class DataBindingAdapters @Inject constructor() {

    @BindingAdapter("load")
    fun loadImage(view: ImageView, url: String) {
        val glide = Glide.with(view)
        glide.load(url).into(view)
    }

}
