package com.example.winebarcodereader.viewmodel.wineinfo

import android.content.Context
import com.example.winebarcodereader.base.repository.BaseRepository
import com.example.winebarcodereader.model.DataModel
import com.example.winebarcodereader.util.HandleTextFileUtil
import com.example.winebarcodereader.util.SharedPreferenceUtil

/**
 * Data 관리 및 API 호출부
 */
class WineInfoResultRepository(private val model: DataModel, private val context: Context) : BaseRepository(model, context){

    /**
     * 사용자가 바코드 리더기를 사용하여 와인정보를 얻은 날짜 및 시간 기록
     */
    fun setCountTextFile(barcodeNo:String){
        HandleTextFileUtil.writeDateToCountingFile(barcodeNo)
    }

    fun getDeviceWidth() : Int?{
        return SharedPreferenceUtil.getScreenWidth(0)
    }

    fun getBarcodeType(): Int?{
        return SharedPreferenceUtil.getBarcodeType(0)
    }
}