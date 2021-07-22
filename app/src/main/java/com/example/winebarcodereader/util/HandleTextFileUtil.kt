package com.example.winebarcodereader.util

import android.os.Environment
import android.util.Log
import java.io.*

object HandleTextFileUtil {

    /**
     * 사용자가 바코드 리더기를 사용하여 와인정보를 얻은 날짜 및 시간 기록
     */
    fun writeDateToCountingFile(barcodeNo:String) {
        try {
            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/count.txt"

            val bw = BufferedWriter(FileWriter(path, true))

            bw.write(getLineNumber(path).toString() + " / " + DateUtil.getTodayDateByYYYYMMDDHHMMSS() + " / " + barcodeNo  + "\n")
            bw.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: $e")
        }
    }

    /**
     * Count Line
     */
    private fun getLineNumber(path:String) : Int{
        try {
            val inputStream = FileInputStream(File(path))
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val lineNumberReader = LineNumberReader(bufferedReader)
            lineNumberReader.skip(java.lang.Long.MAX_VALUE)

            return lineNumberReader.lineNumber + 1
        } catch (e: IOException) {
            return 0
            e.printStackTrace()
        }
    }

}