package com.example.adding

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.ViewAnimator
import androidx.constraintlayout.widget.ConstraintLayout
import com.applovin.sdk.AppLovinSdk
import com.example.adding.webview.jsInterface.AndroidBridge
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAdView
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    lateinit var mBannerAd: LinearLayout
    var overlayAsLayout by Delegates.notNull<Int>()
    lateinit var nAdContainer: RelativeLayout
    lateinit var nAdIcon0: ImageView
    lateinit var nAdTitle0: TextView
    lateinit var nAdDesc0: TextView
    lateinit var nAdIcon1: ImageView
    lateinit var nAdTitle1: TextView
    lateinit var nAdDesc1: TextView
    lateinit var nAdIcon2: ImageView
    lateinit var nAdTitle2: TextView
    lateinit var nAdDesc2: TextView
    lateinit var nAdView: NativeAdView
    lateinit var nAdButton: ImageButton
    lateinit var web: WebView
    lateinit var admob: ConstraintLayout
    lateinit var tnk: ConstraintLayout
    lateinit var applovin: ConstraintLayout
    lateinit var overlayAsView: ConstraintLayout
    lateinit var viewAnimator: ViewAnimator
    lateinit var nAdBinder: FrameLayout
    lateinit var option: LinearLayout
    lateinit var nAdMedia: FrameLayout
    lateinit var nAdMediaView: MediaView

    var applovinLayout: Int = 0
    private val tag = "MainActivity"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        val testDeviceIds = listOf("05C2294DEF56F4BF86B2D3A6F445177C")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            finish()
            return
        }

        AppLovinSdk.getInstance(this).mediationProvider = "max"
        AppLovinSdk.getInstance(this).initializeSdk {}

        this.applicationContext

        setContentView(R.layout.activity_main)


        //webview
        web = findViewById(R.id.webview)
        web.settings.javaScriptEnabled = true
        WebView.setWebContentsDebuggingEnabled(true)

        web.loadUrl("http://192.168.10.234:8080/public/test/etc/testAd.html/")
        //initializing ads
        MobileAds.registerWebView(web)


        web.addJavascriptInterface(AndroidBridge(this), "Android")

        MobileAds.initialize(this) {
            Log.d(tag, "AdMob SDK initialized.")
        }


        this.nAdContainer = findViewById(R.id.native_ad_container)

        this.overlayAsView = findViewById(R.id.native_ad_overlay)

        this.mBannerAd = findViewById(R.id.bannerAd)

        this.overlayAsLayout = R.layout.native_ad_overlay
        this.nAdView = this.overlayAsView.findViewById(R.id.adview)
        this.nAdIcon0 = this.overlayAsView.findViewById(R.id.ad_icon0)
        this.nAdTitle0 = this.overlayAsView.findViewById(R.id.ad_title0)
        this.nAdDesc0 = this.overlayAsView.findViewById(R.id.ad_desc0)
        this.nAdIcon1 = this.overlayAsView.findViewById(R.id.ad_icon1)
        this.nAdTitle1 = this.overlayAsView.findViewById(R.id.ad_title1)
        this.nAdDesc1 = this.overlayAsView.findViewById(R.id.ad_desc1)
        this.nAdIcon2 = this.overlayAsView.findViewById(R.id.ad_icon2)
        this.nAdTitle2 = this.overlayAsView.findViewById(R.id.ad_title2)
        this.nAdDesc2 = this.overlayAsView.findViewById(R.id.ad_desc2)
        this.nAdButton = this.overlayAsView.findViewById(R.id.dismiss_button)
        this.admob = this.findViewById(R.id.admob_layout)
        this.tnk = this.findViewById(R.id.tnk_layout)
        this.applovin = this.findViewById(R.id.applovin_layout)
        this.viewAnimator = this.findViewById(R.id.view_animator)
        this.nAdBinder = this.findViewById(R.id.ad_binder)
        this.option = this.findViewById(R.id.option)
        this.nAdMedia = this.findViewById(R.id.ad_media)
        this.nAdMediaView = this.findViewById(R.id.ad_mediaview)

        applovinLayout = R.layout.applovin_layout


    }
}



