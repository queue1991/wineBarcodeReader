package com.example.winebarcodereader.view.scan

import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.winebarcodereader.R
import com.example.winebarcodereader.action.ScanningViewAction.ActionNo
import com.example.winebarcodereader.base.view.BaseDataLogicActivity
import com.example.winebarcodereader.common.CommonValue
import com.example.winebarcodereader.databinding.ActivityCustomScannerBinding
import com.example.winebarcodereader.util.DialogUtil.quitApplicationDialog
import com.example.winebarcodereader.view.advertisement.AdvertisementActivity
import com.example.winebarcodereader.view.wineinfo.WineInfoResultActivity
import com.example.winebarcodereader.viewmodel.scan.ScanningViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.concurrent.timer


/**
 * DataLogic 바코드 스캐너용 Scanning 화면
 */
class DataLogicScanningActivity : BaseDataLogicActivity<ActivityCustomScannerBinding, ScanningViewModel>() {
    override val viewModel: ScanningViewModel by viewModel()

    override val layoutResourceId: Int
        get() = R.layout.activity_custom_scanner

    lateinit var view:ConstraintLayout

    override val parentView: ViewGroup
        get() = findViewById(R.id.view_root)

    override fun initStartView() {
        animation = animationView.background as AnimationDrawable
        resultBucket = findViewById(R.id.result_bucket)
    }

    override fun initDataBinding() {
        this.viewDataBinding.activity = this
        this.viewDataBinding.viewModel = viewModel
    }

    override fun initAfterBinding() {
        setObserver()
        setBarcodeResultBucket()
    }

    override fun onResume() {
        super.onResume()

        setBarcodeResultListener()

        viewModel.setAdvertisementTimer()
    }

    override fun onPause() {
        super.onPause()

        removeBarcodeResultListener()

        viewModel.cancelAdvertisementTimer()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        quitApplicationProcess()
    }

    /**
     * 바코드를 찍은 후의 동작은 각각 다르기 떄문에 여기서 정의
     * ResultBucket에 결과값이 담기고 1초 후에 afterBarcoding 로직 수행
     */
    override fun setBarcodeResultBucket(){
        // Prevent soft keyboard from popping up while selecting text.
        resultBucket!!.showSoftInputOnFocus = false


        resultBucket!!.addTextChangedListener(object:TextWatcher{
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



    /**
     * 1. barcode 값으로 API 호출 여부를 따져 scanner를 resume, pause시킴.
     * 2. wine Price 값 호출 성공시에 Wine Info Result Activity로 이동
     */
    private fun setObserver(){
        viewModel.action.observe(this, androidx.lifecycle.Observer {
            when(it.action){
                ActionNo.GO_TO_RESULT_ACTIVITY -> {
                    goToWineInfoResultActivity()
                }
                ActionNo.SCANNER_ON -> {

                }

                ActionNo.CHECK_NETWORK->{
                    handleNetworkState()
                    resetBarcodeResultBucket()
                }
                ActionNo.GO_TO_ADVERTISEMENT_ACTIVITY->{
                    goToAdvertisementActivity()
                }
            }
        })
    }


    /**
     * 앱 종료 다이얼로그
     * 1. pauseScan / timer 종료
     * 2. 홍보화면으로 넘어가는 Timer Cancel
     * 2-1. 확인 -> 종료
     * 2-2. 취소 -> resumeScan / timer 재개
     */
    private fun quitApplicationProcess(){
        viewModel.cancelAdvertisementTimer()

        quitApplicationDialog(
            this,
            DialogInterface.OnClickListener { dialogInterface, i -> finish() },
            DialogInterface.OnClickListener { dialogInterface, i ->
                viewModel.setAdvertisementTimer()}
        )
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
     * advertisementActivity로 이동
     */
    private fun goToAdvertisementActivity(){
        val intent = Intent(this@DataLogicScanningActivity, AdvertisementActivity::class.java)
        startActivity(intent)

        overridePendingTransition(R.anim.fadein, R.anim.fadeout)

        finish()
    }
}