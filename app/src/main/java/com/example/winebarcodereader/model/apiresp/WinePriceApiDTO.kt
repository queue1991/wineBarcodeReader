package com.example.winebarcodereader.model.apiresp

import com.example.winebarcodereader.model.common.ApiCommonResp

data class WinePriceApiDTO(
    // 정상, 그외 오류메시지
    var SALE_PRICE: String? = null,
    var SALE_AMT: String? = null,
    var EVENT_TITLE:String? = null,
    var EVENT_MSG:String? = null
) : ApiCommonResp()