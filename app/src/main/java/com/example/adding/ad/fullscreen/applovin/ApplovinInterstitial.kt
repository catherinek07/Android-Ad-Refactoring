package com.example.adding.ad.fullscreen.applovin

import android.content.Context
import android.util.Log
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.example.adding.MainActivity
import com.example.adding.ad.AdCallbackDelegate
import com.example.adding.ad.AdvrtsTyCode
import com.example.adding.ad.AgencySeCode
import com.example.adding.ad.fullscreen.FullScreenAd
import java.lang.ref.WeakReference

class ApplovinInterstitial(
    private val contextRef: WeakReference<Context>,
    override val agencySeCode: AgencySeCode = AgencySeCode.APPLOVIN,
    override val advrtsTyCode: AdvrtsTyCode = AdvrtsTyCode.INTERSTITIAL,

    override val delegate: AdCallbackDelegate?
) : FullScreenAd, MaxAdListener {
    private val tag = "Applovin Interstitial"
    private var preLoadedAd: WeakReference<MaxInterstitialAd?> = WeakReference(null)
    private var adKey: String = ""
    private var jsonObj: Map<String, Any> = mapOf()

    override fun destroy() {
        invalidatePreLoadedAd()
        contextRef.clear()
    }

    override fun isDeveloped(): Boolean {
        return true
    }

    override fun load(
        agencySeCode: AgencySeCode,
        AdvrtsTyCode: AdvrtsTyCode,
        adKey: String,
        jsonObj: Map<String, Any>
    ) {
        val context = contextRef.get()
        if (context != null) {
            if (!isReady()) {
                preLoadedAd = WeakReference(MaxInterstitialAd(adKey, context as MainActivity))
                preLoadedAd.get()?.setListener(this)
                this.adKey = adKey
                this.jsonObj = jsonObj
                preLoadedAd.get()?.loadAd()
            } else {
                Log.d(
                    tag, "load: ad has already been loaded"
                )
            }
        } else {
            Log.d(tag, "context is null")
        }
    }


    override fun show(
        agencySeCode: AgencySeCode, AdvrtsTyCode: AdvrtsTyCode, jsonObj: Map<String, Any>
    ) {
        if (isReady()) {
            Log.d(tag, "onAdShowed: ")
            preLoadedAd.get()?.showAd()
        }
    }

    override fun invalidatePreLoadedAd() {
        preLoadedAd = WeakReference(null)
    }

    override fun isReady(): Boolean {
        return preLoadedAd.get() != null && preLoadedAd.get()!!.isReady
    }

    override fun onAdLoaded(p0: MaxAd?) {
        delegate?.onLoadSuccess(agencySeCode, advrtsTyCode, adKey, jsonObj)
        Log.d(tag, "onAdLoaded: ")
    }

    override fun onAdDisplayed(p0: MaxAd?) {
        delegate?.onOpen(agencySeCode, advrtsTyCode, adKey, jsonObj)
        Log.d(tag, "onAdShowed: ")
    }

    override fun onAdHidden(p0: MaxAd?) {
        delegate?.onClose(agencySeCode, advrtsTyCode, adKey, jsonObj)
        Log.d(tag, "onAdHidden: ")
        invalidatePreLoadedAd()
    }

    override fun onAdClicked(p0: MaxAd?) {
        delegate?.onClick(agencySeCode, advrtsTyCode, adKey, jsonObj)
        Log.d(tag, "onAdClicked: ")
    }

    override fun onAdLoadFailed(p0: String?, p1: MaxError?) {
        val errMsg = p0!!
        errMsg.replace("\'", "_")
        delegate?.onLoadFail(
            agencySeCode, advrtsTyCode, adKey, errMsg, jsonObj
        )
        Log.d(tag, "onAdLoadFailed: errMsg=${errMsg}")
    }

    override fun onAdDisplayFailed(p0: MaxAd?, p1: MaxError?) {
        val errMsg = p1!!.message
        errMsg.replace("\'", "_")
        delegate?.onException(
            agencySeCode, advrtsTyCode, adKey, errMsg, jsonObj
        )
        Log.d(tag, "onAdDisplayFailed: errMsg=${errMsg}")
        invalidatePreLoadedAd()
    }
}