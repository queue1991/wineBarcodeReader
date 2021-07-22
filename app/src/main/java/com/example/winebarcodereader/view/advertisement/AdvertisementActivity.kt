package com.example.winebarcodereader.view.advertisement

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import com.example.winebarcodereader.R
import com.example.winebarcodereader.action.AdvertisementViewAction
import com.example.winebarcodereader.base.view.BaseDataLogicActivity
import com.example.winebarcodereader.common.CommonValue
import com.example.winebarcodereader.common.CommonValue.DATALOGIC_PROCESS
import com.example.winebarcodereader.common.CommonValue.ZXING_PROCESS
import com.example.winebarcodereader.databinding.ActivityAdvertisementBinding
import com.example.winebarcodereader.util.CameraIntegratorSettingUtil
import com.example.winebarcodereader.view.scan.DataLogicScanningActivity
import com.example.winebarcodereader.view.wineinfo.WineInfoResultActivity
import com.example.winebarcodereader.viewmodel.advertisement.AdvertisementViewModel
import com.google.zxing.integration.android.IntentIntegrator
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.concurrent.timer


/**
 * Scanning Activity에서 특정 시간동안 바코드를 찍지 않으면 이동되는 Activity
 */
class AdvertisementActivity : BaseDataLogicActivity<ActivityAdvertisementBinding, AdvertisementViewModel>(){

    /**
     * 바코드를 찍은 후의 동작은 각각 다르기 떄문에 여기서 정의
     * ResultBucket에 결과값이 담기고 1초 후에 afterBarcoding 로직 수행
     */
    override fun setBarcodeResultBucket(){
        // Prevent soft keyboard from popping up while selecting text.
        resultBucket!!.showSoftInputOnFocus = false


        resultBucket!!.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(afterBarcodeState){
                    if(!s.isNullOrEmpty()) {
                        setAfterBarcodeResultTimer()
                        afterBarcodeState = false
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
    }

    /**
     * 바코드를 찍은 후의 동작은 각각 다르기 떄문에 여기서 정의
     * 바코드 기기에서 태블릿으로 결과값이 넘어온 이후에 afterBarcoding 로직 수행하기 위한 function
     * ResultBucket에 결과값이 담기고 1초 후에 afterBarcoding 로직 수행
     */
    override fun setAfterBarcodeResultTimer(){
        barcodeResultTimer = timer(initialDelay = 1000L , period = 1000L){
            viewModel.afterBarcoding(resultBucket!!.text.toString().removeRange(0,1).replace("\n",""))

            cancelSettingAfterBarcodeResultTimer()

        }
    }


    override val layoutResourceId: Int
        get() = R.layout.activity_advertisement

    override val parentView: ViewGroup
        get() = findViewById(R.id.view_root)

    override val viewModel: AdvertisementViewModel by viewModel()


    override fun initStartView() {
        resultBucket = findViewById(R.id.result_bucket)
    }

    override fun initDataBinding() {
        this.viewDataBinding.activity = this
        this.viewDataBinding.viewModel = viewModel
    }

    override fun initAfterBinding() {
        setObserver()
    }

    override fun onResume() {
        super.onResume()
        playAdvertisementVideo()
    }

    override fun onPause() {
        super.onPause()
        stopAdvertisementVideo()
    }

    /**
     * onClick Listener function
     */
    fun goScan(view: View) {
        if(view.id == R.id.view_root){
            stopAdvertisementVideo()
            goToScanningActivity()
        }
    }

    private fun setObserver(){
        viewModel.action.observe(this, androidx.lifecycle.Observer {
            when(it.action){
                AdvertisementViewAction.ActionNo.GO_TO_RESULT_ACTIVITY -> {
                    goToWineInfoResultActivity()
                }

                /**
                 * 가격정보를 가져오지 못했을 때 ViewModel에서 일단 여기로 보낸다.
                 */
                AdvertisementViewAction.ActionNo.CHECK_NETWORK -> {
                    handleNetworkState()
                    resetBarcodeResultBucket()
                }
            }

            viewModel.isLoading.observe(this, androidx.lifecycle.Observer {
                when(it){
                    true -> showLoading()
                    false -> hideLoading()
                }
            })
        })
    }


    /**
     * 화면 터치시 Scanning Activity로 다시 이동
     */
    private fun goToScanningActivity(){
        when(viewModel.getBarcodeType()){
            ZXING_PROCESS -> {
                goToZxingScanningActivity()
            }

            DATALOGIC_PROCESS -> {
                goToDataLogicScanningActivity()
            }
        }
    }

    /**
     * 영상 재생
     */
    private fun playAdvertisementVideo(){
        viewModel.setAdvertisementVideoFromFile()
    }

    /**
     * 영상 멈춤
     */
    private fun stopAdvertisementVideo(){
        viewModel.setVideoStopPlay()
    }

    /**
     * Wine 가격 호출 성공시 WineInfoResultActivity로 이동
     */
    private fun goToWineInfoResultActivity(){
        val intent = Intent(this, WineInfoResultActivity::class.java)
        intent.putExtra(CommonValue.BARCODE_NO, viewModel.barcodeNo)
        intent.putExtra(CommonValue.WINE_PRICE, viewModel.winePrice)
        intent.putExtra(CommonValue.WINE_EVENT_MSG, viewModel.wineEventMsg)
        startActivity(intent)
        finish()
    }

    /**
     * go To DataLogic Scanning Activity
     */
    private fun goToDataLogicScanningActivity(){
        val intent = Intent(this, DataLogicScanningActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * go To Zxing Scanning Activity
     */
    private fun goToZxingScanningActivity(){
        var integrator = IntentIntegrator(this)
        integrator = CameraIntegratorSettingUtil.getZxingScanningActivityCameraSettings(integrator)
        integrator.initiateScan()
        finish()
    }

    override fun onBackPressed() {
        goToZxingScanningActivity()
    }
}