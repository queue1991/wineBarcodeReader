package com.example.winebarcodereader.viewmodel.advertisement

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.winebarcodereader.action.AdvertisementViewAction
import com.example.winebarcodereader.base.viewmodel.BaseScanningViewModel
import com.example.winebarcodereader.model.common.APICommonValue

class AdvertisementViewModel(private val advertisementRepository: AdvertisementRepository) : BaseScanningViewModel(advertisementRepository) {
    override fun handleSharedPreferenceChangedListenerKey(key: String) {

    }

    private var _videoUri = MutableLiveData<Uri>()
    val videoUri : LiveData<Uri> get() = _videoUri

    private var _action = MutableLiveData<AdvertisementViewAction>()
    val action : LiveData<AdvertisementViewAction> get() = _action

    lateinit var barcodeNo:String
    lateinit var winePrice:String

    var wineEventMsg:String? = null


    /**
     * 광고영상 파일 재생
     */
    fun setAdvertisementVideoFromFile(){
        _videoUri.postValue(advertisementRepository.getAdvertisementVideoUri())
    }

    /**
     * 광고영상 파일 멈춤
     */
    fun setVideoStopPlay(){
        _videoUri.postValue(null)
    }

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

        _action.postValue(AdvertisementViewAction(AdvertisementViewAction.ActionNo.GO_TO_RESULT_ACTIVITY))
    }

    /**
     * 통신실패 했을 경우에 다시 스캔하는 로직
     */
    override fun failGettingWinePrice(respCd:String){
        when(respCd){
            APICommonValue.RESPONSE_FAIL -> {
                // wine price 가져오기 실패 시 resumeScanner
                _action.postValue(AdvertisementViewAction(AdvertisementViewAction.ActionNo.CHECK_NETWORK))
            }
        }
    }

    fun getBarcodeType() : Int?{
        return advertisementRepository.getBarcodeType()
    }
}