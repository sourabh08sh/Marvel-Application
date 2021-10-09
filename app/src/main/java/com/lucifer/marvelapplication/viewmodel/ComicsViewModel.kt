package com.lucifer.marvelapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucifer.marvelapplication.MainApplication
import com.lucifer.marvelapplication.models.comic.ComicList
import com.lucifer.marvelapplication.repository.MainRepository
import com.lucifer.marvelapplication.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest

class ComicsViewModel(private val repository: MainRepository) : ViewModel() {
    private var apikey = MainApplication().publicApiKey
    lateinit var ts: String
    lateinit var hash: String
    private var offset: Int = 0
    private var dateDescriptor: String? = null

    init {
        getComicsData()
    }

    private fun getComicsData() {
        initializeVariables()

        viewModelScope.launch(Dispatchers.IO){
            repository.getComics(20, offset, dateDescriptor, apikey, ts, hash)
        }
    }

    private fun initializeVariables() {
        ts = (System.currentTimeMillis() / 1000).toString()
        Log.d("timestamp", ts)

        try {
            val text = ts+MainApplication().privateApiKey+apikey
            val md5 = MessageDigest.getInstance("MD5")
            hash = BigInteger(1, md5.digest(text.toByteArray())).toString(16).padStart(32, '0')
            Log.d("hash", hash)
        } catch ( e: Exception ) {
            hash = "error: ${e.message}"
        }
    }

    fun pagination(selectedFilter: String?, offset: Int) {
        this.offset = offset
        dateDescriptor = selectedFilter
        Log.d("filterView", dateDescriptor.toString())
        getComicsData()
    }

    val comic : LiveData<Response<ComicList>>
    get() = repository.comic
}