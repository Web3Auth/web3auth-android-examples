# MetaMask Embedded Wallets — Android Solana Example

Android example demonstrating Solana blockchain integration with [MetaMask Embedded Wallets](https://docs.metamask.io/embedded-wallets/) (formerly Web3Auth Plug and Play). Built with Jetpack Compose, Koin, and [sol4k](https://github.com/sol4k/sol4k).

## Features

- Social login via Google (using grouped connection)
- Solana wallet creation via Ed25519 private key derivation
- Get Solana wallet address (public key) and devnet SOL balance
- Send SOL transactions on Solana Devnet
- Session persistence across app restarts
- MVVM architecture with Jetpack Compose UI

## Requirements

- Android Studio Hedgehog (2023.1.1) or later
- Android API 26+, Compile SDK 34
- JDK 17+
- [MetaMask Embedded Wallets Dashboard](https://dashboard.web3auth.io) account

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/Web3Auth/web3auth-android-examples.git
cd web3auth-android-examples/android-solana-example
```

### 2. Configure the Web3Auth Dashboard

1. Create a project on the [Embedded Wallets Dashboard](https://dashboard.web3auth.io).
2. Set up a Google connection and note your **Auth Connection ID** and **Grouped Auth Connection ID**.
3. Allowlist your redirect URI: `com.example.androidsolanaexample://auth`

### 3. Update `Web3AuthSampleConfig.kt`

```kotlin
object Web3AuthSampleConfig {
    const val CLIENT_ID = "YOUR_WEB3AUTH_CLIENT_ID"
    const val REDIRECT_URL = "com.example.androidsolanaexample://auth"
    const val GOOGLE_AUTH_CONNECTION_ID = "YOUR_GOOGLE_AUTH_CONNECTION_ID"
    const val GOOGLE_CLIENT_ID = "YOUR_GOOGLE_CLIENT_ID"
    const val GROUPED_AUTH_CONNECTION_ID = "YOUR_GROUPED_AUTH_CONNECTION_ID"
}
```

### 4. Open in Android Studio and run

## How It Works

### Key dependencies (`app/build.gradle.kts`)

```kotlin
dependencies {
    implementation("com.github.Web3Auth:web3auth-android-sdk:10.0.0")
    implementation("org.sol4k:sol4k:0.4.1")
    implementation("io.insert-koin:koin-android:3.5.3")
}
```

### Initialize Web3Auth

The SDK is initialized in the Koin module and the activity calls `initialize()` via the ViewModel:

```kotlin
// Login
val loginParams = LoginParams(
    authConnection = AuthConnection.GOOGLE,
    authConnectionId = Web3AuthSampleConfig.GOOGLE_AUTH_CONNECTION_ID,
    groupedAuthConnectionId = Web3AuthSampleConfig.GROUPED_AUTH_CONNECTION_ID
)
web3Auth.connectTo(loginParams).await()
```

### Derive the Solana keypair

After login, use `getEd25519PrivateKey()` to get the Solana private key:

```kotlin
val ed25519PrivateKey = web3Auth.getEd25519PrivateKey()
val solanaKeyPair = Keypair.fromSecretKey(ed25519PrivateKey.hexToByteArray())
val publicKey = solanaKeyPair.publicKey.toBase58()
```

### Get Solana balance

```kotlin
val connection = Connection(RpcUrl.DEVNET)
val balance = connection.getBalance(solanaKeyPair.publicKey)
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
- [sol4k Documentation](https://github.com/sol4k/sol4k)
- [MetaMask Embedded Wallets Docs](https://docs.metamask.io/embedded-wallets/)
- [Dashboard](https://dashboard.web3auth.io)
- [Builder Hub Community](https://builder.metamask.io/c/embedded-wallets/5)

## License

MIT — see [LICENSE](../LICENSE) for details.
