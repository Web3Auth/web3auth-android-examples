# Web3Auth Flutter Aggregate Verifier Example

This example demonstrates how to integrate Web3Auth in a Flutter application using an Aggregate Verifier. Aggregate Verifiers allow you to combine multiple login providers (like Google and Auth0) under a single verifier, enabling users to log in through different methods while maintaining the same wallet address.

## 📝 Features
- Multi-provider authentication (Google, Auth0)
- Single wallet address across multiple login methods
- Ethereum wallet creation and management
- Basic blockchain interactions
- Secure key management
- Cross-platform support (iOS & Android)

## 🚀 Getting Started

### Prerequisites
- Flutter 2.10.0 or higher
- Dart 2.16.0 or higher
- [Auth0 Account](https://auth0.com)
- [Web3Auth Dashboard](https://dashboard.web3auth.io) account
- Android Studio / VS Code with Flutter extension
- For iOS development:
  - Xcode 13+
  - CocoaPods
- For Android development:
  - Android Studio
  - JDK 11+

### Installation

1. Clone the repository:
```bash
git clone https://github.com/Web3Auth/web3auth-mobile-examples.git
cd web3auth-mobile-examples/flutter/flutter-aggregate-verifier-example
```

2. Install dependencies:
```bash
flutter pub get
```

3. iOS Setup (for iOS development):
```bash
cd ios && pod install && cd ..
```

### Configuration

1. Auth0 Setup:
   - Create a new application in [Auth0 Dashboard](https://manage.auth0.com)
   - Configure callback URLs
   - Enable the required social connections (Google, etc.)
   - Note down Domain and Client ID

2. Web3Auth Setup:
   - Get your Client ID from [Web3Auth Dashboard](https://dashboard.web3auth.io)
   - Create a custom aggregate verifier:
     - Sub-verifier 1: Google
     - Sub-verifier 2: Auth0
   - Update configuration in `lib/main.dart`:
   ```dart
   final web3auth = Web3Auth(
     clientId: "YOUR-WEB3AUTH-CLIENT-ID",
     network: Network.testnet,
   );
   ```

3. Update Auth0 and Google credentials in your configuration files

### Running the App

```bash
# Run in debug mode
flutter run

# Build for release
flutter build ios  # For iOS
flutter build apk  # For Android
```

## 💡 Implementation Details

### Project Structure
```
lib/
├── main.dart              # Entry point
├── config/
│   └── auth_config.dart   # Authentication configuration
├── services/
│   ├── auth_service.dart  # Authentication services
│   ├── web3auth.dart      # Web3Auth service
│   └── blockchain.dart    # Blockchain operations
├── screens/
│   ├── home.dart          # Home screen
│   └── login.dart         # Login screen
└── widgets/               # Reusable widgets
```

### Core Features Implementation

1. **Initialize Web3Auth**
```dart
final web3auth = Web3Auth(
  clientId: "YOUR-CLIENT-ID",
  network: Network.testnet,
);

await web3auth.initialize();
```

2. **Google Authentication**
```dart
// Login with Google
Future<void> loginWithGoogle() async {
  await web3auth.login(
    loginProvider: Provider.google,
    mfaLevel: MFALevel.NONE,
    extraLoginOptions: ExtraLoginOptions(
      verifierIdField: "email",
      aggregateVerifier: "YOUR-AGGREGATE-VERIFIER",
    ),
  );
}
```

3. **Auth0 Authentication**
```dart
// Login with Auth0
Future<void> loginWithAuth0() async {
  // Get Auth0 credentials
  final credentials = await auth0.webAuthentication().login();
  final idToken = credentials.idToken;
  
  // Connect with Web3Auth
  await web3auth.login(
    loginProvider: Provider.jwt,
    extraLoginOptions: ExtraLoginOptions(
      id_token: idToken,
      verifier: "YOUR-AUTH0-VERIFIER",
      domain: "auth0.com",
      aggregateVerifier: "YOUR-AGGREGATE-VERIFIER",
    ),
  );
}
```

## 🔒 Security Considerations

- Consistent wallet addresses across login methods
- JWT token handling
- Private key management
- Session management
- OAuth 2.0 best practices

## 🛠️ Troubleshooting

### Common Issues

1. **Aggregate Verifier Configuration**
   - Verify sub-verifiers are properly configured
   - Check verifier ID format requirements
   - Ensure consistent verifier ID fields

2. **Authentication Flow**
   - Verify JWT token format
   - Check sub-verifier settings
   - Debug authentication state transitions

3. **Platform-Specific Issues**
   - iOS: URL scheme configuration
   - Android: Intent filters setup

## 📚 Resources

- [Web3Auth Documentation](https://web3auth.io/docs)
- [Flutter SDK Reference](https://web3auth.io/docs/sdk/pnp/flutter)
- [Aggregate Verifier Guide](https://web3auth.io/docs/guides/verifiers/aggregate-verifier)
- [Google Authentication Setup](https://web3auth.io/docs/guides/google)
- [Auth0 Setup Guide](https://web3auth.io/docs/guides/auth0)

## 🤝 Support

- [Discord](https://discord.gg/web3auth)
- [GitHub Issues](https://github.com/Web3Auth/web3auth-mobile-examples/issues)
- [Web3Auth Support](https://web3auth.io/docs/troubleshooting/support)

## 📄 License

This example is available under the MIT License. See the [LICENSE](../../LICENSE) file for more info.
