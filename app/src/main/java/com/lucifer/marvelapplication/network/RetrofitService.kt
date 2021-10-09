package com.lucifer.marvelapplication.network

import com.lucifer.marvelapplication.models.character.CharacterList
import com.lucifer.marvelapplication.models.comic.ComicList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {

    // these are input operations so we don't want them to run on main thread. So for running them on background thread we are using suspend.

    @GET("characters")
    suspend fun getCharacters(@Query("limit") limit:Int,
                              @Query("offset") offset:Int?,
                              @Query("nameStartsWith") name:String?,
                              @Query("apikey") apikey:String?,
                              @Query("ts") ts:String?,
                              @Query("hash") hash:String?) : Response<CharacterList>

    @GET("comics")
    suspend fun getComics(@Query("limit") limit:Int,
                          @Query("offset") offset:Int?,
                          @Query("dateDescriptor") dateDescriptor:String?,
                          @Query("apikey") apikey:String?,
                          @Query("ts") ts:String?,
                          @Query("hash") hash:String?) : Response<ComicList>

}