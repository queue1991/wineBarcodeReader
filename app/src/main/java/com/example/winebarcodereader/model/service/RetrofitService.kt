package com.example.winebarcodereader.model.service

import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Single
import okhttp3.RequestBody
import retrofit2.http.*


interface RetrofitService {
    /**
     * call Retrofit Service Without Header And RequestBody
     */
    @GET("{suffix}")
    fun callRetrofitServiceWithoutHeaderAndRequestBody(@Path(value = "suffix", encoded = true) suffix : String): Single<JsonObject>

    /**
     * call Retrofit Servic eWith Headers
     */
    @POST("{suffix}")
    fun callRetrofitServiceWithHeaders(@Path(value = "suffix", encoded = true) suffix : String, @HeaderMap headers:Map<String,String>): Single<JsonObject>


    /**
     * call RetrofitService With Headers And ReqeustBody
     */
    @POST("{suffix}")
    fun callRetrofitServiceWithHeadersAndReqeustBody(@Path(value = "suffix", encoded = true) suffix : String, @HeaderMap headers : Map<String,String>, @Body requestBody : RequestBody): Single<JsonObject>

}