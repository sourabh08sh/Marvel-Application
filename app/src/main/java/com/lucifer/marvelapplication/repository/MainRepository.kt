package com.lucifer.marvelapplication.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lucifer.marvelapplication.models.character.CharacterList
import com.lucifer.marvelapplication.models.comic.ComicList
import com.lucifer.marvelapplication.network.RetrofitService
import java.lang.Exception

/* developed repository to handle data fetching from network and it provides data to view model.
* it holds references to data source to execute functions for accessing data.*/

class MainRepository(private val apiService: RetrofitService) {

    // developed a mutable live data so we can do changes and as it is a live data we will get to know whenever a change happen.
    private val charactersLiveData = MutableLiveData<Response<CharacterList>>()
    private val comicsLiveData = MutableLiveData<Response<ComicList>>()
    private var characterResponse: CharacterList? = null
    private var comicResponse: ComicList? = null
    private var helperVariable: String? = null

    val characters: LiveData<Response<CharacterList>>
    get() = charactersLiveData

    val comic: LiveData<Response<ComicList>>
    get() = comicsLiveData

    // function to get marvel character data
    suspend fun getCharacters(limit: Int, offset:Int?, name:String?, apikey:String?, ts:String?, hash:String?){
        try {
            val result = apiService.getCharacters(limit, offset, name, apikey, ts, hash)
            if (result.body() != null){
                if (helperVariable == name){
                    // this is for checking if user is scrolling so we can add new data in old data
                    charactersLiveData.postValue(handleGetCharacterResponse(result))
                }else{
                    // this is for checking if user is searching for character so we can remove previous data and add new data
                    helperVariable = name
                    characterResponse = result.body()
                    charactersLiveData.postValue(Response.Success(result.body()))
                }
            } else{
                charactersLiveData.postValue(Response.Error("API Error"))
            }
        }
        catch (e: Exception){
            charactersLiveData.postValue(Response.Error(e.message.toString()))
        }
    }

    // function to handle character pagination and new data in old data
    private fun handleGetCharacterResponse(result: retrofit2.Response<CharacterList>): Response<CharacterList> {
        if(result.isSuccessful) {
            result.body()?.let { resultResponse ->
                if(characterResponse == null) {
                    characterResponse = resultResponse
                } else {
                    val oldCharacterResult = characterResponse!!.data.results
                    val newCharacterResult = resultResponse.data.results
                    oldCharacterResult.addAll(newCharacterResult)
                }
                return Response.Success(characterResponse ?: resultResponse)
            }
        }
        return Response.Error(result.message())
    }

    // function to get marvel comics data
    suspend fun getComics(limit: Int, offset:Int?, dateDescriptor:String?, apikey:String?, ts:String?, hash:String?){
        try {
            val resultComicList = apiService.getComics(limit, offset, dateDescriptor, apikey, ts, hash)
            if (resultComicList.body() != null){
                if (helperVariable == dateDescriptor){
                    // this is for checking if user is scrolling so we can add new data in old data
                    comicsLiveData.postValue(handleGetComicResponse(resultComicList))
                }else{
                    // this is for checking if user is searching for character so we can remove previous data and add new data
                    helperVariable = dateDescriptor
                    comicResponse = resultComicList.body()
                    comicsLiveData.postValue(Response.Success(resultComicList.body()))
                }
            } else{
                comicsLiveData.postValue(Response.Error("API Error"))
            }
        }
        catch (e: Exception){
            comicsLiveData.postValue(Response.Error(e.message.toString()))
        }
    }

    // function to handle comic pagination and new data in old data
    private fun handleGetComicResponse(result: retrofit2.Response<ComicList>): Response<ComicList> {
        if(result.isSuccessful) {
            result.body()?.let { resultResponse ->
                if(comicResponse == null) {
                    comicResponse = resultResponse
                } else {
                    val oldComicResult = comicResponse!!.data.results
                    val newComicResult = resultResponse.data.results
                    oldComicResult.addAll(newComicResult)
                }
                return Response.Success(comicResponse ?: resultResponse)
            }
        }
        return Response.Error(result.message())
    }
}