package com.example.winebarcodereader.model.common

/**
 * API 호출 시 정적인 값들
 */
object APICommonValue {

    /**
     *  ------------------- API 호출 URL 관련 -----------------------
     */
    const val REAL_BASE_URL = "https:"
    const val MPOS_DEV_BASE_URL = "http:"
    const val WINE_PRICE_POST_SUFFIX = ""
    const val WINE_PRICE_POST_SUFFIX_FOR_MPOS = ""


    /**
     * ------------------- Header Key -------------------
     */
    const val WINE_PRICE_CONTENT_TYPE_KEY = "Content-Type"
    const val WINE_PRICE_MSG_VER_KEY = ""
    const val WINE_PRICE_MSG_TY_KEY = ""
    const val WINE_PRICE_SYSTEM_TY_KEY = ""
    const val WINE_PRICE_POS_NO_KEY = ""
    const val WINE_PRICE_TRAN_NO_KEY = ""
    const val WINE_PRICE_PMOD_YN_KEY = ""
    const val WINE_PRICE_MOBILE_ID_KEY = ""
    const val WINE_PRICE_COOKIE_KEY = ""
    const val WINE_PRICE_TRAN_YMD_KEY = ""
    const val WINE_PRICE_STORE_CD_KEY = ""


    /**
     * ------------------- Common Header value -------------------
     */
    const val WINE_PRICE_CONTENT_TYPE_VALUE = "application/json"
    const val WINE_PRICE_MSG_VER_VALUE = ""
    const val WINE_PRICE_SYSTEM_TY_VALUE = ""
    const val WINE_PRICE_TRAN_YMD_VALUE = ""
    const val WINE_PRICE_STORE_CD_VALUE = ""
    const val WINE_PRICE_POS_NO_VALUE = ""
    const val WINE_PRICE_TRAN_NO_VALUE = ""
    const val WINE_PRICE_PMOD_YN_VALUE = ""
    const val WINE_PRICE_MOBILE_ID_VALUE = ""
    const val WINE_PRICE_COOKIE_VALUE = ""

    /**
     * ------------------- MSG TY -------------------
     */
    const val WINE_PRICE_MSG_TY_VALUE = ""

    /**
     * ------------------- item.do 1010 data raw 값 (call wine price API) -----------------------
     */
    const val WINE_PRICE_INPUT_TY_KEY = ""
    const val WINE_PRICE_INPUT_TY_VALUE = ""
    const val WINE_PRICE_SCAN_CD_KEY = ""
    const val WINE_PRICE_SCAN_CD_TEST_VALUE = ""
    const val WINE_PRICE_SALE_QTY_KEY = ""
    const val WINE_PRICE_SALE_QTY_VALUE = ""

    /**
     * ------------------- RequestBody MediaType 관련 -----------------------
     */
    const val MEDIA_TYPE_JSON_UTF8 = "application/json; charset=utf-8"


    /**
     * ------------------- API Common Response 관련 -------------------
     */
    const val RESPONSE_SUCCESS = "0000"
    const val RESPONSE_DATE_NOT_CORRECT = "9991"
    const val RESPONSE_UNDER_AGE = "2001"
    const val RESPONSE_FAIL = "9999"
}