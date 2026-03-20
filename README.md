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

> **Note:** The `sfa-android-quick-start` folder uses the Single Factor Auth (SFA) Android SDK, which is deprecated. Use [android-quick-start](./android-quick-start) with the standard Android SDK instead.

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
7. Run on a device or emulator (API 24+).

## Requirements

- Android Studio Hedgehog (2023.1.1) or later
- Android API 24 or newer
- Compile SDK 34
- JDK 17+

## SDK

- **Package**: `com.github.web3auth:web3auth-android-sdk`
- **Current version used in examples**: `10.0.0`
- **Latest releases**: [github.com/Web3Auth/web3auth-android-sdk/releases](https://github.com/Web3Auth/web3auth-android-sdk/releases)

## Resources

- [Android SDK Documentation](https://docs.metamask.io/embedded-wallets/sdk/android/)
- [MetaMask Embedded Wallets Docs](https://docs.metamask.io/embedded-wallets/)
- [Dashboard](https://dashboard.web3auth.io)
- [Builder Hub Community](https://builder.metamask.io/c/embedded-wallets/5)
- [GitHub Issues](https://github.com/Web3Auth/web3auth-android-examples/issues)

## License

MIT — see [LICENSE](./LICENSE) for details.
