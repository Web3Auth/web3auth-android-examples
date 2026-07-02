# MetaMask Embedded Wallets — Android Quick Start

Basic Android integration demonstrating social login with [MetaMask Embedded Wallets](https://docs.metamask.io/embedded-wallets/) (formerly Web3Auth Plug and Play). After login the example derives an EVM wallet, fetches a Sepolia balance, signs messages, and sends transactions using web3j.

## Features

- Social login (Google, Facebook, Email Passwordless, and more)
- EVM wallet creation via private key derivation
- Get wallet address and Sepolia ETH balance
- Sign messages and send transactions
- Session persistence across app restarts

## Requirements

- Android Studio Hedgehog (2023.1.1) or later
- Android API 26+, Compile SDK 34
- JDK 17+
- [MetaMask Embedded Wallets Dashboard](https://dashboard.web3auth.io) account

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/Web3Auth/web3auth-android-examples.git
cd web3auth-android-examples/android-quick-start
```

### 2. Get your Client ID

Create a project on the [Embedded Wallets Dashboard](https://dashboard.web3auth.io). In the dashboard, allowlist your redirect URI: `com.sbz.web3authdemoapp://auth`

### 3. Set your Client ID

In `app/src/main/res/values/strings.xml`, replace the placeholder:

```xml
<string name="web3auth_project_id">YOUR_WEB3AUTH_CLIENT_ID</string>
```

### 4. Open in Android Studio and run

Open the `android-quick-start` directory in Android Studio, sync Gradle, and run on a device or emulator (API 26+).

## How It Works

### Installation

Add JitPack to your project-level `settings.gradle`:

```groovy
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url "https://jitpack.io" }
    }
}
```

Add the SDK to your `app/build.gradle`:

```groovy
dependencies {
    implementation 'com.github.web3auth:web3auth-android-sdk:10.0.1'
}
```

### AndroidManifest.xml

Enable internet access, set `launchMode="singleTop"`, and configure the deep link for OAuth redirects:

```xml
<uses-permission android:name="android.permission.INTERNET" />

<activity
    android:launchMode="singleTop"
    android:name=".MainActivity"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data android:scheme="com.sbz.web3authdemoapp" />
    </intent-filter>
</activity>
```

### Initialize Web3Auth

```kotlin
web3Auth = Web3Auth(
    Web3AuthOptions(
        clientId = getString(R.string.web3auth_project_id),
        web3AuthNetwork = Web3AuthNetwork.SAPPHIRE_MAINNET,
        redirectUrl = "com.sbz.web3authdemoapp://auth"
    ), this
)

// Pass the intent URI to handle OAuth redirects when the app is not alive
web3Auth.setResultUrl(intent?.data)

// Check for an existing session on startup
val sessionResponse: CompletableFuture<Void> = web3Auth.initialize()
sessionResponse.whenComplete { _, error ->
    if (error == null) {
        // User is already logged in — restore session
        val privateKey = web3Auth.getPrivateKey()
        val credentials = Credentials.create(privateKey)
    } else {
        // No active session — show login UI
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
        Toast.makeText(this, "User closed the browser.", Toast.LENGTH_SHORT).show()
        web3Auth.setResultUrl(null)
        Web3Auth.setCustomTabsClosed(false)
    }
}
```

### Login

```kotlin
val loginCompletableFuture: CompletableFuture<Web3AuthResponse> =
    web3Auth.connectTo(
        LoginParams(
            authConnection = AuthConnection.EMAIL_PASSWORDLESS,
            loginHint = "user@example.com"
        )
    )

loginCompletableFuture.whenComplete { _, error ->
    if (error == null) {
        val privateKey = web3Auth.getPrivateKey()
        val credentials = Credentials.create(privateKey)
        // Use credentials with web3j
    }
}
```

### Get wallet address and balance

```kotlin
val web3j = Web3j.build(HttpService("https://1rpc.io/sepolia"))
val credentials = Credentials.create(web3Auth.getPrivateKey())
val address = credentials.address

val balanceResponse = web3j
    .ethGetBalance(address, DefaultBlockParameterName.LATEST)
    .send()
val ethBalance = Convert.fromWei(
    balanceResponse.balance.toBigDecimal(), Convert.Unit.ETHER
)
```

### Logout

```kotlin
val logoutCompletableFuture = web3Auth.logout()
logoutCompletableFuture.whenComplete { _, error ->
    if (error == null) {
        // Session cleared
    }
}
```

## Resources

- [Android SDK Documentation](https://docs.metamask.io/embedded-wallets/sdk/android/)
- [MetaMask Embedded Wallets Docs](https://docs.metamask.io/embedded-wallets/)
- [Dashboard](https://dashboard.web3auth.io)
- [Builder Hub Community](https://builder.metamask.io/c/embedded-wallets/5)
- [SDK Releases](https://github.com/Web3Auth/web3auth-android-sdk/releases)

## License

MIT — see [LICENSE](../LICENSE) for details.
