# MetaMask Embedded Wallets — Android Auth0 Example

Android example demonstrating custom authentication with Auth0 using [MetaMask Embedded Wallets](https://docs.metamask.io/embedded-wallets/) (formerly Web3Auth Plug and Play). Auth0 handles the login flow and Web3Auth derives the user's EVM wallet from the Auth0 session.

## Features

- Custom authentication via Auth0 (SPA/JWT flow)
- EVM wallet creation and management
- Get wallet address, Sepolia balance, sign messages, and send transactions
- Session persistence across app restarts

## Requirements

- Android Studio Hedgehog (2023.1.1) or later
- Android API 24+, Compile SDK 34
- JDK 17+
- [Auth0 account](https://auth0.com) with a Native application configured
- [MetaMask Embedded Wallets Dashboard](https://dashboard.web3auth.io) account

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/Web3Auth/web3auth-android-examples.git
cd web3auth-android-examples/android-auth0-example
```

### 2. Configure Auth0

1. Create a **Native** application in your [Auth0 Dashboard](https://manage.auth0.com/).
2. Note your **Domain** and **Client ID**.
3. Add `com.sbz.web3authdemoapp://auth` to **Allowed Callback URLs** in Auth0.

### 3. Configure the Web3Auth Dashboard

1. Create a project on the [Embedded Wallets Dashboard](https://dashboard.web3auth.io).
2. Go to **Authentication** → create a custom connection with:
   - **Type**: Custom JWT / Auth0
   - **JWKS Endpoint**: `https://YOUR_AUTH0_DOMAIN/.well-known/jwks.json`
   - **User ID field**: `sub`
3. Note your **Auth Connection ID** (verifier name).
4. Allowlist your redirect URI: `com.sbz.web3authdemoapp://auth`

### 4. Set credentials in strings.xml

```xml
<string name="web3auth_project_id">YOUR_WEB3AUTH_CLIENT_ID</string>
<string name="web3auth_auth0_client_id">YOUR_AUTH0_CLIENT_ID</string>
```

### 5. Open in Android Studio and run

## How It Works

### Initialize Web3Auth with Auth0 connection config

```kotlin
web3Auth = Web3Auth(
    Web3AuthOptions(
        clientId = getString(R.string.web3auth_project_id),
        web3AuthNetwork = Web3AuthNetwork.SAPPHIRE_MAINNET,
        redirectUrl = "com.sbz.web3authdemoapp://auth",
        authConnectionConfig = listOf(
            AuthConnectionConfig(
                authConnection = AuthConnection.CUSTOM,
                authConnectionId = "w3a-auth0-demo", // your Auth Connection ID from dashboard
                clientId = getString(R.string.web3auth_auth0_client_id)
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

### Login with Auth0

```kotlin
val loginParams = LoginParams(
    authConnection = AuthConnection.CUSTOM,
    authConnectionId = "w3a-auth0-demo",
    extraLoginOptions = ExtraLoginOptions(
        domain = "https://YOUR_AUTH0_DOMAIN"
    )
)
val loginFuture: CompletableFuture<Web3AuthResponse> = web3Auth.connectTo(loginParams)

loginFuture.whenComplete { _, error ->
    if (error == null) {
        val credentials = Credentials.create(web3Auth.getPrivateKey())
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
- [MetaMask Embedded Wallets Docs](https://docs.metamask.io/embedded-wallets/)
- [Dashboard](https://dashboard.web3auth.io)
- [Builder Hub Community](https://builder.metamask.io/c/embedded-wallets/5)

## License

MIT — see [LICENSE](../LICENSE) for details.
