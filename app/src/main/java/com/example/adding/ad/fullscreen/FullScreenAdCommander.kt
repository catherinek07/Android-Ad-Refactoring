package com.example.adding.ad.fullscreen

import com.example.adding.ad.AdCallbackDelegate
import com.example.adding.ad.AdvrtsTyCode
import com.example.adding.ad.AgencySeCode

interface FullScreenAdCommander : AdCallbackDelegate {
    val fullScreenAds: List<FullScreenAd>
    fun destroy()

    fun isDeveloped(agencySeCode: AgencySeCode, advrtsTyCode: AdvrtsTyCode): Boolean
    fun load(agencySeCode: AgencySeCode, advrtsTyCode: AdvrtsTyCode, adKey: String, jsonStr: String)
    fun show(agencySeCode: AgencySeCode, advrtsTyCode: AdvrtsTyCode, jsonStr: String)
    fun invalidatedPreLoadedAd(agencySeCode: AgencySeCode, advrtsTyCode: AdvrtsTyCode)
    fun isReady(agencySeCode: AgencySeCode, advrtsTyCode: AdvrtsTyCode): Boolean
}