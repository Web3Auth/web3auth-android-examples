package com.sbz.web3authdemoapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.web3auth.core.Web3Auth
import com.web3auth.core.types.AuthConnection
import com.web3auth.core.types.AuthConnectionConfig
import com.web3auth.core.types.BuildEnv
import com.web3auth.core.types.ExtraLoginOptions
import com.web3auth.core.types.Language
import com.web3auth.core.types.LoginParams
import com.web3auth.core.types.MfaSetting
import com.web3auth.core.types.MfaSettings
import com.web3auth.core.types.ThemeModes
import com.web3auth.core.types.UserInfo
import com.web3auth.core.types.WalletServicesConfig
import com.web3auth.core.types.Web3AuthOptions
import com.web3auth.core.types.Web3AuthResponse
import com.web3auth.core.types.WhiteLabelData
import org.torusresearch.fetchnodedetails.types.Web3AuthNetwork
import org.web3j.crypto.Credentials
import org.web3j.crypto.Hash
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.Sign
import org.web3j.crypto.TransactionEncoder
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.EthChainId
import org.web3j.protocol.core.methods.response.EthGetBalance
import org.web3j.protocol.core.methods.response.EthGetTransactionCount
import org.web3j.protocol.core.methods.response.EthSendTransaction
import org.web3j.protocol.http.HttpService
import org.web3j.utils.Convert
import org.web3j.utils.Numeric
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.util.concurrent.CompletableFuture

class MainActivity : AppCompatActivity() {

    companion object {
        private const val GROUPED_AUTH_CONNECTION_ID = "aggregate-sapphire"
        private const val GOOGLE_AUTH_CONNECTION_ID = "w3a-google"
        private const val AUTH0_AUTH_CONNECTION_ID = "w3a-a0-email-passwordless"
        private const val AUTH0_DOMAIN = "https://web3auth.au.auth0.com"
        private const val DEFAULT_CHAIN_ID = "0xaa36a7" // Sepolia
    }

    private lateinit var web3Auth: Web3Auth
    private lateinit var web3: Web3j
    private var credentials: Credentials? = null
    private val rpcUrl = "https://1rpc.io/sepolia"
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        web3 = Web3j.build(HttpService(rpcUrl))

        val whiteLabelData = WhiteLabelData(
            "Web3Auth Android Example",
            null,
            "https://cryptologos.cc/logos/ethereum-eth-logo.png",
            "https://cryptologos.cc/logos/ethereum-eth-logo.png",
            Language.EN,
            ThemeModes.LIGHT,
            true,
            hashMapOf("primary" to "#eb5424"),
            tncLink = "https://web3auth.io/docs/legal/terms-and-conditions",
            privacyPolicy = "https://web3auth.io/docs/legal/privacy-policy"
        )

        val authConnections = listOf(
            AuthConnectionConfig(
                authConnectionId = GOOGLE_AUTH_CONNECTION_ID,
                authConnection = AuthConnection.GOOGLE,
                clientId = getString(R.string.web3auth_google_client_id),
                groupedAuthConnectionId = GROUPED_AUTH_CONNECTION_ID
            ),
            AuthConnectionConfig(
                authConnectionId = AUTH0_AUTH_CONNECTION_ID,
                authConnection = AuthConnection.CUSTOM,
                clientId = getString(R.string.web3auth_auth0_client_id),
                groupedAuthConnectionId = GROUPED_AUTH_CONNECTION_ID,
                jwtParameters = ExtraLoginOptions(
                    domain = AUTH0_DOMAIN,
                    userIdField = "email",
                    isUserIdCaseSensitive = false
                )
            )
        )

        web3Auth = Web3Auth(
            Web3AuthOptions(
                clientId = getString(R.string.web3auth_project_id),
                web3AuthNetwork = Web3AuthNetwork.SAPPHIRE_MAINNET,
                authBuildEnv = BuildEnv.PRODUCTION,
                redirectUrl = "com.sbz.web3authdemoapp://auth",
                whiteLabel = whiteLabelData,
                walletServicesConfig = WalletServicesConfig(whiteLabel = whiteLabelData),
                authConnectionConfig = authConnections,
                mfaSettings = MfaSettings(
                    deviceShareFactor = MfaSetting(true, 1, true),
                    socialBackupFactor = MfaSetting(true, 2, true),
                    passwordFactor = MfaSetting(true, 3, false),
                    backUpShareFactor = MfaSetting(true, 4, false),
                ),
                defaultChainId = DEFAULT_CHAIN_ID
            ), this
        )

        web3Auth.setResultUrl(intent?.data)

        val sessionResponse: CompletableFuture<Void> = web3Auth.initialize()
        sessionResponse.whenComplete { _, error ->
            if (error == null) {
                setCredentialsIfPresent()
                reRender()
                try {
                    println("PrivKey: ${web3Auth.getPrivateKey()}")
                    println("ed25519PrivKey: ${web3Auth.getEd25519PrivateKey()}")
                } catch (ex: Exception) {
                    Log.d("MainActivity_Web3Auth", ex.message ?: "Unable to fetch keys")
                }
                web3Auth.getUserInfoAsync().whenComplete { info, infoError ->
                    if (infoError == null) {
                        Log.d("MainActivity_Web3Auth", "idToken present=${!info.idToken.isNullOrBlank()}")
                    }
                }
            } else {
                Log.d("MainActivity_Web3Auth", error.message ?: "Something went wrong")
            }
        }

        val signInGoogleButton = findViewById<Button>(R.id.signInGoogle)
        signInGoogleButton.setOnClickListener { signInGoogle() }

        val signInEPButton = findViewById<Button>(R.id.signInEP)
        signInEPButton.setOnClickListener { signInEP() }

        val signOutButton = findViewById<Button>(R.id.signOutButton)
        signOutButton.setOnClickListener { signOut() }
        signOutButton.visibility = View.GONE

        val getAddressButton = findViewById<Button>(R.id.getAddress)
        getAddressButton.setOnClickListener { getAddress() }
        getAddressButton.visibility = View.GONE

        val getBalanceButton = findViewById<Button>(R.id.getBalance)
        getBalanceButton.setOnClickListener { getBalance() }
        getBalanceButton.visibility = View.GONE

        val getMessageButton = findViewById<Button>(R.id.getMessage)
        getMessageButton.setOnClickListener { signMessage("Welcome to Web3Auth") }
        getMessageButton.visibility = View.GONE

        val getTransactionButton = findViewById<Button>(R.id.getTransaction)
        getTransactionButton.setOnClickListener {
            sendTransaction(0.001, "0xeaA8Af602b2eDE45922818AE5f9f7FdE50cFa1A8")
        }
        getTransactionButton.visibility = View.GONE

        val getEnableMFAButton = findViewById<Button>(R.id.enableMFA)
        getEnableMFAButton.setOnClickListener { enableMFA() }
        getEnableMFAButton.visibility = View.GONE

        val manageMfaButton = findViewById<Button>(R.id.manageMFA)
        manageMfaButton.setOnClickListener { manageMFA() }
        manageMfaButton.visibility = View.GONE

        val getAccessTokenButton = findViewById<Button>(R.id.getAccessToken)
        getAccessTokenButton.setOnClickListener { getAccessToken() }
        getAccessTokenButton.visibility = View.GONE

        val refreshSessionButton = findViewById<Button>(R.id.refreshSession)
        refreshSessionButton.setOnClickListener { refreshSession() }
        refreshSessionButton.visibility = View.GONE

        val getLaunchWalletServicesButton = findViewById<Button>(R.id.launchWalletServices)
        getLaunchWalletServicesButton.setOnClickListener { launchWalletServices() }
        getLaunchWalletServicesButton.visibility = View.GONE
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        web3Auth.setResultUrl(intent?.data)
    }

    override fun onResume() {
        super.onResume()
        if (Web3Auth.getCustomTabsClosed()) {
            Toast.makeText(this, "User closed the browser.", Toast.LENGTH_SHORT).show()
            web3Auth.setResultUrl(null)
            Web3Auth.setCustomTabsClosed(false)
        }
    }

    private fun signInEP() {
        val loginCompletableFuture: CompletableFuture<Web3AuthResponse> =
            web3Auth.connectTo(
                LoginParams(
                    authConnection = AuthConnection.CUSTOM,
                    authConnectionId = AUTH0_AUTH_CONNECTION_ID,
                    groupedAuthConnectionId = GROUPED_AUTH_CONNECTION_ID,
                    extraLoginOptions = ExtraLoginOptions(
                        domain = AUTH0_DOMAIN,
                        userIdField = "email",
                        isUserIdCaseSensitive = false
                    )
                )
            )

        loginCompletableFuture.whenComplete { _, error ->
            if (error == null) {
                setCredentialsIfPresent()
                reRender()
            } else {
                Log.d("MainActivity_Web3Auth", error.message ?: "Something went wrong")
            }
        }
    }

    private fun signInGoogle() {
        val loginCompletableFuture: CompletableFuture<Web3AuthResponse> =
            web3Auth.connectTo(
                LoginParams(
                    authConnection = AuthConnection.GOOGLE,
                    authConnectionId = GOOGLE_AUTH_CONNECTION_ID,
                    groupedAuthConnectionId = GROUPED_AUTH_CONNECTION_ID,
                )
            )

        loginCompletableFuture.whenComplete { _, error ->
            if (error == null) {
                setCredentialsIfPresent()
                reRender()
            } else {
                Log.d("MainActivity_Web3Auth", error.message ?: "Something went wrong")
            }
        }
    }

    private fun enableMFA() {
        val completableFuture = web3Auth.enableMFA()
        completableFuture.whenComplete { _, error ->
            if (error == null) {
                Log.d("MainActivity_Web3Auth", "MFA launched successfully")
            } else {
                Log.d("MainActivity_Web3Auth", error.message ?: "Something went wrong")
            }
        }
    }

    private fun manageMFA() {
        web3Auth.manageMFA().whenComplete { _, error ->
            runOnUiThread {
                if (error == null) {
                    Log.d("MainActivity_Web3Auth", "MFA management launched")
                } else {
                    showResult(error.message ?: "Unable to manage MFA")
                }
            }
        }
    }

    private fun getAccessToken() {
        web3Auth.getAccessToken().whenComplete { token, error ->
            runOnUiThread {
                showResult(
                    if (error == null) "Access token:\n$token" else error.message ?: "Unable to get access token"
                )
            }
        }
    }

    private fun refreshSession() {
        web3Auth.refreshSession().whenComplete { _, error ->
            runOnUiThread {
                if (error == null) {
                    setCredentialsIfPresent()
                    reRender()
                } else {
                    showResult(error.message ?: "Unable to refresh session")
                }
            }
        }
    }

    private fun showResult(message: String) {
        val contentTextView = findViewById<TextView>(R.id.contentTextView)
        contentTextView.visibility = View.VISIBLE
        contentTextView.text = message
    }

    private fun launchWalletServices() {
        val completableFuture = web3Auth.showWalletUI()
        completableFuture.whenComplete { _, error ->
            if (error == null) {
                Log.d("MainActivity_Web3Auth", "Wallet services launched successfully")
            } else {
                Log.d("MainActivity_Web3Auth", error.message ?: "Something went wrong")
            }
        }
    }

    private fun getAddress(): String {
        val activeCredentials = requireCredentials() ?: return ""
        val contentTextView = findViewById<TextView>(R.id.contentTextView)
        val publicAddress = activeCredentials.address
        contentTextView.text = publicAddress
        println("Address:, $publicAddress")
        return publicAddress
    }

    private fun getBalance(): BigInteger? {
        val activeCredentials = requireCredentials() ?: return null
        val contentTextView = findViewById<TextView>(R.id.contentTextView)
        val publicAddress = activeCredentials.address
        val ethBalance: EthGetBalance =
            web3.ethGetBalance(publicAddress, DefaultBlockParameterName.LATEST).sendAsync().get()
        contentTextView.text = ethBalance.balance.toString()
        println("Balance: ${ethBalance.balance}")
        return ethBalance.balance
    }

    private fun signMessage(message: String): String {
        val activeCredentials = requireCredentials() ?: return ""
        val contentTextView = findViewById<TextView>(R.id.contentTextView)
        val hashedData = Hash.sha3(message.toByteArray(StandardCharsets.UTF_8))
        val signature = Sign.signMessage(hashedData, activeCredentials.ecKeyPair)
        val r = Numeric.toHexString(signature.r)
        val s = Numeric.toHexString(signature.s).substring(2)
        val v = Numeric.toHexString(signature.v).substring(2)
        val signHash = StringBuilder(r).append(s).append(v).toString()
        contentTextView.text = signHash
        println("Signed Hash: $signHash")
        return signHash
    }

    private fun sendTransaction(amount: Double, recipientAddress: String): String? {
        val activeCredentials = requireCredentials() ?: return null
        val contentTextView = findViewById<TextView>(R.id.contentTextView)
        val ethGetTransactionCount: EthGetTransactionCount =
            web3.ethGetTransactionCount(
                activeCredentials.address,
                DefaultBlockParameterName.LATEST
            ).sendAsync().get()
        val nonce: BigInteger = ethGetTransactionCount.transactionCount
        val value: BigInteger = Convert.toWei(amount.toString(), Convert.Unit.ETHER).toBigInteger()
        val gasLimit: BigInteger = BigInteger.valueOf(21000)
        val chainId: EthChainId = web3.ethChainId().sendAsync().get()

        val rawTransaction: RawTransaction = RawTransaction.createTransaction(
            chainId.chainId.toLong(),
            nonce,
            gasLimit,
            recipientAddress,
            value,
            "",
            BigInteger.valueOf(5000000000),
            BigInteger.valueOf(6000000000000)
        )

        val signedMessage: ByteArray = TransactionEncoder.signMessage(rawTransaction, activeCredentials)
        val hexValue: String = Numeric.toHexString(signedMessage)
        val ethSendTransaction: EthSendTransaction =
            web3.ethSendRawTransaction(hexValue).sendAsync().get()
        return if (ethSendTransaction.error != null) {
            println("Tx Error: ${ethSendTransaction.error.message}")
            contentTextView.text = "Tx Error: ${ethSendTransaction.error.message}"
            ethSendTransaction.error.message
        } else {
            println("Tx Hash: ${ethSendTransaction.transactionHash}")
            contentTextView.text = "Tx Hash: ${ethSendTransaction.transactionHash}"
            ethSendTransaction.transactionHash
        }
    }

    private fun signOut() {
        val logoutCompletableFuture = web3Auth.logout()
        logoutCompletableFuture.whenComplete { _, error ->
            if (error == null) {
                credentials = null
                reRender()
            } else {
                Log.d("MainActivity_Web3Auth", error.message ?: "Something went wrong")
            }
        }
        recreate()
    }

    private fun reRender() {
        if (android.os.Looper.myLooper() != android.os.Looper.getMainLooper()) {
            runOnUiThread { reRender() }
            return
        }
        web3Auth.getUserInfoAsync().whenComplete { userInfo, _ ->
            runOnUiThread { renderSession(userInfo) }
        }
    }

    private fun renderSession(userInfo: UserInfo?) {
        val contentTextView = findViewById<TextView>(R.id.contentTextView)
        val signInEPButton = findViewById<Button>(R.id.signInEP)
        val signInGoogleButton = findViewById<Button>(R.id.signInGoogle)
        val signOutButton = findViewById<Button>(R.id.signOutButton)
        val getAddressButton = findViewById<Button>(R.id.getAddress)
        val getBalanceButton = findViewById<Button>(R.id.getBalance)
        val getMessageButton = findViewById<Button>(R.id.getMessage)
        val getTransactionButton = findViewById<Button>(R.id.getTransaction)
        val getEnableMFAButton = findViewById<Button>(R.id.enableMFA)
        val manageMfaButton = findViewById<Button>(R.id.manageMFA)
        val getAccessTokenButton = findViewById<Button>(R.id.getAccessToken)
        val refreshSessionButton = findViewById<Button>(R.id.refreshSession)
        val getLaunchWalletServicesButton = findViewById<Button>(R.id.launchWalletServices)

        val key = try {
            web3Auth.getPrivateKey()
        } catch (ex: Exception) {
            Log.d("MainActivity_Web3Auth", ex.message ?: "Unable to fetch session")
            ""
        }
        val loggedIn = key.isNotEmpty()
        if (loggedIn) {
            contentTextView.text = gson.toJson(userInfo) + "\n Private Key: " + key
            contentTextView.visibility = View.VISIBLE
            signInEPButton.visibility = View.GONE
            signInGoogleButton.visibility = View.GONE
        } else {
            contentTextView.text = getString(R.string.not_logged_in)
            contentTextView.visibility = View.GONE
            signInEPButton.visibility = View.VISIBLE
            signInGoogleButton.visibility = View.VISIBLE
        }
        val sessionVisibility = if (loggedIn) View.VISIBLE else View.GONE
        signOutButton.visibility = sessionVisibility
        getAddressButton.visibility = sessionVisibility
        getBalanceButton.visibility = sessionVisibility
        getMessageButton.visibility = sessionVisibility
        getTransactionButton.visibility = sessionVisibility
        getEnableMFAButton.visibility = sessionVisibility
        manageMfaButton.visibility = sessionVisibility
        getAccessTokenButton.visibility = sessionVisibility
        refreshSessionButton.visibility = sessionVisibility
        getLaunchWalletServicesButton.visibility = sessionVisibility
    }

    private fun setCredentialsIfPresent() {
        try {
            val privateKey = web3Auth.getPrivateKey()
            if (privateKey.isNotBlank()) {
                credentials = Credentials.create(privateKey)
            }
        } catch (ex: Exception) {
            Log.d("MainActivity_Web3Auth", ex.message ?: "No private key available")
        }
    }

    private fun requireCredentials(): Credentials? {
        val activeCredentials = credentials
        if (activeCredentials == null) {
            Toast.makeText(this, "Please login first.", Toast.LENGTH_SHORT).show()
            return null
        }
        return activeCredentials
    }
}
