package com.lucifer.marvelapplication

import android.app.Application
import com.lucifer.marvelapplication.network.RetrofitHelper
import com.lucifer.marvelapplication.network.RetrofitService
import com.lucifer.marvelapplication.repository.MainRepository

class MainApplication : Application() {

    lateinit var repository: MainRepository
    var publicApiKey = ""
    var privateApiKey = ""

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    // as we have to initialize repository again and again so I have initialized it here to make code clean
    private fun initialize() {
        val apiService = RetrofitHelper.getInstance().create(RetrofitService::class.java)
        repository = MainRepository(apiService)
    }
}