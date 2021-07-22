package com.example.winebarcodereader.action

class IntroViewAction(var action:Int) {
    object ActionNo{
        const val GO_TO_DATALOGIC_SCAN_ACTIVITY = 0
        const val GO_TO_ZXING_SCAN_ACTIVITY = 1
    }
}