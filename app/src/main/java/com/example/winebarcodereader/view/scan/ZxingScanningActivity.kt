package com.example.winebarcodereader.view.scan

import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.winebarcodereader.R
import com.example.winebarcodereader.action.ScanningViewAction.ActionNo
import com.example.winebarcodereader.base.view.BaseKotlinActivity
import com.example.winebarcodereader.common.CommonValue
import com.example.winebarcodereader.databinding.ActivityZxingCustomScannerBinding
import com.example.winebarcodereader.util.DialogUtil.quitApplicationDialog
import com.example.winebarcodereader.view.advertisement.AdvertisementActivity
import com.example.winebarcodereader.view.wineinfo.WineInfoResultActivity
import com.example.winebarcodereader.viewmodel.scan.ScanningViewModel
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Device 카메라 사용 Scanning 화면 (Zxing)
 */
class ZxingScanningActivity : BaseKotlinActivity<ActivityZxingCustomScannerBinding, ScanningViewModel>() {
    override val viewModel: ScanningViewModel by viewModel()

    override val layoutResourceId: Int
        get() = R.layout.activity_zxing_custom_scanner

    lateinit var view:ConstraintLayout

    override val parentView: ViewGroup
        get() = findViewById(R.id.view_root)


    override fun initStartView() {

    }

    override fun initDataBinding() {
        this.viewDataBinding.activity = this
        this.viewDataBinding.viewModel = viewModel
    }

    override fun initAfterBinding() {
        setBarcodeScanner()
        setObserver()
    }

    private var barcodeScannerView: DecoratedBarcodeView? = null

    override fun onResume() {
        super.onResume()

        resumeScan()
        viewModel.setAdvertisementTimer()
    }

    override fun onPause() {
        super.onPause()

        pauseScan()
        viewModel.cancelAdvertisementTimer()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        quitApplicationProcess()
    }

    private fun resumeScan(){
        barcodeScannerView!!.resume()
    }

    private fun pauseScan(){
        barcodeScannerView!!.pause()
    }

    private fun setBarcodeScanner(){
        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner)
        barcodeScannerView!!.initializeFromIntent(intent)
        barcodeScannerView!!.decodeContinuous {
            pauseScan()
            viewModel.cancelAdvertisementTimer()
            viewModel.afterBarcoding(it.toString())
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
                    resumeScan()
                    Log.d(this.javaClass.name, "setObserver :: SCANNER_ON ")
                }
                ActionNo.CHECK_NETWORK->{
                    handleNetworkState()
                    resumeScan()
                    Log.d(this.javaClass.name, "setObserver :: CHECK_NETWORK ")
                }
                ActionNo.GO_TO_ADVERTISEMENT_ACTIVITY->{
                    goToAdvertisementActivity()
                }
            }
        })

        viewModel.isLoading.observe(this, androidx.lifecycle.Observer {
            when(it){
                true -> showLoading()
                false -> hideLoading()
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
        pauseScan()
        viewModel.cancelAdvertisementTimer()

        quitApplicationDialog(
            this,
            DialogInterface.OnClickListener { dialogInterface, i -> finish() },
            DialogInterface.OnClickListener { dialogInterface, i ->
                resumeScan()
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
        val intent = Intent(this@ZxingScanningActivity, AdvertisementActivity::class.java)
        startActivity(intent)

        overridePendingTransition(R.anim.fadein, R.anim.fadeout)

        finish()
    }

}