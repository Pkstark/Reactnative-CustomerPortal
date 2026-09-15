package com.zohocustomerportal

data class ZohoAppConfig(
    val key: String,
    val displayName: String,

    // Zoho Accounts Customer Portal authentication URL
    val accountsPortalBaseUrl: String,

    // Generated from Creator Mobile SDK -> For Customers -> Generate Client
    val clientId: String,
    val clientSecret: String,
    val portalId: String,
    val redirectUri: String,

    // Creator Customer Portal URL
    val portalUrl: String,

    // Creator application details
    val appOwner: String,
    val appLinkName: String
)

object ZohoAppConfigs {

    val APP_1 = ZohoAppConfig(
        key = "app1",
        displayName = "TrackMyTime",

        accountsPortalBaseUrl = "https://zohouser172329.zohocreatorportal.com",

        clientId = "10133257149.BZL8SDYQV84CNC3DXIEQ5986AZBJ4E",
        clientSecret = "816a78aa819edc5153e90311ef1ab3b18b25d76b9d",
        portalId = "10133257149",
        redirectUri = "zczohouser1723.trackmytime.portal.sdk://",

        portalUrl = "https://zohouser172329.zohocreatorportal.com",

        appOwner = "zoho_user1723",
        appLinkName = "trackmytime"
    )

    val APP_2 = ZohoAppConfig(
        key = "app2",
        displayName = "Testing App",

        accountsPortalBaseUrl = "https://zohouser172324.zohocreatorportal.com",

        clientId = "10094994916.5F4G77PJE0X9JKSCR98NGGWR20NIOW",
        clientSecret = "bb2b78dd2d8603b70cb13f2e4393bbfc1f362bb679",
        portalId = "10094994916",
        redirectUri = "zczohouser1723.karthiktest.portal.sdk://",

        portalUrl = "https://zohouser172324.zohocreatorportal.com",

        appOwner = "zoho_user1723",
        appLinkName = "karthik-test"
    )

    private val configs = mapOf(
        APP_1.key to APP_1,
        APP_2.key to APP_2
    )

    fun getConfig(key: String): ZohoAppConfig? {
        return configs[key]
    }
}