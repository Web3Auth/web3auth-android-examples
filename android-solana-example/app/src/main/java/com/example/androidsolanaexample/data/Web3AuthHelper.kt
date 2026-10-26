package com.example.androidsolanaexample.data

import android.net.Uri
import com.web3auth.core.types.LoginParams
import com.web3auth.core.types.UserInfo
import com.web3auth.core.types.Web3AuthResponse
import java.util.concurrent.CompletableFuture

interface Web3AuthHelper {
    suspend fun login(loginParams: LoginParams): CompletableFuture<Web3AuthResponse>
    suspend fun logOut(): CompletableFuture<Void>
    fun getSolanaPrivateKey(): String

    fun getUserInfoAsync(): CompletableFuture<UserInfo>
    fun getAccessToken(): CompletableFuture<String>
    fun refreshSession(): CompletableFuture<Web3AuthResponse>
    suspend fun initialize(): CompletableFuture<Void>

    fun setResultUrl(uri: Uri?)
    suspend fun isUserAuthenticated(): Boolean
}
