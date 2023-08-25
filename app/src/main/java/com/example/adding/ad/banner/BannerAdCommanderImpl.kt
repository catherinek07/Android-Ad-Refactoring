package com.example.adding.ad.banner

import android.content.Context
import android.util.Log
import android.webkit.WebView
import com.example.adding.MainActivity
import com.example.adding.ad.AdvrtsTyCode
import com.example.adding.ad.AgencySeCode
import com.example.adding.ad.banner.admob.AdmobBanner
import com.example.adding.ad.banner.applovin.ApplovinBanner
import com.example.adding.ad.banner.tnk.TnkBanner
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.ref.WeakReference


class BannerAdCommanderImpl(context: Context) : BannerAdCommander {
    private val wv: WebView = (context as MainActivity).web
    private val gson: Gson = Gson()

    override val bannerAds: List<BannerAd> = listOf(
        AdmobBanner(WeakReference(context), delegate = this),
        TnkBanner(WeakReference(context), delegate = this),
        ApplovinBanner(WeakReference(context), delegate = this)
    )

    override fun destroy() {
        bannerAds.forEach { it.destroy() }
    }

    override var defaultCallbackName: String = ""
        get() {
            return field.ifBlank {
                "appToCaas.bannerAd." // Return the default value if the backing field is blank.
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
                "${getCallBackName()}onLoadSuccess('${agencySeCode.value}','${advrtsTyCode.value}','${adKey}', '${
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
                "${getCallBackName()}onLoadFail('${agencySeCode.value}','${advrtsTyCode.value}','${adKey}','${errMsg}', '${
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
                "${getCallBackName()}onOpen('${agencySeCode.value}','${advrtsTyCode.value}','${adKey}', '${
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
                "${getCallBackName()}onClose('${agencySeCode.value}','${advrtsTyCode.value}','${adKey}', '${
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
                "${getCallBackName()}onClick('${agencySeCode.value}','${advrtsTyCode.value}','${adKey}', '${
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
                "${getCallBackName()}onImpression('${agencySeCode.value}','${advrtsTyCode.value}','${adKey}', '${
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
                "${getCallBackName()}onException('${agencySeCode.value}','${advrtsTyCode.value}','${adKey}','${errMsg}', '${
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
            bannerAds[targetIdx].isDeveloped()
        } else {
            false
        }
    }


    override fun launch(
        agencySeCode: AgencySeCode, advrtsTyCode: AdvrtsTyCode, adKey: String, jsonStr: String
    ) {
        val targetIdx = iterateAds(agencySeCode, advrtsTyCode)
        if (isIdxExists(targetIdx)) {
            bannerAds[targetIdx].launch(agencySeCode, advrtsTyCode, adKey, parseJson(jsonStr))
        }
    }

    override fun remove(agencySeCode: AgencySeCode, advrtsTyCode: AdvrtsTyCode) {
        val targetIdx = iterateAds(agencySeCode, advrtsTyCode)
        if(isIdxExists(targetIdx)){
            bannerAds[targetIdx].remove()
        }
    }
    override fun openBannerAdSection(agencySeCode: AgencySeCode, advrtsTyCode: AdvrtsTyCode) {
        val targetIdx = iterateAds(agencySeCode, advrtsTyCode)
        if (isIdxExists(targetIdx)) {
            bannerAds[targetIdx].openBannerAdSection()
        }
    }

    override fun closeBannerAdSection(agencySeCode: AgencySeCode, advrtsTyCode: AdvrtsTyCode) {
        val targetIdx = iterateAds(agencySeCode, advrtsTyCode)
        if (isIdxExists(targetIdx)) {
            bannerAds[targetIdx].closeBannerAdSection()
        }
    }

    private fun iterateAds(agencySeCode: AgencySeCode, advrtsTyCode: AdvrtsTyCode): Int {
        var returnIdx = -1
        bannerAds.indices.forEach {
            val target = bannerAds[it]
            if (target.agencySeCode == agencySeCode && target.advrtsTyCode == advrtsTyCode) {
                returnIdx = it
            }
        }

        if (returnIdx == -1) {
            Log.d("BannerAdCommanderImpl", "Index Error")
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