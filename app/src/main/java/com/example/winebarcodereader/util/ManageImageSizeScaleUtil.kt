package com.example.winebarcodereader.util

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log

object ManageImageSizeScaleUtil {

    /**
     * 이미지의 Width를 Device의 Width에 맞추고 Width가 조절된 비율에 맞게 Heigth도 조절
     * return resized image
     */
    fun getResizedImage(bitmap: Bitmap, screenWidth:Int?) : Bitmap{
        var resizedBitmap:Bitmap = bitmap

        var width:Int = bitmap.width

        if(width != screenWidth){
            var height:Int = bitmap.height
            var scale:Float
            var matrix = Matrix()

            scale = (screenWidth!!.toFloat() / width)
            matrix.postScale(scale, scale)

            resizedBitmap = Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true)
        }

        Log.d("resized Bitmap","resized bitmap Test :: " + resizedBitmap.width)
        Log.d("resized Bitmap","resized bitmap Test :: " + resizedBitmap.height)


        return resizedBitmap
    }
}