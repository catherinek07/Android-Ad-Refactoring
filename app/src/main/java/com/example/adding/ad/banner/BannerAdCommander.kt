package com.example.adding.ad.banner

import com.example.adding.ad.AdCallbackDelegate
import com.example.adding.ad.AdvrtsTyCode
import com.example.adding.ad.AgencySeCode

interface BannerAdCommander : AdCallbackDelegate {
    val bannerAds: List<BannerAd>
    fun destroy()

    fun isDeveloped(agencySeCode: AgencySeCode, advrtsTyCode: AdvrtsTyCode): Boolean
    fun launch(
        agencySeCode: AgencySeCode, advrtsTyCode: AdvrtsTyCode, adKey: String, jsonStr: String
    )
    fun remove(agencySeCode: AgencySeCode, advrtsTyCode: AdvrtsTyCode)
    fun openBannerAdSection(agencySeCode: AgencySeCode, advrtsTyCode: AdvrtsTyCode)
    fun closeBannerAdSection(agencySeCode: AgencySeCode, advrtsTyCode: AdvrtsTyCode)
}