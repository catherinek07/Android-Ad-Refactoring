package com.example.adding.ad


interface AdLoadCallbackDelegate {
    fun onLoadSuccess(
        agencySeCode: AgencySeCode,
        advrtsTyCode: AdvrtsTyCode,
        adKey: String,
        jsonObj: Map<String, Any>
    )

    fun onLoadFail(
        agencySeCode: AgencySeCode,
        advrtsTyCode: AdvrtsTyCode,
        adKey: String,
        errMsg: String,
        jsonObj: Map<String, Any>
    )
}