package com.example.adding.webview.jsInterface

import android.content.Context
import android.webkit.JavascriptInterface
import com.example.adding.MainActivity
import com.example.adding.ad.AdvrtsTyCode
import com.example.adding.ad.AgencySeCode
import com.example.adding.ad.banner.BannerAdCommander
import com.example.adding.ad.banner.BannerAdCommanderImpl
import com.example.adding.ad.fullscreen.FullScreenAdCommander
import com.example.adding.ad.fullscreen.FullScreenAdCommanderImpl
class AndroidBridge(context: Context) {
    private val wv = (context as MainActivity).web
    private val fullScreenCommander: FullScreenAdCommander = FullScreenAdCommanderImpl(context)
    private val bannerAdCommander: BannerAdCommander = BannerAdCommanderImpl(context)

    @JavascriptInterface
    fun launchBannerAd(agencySeCode: String, advrtsTyCode: String, adKey: String, jsonStr: String) {
        val agency = AgencySeCode.fromValue(agencySeCode)
        val advrts = AdvrtsTyCode.fromValue(advrtsTyCode)
        if (agency != null && advrts != null) {
            wv.post {
                bannerAdCommander.launch(agency, advrts, adKey, jsonStr)
            }
        }
    }

    @JavascriptInterface
    fun removeBannerAd(agencySeCode: String, advrtsTyCode: String, callback: String) {
        val agency = AgencySeCode.fromValue(agencySeCode)
        val advrts = AdvrtsTyCode.fromValue(advrtsTyCode)
        if (agency != null && advrts != null) {
            wv.post {
                bannerAdCommander.remove(agency, advrts)
                wv.evaluateJavascript("$callback('$agencySeCode', '$advrtsTyCode')", null)
            }
        }
    }

    @JavascriptInterface
    fun bannerAdIsDeveloped(agencySeCode: String, advrtsTyCode: String, callback: String) {
        val agency = AgencySeCode.fromValue(agencySeCode)
        val advrts = AdvrtsTyCode.fromValue(advrtsTyCode)
        if (agency != null && advrts != null) {
            val isDeveloped = bannerAdCommander.isDeveloped(agency, advrts)
            wv.post {
                wv.evaluateJavascript("$callback('$agencySeCode', '$advrtsTyCode', $isDeveloped)", null)
            }
        }
    }

    @JavascriptInterface
    fun bannerAdSectionOpen(agencySeCode: String, advrtsTyCode: String, callback: String) {
        val agency = AgencySeCode.fromValue(agencySeCode)
        val advrts = AdvrtsTyCode.fromValue(advrtsTyCode)
        if (agency != null && advrts != null) {
            wv.post {
                bannerAdCommander.openBannerAdSection(agency, advrts)
                wv.evaluateJavascript("$callback('$agencySeCode', '$advrtsTyCode')", null)
            }
        }
    }

    @JavascriptInterface
    fun bannerAdSectionClose(agencySeCode: String, advrtsTyCode: String, callback: String) {
        val agency = AgencySeCode.fromValue(agencySeCode)
        val advrts = AdvrtsTyCode.fromValue(advrtsTyCode)
        if (agency != null && advrts != null) {
            wv.post {
                bannerAdCommander.closeBannerAdSection(agency, advrts)
                wv.evaluateJavascript("$callback('$agencySeCode', '$advrtsTyCode')", null)
            }
        }
    }

    @JavascriptInterface
    fun bannerAdSetCallbackName(callbackName: String, callback: String) {
        val call = bannerAdCommander.setCallBackName(callbackName)
        wv.post {
            wv.evaluateJavascript("$callback($call)", null)
        }
    }

    @JavascriptInterface
    fun bannerAdGetCallbackName(callback: String) {
        val callbackName = bannerAdCommander.getCallBackName()
        wv.post {
            wv.evaluateJavascript("$callback($callbackName)", null)
        }
    }

    @JavascriptInterface
    fun loadFullScreenAd(
        agencySeCode: String, advrtsTyCode: String, adKey: String, jsonStr: String
    ) {
        val agency = AgencySeCode.fromValue(agencySeCode)
        val advrts = AdvrtsTyCode.fromValue(advrtsTyCode)
        if (agency != null && advrts != null) {
            wv.post {
                fullScreenCommander.load(agency, advrts, adKey, jsonStr)
            }
        }
    }

    @JavascriptInterface
    fun showFullScreenAd(agencySeCode: String, advrtsTyCode: String, jsonStr: String) {
        val agency = AgencySeCode.fromValue(agencySeCode)
        val advrts = AdvrtsTyCode.fromValue(advrtsTyCode)
        if (agency != null && advrts != null) {
            wv.post {
                fullScreenCommander.show(agency, advrts, jsonStr)
            }
        }
    }

    @JavascriptInterface
    fun fullScreenAdIsDeveloped(agencySeCode: String, advrtsTyCode: String, callback: String) {
        val agency = AgencySeCode.fromValue(agencySeCode)
        val advrts = AdvrtsTyCode.fromValue(advrtsTyCode)
        if (agency != null && advrts != null) {
            val isDeveloped = fullScreenCommander.isDeveloped(agency, advrts)
            wv.post {
                wv.evaluateJavascript("$callback('$agencySeCode', '$advrtsTyCode', $isDeveloped)", null)
            }
        }
    }

    @JavascriptInterface
    fun fullScreenAdInvalidatePreLoadedAd(
        agencySeCode: String, advrtsTyCode: String, callback: String
    ) {
        val agency = AgencySeCode.fromValue(agencySeCode)
        val advrts = AdvrtsTyCode.fromValue(advrtsTyCode)
        if (agency != null && advrts != null) {
            fullScreenCommander.invalidatedPreLoadedAd(agency, advrts)
            wv.post {
                wv.evaluateJavascript("$callback('$agencySeCode','$advrtsTyCode')", null)
            }
        }
    }

    @JavascriptInterface
    fun fullScreenAdIsReady(agencySeCode: String, advrtsTyCode: String, callback: String) {
        val agency = AgencySeCode.fromValue(agencySeCode)
        val advrts = AdvrtsTyCode.fromValue(advrtsTyCode)
        if (agency != null && advrts != null) {
            val isReady = fullScreenCommander.isReady(agency, advrts)
            wv.post {
                wv.evaluateJavascript("$callback('$agencySeCode','$advrtsTyCode',$isReady)", null)
            }
        }
    }

    @JavascriptInterface
    fun fullScreenAdSetCallbackName(callbackName: String, callback: String) {
        fullScreenCommander.setCallBackName(callbackName)
        wv.post {
            wv.evaluateJavascript("$callback()", null)
        }
    }

    @JavascriptInterface
    fun fullScreenAdGetCallbackName(callback: String) {
        val callbackName = fullScreenCommander.getCallBackName()
        wv.post {
            wv.evaluateJavascript("$callback('$callbackName')", null)
        }
    }

}