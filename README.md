# MetaMask Embedded Wallets — Android Examples

Android examples for integrating [MetaMask Embedded Wallets](https://docs.metamask.io/embedded-wallets/) (formerly Web3Auth Plug and Play) into native Android applications. The SDK is written in Kotlin and provides social login, custom authentication, and blockchain interactions without requiring users to manage private keys.

## Examples

| Example | Description |
|---|---|
| [android-quick-start](./android-quick-start) | Basic integration with social login and EVM (Sepolia) |
| [android-auth0-example](./android-auth0-example) | Custom authentication via Auth0 JWT flow |
| [android-firebase-example](./android-firebase-example) | Custom authentication via Firebase ID token |
| [android-aggregate-verifier-example](./android-aggregate-verifier-example) | Grouped connections — same wallet across Google + Email Passwordless |
| [android-solana-example](./android-solana-example) | Solana integration using sol4k and Jetpack Compose |
| [android-playground](./android-playground) | Full-featured playground with all SDK capabilities |

## Getting Started

1. Clone this repository:
   ```bash
   git clone https://github.com/Web3Auth/web3auth-android-examples.git
   ```
2. Open the example directory of your choice in Android Studio.
3. Create a project on the [Embedded Wallets Dashboard](https://dashboard.web3auth.io) and get your Client ID.
4. Allowlist your app's redirect URI on the dashboard: `{YOUR_APP_PACKAGE_NAME}://auth`
5. Set your Client ID in the example's `app/src/main/res/values/strings.xml`.
6. Follow the individual example README for any provider-specific configuration.
7. Run on a device or emulator (API 26+).

## Requirements

- Android Studio Hedgehog (2023.1.1) or later
- Android API 26 or newer
- Compile SDK 34
- JDK 17+
- Kotlin 1.9.24 (required by SDK 11)

## What's new in v11

All examples use `com.github.web3auth:web3auth-android-sdk:11.0.0` (Auth v11 citadel sessions, Wallet Services v6). This matches the [SDK sample app](https://github.com/Web3Auth/web3auth-android-sdk/blob/11.0.0/app/src/main/java/com/web3auth/app/MainActivity.kt) and the recent Flutter and iOS example updates.

- Construct `Web3Auth`, pass the redirect with `setResultUrl()`, then call `initialize()` so an existing citadel session can be restored. Single-factor auth falls back to session-service when citadel authorize fails.
- Social and custom login use `connectTo(LoginParams(...))`.
- After login, use `getUserInfoAsync()` for token-backed profile data, `getAccessToken()` for the citadel access token, and `refreshSession()` to re-authorize.
- `enableMFA()` and `manageMFA()` take no arguments. Custom JWT login (Firebase) is single-factor auth, and the SDK rejects MFA on that path.
- Set `authBuildEnv = BuildEnv.PRODUCTION` for Sapphire Mainnet client IDs. `BuildEnv.TESTING` targets develop-auth and fails redirect checks for production projects.
- Leave `sessionTime` unset so `initialize()` reads it from the dashboard project config.
- Do not hard-code `/v10` auth or dashboard URLs. The SDK defaults to `/v11`.

## SDK

- **Package**: `com.github.web3auth:web3auth-android-sdk`
- **Current version used in examples**: `11.0.0`
- **Latest releases**: [github.com/Web3Auth/web3auth-android-sdk/releases](https://github.com/Web3Auth/web3auth-android-sdk/releases)

## Resources

- [Android SDK Documentation](https://docs.metamask.io/embedded-wallets/sdk/android/)
- [MetaMask Embedded Wallets Docs](https://docs.metamask.io/embedded-wallets/)
- [Dashboard](https://dashboard.web3auth.io)
- [Builder Hub Community](https://builder.metamask.io/c/embedded-wallets/5)
- [GitHub Issues](https://github.com/Web3Auth/web3auth-android-examples/issues)

## License

MIT — see [LICENSE](./LICENSE) for details.
