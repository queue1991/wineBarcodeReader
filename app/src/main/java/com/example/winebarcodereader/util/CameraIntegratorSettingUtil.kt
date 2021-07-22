package com.example.winebarcodereader.util

import com.example.winebarcodereader.view.scan.ZxingScanningActivity
import com.google.zxing.integration.android.IntentIntegrator

object CameraIntegratorSettingUtil {
    /**
     * ScanningActivity에서 사용하는 Zxing camera settings
     */
    fun getZxingScanningActivityCameraSettings(integrator: IntentIntegrator) : IntentIntegrator{
        integrator.setBeepEnabled(false)
        integrator.setPrompt("")
        integrator.setCameraId(0)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES)
        integrator.captureActivity = ZxingScanningActivity::class.java
        
        return integrator
    }
}