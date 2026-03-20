# ⚠️ Deprecated — SFA Android Quick Start

> **This example uses the Single Factor Auth (SFA) Android SDK (`single-factor-auth-android`), which is deprecated.**
>
> The SFA SDK is no longer maintained. Please use the standard [MetaMask Embedded Wallets Android SDK](https://docs.metamask.io/embedded-wallets/sdk/android/) instead.

## Migration

Use the [android-quick-start](../android-quick-start) example in this repository, which demonstrates the same Firebase custom authentication flow using the current SDK:

```bash
git clone https://github.com/Web3Auth/web3auth-android-examples.git
cd web3auth-android-examples/android-quick-start
```

For custom JWT authentication (as this SFA example demonstrated with Firebase), refer to the [android-firebase-example](../android-firebase-example), which uses `web3Auth.connectTo(LoginParams(authConnection = AuthConnection.CUSTOM, authConnectionId = "...", idToken = idToken))` — the current equivalent of the SFA login flow.

## Resources

- [Android SDK Documentation](https://docs.metamask.io/embedded-wallets/sdk/android/)
- [Custom Authentication Guide](https://docs.metamask.io/embedded-wallets/sdk/android/advanced/custom-authentication/)
- [MetaMask Embedded Wallets Docs](https://docs.metamask.io/embedded-wallets/)
- [Dashboard](https://dashboard.web3auth.io)
- [Builder Hub Community](https://builder.metamask.io/c/embedded-wallets/5)
