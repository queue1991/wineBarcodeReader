package com.example.winebarcodereader.model

import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Single
import okhttp3.RequestBody


interface DataModel {
    fun getWinePrice(suffix : String, headers : Map<String,String>, requestBody : RequestBody): Single<JsonObject>

}