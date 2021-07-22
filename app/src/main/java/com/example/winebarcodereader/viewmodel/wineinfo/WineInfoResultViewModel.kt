package com.example.winebarcodereader.viewmodel.wineinfo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.winebarcodereader.action.WineInfoResultViewAction
import com.example.winebarcodereader.base.viewmodel.BaseKotlinViewModel
import com.example.winebarcodereader.common.CommonValue
import com.example.winebarcodereader.util.ManageStringBufferUtil
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timer




class WineInfoResultViewModel(private val wineInfoResultRepository: WineInfoResultRepository): BaseKotlinViewModel(wineInfoResultRepository) {
    override fun handleSharedPreferenceChangedListenerKey(key: String) {

    }

    var winePrice = MutableLiveData<String>("")
    var wineInfoImageUrl = MutableLiveData<String>()
    var wineEventMsg = MutableLiveData<String>()
    var poketCUBarcodeString = MutableLiveData<String>()
    var deviceWidth : Int? = 0

    private var _action = MutableLiveData<WineInfoResultViewAction>()
    val action : LiveData<WineInfoResultViewAction> get() = _action

    lateinit var timer: Timer

    init {
        viewModelScope.launch {
            Log.d(this.javaClass.name, "WineInfoResultViewModel >> init setp~~~~")
        }
    }

    /**
     * set wine Price and Image
     * 가격이 없으면 -> set Ready Image
     * 가격이 있으면 -> set Image By Glide
     */
    fun setWineInfo(winePrice:String, barcodeNo:String, wineEventMsg:String?){
        Log.d(this.javaClass.name, "setWineInfo winePrice :: $winePrice")
        Log.d(this.javaClass.name, "setWineInfo barcodeNo :: $barcodeNo")
        Log.d(this.javaClass.name, "setWineInfo wineEventMsg :: $wineEventMsg")

        if(!winePrice.isNullOrEmpty()){
            if(winePrice != CommonValue.HAS_NO_WINE_PRICE){
                // 와인 가격 UI 처리
                setWinePrice(winePrice)

                // 와인 정보 이미지 처리
                setWineInfoImage(barcodeNo)

//                // set poket cu barcode
//                setPoketCUBarcode(barcodeNo)

                // 와인 이벤트 정보 UI 처리
                setWineEventMsg(wineEventMsg)

                // 사용자 사용 횟수 카운트
                setCountTextFile(barcodeNo)


            }else{
                // 가격이 없는 경우 준비중 이미지 처리
                _action.postValue(WineInfoResultViewAction(WineInfoResultViewAction.ActionNo.SET_READY_IMAGE))
            }
        }
    }

    /**
     * wine url 생성 및 set wine Info Image
     */
    private fun setWineInfoImage(barcodeNo:String){
        var urlBuffer = StringBuffer()

        if(!barcodeNo.isNullOrEmpty()){

            urlBuffer = ManageStringBufferUtil.getWineInfoImageUrl(barcodeNo, urlBuffer)

            deviceWidth = wineInfoResultRepository.getDeviceWidth()

            this.wineInfoImageUrl.postValue(urlBuffer.toString())

            urlBuffer.setLength(0)
        }
    }

    /**
     * pocket cu로 이동하는 barcode setting
     */
    private fun setPoketCUBarcode(barcodeNo : String){
        poketCUBarcodeString.postValue("bgfcuk://introactivity")
    }

    /**
     * wineEventMsg에 값이 있으면 wineEvent값 UI 처리
     */
    private fun setWineEventMsg(wineEventMsg:String?){
        // 와인 이벤트 정보 UI 처리
        if(!wineEventMsg.isNullOrEmpty())
            this.wineEventMsg.postValue(wineEventMsg)
    }

    /**
     * winePrice 값 UI 처리
     */
    private fun setWinePrice(winePrice:String){
        var priceBuffer = StringBuffer()

        // 와인 가격 Comma처리 및 돈 단위 처리
        priceBuffer = ManageStringBufferUtil.getWinePriceWithCommaAndMonetaryUnit(winePrice,priceBuffer)

        // 와인 가격 UI 처리
        this.winePrice.postValue(priceBuffer.toString())

        // 버퍼 메모리 해제
        priceBuffer.setLength(0)
    }

    /**
     * WineInfo 페이지에서 특정 시간이 지나면 Scan Activity로 이동
     */
    fun setGoBackToScanActivityTimer(){
        timer = timer(initialDelay = CommonValue.INITIAL_DELAY_FOR_GOING_BACK_TO_SCAN , period = CommonValue.PERIOD_FOR_GOING_BACK_TO_SCAN){
            _action.postValue(WineInfoResultViewAction(WineInfoResultViewAction.ActionNo.GO_TO_BARCODE_ACTIVITY))
        }
    }

    /**
     * Scan Activity로 이동하기 위한 timer cancel
     */
    fun cancelTimer(){
        timer.cancel()
    }

    /**
     * 사용자가 화면을 클릭하면 restart Timer
     */
    fun restartTimer(){
        cancelTimer()
        setGoBackToScanActivityTimer()
    }

    /**
     * 사용자가 바코드 리더기를 사용하여 와인정보를 얻은 날짜 및 시간 기록
     */
    private fun setCountTextFile(barcodeNo:String){
        wineInfoResultRepository.setCountTextFile(barcodeNo)
    }

    fun getBarcodeType() : Int?{
        return wineInfoResultRepository.getBarcodeType()
    }
}