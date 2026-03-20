# MetaMask Embedded Wallets — Android Grouped Connections Example

Android example demonstrating **Grouped Connections** (formerly Aggregate Verifiers) with [MetaMask Embedded Wallets](https://docs.metamask.io/embedded-wallets/) (formerly Web3Auth Plug and Play). Grouped connections allow multiple login methods — Google and Email Passwordless in this example — to resolve to the **same wallet address** for the same user.

## Features

- Grouped connections: Google Sign-In + Email Passwordless → same wallet address
- EVM wallet creation and management
- Get wallet address, Sepolia balance, sign messages, and send transactions
- Session persistence across app restarts

## Requirements

- Android Studio Hedgehog (2023.1.1) or later
- Android API 24+, Compile SDK 34
- JDK 17+
- [MetaMask Embedded Wallets Dashboard](https://dashboard.web3auth.io) account
- Google OAuth Client ID (for the Google login connection)
- Auth0 application (for the Email Passwordless connection)

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/Web3Auth/web3auth-android-examples.git
cd web3auth-android-examples/android-aggregate-verifier-example
```

### 2. Configure the Web3Auth Dashboard

1. Create a project on the [Embedded Wallets Dashboard](https://dashboard.web3auth.io).
2. Go to **Authentication** → create a **Grouped Connection** with two sub-connections:
   - **Google** sub-connection — Auth Connection ID: e.g. `w3a-google`
   - **Auth0 Email Passwordless** sub-connection — Auth Connection ID: e.g. `w3a-a0-email-passwordless`
3. Note the **Grouped Auth Connection ID** (e.g. `aggregate-sapphire`).
4. Allowlist your redirect URI: `com.sbz.web3authdemoapp://auth`

### 3. Set credentials in strings.xml

```xml
<string name="web3auth_project_id">YOUR_WEB3AUTH_CLIENT_ID</string>
<string name="web3auth_google_client_id">YOUR_GOOGLE_CLIENT_ID</string>
<string name="web3auth_auth0_client_id">YOUR_AUTH0_CLIENT_ID</string>
```

### 4. Open in Android Studio and run

## How It Works

### Initialize Web3Auth with grouped connection config

```kotlin
private const val GROUPED_AUTH_CONNECTION_ID = "aggregate-sapphire"
private const val GOOGLE_AUTH_CONNECTION_ID = "w3a-google"
private const val AUTH0_AUTH_CONNECTION_ID = "w3a-a0-email-passwordless"
private const val AUTH0_DOMAIN = "https://web3auth.au.auth0.com"

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
        redirectUrl = "com.sbz.web3authdemoapp://auth",
        authConnectionConfig = authConnections
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

### Login with Google

```kotlin
val loginFuture: CompletableFuture<Web3AuthResponse> =
    web3Auth.login(LoginParams(Provider.GOOGLE))

loginFuture.whenComplete { _, error ->
    if (error == null) {
        val credentials = Credentials.create(web3Auth.getPrivateKey())
    }
}
```

### Login with Email Passwordless (Auth0)

```kotlin
val loginFuture: CompletableFuture<Web3AuthResponse> =
    web3Auth.login(
        LoginParams(
            Provider.JWT,
            extraLoginOptions = ExtraLoginOptions(
                domain = AUTH0_DOMAIN,
                verifierIdField = "email",
                isVerifierIdCaseSensitive = false
            )
        )
    )
```

> Both login methods resolve to the same wallet address because they share the same `groupedAuthConnectionId`.

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
