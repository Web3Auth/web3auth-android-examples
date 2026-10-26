package com.example.androidsolanaexample.di

import android.content.Context
import com.example.androidsolanaexample.data.SolanaUseCaseImpl
import com.example.androidsolanaexample.data.Web3AuthHelper
import com.example.androidsolanaexample.data.Web3AuthHelperImpl
import com.example.androidsolanaexample.data.Web3AuthSampleConfig
import com.example.androidsolanaexample.domain.SolanaUseCase
import com.example.androidsolanaexample.viewmodel.MainViewModel
import com.web3auth.core.Web3Auth
import com.web3auth.core.types.AuthConnection
import com.web3auth.core.types.AuthConnectionConfig
import com.web3auth.core.types.BuildEnv
import com.web3auth.core.types.Language
import com.web3auth.core.types.MfaSetting
import com.web3auth.core.types.MfaSettings
import com.web3auth.core.types.ThemeModes
import com.web3auth.core.types.Web3AuthOptions
import com.web3auth.core.types.WalletServicesConfig
import com.web3auth.core.types.WhiteLabelData
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.sol4k.Connection
import org.sol4k.RpcUrl
import org.torusresearch.fetchnodedetails.types.Web3AuthNetwork

val appModule = module {
    single {
        getWeb3AuthHelper(get())
    }

    factory<Connection> {
        Connection(RpcUrl.DEVNET)
    }

    factory<SolanaUseCase> { SolanaUseCaseImpl(get()) }

    viewModel { MainViewModel(get(), get()) }
}

private fun getWeb3AuthHelper(context: Context): Web3AuthHelper {
    val whiteLabelData = WhiteLabelData(
        appName = "Web3Auth Solana Example",
        logoLight = "https://cryptologos.cc/logos/solana-sol-logo.png",
        logoDark = "https://cryptologos.cc/logos/solana-sol-logo.png",
        defaultLanguage = Language.EN,
        mode = ThemeModes.LIGHT,
        useLogoLoader = true,
        theme = hashMapOf("primary" to "#14F195"),
        tncLink = "https://web3auth.io/docs/legal/terms-and-conditions",
        privacyPolicy = "https://web3auth.io/docs/legal/privacy-policy"
    )

    val authConnections = listOf(
        AuthConnectionConfig(
            authConnectionId = Web3AuthSampleConfig.GOOGLE_AUTH_CONNECTION_ID,
            authConnection = AuthConnection.GOOGLE,
            clientId = Web3AuthSampleConfig.GOOGLE_CLIENT_ID,
            groupedAuthConnectionId = Web3AuthSampleConfig.GROUPED_AUTH_CONNECTION_ID
        )
    )

    val web3Auth: Web3Auth = Web3Auth(
        Web3AuthOptions(
            clientId = Web3AuthSampleConfig.CLIENT_ID,
            web3AuthNetwork = Web3AuthNetwork.SAPPHIRE_MAINNET,
            authBuildEnv = BuildEnv.PRODUCTION,
            redirectUrl = Web3AuthSampleConfig.REDIRECT_URL,
            authConnectionConfig = authConnections,
            whiteLabel = whiteLabelData,
            walletServicesConfig = WalletServicesConfig(whiteLabel = whiteLabelData),
            mfaSettings = MfaSettings(
                deviceShareFactor = MfaSetting(true, 1, true),
                socialBackupFactor = MfaSetting(true, 2, true),
                passwordFactor = MfaSetting(true, 3, false),
                backUpShareFactor = MfaSetting(true, 4, false),
            )
        ), context
    )

    return Web3AuthHelperImpl(web3Auth)
}
