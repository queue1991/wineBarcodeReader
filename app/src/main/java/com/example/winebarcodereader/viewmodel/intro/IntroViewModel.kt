package com.example.winebarcodereader.viewmodel.intro
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.winebarcodereader.action.IntroViewAction
import com.example.winebarcodereader.base.viewmodel.BaseKotlinViewModel
import com.example.winebarcodereader.common.CommonValue.BARCODE_TYPE
import com.example.winebarcodereader.common.CommonValue.DATALOGIC_PROCESS
import com.example.winebarcodereader.common.CommonValue.SCREEN_WIDTH
import com.example.winebarcodereader.common.CommonValue.ZXING_PROCESS

class IntroViewModel(private val introRepository: IntroRepository) : BaseKotlinViewModel(introRepository) {
    override fun handleSharedPreferenceChangedListenerKey(key: String) {
        when(key){
            /**
             * device screen width가 SharedPreference에 저장이 되면 호출 되는 Listener
             */
            SCREEN_WIDTH -> {
                when(barcodeProcess){
                    ZXING_PROCESS -> {
                        _action.postValue(IntroViewAction(IntroViewAction.ActionNo.GO_TO_ZXING_SCAN_ACTIVITY))
                    }

                    DATALOGIC_PROCESS -> {
                        _action.postValue(IntroViewAction(IntroViewAction.ActionNo.GO_TO_DATALOGIC_SCAN_ACTIVITY))
                    }
                }
            }
            BARCODE_TYPE -> {

            }
        }
    }

    /**
     * 바코드를 Zxing으로 할지, DataLogic으로 할지
     */
//    val barcodeProcess = DATALOGIC_PROCESS
    val barcodeProcess = ZXING_PROCESS

    private var _action = MutableLiveData<IntroViewAction>()
    val action : LiveData<IntroViewAction> get() = _action


    /**
     * repository를 통해 sharedPreference에 screenWidth 저장후 permission Check 로직 시작.
     */
    fun setDeviceWidthToSharedPreference(width:Int){
        introRepository.setDeviceWidthToSharedPreference(width)
    }

    /**
     * 바코드 타입 데이터 SharedPreference에 저장
     */
    fun setBarcodeTypeToSharedPreference(){
        introRepository.setBarcodeTypeToSharedPreference(barcodeProcess)
    }

    /**
     * repository에서 device screen width 구하는 fun
     */
    fun getDeviceScreenWidthFromSharedPreference() : Int?{
        return introRepository.getDeviceScreenWidth()
    }
}