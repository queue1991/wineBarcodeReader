package com.example.winebarcodereader.view.wineinfo

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.ScrollView
import com.example.winebarcodereader.viewmodel.wineinfo.WineInfoResultViewModel
import com.example.winebarcodereader.action.WineInfoResultViewAction
import com.example.winebarcodereader.base.view.BaseKotlinActivity
import com.example.winebarcodereader.common.CommonValue
import com.example.winebarcodereader.databinding.ActivityWineInfoResultBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.view.MotionEvent
import android.view.ViewGroup
import com.example.winebarcodereader.R
import com.example.winebarcodereader.common.CommonValue.DATALOGIC_PROCESS
import com.example.winebarcodereader.common.CommonValue.ZXING_PROCESS
import com.example.winebarcodereader.util.CameraIntegratorSettingUtil
import com.example.winebarcodereader.view.scan.DataLogicScanningActivity
import com.google.zxing.integration.android.IntentIntegrator


class WineInfoResultActivity : BaseKotlinActivity<ActivityWineInfoResultBinding, WineInfoResultViewModel>() {
    override val viewModel: WineInfoResultViewModel by viewModel()

    override val layoutResourceId: Int
        get() = R.layout.activity_wine_info_result

    override val parentView: ViewGroup
        get() = findViewById(R.id.root_view)

    lateinit var winePrice:String
    lateinit var barcodeNo:String
    private var wineEventMsg:String? = null

    private lateinit var iv_result:ImageView
    private lateinit var scrollview:ScrollView

    override fun initStartView() {
        iv_result = findViewById(R.id.iv_result)
        scrollview = findViewById(R.id.view_scroll)
    }

    override fun initDataBinding() {
        this.viewDataBinding.viewModel = viewModel
        this.viewDataBinding.activity = this
    }

    override fun initAfterBinding() {
        setWineInfo()
        setObserver()
        setScrollViewListener()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setGoBackToScanActivityTimer()
    }

    override fun onPause() {
        super.onPause()
        viewModel.cancelTimer()
    }

    /**
     * ClickListener class for the goScan Btn
     */
    fun goScan(view:View) {
        if(view.id == R.id.btn_go_barcode){
            goToScanningActivity()
        }
    }

    /**
     * 뒤로가기시 Scan Activity 시작
     */
    private fun goToScanningActivity(){
        when(viewModel.getBarcodeType()){
            ZXING_PROCESS ->{
                goToZxingScanningActivity()
            }

            DATALOGIC_PROCESS ->{
                goToDataLogicScanningActivity()
            }
        }
    }

    /**
     * go To DataLogicScanning Activity
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

    private fun setWineInfo(){
        barcodeNo = intent.getStringExtra(CommonValue.BARCODE_NO)!!
        winePrice = intent.getStringExtra(CommonValue.WINE_PRICE)!!

        if(!intent.getStringExtra(CommonValue.WINE_EVENT_MSG).isNullOrEmpty())
            wineEventMsg = intent.getStringExtra(CommonValue.WINE_EVENT_MSG)!!

        viewModel.setWineInfo(winePrice,barcodeNo, wineEventMsg)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        goToScanningActivity()
    }

    private fun setObserver(){
        viewModel.action.observe(this, androidx.lifecycle.Observer {
            when(it.action){
                WineInfoResultViewAction.ActionNo.SET_READY_IMAGE -> {
                    iv_result.background = null
                    iv_result.setImageDrawable(iv_result.context.getDrawable(R.drawable.wine_info_ready))
                }

                WineInfoResultViewAction.ActionNo.GO_TO_BARCODE_ACTIVITY -> {
                    goToScanningActivity()
                }
            }
        })
    }

    /**
     * 사용자가 스크롤을 시작하면 타이머 재시작
     */
    private fun setScrollViewListener(){
        scrollview.setOnTouchListener{ view: View, motionEvent: MotionEvent ->
            when(motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    viewModel.restartTimer()
                }
            }
            // 이벤트를 사용하지 않음 - false
            false
        }
    }
}
