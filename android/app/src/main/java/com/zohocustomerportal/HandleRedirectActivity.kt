package com.zohocustomerportal

import android.app.Activity
import android.os.Bundle
import com.zoho.accounts.clientframework.IAMClientSDK

class HandleRedirectActivity : Activity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        IAMClientSDK
            .getInstance(this)
            .handleRedirection(this)

        finish()
    }
}