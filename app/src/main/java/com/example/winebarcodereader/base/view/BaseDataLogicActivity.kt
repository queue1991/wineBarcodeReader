package com.example.winebarcodereader.base.view

import android.util.Log
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import com.datalogic.decode.BarcodeManager
import com.datalogic.decode.DecodeException
import com.datalogic.decode.ReadListener
import com.datalogic.device.ErrorManager
import com.example.winebarcodereader.base.viewmodel.BaseKotlinViewModel
import java.util.*

abstract class BaseDataLogicActivity<dataBinding : ViewDataBinding, viewModel : BaseKotlinViewModel>  : BaseKotlinActivity<dataBinding, viewModel>() {
    /**
     * data logic 바코드 리더기를 위한 변수들
     */
    private var decoder: BarcodeManager? = null
    private var listener: ReadListener? = null
    protected var resultBucket: TextView? = null
    protected var afterBarcodeState = true
    protected var barcodeResultTimer: Timer? = null

    /**
     * 바코드를 찍은 후의 동작은 각각 다르기 여기서 정의하지 않음
     * barcode가 찍힌 후의 동작 정의
     */
    abstract fun setAfterBarcodeResultTimer()

    /**
     * 바코드를 찍은 후의 동작은 각각 다르기 여기서 정의하지 않음
     * ResultBucket에 결과값이 담기고 1초 후에 afterBarcoding 로직 수행
     */
    abstract fun setBarcodeResultBucket()



    /**
     * Barcode Reader에서 Result를 받기위한 listener 등록
     */
    protected fun setBarcodeResultListener(){
        // If the decoder instance is null, create it.
        if (decoder == null) { // Remember an onPause call will set it to null.
            decoder = BarcodeManager()
        }

        // From here on, we want to be notified with exceptions in case of errors.
        ErrorManager.enableExceptions(true)

        try {

            // Create an anonymous class.
            listener = ReadListener { decodeResult ->
                // Implement the callback method.
                // Change the displayed text to the current received result.

                if(afterBarcodeState){
                    // 바코드 프로세스 한번만 진행하기 위한 분기
                    resultBucket!!.text = decodeResult.text
                    resultBucket!!.setSelectAllOnFocus(true)
                }

            }


            // Remember to add it, as a listener.
            decoder!!.addReadListener(listener)


        } catch (e: DecodeException) {

        }
    }

    /**
     * Barcode Result listener 해제
     */
    protected fun removeBarcodeResultListener(){
        // If we have an instance of BarcodeManager.
        if (decoder != null) {
            try {
                // Unregister our listener from it and free resources.
                decoder!!.removeReadListener(listener)

                // Let the garbage collector take care of our reference.
                decoder = null
            } catch (e: Exception) {
                Log.e("Error", "Error while trying to remove a listener from BarcodeManager", e)
            }
        }
    }

    /**
     * 사용자가 바코드를 찍을 경우 advertisementActivity로 이동하기 위한 timer cancel
     */
    protected fun cancelSettingAfterBarcodeResultTimer(){
        barcodeResultTimer!!.cancel()
    }

    /**
     * Wine Price API(1010) 통신실패시에 Barcode Reader 활성화
     * 1. 바코드 리더기에서 불러오는 값을 담는 EditText 초기화
     * 2. Barcode Result 값을 처리 하기 위한 Flag 값 초기화
     */
    protected fun resetBarcodeResultBucket(){
        afterBarcodeState = true
        resultBucket!!.text = ""
    }
}