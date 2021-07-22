package com.example.winebarcodereader.viewmodel.scan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.winebarcodereader.action.ScanningViewAction
import com.example.winebarcodereader.base.viewmodel.BaseScanningViewModel
import com.example.winebarcodereader.common.CommonValue
import com.example.winebarcodereader.model.common.APICommonValue
import java.util.*
import kotlin.concurrent.timer


/**
 * barcode 값이 유효하면 WineInfoResultActivity로 barcode값을 보냄.
 */
class ScanningViewModel(private val repository: ScanningRepository): BaseScanningViewModel(repository) {
    override fun handleSharedPreferenceChangedListenerKey(key: String) {

    }

    private var _action = MutableLiveData<ScanningViewAction>()
    val action : LiveData<ScanningViewAction> get() = _action

    lateinit var barcodeNo:String
    lateinit var winePrice:String
    var wineEventMsg:String? = null

    lateinit var timer: Timer

    /**
     * 스캔화면 -> 영상화면 로직 활성화 상태
     */
    private val advertisementActiveState = true

    /**
     * 1. WinePrice 가져오기
     * 2. 가져옴 -> WineInfoResultActivity로 이동 (barcodeNo, WinePrice)
     * 3. 못가져옴 -> WineInfoResultActivity로 이동 (barcodeNo, HAS_NO_WINE_PRICE)
     * 4. 통신실패 -> 다시 barcode
     */
    fun afterBarcoding(barcodeNo:String){
        setProgressBar(true)
        callWinePriceAPI(barcodeNo)
    }

    /**
     * 통신성공 했을 경우에 ResultActivity로 이동하는 로직
     * wineEventMsg는 Nullable
     */
    override fun goToWineInfoResultActivity(barcodeNo:String, winePrice:String, wineEventMsg:String?){
        this.barcodeNo = barcodeNo
        this.winePrice = winePrice

        this.wineEventMsg = wineEventMsg

        _action.postValue(ScanningViewAction(ScanningViewAction.ActionNo.GO_TO_RESULT_ACTIVITY))
    }


    /**
     * 통신실패 했을 경우에 다시 스캔하는 로직
     */
    override fun failGettingWinePrice(respCD:String){
        setProgressBar(false)
        when(respCD){
            APICommonValue.RESPONSE_FAIL -> {
                // wine price 가져오기 실패 시 resumeScanner
                _action.postValue(ScanningViewAction(ScanningViewAction.ActionNo.CHECK_NETWORK))
                setAdvertisementTimer()
            }
        }
    }

    /**
     * 사용자가 바코드를 특정시간 동안 찍지 않는 경우 체크
     * advertisement view Activity로 이동
     */
    fun setAdvertisementTimer(){
        if(advertisementActiveState){
            timer = timer(initialDelay = CommonValue.INITIAL_DELAY_FOR_AD , period = CommonValue.PERIOD_FOR_AD){
                Log.d("setAdvertisementTimer","setAdvertisementTimer called")
                cancelAdvertisementTimer()

                _action.postValue((ScanningViewAction(ScanningViewAction.ActionNo.GO_TO_ADVERTISEMENT_ACTIVITY)))
            }
        }else{

        }
    }

    /**
     * 사용자가 바코드를 찍을 경우 advertisementActivity로 이동하기 위한 timer cancel
     */
    fun cancelAdvertisementTimer(){
        if(advertisementActiveState){
            timer.cancel()
        }else{

        }
    }
}