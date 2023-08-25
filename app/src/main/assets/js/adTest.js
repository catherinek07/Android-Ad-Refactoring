caasToApp = {

    //banner
    launchBannerAd(agencySeCode, advrtsTyCode, adKey, jsonObj) {
        Android.launchBannerAd(agencySeCode, advrtsTyCode, adKey, JSON.stringify(jsonObj))
    },
    bannerAdIsDeveloped(agencySeCode, advrtsTyCode, callback) {
        Android.bannerAdIsDeveloped(agencySeCode, advrtsTyCode, callback)
    },
    bannerAdSectionOpen(agencySeCode, advrtsTyCode, callback) {
        Android.bannerAdSectionOpen(agencySeCode, advrtsTyCode, callback)
    },
    bannerAdSectionClose(agencySeCode, advrtsTyCode, callback) {
        Android.bannerAdSectionClose(agencySeCode, advrtsTyCode, callback)
    },
    bannerAdSetCallbackName(callbackName, callback) {
        Android.bannerAdSetCallbackName(callbackName, callback)
    },
    bannerAdGetCallbackName(callback) {
        Android.bannerAdGetCallbackName(callback)
    },

    //fullscreen
    loadFullScreenAd(agencySeCode, advrtsTyCode, adKey, jsonObj) {
        Android.loadFullScreenAd(agencySeCode, advrtsTyCode, adKey, JSON.stringify(jsonObj))
    },
    showFullScreenAd(agencySeCode, advrtsTyCode) {
        Android.showFullScreenAd(agencySeCode, advrtsTyCode)
    },
    fullScreenAdIsDeveloped(agencySeCode, advrtsTyCode, callback) {
        Android.fullScreenAdIsDeveloped(agencySeCode, advrtsTyCode, callback)
    },
    fullScreenAdInvalidatePreLoadedAd(agencySeCode, advrtsTyCode, callback) {
        Android.fullScreenAdInvalidatePreLoadedAd(agencySeCode, advrtsTyCode, callback)
    },
    fullScreenAdGetLengthPreLoadedAd(agencySeCode, advrtsTyCode, callback) {
        Android.fullScreenAdGetLengthPreLoadedAd(agencySeCode, advrtsTyCode, callback)
    },
    fullScreenAdSetMaxAdCount(agencySeCode, advrtsTyCode, count, callback) {
        Android.fullScreenAdSetMaxAdCount(agencySeCode, advrtsTyCode, count, callback)
    },
    fullScreenAdGetMaxAdCount(agencySeCode, advrtsTyCode, callback) {
        Android.fullScreenAdGetMaxAdCount(agencySeCode, advrtsTyCode, callback)
    },
    fullScreenAdSetCallbackName(callbackName, callback) {
        Android.fullScreenAdSetCallbackName(callbackName, callback)
    },
    fullScreenAdGetCallbackName(callback) {
        Android.fullScreenAdGetCallbackName(callback)
    }
}

appToCaas = {
    fullScreenAd: {
        //AdLoadCallbackDelegate
        onLoadSuccess(agencySeCode, advrtsTyCode, adKey, jsonStr) {
            console.log(`[dl]onLoadSuccess agencySeCode:${agencySeCode}, advrtsTyCode: ${advrtsTyCode}, adKey: ${adKey}, jsonStr: ${jsonStr}`)
            //$('#somediv').text('loadsuccess')
            // $('onLoadSuccess').trigger([])
        },
        onLoadFail(agencySeCode, advrtsTyCode, adKey, errMsg, jsonStr) {
            console.log(`[dl]onLoadFail agencySeCode:${agencySeCode}, advrtsTyCode: ${advrtsTyCode}, adKey: ${adKey}, errMsg: ${errMsg}, jsonStr: ${jsonStr}`)
        },

        //AdEventCallbackDelegate
        onOpen(agencySeCode, advrtsTyCode, adKey, jsonStr) {
            console.log(`[dl]onOpen agencySeCode:${agencySeCode}, advrtsTyCode: ${advrtsTyCode}, adKey: ${adKey}, jsonStr: ${jsonStr}`)
        },
        onClose(agencySeCode, advrtsTyCode, adKey, jsonStr) {
            console.log(`[dl]onClose agencySeCode:${agencySeCode}, advrtsTyCode: ${advrtsTyCode}, adKey: ${adKey}, jsonStr: ${jsonStr}`)
        },
        onClick(agencySeCode, advrtsTyCode, adKey, jsonStr) {
            console.log(`[dl]onClick agencySeCode:${agencySeCode}, advrtsTyCode: ${advrtsTyCode}, adKey: ${adKey}, jsonStr: ${jsonStr}`)
        },
        onImpression(agencySeCode, advrtsTyCode, adKey, jsonStr) {
            console.log(`[dl]onImpression agencySeCode:${agencySeCode}, advrtsTyCode: ${advrtsTyCode}, adKey: ${adKey}, jsonStr: ${jsonStr}`)
        },
        onRewardComplete(agencySeCode, advrtsTyCode, adKey, jsonStr) {
            console.log(`[dl]onRewardComplete agencySeCode:${agencySeCode}, advrtsTyCode: ${advrtsTyCode}, adKey: ${adKey}, jsonStr: ${jsonStr}`)
        },
        onException(agencySeCode, advrtsTyCode, adKey, errMsg, jsonStr) {
            console.log(`[dl]onException agencySeCode:${agencySeCode}, advrtsTyCode: ${advrtsTyCode}, adKey: ${adKey}, errMsg: ${errMsg}, jsonStr: ${jsonStr}`)
        },

        //AdCallbackDelegate
        // defaultCallbackName,
        getCallBackName() { },
        setCallbackName(callbackName) { }
    },
    bannerAd: {

        //AdLoadCallbackDelegate
        onLoadSuccess(agencySeCode, advrtsTyCode, adKey, jsonStr) {
            //$('#somediv').text('loadsuccess')
        },
        onLoadFail(agencySeCode, advrtsTyCode, adKey, errMsg, jsonStr) { },

        //AdEventCallbackDelegate
        onOpen(agencySeCode, advrtsTyCode, adKey, jsonStr) { },
        onClose(agencySeCode, advrtsTyCode, adKey, jsonStr) { },
        onClick(agencySeCode, advrtsTyCode, adKey, jsonStr) { },
        onImpression(agencySeCode, advrtsTyCode, adKey, jsonStr) { },
        onRewardComplete(agencySeCode, advrtsTyCode, adKey, jsonStr) { },
        onException(agencySeCode, advrtsTyCode, adKey, errMsg, jsonSt) { },

        //AdCallbackDelegate
        // defaultCallbackName,
        getCallBackName() { },
        setCallbackName(callbackName) { }
    }
}