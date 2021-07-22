package com.example.winebarcodereader.view.intro

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.winebarcodereader.R
import com.example.winebarcodereader.action.IntroViewAction
import com.example.winebarcodereader.base.view.BaseKotlinActivity
import com.example.winebarcodereader.common.CommonValue.DATALOGIC_PROCESS
import com.example.winebarcodereader.common.CommonValue.ZXING_PROCESS
import com.example.winebarcodereader.databinding.ActivityIntroBinding
import com.example.winebarcodereader.util.CameraIntegratorSettingUtil
import com.example.winebarcodereader.view.scan.DataLogicScanningActivity
import com.example.winebarcodereader.viewmodel.intro.IntroViewModel
import com.google.zxing.integration.android.IntentIntegrator
import org.koin.androidx.viewmodel.ext.android.viewModel



class IntroActivity : BaseKotlinActivity<ActivityIntroBinding, IntroViewModel>() {
    private val DATALOGIC_PERMISSION = 1000
    private val ZXING_PERMISSION = 2000

    override val layoutResourceId: Int
        get() = R.layout.activity_intro

    override val viewModel: IntroViewModel by viewModel()

    override val parentView: ViewGroup
        get() = findViewById(R.id.root_view_intro)

    override fun initStartView() {

    }

    override fun initDataBinding() {
        this.viewDataBinding.activity = this
        this.viewDataBinding.viewModel = viewModel
    }

    override fun initAfterBinding() {
        setObserver()
        setScreenWidthToSharedPreference()
        viewModel.setBarcodeTypeToSharedPreference()
    }

    /**
     * Screen Width SharedPreference에 저장
     * 이미지를 불러와서 ImageView에 set할때 사용 할 값
     */
    private fun setScreenWidthToSharedPreference(){
        var screenWidth = 0

        /**
         * sharedPreference에 저장이 안되어있으면 width 저장 로직 후 setupPermission -> scan 화면 이동
         * getDeviceScreenWidthFromSharedPreference가 0이면 저장한 적이 없기때문에 구해서 Put
         */
        if(viewModel.getDeviceScreenWidthFromSharedPreference() == 0){
            parentView.viewTreeObserver.addOnGlobalLayoutListener(object:
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {

                    if(parentView.rootView.width != 0){
                        screenWidth =  parentView.rootView.width
                        viewModel.setDeviceWidthToSharedPreference(screenWidth)
                        parentView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
            })
        }else{
            when(viewModel.barcodeProcess){
                DATALOGIC_PROCESS -> {
                    setupDataLogicPermissions()
                }
                ZXING_PROCESS -> {
                    setupZxingPermissions()
                }
            }
        }
    }

    /**
     * screen Width를 viewModel에서 SharedPreference에 저장 후 Scan Activity로 이동하기 위한 observer
     */
    private fun setObserver(){
        viewModel.action.observe(this, androidx.lifecycle.Observer {
            when(it.action){
                /**
                 * DataLogic Barcode Scanner를 사용하는 View로 이동
                 */
                IntroViewAction.ActionNo.GO_TO_DATALOGIC_SCAN_ACTIVITY -> {
                    setupDataLogicPermissions()
                }

                /**
                 * zxing을 사용하는 view로 이동
                 */
                IntroViewAction.ActionNo.GO_TO_ZXING_SCAN_ACTIVITY -> {
                    setupZxingPermissions()
                }
            }
        })
    }

    /**
     * DataLogic 바코드 리더기 사용시에는 WRITE_EXTERNAL_STORAGE 권한 필요
     */
    private fun setupDataLogicPermissions() {
        // 카메라 읽기 퍼미션을 permission 변수에 담는다
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission == PackageManager.PERMISSION_GRANTED) {
            goToDataLogicScanningActivity()
        } else{
            makeDataLogicPermissionRequest()
        }
    }

    /**
     * Zxing 바코드 리더기 사용시에는 WRITE_EXTERNAL_STORAGE / CAMERA) 권한 필요
     */
    private fun setupZxingPermissions() {
        val requiredPermissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA)

        var rejectedPermissionList = ArrayList<String>()

        for(permission in requiredPermissions){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                //만약 권한이 없다면 rejectedPermissionList에 추가
                rejectedPermissionList.add(permission)
            }
        }

        // Permission Denied가 있는 경우
        if(rejectedPermissionList.isNotEmpty()){
            makeZxingPermissionRequest(rejectedPermissionList)
        }else{
            // 모든 Permission이 granted된 경우
            goToZxingScanningActivity()
        }
    }


    /**
     * DataLogic을 위한 퍼미션 요청
     */
    private fun makeDataLogicPermissionRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            DATALOGIC_PERMISSION)
    }

    /**
     * Zxing을 위한 퍼미션 요청
     */
    private fun makeZxingPermissionRequest(list:ArrayList<String>){
        //권한 요청!
        val array = arrayOfNulls<String>(list.size)
        ActivityCompat.requestPermissions(this, list.toArray(array), ZXING_PERMISSION)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when(requestCode){
            DATALOGIC_PERMISSION ->{
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // permission denied
                    finish()
                }else{
                    // permission granted
                    goToDataLogicScanningActivity()
                }
                return
            }

            ZXING_PERMISSION -> {
                if (grantResults.isNotEmpty()) {
                    for ((i, permission) in permissions.withIndex()) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            finish()
                        }
                    }
                    goToZxingScanningActivity()
                }
            }
        }
    }

    /**
     * go To Scanning Activity
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

}