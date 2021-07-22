package com.example.winebarcodereader.util

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*


object DateUtil {
    fun getTodayDateByYYYYMMDD() : String{
        val simpleDataFormat = SimpleDateFormat("yyyyMMdd")
        val calendar = Calendar.getInstance()

        val result = simpleDataFormat.format(calendar.time)

        Log.d("dateUtil", "getTodayDateByYYYYMMDD :: $result")

        return result
    }

    fun getTodayDateByYYYYMMDDHHMMSS() : String{
        val simpleDataFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        val calendar = Calendar.getInstance()

        val result = simpleDataFormat.format(calendar.time)

        return result
    }
}