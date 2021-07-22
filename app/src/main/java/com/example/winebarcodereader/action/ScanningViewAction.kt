package com.example.winebarcodereader.action

class ScanningViewAction(var action:Int) {
    object ActionNo{
        const val GO_TO_RESULT_ACTIVITY = 0
        const val SCANNER_ON = 1
        const val CHECK_NETWORK = 2
        const val GO_TO_ADVERTISEMENT_ACTIVITY = 3
    }
}