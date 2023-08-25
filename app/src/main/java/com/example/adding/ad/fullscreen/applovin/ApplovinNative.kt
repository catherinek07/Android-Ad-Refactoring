package com.example.adding.ad.fullscreen.applovin

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder
import com.example.adding.MainActivity
import com.example.adding.ad.AdCallbackDelegate
import com.example.adding.ad.AdvrtsTyCode
import com.example.adding.ad.AgencySeCode
import com.example.adding.ad.fullscreen.FullScreenAd
import java.lang.ref.WeakReference

class ApplovinNative(
    private val contextRef: WeakReference<Context>,
    override val agencySeCode: AgencySeCode = AgencySeCode.APPLOVIN,
    override val advrtsTyCode: AdvrtsTyCode = AdvrtsTyCode.NATIVE,
    override val delegate: AdCallbackDelegate?
) : FullScreenAd, MaxNativeAdListener() {
    private val tag = "Applovin Native"
    private var preLoadedAd: WeakReference<MaxAd?> = WeakReference(null)
    private var adKey: String = ""
    private var jsonObj: Map<String, Any> = mapOf()
    private var nativeAdLoader: MaxNativeAdLoader? = null
    private lateinit var container: ViewGroup
    private lateinit var parentView: ViewGroup

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
        val context = contextRef.get() as MainActivity
        this.adKey = adKey
        this.jsonObj = jsonObj
        context.viewAnimator.displayedChild = 2
        context.applovin.visibility = View.VISIBLE
        if (!isReady()) {
            container = (context).applovin
            parentView = context.viewAnimator
            container.removeAllViews()
            parentView.removeView(container)
            context.overlayAsView.removeView(context.viewAnimator)
            context.nAdContainer.removeView(context.overlayAsView)
            nativeAdLoader = MaxNativeAdLoader(adKey, context)
            nativeAdLoader?.setNativeAdListener(this)
            nativeAdLoader?.loadAd(createNativeAdView())
        } else {
            Log.d(
                tag, "load: ad has already been loaded"
            )
        }
    }

    override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, nativeAd: MaxAd) {
        val context = contextRef.get() as MainActivity
        if (isReady()) {
            nativeAdLoader?.destroy(preLoadedAd.get())
        }
        preLoadedAd = WeakReference(nativeAd)
        container.addView(nativeAdView)
        parentView.addView(container)
        context.overlayAsView.addView(parentView)
        context.nAdContainer.addView(context.overlayAsView)

        delegate?.onLoadSuccess(agencySeCode, advrtsTyCode, adKey, jsonObj)
        Log.d(tag, "onAdLoaded: ")
    }

    override fun onNativeAdLoadFailed(p0: String?, p1: MaxError?) {
        super.onNativeAdLoadFailed(p0, p1)
        val errMsg = p0!!
        errMsg.replace("\'", "_")
        delegate?.onLoadFail(
            agencySeCode, advrtsTyCode, adKey, errMsg, jsonObj
        )
        Log.d(tag, "onAdLoadFailed: errMsg=${errMsg}")
        Log.d(tag, p1!!.message)
    }

    override fun onNativeAdClicked(p0: MaxAd?) {
        super.onNativeAdClicked(p0)
        delegate?.onClick(agencySeCode, advrtsTyCode, adKey, jsonObj)
        Log.d(tag, "onAdClicked: ")
    }

    private fun createNativeAdView(): MaxNativeAdView {
        val context = contextRef.get() as MainActivity
        val binder: MaxNativeAdViewBinder = MaxNativeAdViewBinder.Builder(context.applovinLayout)
            .setTitleTextViewId(context.nAdTitle2.id)
            .setBodyTextViewId(context.nAdDesc2.id)
            .setIconImageViewId(context.nAdIcon2.id)
            .setOptionsContentViewGroupId(context.option.id)
            .setMediaContentViewGroupId(context.nAdMedia.id)
            .build()
        return MaxNativeAdView(binder, context)
    }

    override fun show(
        agencySeCode: AgencySeCode, AdvrtsTyCode: AdvrtsTyCode, jsonObj: Map<String, Any>
    ) {
        val context = contextRef.get() as MainActivity
        if (isReady()) {
            context.admob.visibility = View.GONE
            context.tnk.visibility = View.GONE
            context.applovin.visibility = View.VISIBLE
            context.nAdContainer.visibility = View.VISIBLE
            context.nAdButton.bringToFront()
            context.nAdButton.setOnClickListener {
                context.nAdContainer.visibility = View.GONE
                invalidatePreLoadedAd()
                delegate?.onClose(agencySeCode, advrtsTyCode, adKey, jsonObj)
                Log.d(tag, "onAdClosed: ")
            }
            delegate?.onOpen(agencySeCode, advrtsTyCode, adKey, jsonObj)
            Log.d(tag, "onAdDisplayed: ")
            invalidatePreLoadedAd()
        } else {
            Log.d(tag, "show: native ad is null or not loaded yet.")
        }
    }

    override fun invalidatePreLoadedAd() {
        preLoadedAd = WeakReference(null)
    }

    override fun isReady(): Boolean {
        return preLoadedAd.get() != null && preLoadedAd.get()!!.nativeAd != null
    }
}