package com.zohocustomerportal

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.zoho.accounts.clientframework.IAMClientSDK
import com.zoho.accounts.clientframework.IAMErrorCodes
import com.zoho.accounts.clientframework.IAMToken
import com.zoho.accounts.clientframework.IAMTokenCallback
import com.zoho.creator.framework.apiutil.ZCAPIUtil

class ZohoAuthModule(
    private val reactContext: ReactApplicationContext
) : ReactContextBaseJavaModule(reactContext) {

    companion object {
        private const val MODULE_NAME = "ZohoAuth"
    }

    private val scopes =
        "AaaServer.profile.READ,ZohoCreator.report.READ"

    private var selectedAppConfig: ZohoAppConfig? = null

    override fun getName(): String {
        return MODULE_NAME
    }

    private fun initializeSelectedApp(
        config: ZohoAppConfig
    ) {
        val context = reactContext.applicationContext

        val sdk = IAMClientSDK.getInstance(context)

        sdk.init(
            config.accountsPortalBaseUrl,
            config.redirectUri,
            config.clientId,
            config.clientSecret,
            config.portalId,
            scopes
        )

        ZCAPIUtil.setOAuthHelper(
            ZCAuthImpl(context)
        )

        ZCAPIUtil.initialize(context)

        ZCAPIUtil.setPortalDetails(
            config.appOwner,
            config.appLinkName,
            config.portalUrl,
            config.portalId.toLong()
        )

        selectedAppConfig = config
    }

    @ReactMethod
    fun login(
        appKey: String,
        promise: Promise
    ) {
        try {
            val config = ZohoAppConfigs.getConfig(appKey)

            if (config == null) {
                promise.reject(
                    "INVALID_APP",
                    "No Zoho Creator configuration found for app: $appKey"
                )
                return
            }

            val context = reactContext.applicationContext
            val sdk = IAMClientSDK.getInstance(context)

            initializeSelectedApp(config)

            if (sdk.isUserSignedInForPortalID(config.portalId)) {
                selectedAppConfig = config
                promise.resolve(true)
                return
            }

            val activity = reactContext.currentActivity

            if (activity == null) {
                promise.reject(
                    "NO_ACTIVITY",
                    "Unable to open Zoho Customer Portal login."
                )
                return
            }

            sdk.presentLoginScreen(
                activity,
                object : IAMTokenCallback {

                    override fun onTokenFetchInitiated() {
                    }

                    override fun onTokenFetchComplete(
                        token: IAMToken
                    ) {
                        if (!token.token.isNullOrEmpty()) {
                            ZCAPIUtil.setPortalDetails(
                                config.appOwner,
                                config.appLinkName,
                                config.portalUrl,
                                config.portalId.toLong()
                            )

                            selectedAppConfig = config

                            promise.resolve(true)
                        } else {
                            promise.reject(
                                "LOGIN_FAILED",
                                "Zoho Customer Portal login failed."
                            )
                        }
                    }

                    override fun onTokenFetchFailed(
                        errorCode: IAMErrorCodes
                    ) {
                        promise.reject(
                            "LOGIN_FAILED",
                            "Zoho Customer Portal login failed."
                        )
                    }
                },
                null
            )

        } catch (e: Exception) {
            promise.reject(
                "LOGIN_ERROR",
                e.message,
                e
            )
        }
    }

    @ReactMethod
    fun isLoggedIn(
        promise: Promise
    ) {
        try {
            val context = reactContext.applicationContext
            val sdk = IAMClientSDK.getInstance(context)

            val config = selectedAppConfig

            if (config != null) {
                promise.resolve(
                    sdk.isUserSignedInForPortalID(config.portalId)
                )
                return
            }

            promise.resolve(
                sdk.isUserSignedIn
            )

        } catch (e: Exception) {
            promise.reject(
                "LOGIN_STATUS_ERROR",
                e.message,
                e
            )
        }
    }

    @ReactMethod
    fun logout(
        promise: Promise
    ) {
        try {
            val context = reactContext.applicationContext
            val sdk = IAMClientSDK.getInstance(context)

            if (!sdk.isUserSignedIn) {
                selectedAppConfig = null
                ZCAPIUtil.clearPortalDetails()
                promise.resolve(true)
                return
            }

            sdk.revoke(
                object : IAMClientSDK.OnLogoutListener {

                    override fun onLogoutSuccess() {
                        selectedAppConfig = null
                        ZCAPIUtil.clearPortalDetails()
                        promise.resolve(true)
                    }

                    override fun onLogoutFailed() {
                        selectedAppConfig = null
                        ZCAPIUtil.clearPortalDetails()

                        promise.reject(
                            "LOGOUT_FAILED",
                            "Unable to logout from Zoho Customer Portal."
                        )
                    }
                }
            )

        } catch (e: Exception) {
            selectedAppConfig = null
            ZCAPIUtil.clearPortalDetails()

            promise.reject(
                "LOGOUT_ERROR",
                e.message,
                e
            )
        }
    }
}