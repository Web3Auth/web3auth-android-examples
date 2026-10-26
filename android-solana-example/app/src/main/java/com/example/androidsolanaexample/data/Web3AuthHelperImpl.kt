package com.example.androidsolanaexample.data

import android.net.Uri
import com.web3auth.core.Web3Auth
import com.web3auth.core.types.LoginParams
import com.web3auth.core.types.UserInfo
import com.web3auth.core.types.Web3AuthResponse
import java.util.concurrent.CompletableFuture

class Web3AuthHelperImpl(
    private val web3Auth: Web3Auth
): Web3AuthHelper {
    override suspend fun login(loginParams: LoginParams): CompletableFuture<Web3AuthResponse> {
       return web3Auth.connectTo(loginParams)
    }

    override suspend fun logOut(): CompletableFuture<Void> {
        return web3Auth.logout()
    }

    override fun getSolanaPrivateKey(): String {
        return web3Auth.getEd25519PrivateKey()
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

    override suspend fun initialize(): CompletableFuture<Void> {
       try {
          return web3Auth.initialize()
       } catch(e: Exception) {
           // Something went wrong
           throw e
       }
    }

    override fun setResultUrl(uri: Uri?) {
        web3Auth.setResultUrl(uri)
    }

    override suspend fun isUserAuthenticated(): Boolean =
        try {
            web3Auth.getPrivateKey().isNotEmpty()
        } catch (_: Exception) {
            false
        }

}
