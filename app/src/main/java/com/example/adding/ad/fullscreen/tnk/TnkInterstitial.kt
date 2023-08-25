package com.example.adding.ad.fullscreen.tnk

import android.content.Context
import android.util.Log
import com.example.adding.MainActivity
import com.example.adding.ad.AdCallbackDelegate
import com.example.adding.ad.AdvrtsTyCode
import com.example.adding.ad.AgencySeCode
import com.example.adding.ad.fullscreen.FullScreenAd
import com.tnkfactory.ad.AdError
import com.tnkfactory.ad.AdItem
import com.tnkfactory.ad.AdListener
import com.tnkfactory.ad.InterstitialAdItem
import java.lang.ref.WeakReference


class TnkInterstitial(
    private val contextRef: WeakReference<Context>,
    override val agencySeCode: AgencySeCode = AgencySeCode.TNK,
    override val advrtsTyCode: AdvrtsTyCode = AdvrtsTyCode.INTERSTITIAL,

    override val delegate: AdCallbackDelegate?
) : FullScreenAd {
    private val tag = "Tnk Interstitial"
    private var preLoadedAd: WeakReference<InterstitialAdItem?> = WeakReference(null)
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
                preLoadedAd = WeakReference(InterstitialAdItem(context as MainActivity, adKey))
                preLoadedAd.get()?.setListener(object : AdListener() {
                    override fun onError(adItem: AdItem, error: AdError) {
                        super.onError(adItem, error)
                        val errMsg = error.message
                        errMsg.replace("\'", "_")
                        if (isReady()) {
                            delegate?.onException(
                                agencySeCode, advrtsTyCode, adKey, errMsg, jsonObj
                            )
                            Log.d(tag, "Error: Exception: $errMsg")
                        } else {
                            delegate?.onLoadFail(agencySeCode, advrtsTyCode, adKey, errMsg, jsonObj)
                            Log.d(tag, "Error: Load Fail: $errMsg")
                        }
                        invalidatePreLoadedAd()
                    }

                    override fun onLoad(adItem: AdItem) {
                        super.onLoad(adItem)
                        delegate?.onLoadSuccess(agencySeCode, advrtsTyCode, adKey, jsonObj)
                        Log.d(tag, "onLoad: ")
                    }

                    override fun onShow(adItem: AdItem) {
                        super.onShow(adItem)
                        delegate?.onOpen(agencySeCode, advrtsTyCode, adKey, jsonObj)
                        Log.d(tag, "onShow: ")
                    }

                    override fun onClick(adItem: AdItem) {
                        super.onClick(adItem)
                        delegate?.onClick(agencySeCode, advrtsTyCode, adKey, jsonObj)
                        Log.d(tag, "onClick: ")

                    }

                    override fun onClose(adItem: AdItem, type: Int) {
                        delegate?.onClose(agencySeCode, advrtsTyCode, adKey, jsonObj)
                        when (type) {
                            0 -> {
                                Log.d(tag, "Simple Close (Dismissed full screen content): ")
                            }

                            1 -> {
                                Log.d(tag, "Automatic Close: ")
                            }

                            else -> {
                                Log.d(tag, "User Exited: ")
                            }
                        }
                        invalidatePreLoadedAd()
                    }
                })
                preLoadedAd.get()?.load()
            } else {
                Log.d(
                    tag, "load: ad not loaded or ready"
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
            preLoadedAd.get()?.show()
        } else {
            Log.d(tag, "show: preLoadAd is Empty")
        }
    }

    override fun invalidatePreLoadedAd() {
        preLoadedAd = WeakReference(null)
    }

    override fun isReady(): Boolean {
        return preLoadedAd.get() != null && preLoadedAd.get()!!.isLoaded
    }
}