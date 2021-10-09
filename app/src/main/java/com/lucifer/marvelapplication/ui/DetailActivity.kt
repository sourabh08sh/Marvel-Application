package com.lucifer.marvelapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lucifer.marvelapplication.R
import com.lucifer.marvelapplication.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        binding.name = intent.getStringExtra("name")
        binding.description = intent.getStringExtra("description")

        val url = intent.getStringExtra("imagePath")+"/portrait_uncanny."+intent.getStringExtra("imageExt")
        binding.imageUrl = url
    }

    fun onClose(view: View) {
        finish()
    }

}