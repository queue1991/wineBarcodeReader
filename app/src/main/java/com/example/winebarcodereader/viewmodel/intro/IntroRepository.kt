package com.example.winebarcodereader.viewmodel.intro

import android.content.Context
import android.util.Log
import com.example.winebarcodereader.base.repository.BaseRepository
import com.example.winebarcodereader.model.DataModel
import com.example.winebarcodereader.util.SharedPreferenceUtil

/**
 * Data 관리 및 API 호출부
 */
class IntroRepository(private val model: DataModel,private val context: Context) : BaseRepository(model, context) {

    /**
     * screen width 저장
     */
    fun setDeviceWidthToSharedPreference(width:Int){
        Log.d("setDeviceWidthToSharedPreference", "setDeviceWidthToSharedPreference :: $width")
        SharedPreferenceUtil.putScreenWidth(width)
    }

    fun getDeviceScreenWidth() : Int?{
        return SharedPreferenceUtil.getScreenWidth()
    }

    /**
     * BarcodeType 저장
     */
    fun setBarcodeTypeToSharedPreference(barcodeType:Int){
        Log.d("setBarcodeTypeToSharedPreference", "setBarcodeTypeToSharedPreference :: $barcodeType")
        SharedPreferenceUtil.putBarcodeType(barcodeType)
    }
}