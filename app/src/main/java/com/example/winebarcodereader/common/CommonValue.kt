package com.example.winebarcodereader.common

object CommonValue {
    /**
     * Zxing or DataLogic
     */
    const val ZXING_PROCESS = 1
    const val DATALOGIC_PROCESS = 2


    /**
     * Wine Image Info 관련
     */
    const val WINE_INFO_IMAGE_BASE_URL = "https:"
    const val WINE_INFO_IMAGE_DATA_FORM_PNG = ".png"

    /**
     * Wine Price 단위
     */
    const val MONETARY_UNIT = "원"

    /**
     * ScanningActivity -> WineInfoResultActivity Intent Extra Key
     */
    const val BARCODE_NO = "barode_no"
    const val WINE_PRICE = "wine_price"
    const val WINE_EVENT_MSG = "wine_event_msg"

    /**
     * SharedPreference 관련
     */
    const val PREFERENCE_NAME = "WINE_PREFERENCE"
    const val SCREEN_WIDTH = "SCREEN_WIDTH"
    const val BARCODE_TYPE = "BARCODE_TYPE"
    const val STORE_CODE = "STORE_CDOE"

    /**
     * SharedPreference Default 값 관련
     */
    const val DEFAULT_SCREEN_WIDTH = 0
    const val DEFAULT_BARCODE_TYPE = 0

    /**
     * WinePrice를 API에서 가져오지 못했을 때의 값
     */
    const val HAS_NO_WINE_PRICE = "HAS_NO_WINE_PRICE"


    /**
     * setAdvertisementTimer miliseconds
     */
    const val INITIAL_DELAY_FOR_AD = 30000L // 특정 시간
    const val PERIOD_FOR_AD = 30000L

    /**
     * set Timer for WineInfoResultActivity to go back to ScanningActivity
     */
    const val INITIAL_DELAY_FOR_GOING_BACK_TO_SCAN = 60000L
    const val PERIOD_FOR_GOING_BACK_TO_SCAN = 30000L



}