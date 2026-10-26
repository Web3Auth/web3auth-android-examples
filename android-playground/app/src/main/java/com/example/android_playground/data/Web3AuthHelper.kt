package com.example.android_playground.data

import android.net.Uri
import com.google.gson.JsonArray
import com.web3auth.core.types.LoginParams
import com.web3auth.core.types.SignResponse
import com.web3auth.core.types.UserInfo
import com.web3auth.core.types.Web3AuthResponse
import java.util.concurrent.CompletableFuture

interface Web3AuthHelper {
    suspend fun connectTo(loginParams: LoginParams): CompletableFuture<Web3AuthResponse>
    suspend fun logOut(): CompletableFuture<Void>
    fun getPrivateKey(): String

    fun getUserInfoAsync(): CompletableFuture<UserInfo>
    fun getAccessToken(): CompletableFuture<String>
    fun refreshSession(): CompletableFuture<Web3AuthResponse>
    fun enableMFA(): CompletableFuture<Boolean>
    fun manageMFA(): CompletableFuture<Boolean>
    fun showWalletUI(): CompletableFuture<Void>
    fun request(method: String, requestParams: JsonArray, appState: String?): CompletableFuture<SignResponse>
    suspend fun initialize(): CompletableFuture<Void>

    suspend fun setResultUrl(uri: Uri?)
    suspend fun isUserAuthenticated(): Boolean
}
