package com.example.winebarcodereader.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.winebarcodereader.common.CommonValue.BARCODE_TYPE
import com.example.winebarcodereader.common.CommonValue.DEFAULT_BARCODE_TYPE
import com.example.winebarcodereader.common.CommonValue.DEFAULT_SCREEN_WIDTH
import com.example.winebarcodereader.common.CommonValue.PREFERENCE_NAME
import com.example.winebarcodereader.common.CommonValue.SCREEN_WIDTH
import com.example.winebarcodereader.common.CommonValue.STORE_CODE

object SharedPreferenceUtil {

    private lateinit var preferences: SharedPreferences

    fun init(context:Context){
        preferences = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
    }

    fun getSharedPreferences(): SharedPreferences {
        return preferences
    }


    fun registerSharedPreferenceChangedListner(listener : SharedPreferences.OnSharedPreferenceChangeListener){
        getSharedPreferences().registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterSharedPreferenceChangedListner(listener : SharedPreferences.OnSharedPreferenceChangeListener){
        getSharedPreferences().unregisterOnSharedPreferenceChangeListener(listener)
    }


    fun getScreenWidth(defaultValue: Int? = DEFAULT_SCREEN_WIDTH): Int? {
        return defaultValue?.let { preferences.getInt(SCREEN_WIDTH, it) }
    }

    fun putScreenWidth(valueInt: Int?) {
        val editor = preferences.edit()
        valueInt?.let { editor.putInt(SCREEN_WIDTH, it) }
        editor.apply()
    }

    fun getBarcodeType(defaultValue: Int? = DEFAULT_BARCODE_TYPE): Int? {
        return defaultValue?.let { preferences.getInt(BARCODE_TYPE, it) }
    }

    fun putBarcodeType(valueInt: Int?) {
        val editor = preferences.edit()
        valueInt?.let { editor.putInt(BARCODE_TYPE, it) }
        editor.apply()
    }

    /**
     * 점포코드 저장
     */
    fun putStoreCode(storeCode:String?){
        val editor = getSharedPreferences().edit()
        storeCode?.let {editor.putString(STORE_CODE, it)}
        editor.apply()
    }

    /**
     * 점포코드 return
     */
    fun getStoreCode(storeCode:String?):String?{
        return storeCode?.let{ getSharedPreferences().getString(STORE_CODE, it) }
    }


}
