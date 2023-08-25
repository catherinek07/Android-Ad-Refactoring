package com.example.adding.ad.banner.tnk

import android.content.Context
import android.util.Log
import android.view.View
import com.example.adding.MainActivity
import com.example.adding.ad.AdCallbackDelegate
import com.example.adding.ad.AdvrtsTyCode
import com.example.adding.ad.AgencySeCode
import com.example.adding.ad.banner.BannerAd
import com.tnkfactory.ad.AdError
import com.tnkfactory.ad.AdItem
import com.tnkfactory.ad.AdListener
import com.tnkfactory.ad.BannerAdView
import java.lang.ref.WeakReference

class TnkBanner(
    private val contextRef: WeakReference<Context>,
    override val agencySeCode: AgencySeCode = AgencySeCode.TNK,
    override val advrtsTyCode: AdvrtsTyCode = AdvrtsTyCode.BANNER,
    override val delegate: AdCallbackDelegate?
) : BannerAd {
    private val tag = "TNK Banner"
    private var bannerAdViewRef: WeakReference<BannerAdView?> = WeakReference(null)
    private var adKey: String = ""
    private var jsonObj: Map<String, Any> = mapOf()
    override fun destroy() {
        closeBannerAdSection()
        bannerAdViewRef.clear()
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
            bannerAdViewRef = WeakReference(BannerAdView(context as MainActivity, adKey))
            if (bannerAdViewRef.get() != null) {
                val bannerAdView = bannerAdViewRef.get()!!

                val adListener = object : AdListener() {
                    override fun onLoad(adItem: AdItem) {
                        super.onLoad(adItem)
                        delegate?.onLoadSuccess(agencySeCode, advrtsTyCode, adKey, jsonObj)
                        Log.d(tag, "onLoad: ")
                        openBannerAdSection()
                    }

                    override fun onError(adItem: AdItem, adError: AdError) {
                        super.onError(adItem, adError)
                        val errMsg = adError.message
                        errMsg.replace("\'", "_")
                        delegate?.onException(
                            agencySeCode, advrtsTyCode, adKey, errMsg, jsonObj
                        )
                        Log.d(tag, "onError: onException errMsg=${errMsg}")
                        closeBannerAdSection()
                    }

                    override fun onShow(adItem: AdItem) {
                        super.onShow(adItem)
                        delegate?.onImpression(agencySeCode, advrtsTyCode, adKey, jsonObj)
                        Log.d(tag, "onShow: ")
                    }

                    override fun onClick(adItem: AdItem) {
                        super.onClick(adItem)
                        delegate?.onClick(agencySeCode, advrtsTyCode, adKey, jsonObj)
                        Log.d(tag, "onClick: ")

                    }
                }
                bannerAdView.setListener(adListener)
                (context).mBannerAd.removeAllViews()
                (context).mBannerAd.addView(bannerAdView)
                bannerAdView.load()
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
        bannerAdViewRef.clear()
        (context).mBannerAd.removeAllViews()
    }

    override fun remove(){
        val context = contextRef.get()
        bannerAdViewRef.clear()
        (context as MainActivity).mBannerAd.removeAllViews()
        Log.d(tag, "onAdRemoved: ")
        closeBannerAdSection()
    }
}