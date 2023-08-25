package com.example.adding.ad.banner.applovin

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdFormat
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.example.adding.MainActivity
import com.example.adding.ad.AdCallbackDelegate
import com.example.adding.ad.AdvrtsTyCode
import com.example.adding.ad.AgencySeCode
import com.example.adding.ad.banner.BannerAd
import com.applovin.mediation.ads.MaxAdView
import com.applovin.sdk.AppLovinSdkUtils
import java.lang.ref.WeakReference

class ApplovinBanner(
    private val contextRef: WeakReference<Context>,
    override val agencySeCode: AgencySeCode = AgencySeCode.APPLOVIN,
    override val advrtsTyCode: AdvrtsTyCode = AdvrtsTyCode.BANNER,
    override val delegate: AdCallbackDelegate?
) : BannerAd, MaxAdViewAdListener {
    private val tag = "APPLOVIN Banner"
    private var adViewRef: WeakReference<MaxAdView?> = WeakReference(null)
    private var adKey: String = ""
    private var jsonObj: Map<String, Any> = mapOf()

    override fun destroy() {
        closeBannerAdSection()
        adViewRef.clear()
        contextRef.clear()
    }

    override fun isDeveloped(): Boolean {
        return true
    }

    override fun launch(
        agencySeCode: AgencySeCode,
        AdvrtsTyCode: AdvrtsTyCode,
        adKey: String,
        jsonObj: Map<String, Any>
    ) {
        val context = contextRef.get()
        if (context != null) {
            adViewRef = WeakReference(MaxAdView(adKey, context as MainActivity))
            if (adViewRef.get() != null) {
                val adView = adViewRef.get()!!
                adView.setListener(this)
                val width = ViewGroup.LayoutParams.MATCH_PARENT
                val heightDp = MaxAdFormat.BANNER.getAdaptiveSize(context).height
                val heightPx = AppLovinSdkUtils.dpToPx(context, heightDp)
                adView.layoutParams = FrameLayout.LayoutParams(width, heightPx)
                adView.setExtraParameter("adaptive_banner", "true")
                adView.setLocalExtraParameter("adaptive_banner_width", 400)
                adView.adFormat.getAdaptiveSize(400, context).height
                this.adKey = adKey
                this.jsonObj = jsonObj
                (context).mBannerAd.removeAllViews()
                (context).mBannerAd.addView(adView)
                adView.loadAd()
            }
        } else {
            Log.d(tag, "context is null")
        }
    }

    override fun onAdLoaded(ad: MaxAd?) {
        delegate?.onLoadSuccess(agencySeCode, advrtsTyCode, adKey, jsonObj)
        Log.d(tag, "onAdLoaded: ")
        openBannerAdSection()
    }

    override fun onAdClicked(ad: MaxAd?) {
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
        closeBannerAdSection()
    }

    override fun onAdDisplayFailed(p0: MaxAd?, p1: MaxError?) {
        val errMsg = p1!!.message
        errMsg.replace("\'", "_")
        delegate?.onException(
            agencySeCode, advrtsTyCode, adKey, errMsg, jsonObj
        )
        Log.d(tag, "onAdDisplayFailed: errMsg=${errMsg}")
        closeBannerAdSection()
    }

    override fun onAdExpanded(p0: MaxAd?) {
        delegate?.onOpen(agencySeCode, advrtsTyCode, adKey, jsonObj)
        Log.d(tag, "onAdExpanded: ")
    }

    override fun onAdCollapsed(p0: MaxAd?) {
        delegate?.onClose(agencySeCode, advrtsTyCode, adKey, jsonObj)
        Log.d(tag, "onAdCollapsed: ")
    }

    override fun onAdDisplayed(p0: MaxAd?) {
        // only for full screen ads
    }

    override fun onAdHidden(p0: MaxAd?) {
        // only for full screen ads
    }


    override fun openBannerAdSection() {
        val context = contextRef.get()
        (context as MainActivity).mBannerAd.visibility = View.VISIBLE
        delegate?.onOpen(agencySeCode, advrtsTyCode, adKey, jsonObj)
        Log.d(tag, "onAdOpened: ")
    }

    override fun closeBannerAdSection() {
        val context = contextRef.get()
        (context as MainActivity).mBannerAd.visibility = View.GONE
        delegate?.onClose(agencySeCode, advrtsTyCode, adKey, jsonObj)
        Log.d(tag, "onAdCollapsed: ")
        (context).mBannerAd.removeAllViews()
        adViewRef.clear()
    }

    override fun remove(){
        val context = contextRef.get()
        (context as MainActivity).mBannerAd.removeAllViews()
        adViewRef.clear()
        Log.d(tag, "onAdRemoved: ")
        closeBannerAdSection()
    }
}