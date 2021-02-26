package com.answer.anything.manager

import com.facebook.CallbackManager

class FacebookAuthenticationManager {
    companion object {
        val EMAIL = "email"
    }
    private lateinit var callbackManager: CallbackManager
    fun init() {
        callbackManager = CallbackManager.Factory.create();
    }

    fun getCallbackManager() = callbackManager
}