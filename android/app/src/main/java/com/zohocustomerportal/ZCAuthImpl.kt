package com.zohocustomerportal

import android.content.Context
import com.zoho.accounts.clientframework.IAMClientSDK
import com.zoho.accounts.clientframework.IAMErrorCodes
import com.zoho.accounts.clientframework.IAMToken
import com.zoho.accounts.clientframework.IAMTokenCallback
import com.zoho.creator.framework.exception.ZCException
import com.zoho.creator.framework.interfaces.ZCOauthHelper
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ZCAuthImpl(
    private val context: Context
) : ZCOauthHelper {

    @Throws(ZCException::class)
    override suspend fun getAccessToken(): String? {
        return suspendCoroutine { continuation ->
            try {
                IAMClientSDK.getInstance(context).getToken(
                    object : IAMTokenCallback {

                        override fun onTokenFetchInitiated() {
                        }

                        override fun onTokenFetchComplete(token: IAMToken) {
                            continuation.resume(token.token)
                        }

                        override fun onTokenFetchFailed(
                            errorCode: IAMErrorCodes
                        ) {
                            continuation.resume(null)
                        }
                    }
                )
            } catch (e: Exception) {
                continuation.resume(null)
            }
        }
    }

    override fun getInitialScopes(context: Context): String {
        return ""
    }

    override fun isUserSignedIn(): Boolean {
        return IAMClientSDK.getInstance(context).isUserSignedIn
    }

    override fun getUserData(): Any? {
        return null
    }

    override fun getTransformedUrl(url: String): String {
        return url
    }

    override fun checkAndLogout(): Boolean {
        return false
    }

    override fun isEnhanceTokenNeeded(newScopes: String): Boolean {
        return false
    }

    override fun enhanceToken(
        tokenHelper: ZCOauthHelper.ZCOAuthTokenHelper,
        newScopes: String
    ) {
    }

    override fun enhanceTokenWithOnDemandScope(
        tokenHelper: ZCOauthHelper.ZCOAuthTokenHelper
    ) {
    }
}