package com.lucifer.marvelapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.lucifer.marvelapplication.MainActivity
import com.lucifer.marvelapplication.R

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        val logo: ImageView = findViewById(R.id.logo)

        Handler().postDelayed({
            logo.visibility = View.GONE
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        },2500)
    }
}