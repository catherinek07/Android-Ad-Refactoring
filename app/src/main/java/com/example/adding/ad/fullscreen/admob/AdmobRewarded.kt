package com.example.adding.ad.fullscreen.admob

import android.content.Context
import android.util.Log
import com.example.adding.MainActivity
import com.example.adding.ad.AdCallbackDelegate
import com.example.adding.ad.AdvrtsTyCode
import com.example.adding.ad.AgencySeCode
import com.example.adding.ad.fullscreen.FullScreenAd
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import java.lang.ref.WeakReference

class AdmobRewarded(
    private val contextRef: WeakReference<Context>,
    override val agencySeCode: AgencySeCode = AgencySeCode.ADMOB,
    override val advrtsTyCode: AdvrtsTyCode = AdvrtsTyCode.REWARDED,
    override val delegate: AdCallbackDelegate?
) : FullScreenAd {
    private val tag = "Admob Rewarded"
    private var preLoadedAd: WeakReference<RewardedAd?> = WeakReference(null)
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
                this.adKey = adKey
                this.jsonObj = jsonObj
                val adRequest = AdRequest.Builder().build()
                RewardedAd.load(context, adKey, adRequest, object : RewardedAdLoadCallback() {
                    override fun onAdLoaded(rewardedAd: RewardedAd) {
                        rewardedAd.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdClicked() {
                                    super.onAdClicked()
                                    delegate?.onClick(
                                        agencySeCode, advrtsTyCode, adKey, jsonObj
                                    )
                                    Log.d(tag, "onAdClicked: ")
                                }

                                override fun onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent()
                                    delegate?.onClose(
                                        agencySeCode, advrtsTyCode, adKey, jsonObj
                                    )
                                    Log.d(tag, "onAdDismissedFullScreenContent: ")
                                    invalidatePreLoadedAd()
                                    rewardedAd.fullScreenContentCallback = null
                                }

                                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                    super.onAdFailedToShowFullScreenContent(p0)
                                    val errMsg = p0.message
                                    errMsg.replace("'", "_")
                                    delegate?.onException(
                                        agencySeCode, advrtsTyCode, adKey, errMsg, jsonObj
                                    )
                                    Log.d(
                                        tag, "onAdFailedToShowFullScreenContent: errMsg=$errMsg"
                                    )
                                    invalidatePreLoadedAd()
                                    rewardedAd.fullScreenContentCallback = null
                                }

                                override fun onAdImpression() {
                                    super.onAdImpression()
                                    delegate?.onImpression(
                                        agencySeCode, advrtsTyCode, adKey, jsonObj
                                    )
                                    Log.d(tag, "onAdImpression: ")
                                }

                                override fun onAdShowedFullScreenContent() {
                                    super.onAdShowedFullScreenContent()
                                    delegate?.onOpen(agencySeCode, advrtsTyCode, adKey, jsonObj)
                                    Log.d(tag, "onAdShowedFullScreenContent: ")
                                }
                            }

                        preLoadedAd = WeakReference(rewardedAd)
                        delegate?.onLoadSuccess(agencySeCode, advrtsTyCode, adKey, jsonObj)
                        Log.d(tag, "onAdLoaded: ")
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        val errMsg = loadAdError.message
                        errMsg.replace("'", "_")
                        delegate?.onLoadFail(agencySeCode, advrtsTyCode, adKey, errMsg, jsonObj)
                        Log.d(tag, "onAdFailedToLoad: errMsg: $errMsg")
                    }
                })
            } else {
                Log.d(
                    tag, "load: ad not loaded or ready  preLoadedAd.count=1"
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
            val context = contextRef.get()
            if (context is MainActivity) {
                preLoadedAd.get()?.show(context) { rewardItem ->
                    delegate?.onRewardComplete(
                        agencySeCode, advrtsTyCode, adKey, rewardItem.amount, jsonObj
                    )
                    Log.d(tag, "onRewardShowed")
                    invalidatePreLoadedAd()
                }
            } else {
                Log.d(tag, "show: context not casting to mainActivity")
            }
        } else {
            Log.d(tag, "show: preLoadAd is Empty")
        }
    }

    override fun invalidatePreLoadedAd() {
        preLoadedAd.clear()
    }

    override fun isReady(): Boolean {
        return preLoadedAd.get() != null
    }

}
