package com.example.adding.ad

interface AdCallbackDelegate : AdLoadCallbackDelegate, AdEventCallbackDelegate {
    var defaultCallbackName: String
    fun getCallBackName(): String
    fun setCallBackName(callbackName: String)

}