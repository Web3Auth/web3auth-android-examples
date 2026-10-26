package com.sbz.web3authdemoapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.web3auth.core.Web3Auth
import com.web3auth.core.types.*
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
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    private lateinit var web3Auth: Web3Auth
    private lateinit var web3: Web3j
    private lateinit var credentials: Credentials
    private lateinit var auth: FirebaseAuth
    private val rpcUrl = "https://1rpc.io/sepolia"

    private val gson = Gson()
    private val chainCalls = Executors.newSingleThreadExecutor()

    companion object {
        private const val TAG = "MainActivity_Web3Auth"
        // Same demo account as the iOS Firebase example, on project web3auth-oauth-logins.
        private const val FIREBASE_EMAIL = "custom+id_token@firebase.login"
        private const val FIREBASE_PASSWORD = "Welcome@W3A"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            auth = Firebase.auth
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize Firebase Auth", e)
            Toast.makeText(this, "Failed to initialize Firebase: ${e.message}",
                Toast.LENGTH_LONG).show()
            return
        }
        web3 = Web3j.build(HttpService(rpcUrl))

        web3Auth = Web3Auth(
           Web3AuthOptions(
               clientId = getString(R.string.web3auth_project_id), // pass over your Web3Auth Client ID from Developer Dashboard
               web3AuthNetwork = Web3AuthNetwork.SAPPHIRE_MAINNET, // pass over the network you want to use
               authBuildEnv = BuildEnv.PRODUCTION,
               redirectUrl = "com.sbz.web3authdemoapp://auth", // your app's redirect URL
               
               // Optional parameters
               walletServicesConfig = WalletServicesConfig(
                   whiteLabel = WhiteLabelData(
                       "Web3Auth Android FireBase Example",
                       null,
                       "https://cryptologos.cc/logos/ethereum-eth-logo.png",
                       "https://cryptologos.cc/logos/ethereum-eth-logo.png",
                       Language.EN,
                       ThemeModes.LIGHT,
                       true,
                       hashMapOf(
                           "primary" to "#eb5424"
                       ),
                       tncLink = "https://web3auth.io/docs/legal/terms-and-conditions",
                       privacyPolicy = "https://web3auth.io/docs/legal/privacy-policy"
                   ),

               ),
               authConnectionConfig = listOf(
                   AuthConnectionConfig(
                       authConnection = AuthConnection.CUSTOM,
                       authConnectionId = "w3a-firebase-demo",
                       clientId = getString(R.string.web3auth_project_id)
                   )
               )
           ), this
       )

        // Handle user signing in when app is not alive
        web3Auth.setResultUrl(intent?.data)

        // Call initialize() in onCreate() to check for any existing session.
        val sessionResponse: CompletableFuture<Void> = web3Auth.initialize()
        sessionResponse.whenComplete { _, error ->
            if (error == null) {
                reRender()
                println("PrivKey: " + web3Auth.getPrivateKey())
                println("ed25519PrivKey: " + web3Auth.getEd25519PrivateKey())
                web3Auth.getUserInfoAsync().whenComplete { info, infoError ->
                    if (infoError == null) {
                        Log.d(TAG, "idToken present=${info?.idToken?.isNotBlank() == true}")
                    }
                }
                credentials = Credentials.create(web3Auth.getPrivateKey())

            } else {
                Log.d("MainActivity_Web3Auth", error.message ?: "Something went wrong")
                // Ideally, you should initiate the login function here.
            }
        }

        // Setup UI and event handlers
        val signInButton = findViewById<Button>(R.id.signInButton)
        signInButton.setOnClickListener { signIn() }

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
        getTransactionButton.setOnClickListener { sendTransaction(0.001, "0xeaA8Af602b2eDE45922818AE5f9f7FdE50cFa1A8") }
        getTransactionButton.visibility = View.GONE

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

        // Handle user signing in when app is active
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

    private fun signIn() {
        try {
            if (!::auth.isInitialized) {
                Log.e(TAG, "Firebase Auth is not initialized")
                Toast.makeText(this, "Firebase Auth is not initialized",
                    Toast.LENGTH_SHORT).show()
                return
            }
            Log.d(TAG, "Attempting Firebase sign in...")
            auth.signInWithEmailAndPassword(FIREBASE_EMAIL, FIREBASE_PASSWORD)
                .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    if (user != null) {
                        user.getIdToken(true).addOnSuccessListener { result ->
                            try {
                                val idToken = result.token
                                //Do whatever
                                Log.d(TAG, "GetTokenResult result = $idToken")
                                if (idToken != null) {
                                    if (!::web3Auth.isInitialized) {
                                        Log.e(TAG, "Web3Auth is not initialized")
                                        if (!isFinishing && !isDestroyed) {
                                            Toast.makeText(this@MainActivity, "Web3Auth is not initialized",
                                                Toast.LENGTH_SHORT).show()
                                        }
                                        return@addOnSuccessListener
                                    }
                                    // Same params as the Flutter Firebase example, which calls this
                                    // SDK on a background dispatcher. connectTo blocks while it
                                    // fetches shares, so it must not run on the main thread.
                                    val loginParams = LoginParams(
                                        authConnection = AuthConnection.CUSTOM,
                                        authConnectionId = "w3a-firebase-demo",
                                        extraLoginOptions = ExtraLoginOptions(userIdField = "sub"),
                                        idToken = idToken
                                    )
                                    chainCalls.execute {
                                        try {
                                            web3Auth.connectTo(loginParams).get()
                                            runOnUiThread {
                                                if (isFinishing || isDestroyed) return@runOnUiThread
                                                credentials = Credentials.create(web3Auth.getPrivateKey())
                                                Log.d(TAG, "connectTo success address=${credentials.address}")
                                                reRender()
                                            }
                                        } catch (error: Exception) {
                                            Log.e(TAG, "Web3Auth login failed", error)
                                            runOnUiThread {
                                                if (isFinishing || isDestroyed) return@runOnUiThread
                                                Toast.makeText(
                                                    this@MainActivity,
                                                    "Web3Auth login failed: ${error.message}",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }
                                    }
                                } else {
                                    Log.e(TAG, "ID token is null")
                                    if (!isFinishing && !isDestroyed) {
                                        Toast.makeText(this@MainActivity, "Failed to get ID token.",
                                            Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } catch (e: Exception) {
                                Log.e(TAG, "Exception in getIdToken success callback", e)
                                e.printStackTrace()
                                if (!isFinishing && !isDestroyed) {
                                    Toast.makeText(this@MainActivity, "Error processing ID token: ${e.message}",
                                        Toast.LENGTH_SHORT).show()
                                }
                            }
                        }.addOnFailureListener { exception ->
                            Log.e(TAG, "Failed to get ID token", exception)
                            exception.printStackTrace()
                            if (!isFinishing && !isDestroyed) {
                                Toast.makeText(this@MainActivity, "Failed to get ID token: ${exception.message}",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Log.e(TAG, "Current user is null after successful sign in")
                        if (!isFinishing && !isDestroyed) {
                            Toast.makeText(this@MainActivity, "User is null after sign in.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    val exception = task.exception
                    Log.e(TAG, "signInWithEmail:failure", exception)
                    val errorMessage = when {
                        exception?.message?.contains("no user record") == true -> 
                            "User does not exist. Please create the user in Firebase Console first."
                        exception?.message?.contains("password") == true -> 
                            "Invalid password."
                        else -> 
                            "Authentication failed: ${exception?.message ?: "Unknown error"}"
                    }
                    Toast.makeText(this@MainActivity, errorMessage,
                        Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception during sign in", e)
            e.printStackTrace()
            Toast.makeText(this, "Sign in error: ${e.message}",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun signOut() {
        val logoutCompletableFuture =  web3Auth.logout()
        logoutCompletableFuture.whenComplete { _, error ->
            if (error == null) {
                reRender()
            } else {
                Log.d("MainActivity_Web3Auth", error.message ?: "Something went wrong" )
            }
        }
        recreate()
    }


    private fun getAccessToken() {
        web3Auth.getAccessToken().whenComplete { token, error ->
            runOnUiThread {
                showResult(if (error == null) "Access token:\n$token" else error.message ?: "Unable to get access token")
            }
        }
    }

    private fun refreshSession() {
        web3Auth.refreshSession().whenComplete { _, error ->
            runOnUiThread {
                if (error == null) {
                    credentials = Credentials.create(web3Auth.getPrivateKey())
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

        completableFuture.whenComplete{_, error ->
            if(error == null) {
                // Add your logic
                Log.d("MainActivity_Web3Auth", "Wallet services launched successfully")
            } else {
                // Add your logic for error
                Log.d("MainActivity_Web3Auth", error.message ?: "Something went wrong")
            }
        }
    }

    private fun getAddress(): String {
        val contentTextView = findViewById<TextView>(R.id.contentTextView)
        val publicAddress = credentials.address
        contentTextView.text = publicAddress
        println("Address:, $publicAddress")
        return publicAddress
    }

    private fun getBalance() {
        chainCalls.execute {
            try {
                val ethBalance: EthGetBalance = web3.ethGetBalance(
                    credentials.address,
                    DefaultBlockParameterName.LATEST
                ).send()
                Log.d(TAG, "Balance: ${ethBalance.balance}")
                runOnUiThread { showResult(ethBalance.balance.toString()) }
            } catch (e: Exception) {
                Log.e(TAG, "getBalance failed", e)
                runOnUiThread { showResult(e.message ?: "Balance failed") }
            }
        }
    }

    private fun signMessage(message: String): String {
        val contentTextView = findViewById<TextView>(R.id.contentTextView)
        val hashedData = Hash.sha3(message.toByteArray(StandardCharsets.UTF_8))
        val signature = Sign.signMessage(hashedData, credentials.ecKeyPair)
        val r = Numeric.toHexString(signature.r)
        val s = Numeric.toHexString(signature.s).substring(2)
        val v = Numeric.toHexString(signature.v).substring(2)
        val signHash = StringBuilder(r).append(s).append(v).toString()
        contentTextView.text = signHash
        Log.d(TAG, "Signed message length=${signHash.length}")
        return signHash
    }

    private fun sendTransaction(amount: Double, recipientAddress: String) {
        chainCalls.execute {
            try {
                val ethGetTransactionCount: EthGetTransactionCount = web3.ethGetTransactionCount(
                    credentials.address,
                    DefaultBlockParameterName.LATEST
                ).send()
                val nonce: BigInteger = ethGetTransactionCount.transactionCount
                val value: BigInteger = Convert.toWei(amount.toString(), Convert.Unit.ETHER).toBigInteger()
                val gasLimit: BigInteger = BigInteger.valueOf(21000)
                val chainId: EthChainId = web3.ethChainId().send()

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

                val signedMessage: ByteArray = TransactionEncoder.signMessage(rawTransaction, credentials)
                val hexValue: String = Numeric.toHexString(signedMessage)
                val ethSendTransaction: EthSendTransaction = web3.ethSendRawTransaction(hexValue).send()
                val message = if (ethSendTransaction.error != null) {
                    "Tx Error: ${ethSendTransaction.error.message}"
                } else {
                    "Tx Hash: ${ethSendTransaction.transactionHash}"
                }
                Log.d(TAG, message)
                runOnUiThread { showResult(message) }
            } catch (e: Exception) {
                Log.e(TAG, "sendTransaction failed", e)
                runOnUiThread { showResult(e.message ?: "Transaction failed") }
            }
        }
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
        val signInButton = findViewById<Button>(R.id.signInButton)
        val signOutButton = findViewById<Button>(R.id.signOutButton)
        val getAddressButton = findViewById<Button>(R.id.getAddress)
        val getBalanceButton = findViewById<Button>(R.id.getBalance)
        val getMessageButton = findViewById<Button>(R.id.getMessage)
        val getTransactionButton = findViewById<Button>(R.id.getTransaction)
        val getAccessTokenButton = findViewById<Button>(R.id.getAccessToken)
        val refreshSessionButton = findViewById<Button>(R.id.refreshSession)
        val getLaunchWalletServicesButton = findViewById<Button>(R.id.launchWalletServices)

        val key = try {
            web3Auth.getPrivateKey()
        } catch (ex: Exception) {
            Log.d(TAG, ex.message ?: "No private key")
            ""
        }
        val loggedIn = key.isNotEmpty()
        if (loggedIn) {
            contentTextView.text = gson.toJson(userInfo) + "\n Private Key: " + key
            contentTextView.visibility = View.VISIBLE
            signInButton.visibility = View.GONE
        } else {
            contentTextView.text = getString(R.string.not_logged_in)
            contentTextView.visibility = View.GONE
            signInButton.visibility = View.VISIBLE
        }
        val sessionVisibility = if (loggedIn) View.VISIBLE else View.GONE
        signOutButton.visibility = sessionVisibility
        getAddressButton.visibility = sessionVisibility
        getBalanceButton.visibility = sessionVisibility
        getMessageButton.visibility = sessionVisibility
        getTransactionButton.visibility = sessionVisibility
        getAccessTokenButton.visibility = sessionVisibility
        refreshSessionButton.visibility = sessionVisibility
        getLaunchWalletServicesButton.visibility = sessionVisibility
    }
}