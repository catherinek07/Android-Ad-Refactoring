package com.example.adding.ad.banner.admob

import android.content.Context
import android.util.Log
import android.view.View
import com.example.adding.MainActivity
import com.example.adding.ad.AdCallbackDelegate
import com.example.adding.ad.AdvrtsTyCode
import com.example.adding.ad.AgencySeCode
import com.example.adding.ad.banner.BannerAd
import com.google.android.gms.ads.*
import java.lang.ref.WeakReference

class AdmobBanner(
    private val contextRef: WeakReference<Context>,
    override val agencySeCode: AgencySeCode = AgencySeCode.ADMOB,
    override val advrtsTyCode: AdvrtsTyCode = AdvrtsTyCode.BANNER,
    override val delegate: AdCallbackDelegate?
) : BannerAd {
    private val tag = "Admob Banner"
    private var bannerViewRef: WeakReference<AdView?> = WeakReference(null)
    private var adKey: String = ""
    private var jsonObj: Map<String, Any> = mapOf()
    override fun destroy() {
        closeBannerAdSection()
        bannerViewRef.clear()
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
            this.adKey = adKey
            this.jsonObj = jsonObj
            val adSize = AdSize.getCurrentOrientationInlineAdaptiveBannerAdSize(context, 320)
            bannerViewRef = WeakReference(AdView(context))
            if (bannerViewRef.get() != null) {
                val bannerView = bannerViewRef.get()!!
                bannerView.adUnitId = adKey
                bannerView.setAdSize(adSize)

                bannerView.adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        delegate?.onOpen(agencySeCode, advrtsTyCode, adKey, jsonObj)
                        Log.d(tag, "onAdLoaded: ")
                        openBannerAdSection()
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                        val errMsg = p0.message
                        errMsg.replace("\'", "_")
                        delegate?.onLoadFail(
                            agencySeCode, advrtsTyCode, adKey, errMsg, jsonObj
                        )
                        Log.d(tag, "onAdFailedToLoad: errMsg=${errMsg}")
                        closeBannerAdSection()
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        delegate?.onImpression(agencySeCode, advrtsTyCode, adKey, jsonObj)
                        Log.d(tag, "onAdImpression: ")
                    }


                    override fun onAdClicked() {
                        super.onAdClicked()
                        delegate?.onClick(agencySeCode, advrtsTyCode, adKey, jsonObj)
                        Log.d(tag, "onAdClicked: ")
                    }


                    override fun onAdOpened() {
                        super.onAdOpened()
                        delegate?.onOpen(agencySeCode, advrtsTyCode, adKey, jsonObj)
                        Log.d(tag, "onAdOpened: ")
                    }

                    override fun onAdClosed() {
                        super.onAdClosed()
                        delegate?.onClose(agencySeCode, advrtsTyCode, adKey, jsonObj)
                        Log.d(tag, "onAdClosed: ")
                    }
                }
                val bannerAdRequest = AdRequest.Builder().build()
                (context as MainActivity).mBannerAd.removeAllViews()
                (context).mBannerAd.addView(bannerView)
                bannerView.loadAd(bannerAdRequest)
            } else {
                Log.d(tag, "Banner View Reference is null")
            }
        } else {
            Log.d(tag, "context is null")
        }
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
        Log.d(tag, "onAdClosed: ")
        bannerViewRef.clear()
    }

    override fun remove(){
        val context = contextRef.get()
        bannerViewRef.clear()
        (context as MainActivity).mBannerAd.removeAllViews()
        Log.d(tag, "onAdRemoved: ")
        closeBannerAdSection()
    }
}