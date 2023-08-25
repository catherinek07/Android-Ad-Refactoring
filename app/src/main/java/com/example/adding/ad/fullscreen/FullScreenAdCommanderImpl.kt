package com.example.adding.ad.fullscreen

import android.content.Context
import android.util.Log
import com.example.adding.MainActivity
import com.example.adding.ad.AdvrtsTyCode
import com.example.adding.ad.AgencySeCode
import com.example.adding.ad.fullscreen.admob.AdmobInterstitial
import com.example.adding.ad.fullscreen.admob.AdmobRewarded
import com.example.adding.ad.fullscreen.admob.AdmobNative
import com.example.adding.ad.fullscreen.applovin.ApplovinInterstitial
import com.example.adding.ad.fullscreen.applovin.ApplovinRewarded
import com.example.adding.ad.fullscreen.applovin.ApplovinNative
import com.example.adding.ad.fullscreen.tnk.TnkInterstitial
import com.example.adding.ad.fullscreen.tnk.TnkRewarded
import com.example.adding.ad.fullscreen.tnk.TnkNative
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.ref.WeakReference


class FullScreenAdCommanderImpl(context: Context) : FullScreenAdCommander {
    private val wv = (context as MainActivity).web
    private val gson: Gson = Gson()

    override val fullScreenAds: List<FullScreenAd> = listOf(
        AdmobInterstitial(WeakReference(context), delegate = this),
        AdmobRewarded(WeakReference(context), delegate = this),
        AdmobNative(WeakReference(context), delegate = this),
        TnkInterstitial(WeakReference(context), delegate = this),
        TnkRewarded(WeakReference(context), delegate = this),
        TnkNative(WeakReference(context), delegate = this),
        ApplovinInterstitial(WeakReference(context), delegate = this),
        ApplovinRewarded(WeakReference(context), delegate = this),
        ApplovinNative(WeakReference(context), delegate = this)
    )

    override fun destroy() {
        fullScreenAds.forEach { it.destroy() }
    }

    override var defaultCallbackName: String = ""
        get() {
            return field.ifBlank {
                "appToCaas.fullScreenAd." // Return the default value if the backing field is blank.
            }
        }

    override fun getCallBackName(): String {
        return defaultCallbackName
    }

    override fun setCallBackName(callbackName: String) {
        defaultCallbackName = callbackName
    }

    ///
    /// event callback 구현
    ///
    override fun onLoadSuccess(
        agencySeCode: AgencySeCode,
        advrtsTyCode: AdvrtsTyCode,
        adKey: String,
        jsonObj: Map<String, Any>
    ) {
        wv.post {
            wv.evaluateJavascript(
                "${getCallBackName()}onLoadSuccess('${agencySeCode.value}','${advrtsTyCode.value}','${adKey}','${
                    gson.toJson(
                        jsonObj
                    )
                }')", null
            )
        }
    }

    override fun onLoadFail(
        agencySeCode: AgencySeCode,
        advrtsTyCode: AdvrtsTyCode,
        adKey: String,
        errMsg: String,
        jsonObj: Map<String, Any>
    ) {
        wv.post {
            wv.evaluateJavascript(
                "${getCallBackName()}onLoadFail('${agencySeCode.value}','${advrtsTyCode.value}','${adKey}','${errMsg}','${
                    gson.toJson(
                        jsonObj
                    )
                }')", null
            )
        }
    }

    override fun onOpen(
        agencySeCode: AgencySeCode,
        advrtsTyCode: AdvrtsTyCode,
        adKey: String,
        jsonObj: Map<String, Any>
    ) {
        wv.post {
            wv.evaluateJavascript(
                "${getCallBackName()}onOpen('${agencySeCode.value}','${advrtsTyCode.value}','${adKey}','${
                    gson.toJson(
                        jsonObj
                    )
                }')", null
            )
        }
    }

    override fun onClose(
        agencySeCode: AgencySeCode,
        advrtsTyCode: AdvrtsTyCode,
        adKey: String,
        jsonObj: Map<String, Any>
    ) {
        wv.post {
            wv.evaluateJavascript(
                "${getCallBackName()}onClose('${agencySeCode.value}','${advrtsTyCode.value}','${adKey}','${
                    gson.toJson(
                        jsonObj
                    )
                }')", null
            )
        }
    }

    override fun onClick(
        agencySeCode: AgencySeCode,
        advrtsTyCode: AdvrtsTyCode,
        adKey: String,
        jsonObj: Map<String, Any>
    ) {
        wv.post {
            wv.evaluateJavascript(
                "${getCallBackName()}onClick('${agencySeCode.value}','${advrtsTyCode.value}','${adKey}','${
                    gson.toJson(
                        jsonObj
                    )
                }')", null
            )
        }
    }

    override fun onImpression(
        agencySeCode: AgencySeCode,
        advrtsTyCode: AdvrtsTyCode,
        adKey: String,
        jsonObj: Map<String, Any>
    ) {
        wv.post {
            wv.evaluateJavascript(
                "${getCallBackName()}onImpression('${agencySeCode.value}','${advrtsTyCode.value}','${adKey}','${
                    gson.toJson(
                        jsonObj
                    )
                }')", null
            )
        }
    }

    override fun onRewardComplete(
        agencySeCode: AgencySeCode,
        advrtsTyCode: AdvrtsTyCode,
        adKey: String,
        amount: Int,
        jsonObj: Map<String, Any>
    ) {
        wv.post {
            wv.evaluateJavascript(
                "${getCallBackName()}onRewardComplete('${agencySeCode.value}','${advrtsTyCode.value}','${adKey}', '${amount}','${
                    gson.toJson(
                        jsonObj
                    )
                }')", null
            )
        }
    }

    override fun onException(
        agencySeCode: AgencySeCode,
        advrtsTyCode: AdvrtsTyCode,
        adKey: String,
        errMsg: String,
        jsonObj: Map<String, Any>
    ) {
        wv.post {
            wv.evaluateJavascript(
                "${getCallBackName()}onException('${agencySeCode.value}','${advrtsTyCode.value}','${adKey}','${errMsg}','${
                    gson.toJson(
                        jsonObj
                    )
                }')", null
            )
        }
    }

    ///
    /// ad 호출
    ///


    override fun isDeveloped(agencySeCode: AgencySeCode, advrtsTyCode: AdvrtsTyCode): Boolean {
        val targetIdx = iterateAds(agencySeCode, advrtsTyCode)
        return if (isIdxExists(targetIdx)) {
            fullScreenAds[targetIdx].isDeveloped()
        } else {
            false
        }
    }

    override fun load(
        agencySeCode: AgencySeCode, advrtsTyCode: AdvrtsTyCode, adKey: String, jsonStr: String
    ) {
        val targetIdx = iterateAds(agencySeCode, advrtsTyCode)
        if (isIdxExists(targetIdx)) {
            fullScreenAds[targetIdx].load(agencySeCode, advrtsTyCode, adKey, parseJson(jsonStr))
        }
    }

    override fun show(agencySeCode: AgencySeCode, advrtsTyCode: AdvrtsTyCode, jsonStr: String) {
        val targetIdx = iterateAds(agencySeCode, advrtsTyCode)
        if (isIdxExists(targetIdx)) {
            fullScreenAds[targetIdx].show(agencySeCode, advrtsTyCode, parseJson(jsonStr))
        }
    }

    override fun invalidatedPreLoadedAd(agencySeCode: AgencySeCode, advrtsTyCode: AdvrtsTyCode) {
        val targetIdx = iterateAds(agencySeCode, advrtsTyCode)
        if (isIdxExists(targetIdx)) {
            fullScreenAds[targetIdx].invalidatePreLoadedAd()
        }
    }

    override fun isReady(agencySeCode: AgencySeCode, advrtsTyCode: AdvrtsTyCode): Boolean {
        val targetIdx = iterateAds(agencySeCode, advrtsTyCode)
        return if (isIdxExists(targetIdx)) {
            fullScreenAds[targetIdx].isReady()
        } else {
            false
        }
    }

    private fun iterateAds(agencySeCode: AgencySeCode, advrtsTyCode: AdvrtsTyCode): Int {
        var returnIdx = -1
        fullScreenAds.indices.forEach {
            val target = fullScreenAds[it]
            if (target.agencySeCode == agencySeCode && target.advrtsTyCode == advrtsTyCode) {
                returnIdx = it
            }
        }

        if (returnIdx == -1) {
            Log.d("FullScreenAdCommanderImpl", "Index Error")
        }
        return returnIdx
    }

    private fun parseJson(jsonStr: String): Map<String, Any> {
        val mapType = object : TypeToken<Map<String, Any>>() {}.type
        return gson.fromJson(jsonStr, mapType)
    }

    private fun isIdxExists(idx: Int): Boolean {
        return idx != -1
    }
}