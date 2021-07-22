package com.example.winebarcodereader.action

class WineInfoResultViewAction(var action:Int) {
    object ActionNo{
        const val SET_READY_IMAGE = 0
        const val GO_TO_BARCODE_ACTIVITY = 1
    }
}