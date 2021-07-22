package com.example.winebarcodereader.base.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.winebarcodereader.model.DataModel
import com.example.winebarcodereader.model.common.APICommonValue
import com.example.winebarcodereader.model.common.APICommonValue.WINE_PRICE_CONTENT_TYPE_KEY
import com.example.winebarcodereader.model.common.APICommonValue.WINE_PRICE_CONTENT_TYPE_VALUE
import com.example.winebarcodereader.model.common.APICommonValue.WINE_PRICE_COOKIE_KEY
import com.example.winebarcodereader.model.common.APICommonValue.WINE_PRICE_COOKIE_VALUE
import com.example.winebarcodereader.model.common.APICommonValue.WINE_PRICE_MOBILE_ID_KEY
import com.example.winebarcodereader.model.common.APICommonValue.WINE_PRICE_MOBILE_ID_VALUE
import com.example.winebarcodereader.model.common.APICommonValue.WINE_PRICE_MSG_VER_KEY
import com.example.winebarcodereader.model.common.APICommonValue.WINE_PRICE_MSG_VER_VALUE
import com.example.winebarcodereader.model.common.APICommonValue.WINE_PRICE_PMOD_YN_KEY
import com.example.winebarcodereader.model.common.APICommonValue.WINE_PRICE_PMOD_YN_VALUE
import com.example.winebarcodereader.model.common.APICommonValue.WINE_PRICE_POS_NO_KEY
import com.example.winebarcodereader.model.common.APICommonValue.WINE_PRICE_POS_NO_VALUE
import com.example.winebarcodereader.model.common.APICommonValue.WINE_PRICE_SYSTEM_TY_KEY
import com.example.winebarcodereader.model.common.APICommonValue.WINE_PRICE_SYSTEM_TY_VALUE
import com.example.winebarcodereader.model.common.APICommonValue.WINE_PRICE_TRAN_NO_KEY
import com.example.winebarcodereader.model.common.APICommonValue.WINE_PRICE_TRAN_NO_VALUE
import com.example.winebarcodereader.util.DateUtil
import com.example.winebarcodereader.util.SharedPreferenceUtil
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Single
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

abstract class BaseRepository(private val model : DataModel, private val context: Context){

    fun registerSharedPreferenceChangedListner(sharedPreferenceListener: SharedPreferences.OnSharedPreferenceChangeListener){
        SharedPreferenceUtil.registerSharedPreferenceChangedListner(sharedPreferenceListener)
    }

    fun unregisterSharedPreferenceChangedListner(sharedPreferenceListener: SharedPreferences.OnSharedPreferenceChangeListener){
        SharedPreferenceUtil.unregisterSharedPreferenceChangedListner(sharedPreferenceListener)
    }


    /**
     * item.do(WINE_PRICE_POST_SUFFIX) 1010  호출
     */
    fun callWinePriceAPI(barcode:String) : Single<JsonObject> {
        return model.getWinePrice(APICommonValue.WINE_PRICE_POST_SUFFIX, getHeadersForCallingWinePriceAPI(), getRequestJsonForCallingWinePriceAPI(barcode))
    }

    /**
     * WINE_PRICE_MSG_TY_KEY - 1010
     * WINE_PRICE_TRAN_YMD_KEY - 오늘 날짜
     * WINE_PRICE_STORE_CD_KEY - 와인 점포
     */
    private fun getHeadersForCallingWinePriceAPI() : Map<String,String>{
        val headers = mutableMapOf<String,String>()
        headers[APICommonValue.WINE_PRICE_MSG_TY_KEY] = APICommonValue.WINE_PRICE_MSG_TY_VALUE
        headers[APICommonValue.WINE_PRICE_TRAN_YMD_KEY] = DateUtil.getTodayDateByYYYYMMDD()
        headers[APICommonValue.WINE_PRICE_STORE_CD_KEY] = SharedPreferenceUtil.getStoreCode(APICommonValue.WINE_PRICE_STORE_CD_VALUE)!!

        return addCommonHeaders(headers)
    }

    /**
     * WINE_PRICE_INPUT_TY_KEY - S
     * WINE_PRICE_SCAN_CD_KEY - barcode No
     * WINE_PRICE_SALE_QTY_KEY - 1
     */
    private fun getRequestJsonForCallingWinePriceAPI(barcodeNo : String) : RequestBody {
        val json = JSONObject()
        json.put(APICommonValue.WINE_PRICE_INPUT_TY_KEY, APICommonValue.WINE_PRICE_INPUT_TY_VALUE)
        json.put(APICommonValue.WINE_PRICE_SCAN_CD_KEY,barcodeNo) // barcodeNo
        json.put(APICommonValue.WINE_PRICE_SALE_QTY_KEY, APICommonValue.WINE_PRICE_SALE_QTY_VALUE)

        val requestBody: RequestBody = RequestBody.create(
            MediaType.parse(APICommonValue.MEDIA_TYPE_JSON_UTF8),
            json.toString()
        )

        return requestBody
    }

    /**
     * 공통 Header 추가
     */
    protected fun addCommonHeaders(headers : MutableMap<String,String>) : Map<String,String>{
        headers[WINE_PRICE_CONTENT_TYPE_KEY] = WINE_PRICE_CONTENT_TYPE_VALUE
        headers[WINE_PRICE_MSG_VER_KEY] = WINE_PRICE_MSG_VER_VALUE
        headers[WINE_PRICE_SYSTEM_TY_KEY] = WINE_PRICE_SYSTEM_TY_VALUE
        headers[WINE_PRICE_POS_NO_KEY] = WINE_PRICE_POS_NO_VALUE
        headers[WINE_PRICE_TRAN_NO_KEY] = WINE_PRICE_TRAN_NO_VALUE
        headers[WINE_PRICE_PMOD_YN_KEY] = WINE_PRICE_PMOD_YN_VALUE
        headers[WINE_PRICE_MOBILE_ID_KEY] = WINE_PRICE_MOBILE_ID_VALUE
        headers[WINE_PRICE_COOKIE_KEY] = WINE_PRICE_COOKIE_VALUE

        return headers
    }

}