package com.example.winebarcodereader.util

import android.util.Log
import com.example.winebarcodereader.common.CommonValue
import com.example.winebarcodereader.common.CommonValue.MONETARY_UNIT
import java.text.NumberFormat

object ManageStringBufferUtil {

    /**
     * 매개변수 -> barcodeNo
     * make a wine Info ImageUrl By StringBuffer
     * val로 해도 변수의 참조값이 가리키는 값을 변경할 수 있음
     */
    fun getWineInfoImageUrl(barcodeNo: String, buffer:StringBuffer) : StringBuffer{
        buffer.append(CommonValue.WINE_INFO_IMAGE_BASE_URL)
        buffer.append(barcodeNo)
        buffer.append(CommonValue.WINE_INFO_IMAGE_DATA_FORM_PNG)

        Log.d("getWineInfoImageUrl", "getWineInfoImageUrl :: $buffer")

        return buffer
    }

    /**
     * 매개변수 -> winePrice
     * set Wine price with MonetaryUnit like won
     * val로 해도 변수의 참조값이 가리키는 값을 변경할 수 있음
     */
    fun getWinePriceWithCommaAndMonetaryUnit(winePrice:String, buffer:StringBuffer) : StringBuffer{
        var winePriceWithComma = winePrice.toDouble()

        buffer.append(NumberFormat.getInstance().format(winePriceWithComma))
        buffer.append(MONETARY_UNIT)

        return buffer
    }
}