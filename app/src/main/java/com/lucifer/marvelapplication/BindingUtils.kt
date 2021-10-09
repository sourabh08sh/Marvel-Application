package com.lucifer.marvelapplication

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url:String) {
            Glide.with(view.context)
            .load(url)
            //.diskCacheStrategy(DiskCacheStrategy.NONE)
            //.skipMemoryCache(true)
            .placeholder(R.drawable.ic_test_image)
            .error(R.drawable.ic_test_image)
            .into(view)
}