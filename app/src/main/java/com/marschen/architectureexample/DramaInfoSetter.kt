package com.marschen.architectureexample

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


@BindingAdapter("bind:imageUrl")
fun loadImage(view: ImageView, url: String?) {
    if (url == null) return
    Glide.with(view.getContext()).load(url)
        .into(view)
}