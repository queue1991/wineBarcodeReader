package com.example.winebarcodereader.base.viewmodel

import android.util.Log
import com.example.winebarcodereader.base.repository.BaseRepository
import com.example.winebarcodereader.common.CommonValue
import com.example.winebarcodereader.model.apiresp.WinePriceApiDTO
import com.example.winebarcodereader.model.common.APICommonValue
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Wine Price API 호출 후 와인정보 페이지로 이동이 필요한 View의 ViewModel
 */
abstract class BaseScanningViewModel(private val repository: BaseRepository) : BaseKotlinViewModel(repository) {
    override fun handleSharedPreferenceChangedListenerKey(key: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    /**
     * Wine Price API 호출 후
     */
    fun callWinePriceAPI(barcodeNo : String){
        // call wine price API from DatModel
        addDisposable(repository.callWinePriceAPI(barcodeNo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.run{
                    val winePriceApiDTO  = Gson().fromJson(it.toString(), WinePriceApiDTO::class.java)

                    Log.d(this.javaClass.name, "RetrofitService onSuccess :: $it")
                    setProgressBar(false)

                    if(!winePriceApiDTO.RES_CD.isNullOrEmpty()){
                        when(winePriceApiDTO.RES_CD){
                            /**
                             * Response Code 0000
                             */
                            APICommonValue.RESPONSE_SUCCESS -> {
                                if(!winePriceApiDTO.SALE_PRICE.isNullOrEmpty()){
                                    goToWineInfoResultActivity(barcodeNo, winePriceApiDTO.SALE_PRICE!!,winePriceApiDTO.EVENT_MSG)
                                }else{
                                    goToWineInfoResultActivity(barcodeNo, CommonValue.HAS_NO_WINE_PRICE,null)
                                }
                            }
                            /**
                             * 와인의 경우 19세 이상에게 파는 제품이기때문에 Response code가 RESPONSE_SUCCESS(0000)이 아닌 RESPONSE_UNDER_AGE(2001)가 뜰 수 있음
                             * 2001이 떠도 0000과 동일한 로직
                             */
                            APICommonValue.RESPONSE_UNDER_AGE ->{
                                if(!winePriceApiDTO.SALE_PRICE.isNullOrEmpty()){
                                    goToWineInfoResultActivity(barcodeNo, winePriceApiDTO.SALE_PRICE!!,winePriceApiDTO.EVENT_MSG)
                                }else{
                                    goToWineInfoResultActivity(barcodeNo, CommonValue.HAS_NO_WINE_PRICE,null)
                                }
                            }
                            /**
                             * Response Code 0000 / 2001이 아닌 경우
                             */
                            else -> {
                                goToWineInfoResultActivity(barcodeNo, CommonValue.HAS_NO_WINE_PRICE,null)
                            }
                        }
                    }

                }
            },{
                failGettingWinePrice(APICommonValue.RESPONSE_FAIL)
            })
        )

    }

    /**
     * 자식 클래스에서 재정의
     * 와인정보 결과 화면으로 이동하는 로직
     */
    protected open fun goToWineInfoResultActivity(barcodeNo:String, winePrice:String, wineEventMsg:String?){

    }

    /**
     * 자식 클래스에서 재정의
     * 와인 가격 호출 실패 시
     */
    protected open fun failGettingWinePrice(respCD:String){

    }
}