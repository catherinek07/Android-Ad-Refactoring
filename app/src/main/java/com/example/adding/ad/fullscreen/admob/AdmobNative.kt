package com.example.adding.ad.fullscreen.admob

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.example.adding.MainActivity
import com.example.adding.ad.AdCallbackDelegate
import com.example.adding.ad.AdvrtsTyCode
import com.example.adding.ad.AgencySeCode
import com.example.adding.ad.fullscreen.FullScreenAd
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import java.lang.ref.WeakReference

class AdmobNative(
    private val contextRef: WeakReference<Context>,
    override val agencySeCode: AgencySeCode = AgencySeCode.ADMOB,
    override val advrtsTyCode: AdvrtsTyCode = AdvrtsTyCode.NATIVE,
    override val delegate: AdCallbackDelegate?
) : FullScreenAd {
    private val tag = "Admob Native"
    private var preLoadedAd: WeakReference<NativeAd?> = WeakReference(null)
    //cheating????
//    private  var nativeAd: NativeAd? = null
//    private var preLoadedAd: NativeAd? = null
    private var adKey: String = ""
    private var jsonObj: Map<String, Any> = mapOf()
    override fun destroy() {
        Log.d(tag, "destroy: ")
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
        val context = contextRef.get() as MainActivity
        context.viewAnimator.displayedChild = 1
        if (!isReady()) {
            this.adKey = adKey
            this.jsonObj = jsonObj
            val adLoader = AdLoader.Builder(context, adKey)
                .forNativeAd { nativeAd ->
                    preLoadedAd = WeakReference(nativeAd)
//                    preLoadedAd = nativeAd
                    delegate?.onLoadSuccess(agencySeCode, advrtsTyCode, adKey, jsonObj)
                }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        val errMsg = adError.message
                        errMsg.replace("\'", "_")
                        delegate?.onLoadFail(agencySeCode, advrtsTyCode, adKey, errMsg, jsonObj)
                        Log.d(tag, "Error: errMsg: $errMsg")
                    }
                    override fun onAdClicked() {
                        super.onAdClicked()
                        delegate?.onClick(agencySeCode, advrtsTyCode, adKey, jsonObj)
                        Log.d(tag, "Ad Clicked")
                    }
                }).withNativeAdOptions(NativeAdOptions.Builder().build()).build()
            adLoader.loadAd(AdRequest.Builder().build())
        }else{
            Log.d(tag, "ad is already loaded")
        }
    }

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        val context = contextRef.get() as MainActivity
        adView.apply {
            val headlineView = context.nAdTitle1
            val bodyView = context.nAdDesc1
            val iconView = context.nAdIcon1
            val mediaView = context.nAdMediaView
            headlineView.text = nativeAd.headline
            bodyView.text = nativeAd.body
            iconView.setImageDrawable(nativeAd.icon?.drawable)
            adView.mediaView = mediaView
            adView.mediaView?.mediaContent = nativeAd.mediaContent


            adView.setNativeAd(nativeAd)
        }
    }

    override fun show(
        agencySeCode: AgencySeCode, AdvrtsTyCode: AdvrtsTyCode, jsonObj: Map<String, Any>
    ) {
        if (isReady()) {
            val context = contextRef.get() as MainActivity
            val adContainer: ViewGroup = context.nAdContainer
            val adView = context.nAdView
            context.admob.removeView(adView)
            context.viewAnimator.removeView(context.admob)
            context.overlayAsView.removeView(context.viewAnimator)
            adContainer.removeView(context.overlayAsView)
            context.nAdButton.setOnClickListener {
                adContainer.visibility = View.GONE
                delegate?.onClose(agencySeCode, advrtsTyCode, adKey, jsonObj)
                Log.d(tag, "onAdClosed: ")
                invalidatePreLoadedAd()
            }

            populateNativeAdView(preLoadedAd.get()!!, adView)
//            populateNativeAdView(preLoadedAd!!, adView)
            context.admob.addView(adView)
            context.viewAnimator.addView(context.admob)
            context.overlayAsView.addView(context.viewAnimator)
            adContainer.addView(context.overlayAsView)

            delegate?.onOpen(agencySeCode, advrtsTyCode, adKey, jsonObj)
            Log.d(tag, "onAdOpened: ")

            adContainer.setOnClickListener{
                delegate?.onClick(agencySeCode, advrtsTyCode, adKey, jsonObj)
                Log.d(tag, "onAdClicked: ")
            }

            context.admob.visibility = View.VISIBLE
            context.tnk.visibility = View.GONE
            context.applovin.visibility = View.GONE
            adContainer.visibility = View.VISIBLE

        } else {
            Log.d(tag, "preLoadedAd is empty")
        }
    }

    override fun invalidatePreLoadedAd() {
//        Log.d(tag, "invalidatePreLoadedAd: " + preLoadedAd.get())
//        preLoadedAd = WeakReference(null)
//        preLoadedAd = null
        preLoadedAd.clear()
    }

    override fun isReady(): Boolean {
//        Log.d(tag, "isReady: " + preLoadedAd.get())
//        Log.d(tag, "isReady: " + nativeAd)
        return preLoadedAd.get() != null// && preLoadedAd.get()!!.mediaContent != null
//        return preLoadedAd != null
    }
}