package com.example.adding.ad

enum class AgencySeCode(val value: String) {
    ADMOB("201"), TNK("202"), APPLOVIN("206");

    companion object {
        fun fromValue(value: String): AgencySeCode? {
            return values().find { it.value == value }
        }
    }

}
