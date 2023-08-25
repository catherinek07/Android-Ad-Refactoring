package com.example.adding.ad

enum class AdvrtsTyCode(val value: String) {
    INTERSTITIAL("01"), REWARDED("02"), NATIVE("03"), BANNER("05");

    companion object {
        fun fromValue(value: String): AdvrtsTyCode? {
            return AdvrtsTyCode.values().find { it.value == value }
        }
    }
}