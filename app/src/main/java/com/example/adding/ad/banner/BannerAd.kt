package com.example.adding.ad.banner

import com.example.adding.ad.AdCallbackDelegate
import com.example.adding.ad.AdvrtsTyCode
import com.example.adding.ad.AgencySeCode

interface BannerAd {
    val agencySeCode: AgencySeCode
    val advrtsTyCode: AdvrtsTyCode
    val delegate: AdCallbackDelegate?
    fun destroy()

    fun isDeveloped(): Boolean
    fun launch(
        agencySeCode: AgencySeCode,
        AdvrtsTyCode: AdvrtsTyCode,
        adKey: String,
        jsonObj: Map<String, Any>
    )
    fun remove()
    fun openBannerAdSection()
    fun closeBannerAdSection()

}