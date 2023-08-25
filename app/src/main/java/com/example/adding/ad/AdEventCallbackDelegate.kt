package com.example.adding.ad

interface AdEventCallbackDelegate {
    fun onOpen(
        agencySeCode: AgencySeCode,
        advrtsTyCode: AdvrtsTyCode,
        adKey: String,
        jsonObj: Map<String, Any>
    )

    fun onClose(
        agencySeCode: AgencySeCode,
        advrtsTyCode: AdvrtsTyCode,
        adKey: String,
        jsonObj: Map<String, Any>
    )

    fun onClick(
        agencySeCode: AgencySeCode,
        advrtsTyCode: AdvrtsTyCode,
        adKey: String,
        jsonObj: Map<String, Any>
    )

    fun onImpression(
        agencySeCode: AgencySeCode,
        advrtsTyCode: AdvrtsTyCode,
        adKey: String,
        jsonObj: Map<String, Any>
    )

    fun onRewardComplete(
        agencySeCode: AgencySeCode,
        advrtsTyCode: AdvrtsTyCode,
        adKey: String,
        amount: Int,
        jsonObj: Map<String, Any>
    )

    fun onException(
        agencySeCode: AgencySeCode,
        advrtsTyCode: AdvrtsTyCode,
        adKey: String,
        errMsg: String,
        jsonObj: Map<String, Any>
    )
}