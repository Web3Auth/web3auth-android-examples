# MetaMask Embedded Wallets — Android Firebase Example

Android example demonstrating custom authentication with Firebase using [MetaMask Embedded Wallets](https://docs.metamask.io/embedded-wallets/) (formerly Web3Auth Plug and Play). Firebase authenticates the user, then the Firebase ID token is passed directly to Web3Auth to derive the user's EVM wallet.

## Features

- Firebase Email/Password authentication
- Custom JWT flow: Firebase ID token → Web3Auth wallet derivation
- EVM wallet creation and management
- Get wallet address, Sepolia balance, sign messages, and send transactions
- Session persistence across app restarts

## Requirements

- Android Studio Hedgehog (2023.1.1) or later
- Android API 24+, Compile SDK 34
- JDK 17+
- [Firebase account](https://firebase.google.com) with a project configured
- [MetaMask Embedded Wallets Dashboard](https://dashboard.web3auth.io) account

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/Web3Auth/web3auth-android-examples.git
cd web3auth-android-examples/android-firebase-example
```

### 2. Configure Firebase

1. Create an Android app in [Firebase Console](https://console.firebase.google.com).
2. Enable **Email/Password** sign-in under Authentication → Sign-in methods.
3. Download `google-services.json` and place it in the `app/` directory.
4. Add the SHA-1 fingerprint of your debug keystore in Firebase Console (Project Settings → Your App).

### 3. Configure the Web3Auth Dashboard

1. Create a project on the [Embedded Wallets Dashboard](https://dashboard.web3auth.io).
2. Go to **Authentication** → create a custom connection with:
   - **Type**: Firebase
   - **JWKS Endpoint**: `https://www.googleapis.com/service_accounts/v1/jwk/securetoken@system.gserviceaccount.com`
   - **User ID field**: `sub`
   - **JWT Validation**: `aud` field equals your Firebase project ID
3. Note your **Auth Connection ID** (verifier name). This example uses `w3a-firebase-demo`.
4. Allowlist your redirect URI: `com.sbz.web3authdemoapp://auth`

### 4. Set credentials in strings.xml

```xml
<string name="web3auth_project_id">YOUR_WEB3AUTH_CLIENT_ID</string>
```

### 5. Open in Android Studio and run

## How It Works

### Initialize Web3Auth with Firebase connection config

```kotlin
web3Auth = Web3Auth(
    Web3AuthOptions(
        clientId = getString(R.string.web3auth_project_id),
        web3AuthNetwork = Web3AuthNetwork.SAPPHIRE_MAINNET,
        redirectUrl = "com.sbz.web3authdemoapp://auth",
        authConnectionConfig = listOf(
            AuthConnectionConfig(
                authConnection = AuthConnection.CUSTOM,
                authConnectionId = "w3a-firebase-demo", // your Auth Connection ID from dashboard
                clientId = getString(R.string.web3auth_project_id)
            )
        )
    ), this
)

web3Auth.setResultUrl(intent?.data)

val sessionResponse: CompletableFuture<Void> = web3Auth.initialize()
sessionResponse.whenComplete { _, error ->
    if (error == null) {
        val credentials = Credentials.create(web3Auth.getPrivateKey())
    }
}
```

### Firebase sign-in → Web3Auth wallet

```kotlin
// 1. Sign in with Firebase
Firebase.auth.signInWithEmailAndPassword(email, password)
    .addOnCompleteListener { task ->
        if (task.isSuccessful) {
            // 2. Get the Firebase ID token (must be fresh — max 60s old)
            Firebase.auth.currentUser?.getIdToken(true)
                ?.addOnSuccessListener { result ->
                    val idToken = result.token ?: return@addOnSuccessListener

                    // 3. Pass the ID token directly to Web3Auth
                    val loginFuture: CompletableFuture<Web3AuthResponse> =
                        web3Auth.connectTo(
                            LoginParams(
                                authConnection = AuthConnection.CUSTOM,
                                authConnectionId = "w3a-firebase-demo",
                                idToken = idToken
                            )
                        )

                    loginFuture.whenComplete { _, error ->
                        if (error == null) {
                            val credentials = Credentials.create(web3Auth.getPrivateKey())
                        }
                    }
                }
        }
    }
```

### Handle deep link callbacks

```kotlin
override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    web3Auth.setResultUrl(intent?.data)
}

override fun onResume() {
    super.onResume()
    if (Web3Auth.getCustomTabsClosed()) {
        web3Auth.setResultUrl(null)
        Web3Auth.setCustomTabsClosed(false)
    }
}
```

## Resources

- [Android SDK Documentation](https://docs.metamask.io/embedded-wallets/sdk/android/)
- [Custom Authentication Guide](https://docs.metamask.io/embedded-wallets/sdk/android/advanced/custom-authentication/)
- [Firebase Android Setup](https://firebase.google.com/docs/android/setup)
- [MetaMask Embedded Wallets Docs](https://docs.metamask.io/embedded-wallets/)
- [Dashboard](https://dashboard.web3auth.io)
- [Builder Hub Community](https://builder.metamask.io/c/embedded-wallets/5)

## License

MIT — see [LICENSE](../LICENSE) for details.
