package com.example.adding.ad.fullscreen.tnk

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.adding.MainActivity
import com.example.adding.ad.AdCallbackDelegate
import com.example.adding.ad.AdvrtsTyCode
import com.example.adding.ad.AgencySeCode
import com.example.adding.ad.fullscreen.FullScreenAd
import com.tnkfactory.ad.AdError
import com.tnkfactory.ad.AdItem
import com.tnkfactory.ad.AdListener
import com.tnkfactory.ad.NativeAdItem
import com.tnkfactory.ad.NativeViewBinder
import java.lang.ref.WeakReference

class TnkNative(
    private val contextRef: WeakReference<Context>,
    override val agencySeCode: AgencySeCode = AgencySeCode.TNK,
    override val advrtsTyCode: AdvrtsTyCode = AdvrtsTyCode.NATIVE,
    override val delegate: AdCallbackDelegate?
) : FullScreenAd {
    private val tag = "Tnk Native"
    private var preLoadedAd: WeakReference<NativeAdItem?> = WeakReference(null)

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
        context.viewAnimator.displayedChild = 0
        context.nAdContainer.visibility = View.GONE

        if (!isReady()) {
            preLoadedAd = WeakReference(NativeAdItem(context, adKey))
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


            })
            preLoadedAd.get()!!.load()
            val adContainer: ViewGroup = (context).nAdContainer
            adContainer.removeAllViews()
            val view = View.inflate(context, (context).overlayAsLayout, adContainer) as ViewGroup
            val binder = NativeViewBinder((context).nAdBinder.id)
            binder.iconId((context).nAdIcon0.id)
                .titleId((context).nAdTitle0.id).textId((context).nAdDesc0.id).addClickView((context).nAdBinder.id)
            preLoadedAd.get()?.attach(view, binder)

            val dismissButton = (context).nAdContainer.findViewById<ImageButton>(context.nAdButton.id)
            dismissButton.setOnClickListener {
                adContainer.visibility = View.GONE
                adContainer.removeAllViews()
                delegate?.onClose(agencySeCode, advrtsTyCode, adKey, jsonObj)
                Log.d(tag,"Ad Closed")
                invalidatePreLoadedAd()
            }
        } else {
            Log.d(
                tag, "Ad is already loaded"
            )
        }
    }

    override fun show(
        agencySeCode: AgencySeCode, AdvrtsTyCode: AdvrtsTyCode, jsonObj: Map<String, Any>
    ) {
        if (isReady()) {
            val context = contextRef.get() as MainActivity
            context.nAdContainer.visibility = View.VISIBLE

        } else {
            Log.d(tag, "show: preLoadedAd is Empty")
        }
    }

    override fun invalidatePreLoadedAd() {
        preLoadedAd = WeakReference(null)
    }

    override fun isReady(): Boolean {
        return preLoadedAd.get() != null
    }
}