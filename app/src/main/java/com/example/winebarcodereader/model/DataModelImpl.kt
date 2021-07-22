package com.example.winebarcodereader.model

import com.example.winebarcodereader.model.service.RetrofitService
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Single
import okhttp3.RequestBody

class DataModelImpl(private val service: RetrofitService):DataModel{
    override fun getWinePrice(
        suffix: String,
        headers: Map<String, String>,
        requestBody: RequestBody
    ): Single<JsonObject> {
        return service.callRetrofitServiceWithHeadersAndReqeustBody(suffix, headers, requestBody)
    }

}