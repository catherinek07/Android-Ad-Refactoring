package com.example.adding.ad.fullscreen

import com.example.adding.ad.AdCallbackDelegate
import com.example.adding.ad.AdvrtsTyCode
import com.example.adding.ad.AgencySeCode

interface FullScreenAd {
    val agencySeCode: AgencySeCode
    val advrtsTyCode: AdvrtsTyCode
    val delegate: AdCallbackDelegate?
    fun destroy()

    fun isDeveloped(): Boolean
    fun load(
        agencySeCode: AgencySeCode,
        AdvrtsTyCode: AdvrtsTyCode,
        adKey: String,
        jsonObj: Map<String, Any>
    )

    fun show(agencySeCode: AgencySeCode, AdvrtsTyCode: AdvrtsTyCode, jsonObj: Map<String, Any>)
    fun invalidatePreLoadedAd()
    fun isReady(): Boolean

}





