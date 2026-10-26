package com.example.android_playground.data

import android.net.Uri
import com.google.gson.JsonArray
import com.web3auth.core.Web3Auth
import com.web3auth.core.types.LoginParams
import com.web3auth.core.types.SignResponse
import com.web3auth.core.types.UserInfo
import com.web3auth.core.types.Web3AuthResponse
import java.util.concurrent.CompletableFuture

class Web3AuthHelperImpl(
    private val web3Auth: Web3Auth
): Web3AuthHelper {
    override suspend fun connectTo(loginParams: LoginParams): CompletableFuture<Web3AuthResponse> {
        return web3Auth.connectTo(loginParams)
    }

    override suspend fun logOut(): CompletableFuture<Void> {
        return web3Auth.logout()
    }

    override fun getPrivateKey(): String {
      return web3Auth.getPrivateKey()
    }

    override fun getUserInfoAsync(): CompletableFuture<UserInfo> {
        return web3Auth.getUserInfoAsync()
    }

    override fun getAccessToken(): CompletableFuture<String> {
        return web3Auth.getAccessToken()
    }

    override fun refreshSession(): CompletableFuture<Web3AuthResponse> {
        return web3Auth.refreshSession()
    }

    override fun enableMFA(): CompletableFuture<Boolean> {
        return web3Auth.enableMFA()
    }

    override fun manageMFA(): CompletableFuture<Boolean> {
        return web3Auth.manageMFA()
    }

    override fun showWalletUI(): CompletableFuture<Void> {
        return web3Auth.showWalletUI()
    }

    override fun request(
        method: String,
        requestParams: JsonArray,
        appState: String?
    ): CompletableFuture<SignResponse> {
        return web3Auth.request(method, requestParams, appState = appState)
    }

    override suspend fun initialize(): CompletableFuture<Void> {
           return web3Auth.initialize()
    }

    override suspend fun setResultUrl(uri: Uri?) {
        return web3Auth.setResultUrl(uri)
    }

    override suspend fun isUserAuthenticated(): Boolean {
        return try {
            web3Auth.getPrivateKey().isNotEmpty()
        } catch (_: Exception) {
            false
        }
    }

}
