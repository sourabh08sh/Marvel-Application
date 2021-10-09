package com.lucifer.marvelapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucifer.marvelapplication.MainApplication
import com.lucifer.marvelapplication.models.character.CharacterList
import com.lucifer.marvelapplication.repository.MainRepository
import com.lucifer.marvelapplication.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest

class CharacterViewModel(private val repository: MainRepository) : ViewModel() {
    private var apikey = MainApplication().publicApiKey
    lateinit var ts: String
    lateinit var hash: String
    private var offset: Int = 0
    private var name: String? = null

    init {
        // whenever this view model get initialized I want it to get characters data for me. So that's why I have defined this fn. in init block.
        getCharactersData()
    }

    private fun getCharactersData() {
        initializeVariables() // this function is to initialize timestamp and hash that is needed to get data from marvel api.

        viewModelScope.launch(Dispatchers.IO){
            repository.getCharacters(20, offset, name, apikey, ts, hash)
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
            Log.d("hash", hash)
        }
    }

    //this function is helping in pagination and it is also getting the search string if user have searched for some character
    fun pagination(searchString: String?, offset: Int) {
        this.offset = offset
        name = searchString
        getCharactersData()
    }

    val characters : LiveData<Response<CharacterList>>
    get() = repository.characters // this live data is pointing on the live data that is present in repository
}