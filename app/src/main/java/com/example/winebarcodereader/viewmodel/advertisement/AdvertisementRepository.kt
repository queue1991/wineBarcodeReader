package com.example.winebarcodereader.viewmodel.advertisement

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.example.winebarcodereader.R
import com.example.winebarcodereader.base.repository.BaseRepository
import com.example.winebarcodereader.model.DataModel
import com.example.winebarcodereader.util.SharedPreferenceUtil


/**
 * Data 관리 및 API 호출부
 */
class AdvertisementRepository(private val model: DataModel, private val context: Context) : BaseRepository(model, context) {

    /**
     * 광고 영상 uri 생성 및 return
     */
    fun getAdvertisementVideoUri(): Uri {
        val path = "android.resource://" + context.packageName + "/" + R.raw.advertisement
        return path.toUri()
    }


    fun getBarcodeType() : Int?{
        return SharedPreferenceUtil.getBarcodeType(10)
    }
}