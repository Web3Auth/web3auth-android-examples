package com.example.androidsolanaexample.viewmodel
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidsolanaexample.data.Web3AuthHelper
import com.example.androidsolanaexample.data.Web3AuthSampleConfig
import com.example.androidsolanaexample.domain.SolanaUseCase
import com.web3auth.core.types.AuthConnection
import com.web3auth.core.types.LoginParams
import com.web3auth.core.types.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch
import org.sol4k.Keypair

@OptIn(ExperimentalStdlibApi::class)
class MainViewModel(private val web3AuthHelper: Web3AuthHelper, private val solanaUseCase: SolanaUseCase): ViewModel() {

    private val _isLoggedIn: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _isAccountLoaded: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isAccountLoaded: StateFlow<Boolean> = _isAccountLoaded

    lateinit var solanaKeyPair: Keypair
    lateinit var balance: String

    fun solanaPrivateKey(): String {
        return web3AuthHelper.getSolanaPrivateKey()
    }

    private fun prepareKeyPair() {
        solanaKeyPair = Keypair.fromSecretKey(solanaPrivateKey().hexToByteArray())
    }
    fun login(){
        val loginParams = LoginParams(
            authConnection = AuthConnection.GOOGLE,
            authConnectionId = Web3AuthSampleConfig.GOOGLE_AUTH_CONNECTION_ID,
            groupedAuthConnectionId = Web3AuthSampleConfig.GROUPED_AUTH_CONNECTION_ID
        )
        viewModelScope.launch {
            try {
                web3AuthHelper.login(loginParams = loginParams).await()
                prepareKeyPair()
                _isLoggedIn.emit(true)
            } catch (error: Exception){
                _isLoggedIn.emit(false)
                throw error
            }
        }
    }

    fun initialise() {
        viewModelScope.launch {
            try {
                web3AuthHelper.initialize().await()
            } catch (e:Exception) {
                Log.e("Initialization", e.toString())
            }
            isUserLoggedIn()
        }
    }

    private fun isUserLoggedIn() {
        viewModelScope.launch {
            try {
                val isLoggedIn = web3AuthHelper.isUserAuthenticated()
                if(isLoggedIn) {
                    prepareKeyPair()
                }
                _isLoggedIn.emit(isLoggedIn)
            }  catch (e: Exception) {
                _isLoggedIn.emit(false)
            }
        }
    }

    fun getBalance() {
        viewModelScope.launch {
            _isAccountLoaded.emit(false)
            try {
                balance = solanaUseCase.getBalance(solanaKeyPair.publicKey)
                _isAccountLoaded.emit(true)
            } catch (e: Exception) {
                _isAccountLoaded.emit(false)
                throw e
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            try {
                web3AuthHelper.logOut().await()
                _isLoggedIn.emit(false)
            } catch (e: Exception) {
                Log.e("Logout", e.toString())
                _isLoggedIn.emit(true)
            }
        }
    }

    fun setResultUrl(uri: Uri?) {
        web3AuthHelper.setResultUrl(uri)
    }

     fun signAndSendTransaction(onSign: (hash: String?, error: String?) -> Unit) {
        viewModelScope.launch {
            try {
                val signedTransaction = solanaUseCase.signAndSendSol(solanaKeyPair)
                onSign(signedTransaction, null)
            } catch (e: Exception) {
                e.localizedMessage?.let { onSign( null, it) }
            }
        }
    }

    fun signTransaction(onSign: (signedTransaction: String?, error: String?) -> Unit) {
        viewModelScope.launch {
          try {
              val signedTransaction = solanaUseCase.signSendSol(solanaKeyPair)
              onSign(signedTransaction, null)
          } catch (e: Exception) {
              e.localizedMessage?.let { onSign( null, it) }
          }
        }
    }

    fun userInfo(onAvailable:(userInfo: UserInfo?, error: String?) -> Unit) {
        viewModelScope.launch {
            try {
                val info = web3AuthHelper.getUserInfoAsync().await()
                onAvailable(info, null)
            } catch (e: Exception) {
                e.localizedMessage?.let { onAvailable(null, it) }
            }
        }
    }

    fun fetchAccessToken(onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                onResult(web3AuthHelper.getAccessToken().await())
            } catch (e: Exception) {
                onResult(e.message ?: "Unable to get access token")
            }
        }
    }

    fun refreshSession(onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                web3AuthHelper.refreshSession().await()
                prepareKeyPair()
                _isLoggedIn.emit(true)
                onResult("Session refreshed")
            } catch (e: Exception) {
                onResult(e.message ?: "Unable to refresh session")
            }
        }
    }
}
